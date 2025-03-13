package com.metis.sdk.common.models

import com.squareup.moshi.JsonClass
import java.util.*

/**
 * Represents a corpus for retrieval-augmented generation (RAG).
 */
@JsonClass(generateAdapter = true)
data class Corpus(
    val id: String,
    val name: String,
    val description: String? = null,
    val type: String,
    val userId: String? = null,
    val embedding: EmbeddingConfig? = null,
    val chunking: ChunkingConfig? = null,
    val ranking: RankingConfig? = null,
    val reranking: RerankingConfig? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)

/**
 * Configuration for embedding in a corpus.
 */
@JsonClass(generateAdapter = true)
data class EmbeddingConfig(
    val provider: String,
    val model: String? = null
)

/**
 * Configuration for chunking in a corpus.
 */
@JsonClass(generateAdapter = true)
data class ChunkingConfig(
    val provider: String,
    val chunkSize: Int? = null,
    val chunkOverlap: Int? = null
)

/**
 * Configuration for ranking in a corpus.
 */
@JsonClass(generateAdapter = true)
data class RankingConfig(
    val topK: Int? = null
)

/**
 * Configuration for reranking in a corpus.
 */
@JsonClass(generateAdapter = true)
data class RerankingConfig(
    val provider: String,
    val model: String? = null,
    val topK: Int? = null
)