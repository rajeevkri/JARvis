package com.example.JARvis.embedder;

import io.milvus.client.MilvusClient;
import io.milvus.grpc.DataType;
import io.milvus.param.collection.*;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentEmbedder {

    private final MilvusClient milvusClient;
    private final EmbeddingClient embeddingClient;

    public DocumentEmbedder(MilvusClient milvusClient, EmbeddingClient embeddingClient) {
        this.milvusClient = milvusClient;
        this.embeddingClient = embeddingClient;
        createCollectionIfNotExists();
    }

    private void createCollectionIfNotExists() {
        String collectionName = "rag_docs";

        // Create HasCollectionParam
        HasCollectionParam hasCollectionParam = HasCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .build();

        // Check if collection exists
        if (!milvusClient.hasCollection(hasCollectionParam).getData()) {
            // Define fields
            FieldType idField = FieldType.newBuilder()
                    .withName("id")
                    .withDataType(DataType.Int64)
                    .withPrimaryKey(true)
                    .withAutoID(true)
                    .build();

            FieldType contentField = FieldType.newBuilder()
                    .withName("content")
                    .withDataType(DataType.VarChar)
                    .withMaxLength(65535)
                    .build();

            FieldType embeddingField = FieldType.newBuilder()
                    .withName("embedding")
                    .withDataType(DataType.FloatVector)
                    .withDimension(384) // Must match your embedding model
                    .build();

            // Create collection
            CreateCollectionParam createParam = CreateCollectionParam.newBuilder()
                    .withCollectionName(collectionName)
                    .withFieldTypes(List.of(idField, contentField, embeddingField))
                    .build();

            milvusClient.createCollection(createParam);
        }
    }

    public void embedAndStore(List<Document> documents) {
        // Implement embedding and storage logic
    }
}