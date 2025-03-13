package com.metis.sdk.corpora

import com.squareup.moshi.JsonClass
import java.io.File
import java.util.*

/**
 * Represents a corpus type.
 */
enum class CorpusType {
    TEXT,
    URL,
    FILE
}

/**
 * Base interface for corpus creation requests.
 */
interface CreateCorpusRequest {
    val name: String
    val description: String?
    val embedding: EmbeddingMeta?
    val chunking: ChunkingMeta?
    val ranking: RankingMeta?
    val reranking: RerankingMeta?
}

/**
 * Request to create a text corpus.
 */
data class TextCorpusRequest(
    override val name: String,
    val text: String,
    override val description: String? = null,
    override val embedding: EmbeddingMeta? = null,
    override val chunking: ChunkingMeta? = null,
    override val ranking: RankingMeta? = null,
    override val reranking: RerankingMeta? = null
) : CreateCorpusRequest

/**
 * Request to create a URL corpus.
 */
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

/**
 * Request to create a file corpus.
 */
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

/**
 * Request to update a corpus.
 */
@JsonClass(generateAdapter = true)
data class UpdateCorpusRequest(
    val name: String? = null,
    val description: String? = null,
    val embedding: EmbeddingMeta? = null,
    val chunking: ChunkingMeta? = null,
    val ranking: RankingMeta? = null,
    val reranking: RerankingMeta? = null
)

/**
 * Request to update text in a text corpus.
 */
@JsonClass(generateAdapter = true)
data class UpdateTextRequest(
    val text: String
)

/**
 * Request to toggle a URL page.
 */
@JsonClass(generateAdapter = true)
data class ToggleUrlPageRequest(
    val enable: Boolean
)

/**
 * Response from toggling a URL page.
 */
@JsonClass(generateAdapter = true)
data class ToggleUrlPageResponse(
    val corpusId: String,
    val pageId: String,
    val updatedChunks: Long
)

/**
 * Corpus response DTO.
 */
@JsonClass(generateAdapter = true)
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

/**
 * Embedding metadata.
 */
@JsonClass(generateAdapter = true)
data class EmbeddingMeta(
    val provider: String,
    val model: String? = null
)

/**
 * Chunking metadata.
 */
@JsonClass(generateAdapter = true)
data class ChunkingMeta(
    val provider: String,
    val chunkSize: Int? = null,
    val chunkOverlap: Int? = null
)

/**
 * Ranking metadata.
 */
@JsonClass(generateAdapter = true)
data class RankingMeta(
    val topK: Int? = null
)

/**
 * Reranking metadata.
 */
@JsonClass(generateAdapter = true)
data class RerankingMeta(
    val provider: String,
    val model: String? = null,
    val topK: Int? = null
)

/**
 * Chunk.
 */
@JsonClass(generateAdapter = true)
data class Chunk(
    val id: String,
    val corpusId: String,
    val content: String,
    val metadata: Map<String, Any>?,
    val enabled: Boolean,
    val createdAt: Date,
    val updatedAt: Date
)

/**
 * Request to update a chunk.
 */
@JsonClass(generateAdapter = true)
data class UpdateChunkRequest(
    val content: String? = null,
    val metadata: Map<String, Any>? = null,
    val enabled: Boolean? = null
)

/**
 * Request to create a chunk.
 */
@JsonClass(generateAdapter = true)
data class CreateChunkRequest(
    val content: String,
    val metadata: Map<String, Any>? = null
)

/**
 * Relevant chunk.
 */
@JsonClass(generateAdapter = true)
data class RelevantChunk(
    val chunk: Chunk,
    val score: Double
)

/**
 * RAG web page.
 */
@JsonClass(generateAdapter = true)
data class RagWebPage(
    val id: String,
    val corpusId: String,
    val url: String,
    val title: String?,
    val enabled: Boolean,
    val createdAt: Date,
    val updatedAt: Date
)

/**
 * Generic page of results.
 */
@JsonClass(generateAdapter = true)
data class Page<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Int,
    val currentPage: Int
)