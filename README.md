# RAG Application with Milvus & Ollama

*Retrieval-Augmented Generation Pipeline*
## Members 
- Abdulrhman muhammed ahmed hegazy
- Abdulrhman basheer muhammed
- Abdulhammed muhammed amin
- Abdullah said kamal
## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Quick Start (Docker)](#quick-start-docker)
- [Local Development](#local-development)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)
- [Cleanup](#cleanup)
- [License](#license)
- [Demo](#demo)

## Features
- **Document Processing**: Chunking and embedding with `all-minilm`
- **Vector Search**: Milvus for low-latency semantic search
- **LLM Integration**: `tinyllama` for local answer generation
- **Spring Boot**: REST API with Docker support
- Web Interface: A user-friendly chat interface built with plain HTML, CSS, and JavaScript.

## Prerequisites

### For Docker Setup
- Docker Desktop ([Install](https://www.docker.com/products/docker-desktop))
- 4GB+ free disk space

### For Local Setup
- Java 17+ ([OpenJDK](https://openjdk.org/projects/jdk/17/))
- Python 3.8+ (for PyMilvus)
- 3.5GB+ free disk space

## Quick Start (Docker)

### 1. Clone and Build
```bash
git clone https://github.com/your-repo/rag-app.git
cd rag-app
docker-compose up -d --build
```

### 2. Initialize Models
```bash
# In a new terminal
docker exec ollama ollama pull tinyllama
docker exec ollama ollama pull all-minilm
```

### 3. Test the System
```bash
# Ingest sample data
curl -X POST http://localhost:8082/api/rag/ingest

# Ask a question
curl "http://localhost:8082/api/rag/ask?question=What%20is%20RAG?"

#Access the Application
http://localhost:8082/
```

## Local Development

### 1. Install Services
- **Milvus**:
  ```bash
  wget https://github.com/milvus-io/milvus/releases/download/v2.4.4/milvus-standalone-darwin-amd64.tar.gz
  tar -xzvf milvus-standalone-*.tar.gz
  cd milvus-standalone-*/bin && ./milvus run
  ```

- **Ollama**:
  ```bash
  curl -fsSL https://ollama.com/install.sh | sh
  ollama pull tinyllama
  ollama serve
  ```

### 2. Configure Application
Create `src/main/resources/application-local.properties`:
```properties
milvus.host=localhost
milvus.port=19530
ollama.base-url=http://localhost:11434
```

### 3. Run Spring Boot
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## Project Structure
```
.
├── docker-compose.yml          # Defines Milvus, Ollama, etc.
├── Dockerfile                  # Spring Boot container
├── src/
│   ├── main/
│   │   ├── java/com/example/rag/
│   │   │   ├── controller/     # REST endpoints
│   │   │   ├── service/        # Business logic
│   │   │   └── Application.java
│   │   └── resources/
│   │       ├── data.txt        # Your documents
│   │       └── application.properties
└── volumes/                    # Persistent data
    ├── milvus/                 # Vector data
    └── minio/                  # Object storage
```

## API Documentation

### Ingest Documents
```http
POST /api/rag/ingest
```
- Processes `data.txt` into vector embeddings
- **Sample Response**:
  ```json
  {"status": "Data ingestion completed successfully!"}
  ```

### Ask Questions
```http
GET /api/rag/ask?question={query}&topK={num}
```
- Parameters:
  - `question`: Your query (URL-encoded)
  - `topK`: Number of chunks to retrieve (default: 3)
- **Sample Response**:
  ```json
  {"answer": "Retrieval-Augmented Generation combines..."}
  ```

## Configuration

### Environment Variables
| Variable | Default | Description |
|----------|---------|-------------|
| `MILVUS_HOST` | `milvus-standalone` | Milvus service name |
| `OLLAMA_BASE_URL` | `http://ollama:11434` | Ollama endpoint |
| `SPRING_PROFILES_ACTIVE` | `docker` | Profile for Docker vs local |

### Customizing Models
Edit `docker-compose.yml` to change models:
```yaml
services:
  ollama:
    environment:
      - OLLAMA_MODELS=tinyllama,all-minilm
```

## Troubleshooting

### Common Issues
1. **Port Conflicts**:
   ```bash
   # Windows
   netstat -ano | findstr :8082
   # Linux/macOS
   lsof -i :8082
   ```

2. **Ollama Model Errors**:
   ```bash
   docker logs ollama
   ollama list
   ```

3. **Milvus Connection Problems**:
   ```bash
   curl http://localhost:9091/healthz
   ```

### Log Inspection
```bash
# Spring Boot
docker logs rag-app

# Milvus
docker logs milvus-standalone
```

## Cleanup

### Remove All Containers
```bash
docker-compose down -v
```

### Delete Local Data
```bash
rm -rf volumes/
```

## License
MIT License - See [LICENSE](LICENSE) for details.

## Demo
![demo](https://github.com/user-attachments/assets/874af772-3794-46b5-b1c1-269c10f8e656)
