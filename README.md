
# 🧠 JARvis – AI Assistant with RAG Capabilities

JARvis is a Spring Boot-based AI assistant integrating document ingestion, embedding, and querying using Retrieval-Augmented Generation (RAG). It enables training on domain-specific examples and querying a knowledge base through RESTful APIs.

---

## 🔧 Tech Stack

- Java 17+
- Spring Boot
- Spring AI (Ollama Integration)
- Milvus (Vector Database)
- REST APIs
- Lombok, SLF4J

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17+
- Maven 3.8+
- Docker (for Milvus and Ollama)

### 🛠️ Installation

```bash
git clone https://github.com/rajeevkri/JARvis.git
cd JARvis
mvn clean install
mvn spring-boot:run
```

---

## 🧩 Project Structure

```
com.jarvis
├── controller
│   ├── TrainingController.java
│   └── RagController.java
├── service
│   ├── DocumentEmbedder.java
│   ├── KnowledgeService.java
│   └── ModelTrainingService.java
├── model
│   ├── TrainingExample.java
│   └── KnowledgeDocument.java
└── JaRvisApplication.java
```

---

## 🧠 Features

- Train with question-answer examples.
- Embed documents into vector DB (Milvus).
- Query the knowledge base via a smart assistant using RAG.

---

## 📡 API Endpoints

### 1. `POST /train` – Train the model

**Payload**:

```json
[
  {
    "question": "What is the refund policy?",
    "answer": "Refunds can be requested within 14 days."
  }
]
```

### 2. `POST /embed` – Embed document

**Payload**:

```json
{
  "id": "doc1",
  "text": "JARvis is a smart assistant...",
  "metadata": {
    "source": "UserManual.pdf"
  }
}
```

### 3. `POST /query` – Ask a question

**Payload**:

```json
{
  "question": "What is JARvis?",
  "topK": 3
}
```

---

## 🔍 Key Classes

| Class | Purpose |
|-------|---------|
| `JaRvisApplication` | Bootstraps the Spring Boot app |
| `TrainingController` | Endpoint to submit training data |
| `RagController` | Embedding/query APIs |
| `DocumentEmbedder` | Handles vector storage and embedding |
| `ModelTrainingService` | Trains on question-answer pairs |
| `KnowledgeService` | Handles RAG logic |
| `TrainingExample`, `KnowledgeDocument` | Data models |

---

## 🧪 Example Usage

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

## 🛠 Configuration Notes

Ensure Spring AI is connected to:
- Ollama (LLM backend)
- Milvus (for vector search)

Configure in `application.properties`.

---

## 👥 Contribution

PRs are welcome! Areas to improve:
- UI Integration
- PDF/CSV ingestion
- Batch processing

---

## ❓ FAQ

**Q: Can it read PDFs?**  
A: Extend `DocumentEmbedder` to parse files.

**Q: How to scale?**  
A: Use Docker, async ingestion, Milvus clustering.

---

© 2025 Rajeev Singh – Open for collaboration!
