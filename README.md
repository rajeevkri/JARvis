# 🧠 JARvis – AI Assistant with RAG Capabilities

**JARvis** is a Java Spring Boot-based framework for building Retrieval-Augmented Generation (RAG) assistants. It allows ingestion of documents, embedding them into a vector store (Milvus), and querying the knowledge base using Large Language Models (LLMs) via Spring AI (Ollama backend).

---

## 🧰 Tech Stack

- **Java 21**
- **Spring Boot 3.2.5**
- **Spring AI 0.8.1** (Ollama, Transformers)
- **Milvus SDK (2.3.4)**
- **Swagger UI** (`springdoc-openapi`)
- **Spring Security (optional)**

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 21+
- Maven 3.8+
- Docker (for Milvus and Ollama)
- Git

### 🛠 Installation

```bash
git clone https://github.com/rajeevkri/JARvis.git
cd JARvis
mvn clean install
mvn spring-boot:run
```

---

## 🔩 Configuration Files

| File | Description |
|------|-------------|
| `EmbeddingConfig.java` | Spring AI embedding model config |
| `OllamaConfig.java` | LLM connection config using Ollama |
| `MilvusConfig.java` | Config for connecting to Milvus vector DB |
| `VectorStoreConfig.java` | Setup for using Milvus as a vector store |
| `SwaggerConfig.java` | Enables Swagger UI for REST APIs |
| `SecurityConfig.java` | (Optional) Security configuration for securing endpoints |

---

## 🧠 Features

- Upload and embed documents with metadata
- Train a small model with question-answer examples
- Query your knowledge base using semantic search + LLM (RAG flow)
- Integrated with Ollama LLM via Spring AI
- Swagger UI enabled for testing

---

## 📡 API Endpoints

Base URL: `http://localhost:8080`

### 1. `POST /train`  
Train the assistant with structured examples.

**Payload:**
```json
[
  {
    "question": "What is the refund policy?",
    "answer": "Refunds can be requested within 14 days."
  }
]
```

### 2. `POST /embed`  
Embed a new document into the vector store.

**Payload:**
```json
{
  "id": "doc1",
  "text": "JARvis is a smart assistant...",
  "metadata": {
    "source": "UserManual.pdf"
  }
}
```

### 3. `POST /query`  
Ask a question to the assistant.

**Payload:**
```json
{
  "question": "What is JARvis?",
  "topK": 3
}
```

---

## 📁 Project Structure

```
com.jarvis
├── controller
│   ├── TrainingController.java
│   └── RagController.java
├── model
│   ├── TrainingExample.java
│   └── KnowledgeDocument.java
├── service
│   ├── DocumentEmbedder.java
│   ├── KnowledgeService.java
│   └── ModelTrainingService.java
├── config
│   ├── MilvusConfig.java
│   ├── OllamaConfig.java
│   ├── EmbeddingConfig.java
│   ├── VectorStoreConfig.java
│   ├── SwaggerConfig.java
│   └── SecurityConfig.java (optional)
└── JaRvisApplication.java
```

---

## 🔍 Key Components

| Component | Purpose |
|----------|---------|
| `DocumentEmbedder` | Embeds text and stores it in Milvus |
| `ModelTrainingService` | Trains a local in-memory QA model |
| `KnowledgeService` | Combines retrieval and LLM response |
| `MilvusConfig` | Vector DB configuration |
| `OllamaConfig` | LLM setup (e.g., llama3, mistral, etc.) |
| `SwaggerConfig` | Swagger UI (`/swagger-ui.html`) |

---

## 🧪 Example cURL Requests

```bash
curl -X POST http://localhost:8080/embed -H "Content-Type: application/json" -d '{
  "id": "doc42",
  "text": "JARvis helps automate answers.",
  "metadata": { "domain": "support" }
}'
```

```bash
curl -X POST http://localhost:8080/query -H "Content-Type: application/json" -d '{
  "question": "What does JARvis do?",
  "topK": 2
}'
```

---

## 🔒 Security (Optional)

- Use `SecurityConfig.java` to enable endpoint protection
- Integrate JWT or Basic Auth if needed

---

## 🔬 Development Tips

- LLM model configuration can be updated in `OllamaConfig`
- Customize Milvus collection, index config in `MilvusConfig`
- Extend embedding logic in `DocumentEmbedder` to handle PDF/CSV

---

## 📚 Swagger UI

Once the app is running, open:
```
http://localhost:8080/swagger-ui.html
```

To try out API endpoints interactively.

---

## 🤝 Contributing

- Fork the repo
- Add improvements (UI, ingestors, adapters)
- Create a pull request

---

## ❓ FAQ

**Q: Can I use a local LLM like LLaMA or Mistral?**  
Yes, Ollama supports local model serving. Configure your desired model in `OllamaConfig`.

**Q: Can it ingest PDFs or CSVs?**  
You’ll need to extend `DocumentEmbedder` with file parsing logic.

**Q: How scalable is this?**  
Milvus and Ollama can be containerized; scale services using Kubernetes or Docker Swarm.

---

© 2025 Rajeev Singh – Built with ❤️ for RAG applications.
