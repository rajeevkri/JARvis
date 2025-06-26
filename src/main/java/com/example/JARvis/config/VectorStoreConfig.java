package com.example.JARvis.config;

import io.milvus.client.MilvusClient;
import io.milvus.grpc.DataType;
import io.milvus.grpc.DescribeIndexResponse;
import io.milvus.grpc.SearchResults;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.*;
import io.milvus.param.index.*;
import io.milvus.param.dml.SearchParam;
import io.milvus.response.SearchResultsWrapper;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class VectorStoreConfig {

    private static final String COLLECTION_NAME = "rag_docs";
    private static final String VECTOR_FIELD_NAME = "embedding";
    private static final String CONTENT_FIELD_NAME = "content";
    private static final MetricType METRIC_TYPE = MetricType.COSINE;
    private static final String INDEX_NAME = "vector_index";
    private static final IndexType INDEX_TYPE = IndexType.IVF_FLAT;
    private static final int DIMENSION = 1536; // Adjust based on your embedding model
    private static final int NLIST = 128; // IVF_FLAT parameter

    @Bean
    public VectorStore vectorStore(MilvusClient milvusClient, EmbeddingClient embeddingClient) {
        try {
            ensureCollectionReady(milvusClient);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Milvus collection", e);
        }

        return new VectorStore() {
            @Override
            public void add(List<Document> documents) {
                List<List<Float>> embeddings = documents.stream()
                        .map(doc -> toFloatList(embeddingClient.embed(doc)))
                        .collect(Collectors.toList());

                // TODO: Implement document insertion logic
            }

            @Override
            public Optional<Boolean> delete(List<String> idList) {
                return Optional.of(true);
            }

            @Override
            public List<Document> similaritySearch(String query) {
                List<Float> queryEmbedding = toFloatList(embeddingClient.embed(query));

                SearchParam searchParam = SearchParam.newBuilder()
                        .withCollectionName(COLLECTION_NAME)
                        .withMetricType(METRIC_TYPE)
                        .withOutFields(List.of(CONTENT_FIELD_NAME))
                        .withTopK(5)
                        .withVectors(List.of(queryEmbedding))
                        .withVectorFieldName(VECTOR_FIELD_NAME)
                        .build();

                try {
                    R<SearchResults> response = milvusClient.search(searchParam);
                    if (response.getStatus() != 0) {
                        throw new RuntimeException("Search failed: " + response.getMessage());
                    }
                    SearchResultsWrapper wrapper = new SearchResultsWrapper(response.getData().getResults());
                    return wrapper.getRowRecords().stream()
                            .map(row -> new Document(row.get(CONTENT_FIELD_NAME).toString()))
                            .collect(Collectors.toList());
                } catch (Exception e) {
                    throw new RuntimeException("Search failed for query: " + query, e);
                }
            }

            @Override
            public List<Document> similaritySearch(SearchRequest request) {
                return similaritySearch(request.getQuery());
            }

            private List<Float> toFloatList(List<Double> doubleList) {
                return doubleList.stream()
                        .map(Double::floatValue)
                        .collect(Collectors.toList());
            }
        };
    }

    private void ensureCollectionReady(MilvusClient milvusClient) {
        // Check if collection exists
        R<Boolean> hasCollection = milvusClient.hasCollection(
                HasCollectionParam.newBuilder()
                        .withCollectionName(COLLECTION_NAME)
                        .build());

        if (!hasCollection.getData()) {
            // Collection doesn't exist, create it and the index
            createCollection(milvusClient);
            createIndex(milvusClient);
        } else {
            // Collection exists, check if index exists
            try {
                R<DescribeIndexResponse> describeIndexResponse = milvusClient.describeIndex(
                        DescribeIndexParam.newBuilder()
                                .withCollectionName(COLLECTION_NAME)
                                .withIndexName(INDEX_NAME)
                                .build());

                if (describeIndexResponse.getStatus() != 0 || describeIndexResponse.getData() == null) {
                    // Index doesn't exist, create it
                    createIndex(milvusClient);
                }
            } catch (Exception e) {
                // If describeIndex fails, assume index doesn't exist
                createIndex(milvusClient);
            }
        }

        // Load the collection
        R<RpcStatus> loadResponse = milvusClient.loadCollection(
                LoadCollectionParam.newBuilder()
                        .withCollectionName(COLLECTION_NAME)
                        .build());

        if (loadResponse.getStatus() != 0) {
            throw new RuntimeException("Failed to load collection: " + loadResponse.getMessage());
        }
    }

    private void createCollection(MilvusClient milvusClient) {
        // Define fields
        FieldType idField = FieldType.newBuilder()
                .withName("id")
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(true)
                .build();

        FieldType contentField = FieldType.newBuilder()
                .withName(CONTENT_FIELD_NAME)
                .withDataType(DataType.VarChar)
                .withMaxLength(65535)
                .build();

        FieldType vectorField = FieldType.newBuilder()
                .withName(VECTOR_FIELD_NAME)
                .withDataType(DataType.FloatVector)
                .withDimension(DIMENSION)
                .build();

        // Create collection
        CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .withFieldTypes(List.of(idField, contentField, vectorField))
                .build();

        R<RpcStatus> collectionResponse = milvusClient.createCollection(createCollectionReq);
        if (collectionResponse.getStatus() != 0) {
            throw new RuntimeException("Failed to create collection: " + collectionResponse.getMessage());
        }
    }

    private void createIndex(MilvusClient milvusClient) {
        // Create index
        CreateIndexParam createIndexReq = CreateIndexParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .withFieldName(VECTOR_FIELD_NAME)
                .withIndexName(INDEX_NAME)
                .withIndexType(INDEX_TYPE)
                .withMetricType(METRIC_TYPE)
                .withExtraParam(String.format("{\"nlist\":%d}", NLIST))
                .build();

        R<RpcStatus> indexResponse = milvusClient.createIndex(createIndexReq);
        if (indexResponse.getStatus() != 0) {
            throw new RuntimeException("Failed to create index: " + indexResponse.getMessage());
        }

        // Add delay to allow index to build
        try {
            Thread.sleep(3000); // 3 second delay to allow index to build
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for index to build", e);
        }
    }
}