# Corpora (Knowledge Bases)

The Corpora client provides methods for creating and managing knowledge bases for retrieval-augmented generation (RAG).

## Initialize

```kotlin
val corporaClient = metis.corpora
```

## Create Text Corpus

```kotlin
val textCorpus = metis.corpora.createTextCorpus(
    TextCorpusRequest(
        name = "Product Documentation",
        text = "This is the documentation for our product...",
        description = "Product documentation for RAG",
        embedding = EmbeddingMeta(provider = "openai", model = "text-embedding-3-small"),
        chunking = ChunkingMeta(provider = "recursive", chunkSize = 1000, chunkOverlap = 200)
    )
)
```

## Create URL Corpus

```kotlin
val urlCorpus = metis.corpora.createUrlCorpus(
    UrlCorpusRequest(
        name = "Company Website",
        url = "https://example.com",
        crawlingDepth = 2,
        description = "Company website for RAG"
    )
)
```

## Create File Corpus

```kotlin
val fileCorpus = metis.corpora.createFileCorpus(
    FileCorpusRequest(
        name = "Research Papers",
        files = listOf(File("paper1.pdf"), File("paper2.pdf")),
        description = "Research papers for RAG",
        ocr = true
    )
)
```

## Update Corpus

```kotlin
val updatedCorpus = metis.corpora.updateCorpus(
    "corpus-id",
    UpdateCorpusRequest(
        name = "Updated Documentation",
        description = "Updated product documentation"
    )
)
```

## Get Corpus

```kotlin
val corpus = metis.corpora.getCorpus("corpus-id")
```

## List All Corpora

```kotlin
val allCorpora = metis.corpora.getAllCorpora()
```

## Delete Corpus

```kotlin
metis.corpora.deleteCorpus("corpus-id")
```

## Manage Chunks

### Get Chunks

```kotlin
val chunks = metis.corpora.getChunks("corpus-id", page = 0, size = 20)
```

### Create a Chunk

```kotlin
val newChunk = metis.corpora.createChunk(
    "corpus-id",
    CreateChunkRequest(
        content = "This is a new chunk of information.",
        metadata = mapOf("source" to "manual", "category" to "introduction")
    )
)
```

### Update a Chunk

```kotlin
val updatedChunk = metis.corpora.updateChunk(
    "corpus-id",
    "chunk-id",
    UpdateChunkRequest(
        content = "Updated chunk content",
        enabled = true
    )
)
```

### Find Relevant Chunks

```kotlin
val relevantChunks = metis.corpora.findRelevant("corpus-id", "query text")
```

## Manage URL Corpus

### Get URL Pages

```kotlin
val pages = metis.corpora.getUrlPages("corpus-id", page = 0, size = 20)
```

### Toggle URL Page

```kotlin
val toggleResult = metis.corpora.toggleUrlPage("corpus-id", "page-id", enable = true)
```

## Manage File Corpus

### Add Files to Corpus

```kotlin
val updatedCorpus = metis.corpora.addFiles(
    "corpus-id",
    listOf(File("new_document.pdf")),
    ocr = true
)
```

### Delete File

```kotlin
metis.corpora.deleteFile("corpus-id", "file-id")
```

### Rechunk File

```kotlin
metis.corpora.rechunkFile("corpus-id", "file-id")
```

## Update Text Corpus Content

```kotlin
val updatedCorpus = metis.corpora.updateText(
    "corpus-id", 
    "This is the updated text content for the corpus."
)
```

## Request Models

### TextCorpusRequest

```kotlin
data class TextCorpusRequest(
    override val name: String,
    val text: String,
    override val description: String? = null,
    override val embedding: EmbeddingMeta? = null,
    override val chunking: ChunkingMeta? = null,
    override val ranking: RankingMeta? = null,
    override val reranking: RerankingMeta? = null
) : CreateCorpusRequest
```

### UrlCorpusRequest

```kotlin
data class UrlCorpusRequest(
    override val name: String,
    val url: String,
    val crawlingDepth: Int,
    override val description: String? = null,
    override val embedding: EmbeddingMeta? = null,
    override val chunking: ChunkingMeta? = null,
    override val ranking: RankingMeta? = null,
    override val reranking: RerankingMeta? = null
) : CreateCorpusRequest
```

### FileCorpusRequest

```kotlin
data class FileCorpusRequest(
    override val name: String,
    val files: List<File>,
    override val description: String? = null,
    override val embedding: EmbeddingMeta? = null,
    override val chunking: ChunkingMeta? = null,
    override val ranking: RankingMeta? = null,
    override val reranking: RerankingMeta? = null,
    val ocr: Boolean? = null
) : CreateCorpusRequest
```

### Configuration Models

```kotlin
data class EmbeddingMeta(
    val provider: String,
    val model: String? = null
)

data class ChunkingMeta(
    val provider: String,
    val chunkSize: Int? = null,
    val chunkOverlap: Int? = null
)

data class RankingMeta(
    val topK: Int? = null
)

data class RerankingMeta(
    val provider: String,
    val model: String? = null,
    val topK: Int? = null
)
```

## Response Models

### CorpusResponseDTO

```kotlin
data class CorpusResponseDTO(
    val id: String,
    val name: String,
    val description: String?,
    val type: CorpusType,
    val userId: String,
    val embedding: EmbeddingMeta?,
    val chunking: ChunkingMeta?,
    val ranking: RankingMeta?,
    val reranking: RerankingMeta?,
    val createdAt: Date,
    val updatedAt: Date
)
```

### Chunk

```kotlin
data class Chunk(
    val id: String,
    val corpusId: String,
    val content: String,
    val metadata: Map<String, Any>?,
    val enabled: Boolean,
    val createdAt: Date,
    val updatedAt: Date
)
```

### RelevantChunk

```kotlin
data class RelevantChunk(
    val chunk: Chunk,
    val score: Double
)
```

## Advanced Configuration

### OCR for PDF Files

When creating a file corpus with PDF files, you can enable OCR (Optical Character Recognition) to extract text from scanned documents:

```kotlin
val fileCorpus = metis.corpora.createFileCorpus(
    FileCorpusRequest(
        name = "Scanned Documents",
        files = listOf(File("scanned_document.pdf")),
        description = "Scanned documents with OCR",
        ocr = true
    )
)
```

### Custom Chunking

You can customize how your documents are chunked:

```kotlin
val textCorpus = metis.corpora.createTextCorpus(
    TextCorpusRequest(
        name = "Custom Chunked Documentation",
        text = "Long document text...",
        chunking = ChunkingMeta(
            provider = "recursive",
            chunkSize = 500,  // Smaller chunks
            chunkOverlap = 50 // Less overlap
        )
    )
)
```

### Custom Embedding

You can specify which embedding model to use:

```kotlin
val textCorpus = metis.corpora.createTextCorpus(
    TextCorpusRequest(
        name = "Custom Embedded Documentation",
        text = "Document text...",
        embedding = EmbeddingMeta(
            provider = "openai",
            model = "text-embedding-3-large" // Higher quality embeddings
        )
    )
)
```

### Reranking

Enable reranking to improve search quality:

```kotlin
val textCorpus = metis.corpora.createTextCorpus(
    TextCorpusRequest(
        name = "Reranked Documentation",
        text = "Document text...",
        reranking = RerankingMeta(
            provider = "cohere",
            model = "rerank-english-v2.0",
            topK = 5
        )
    )
)
```
