package com.metis.sdk.wrapper

import com.metis.sdk.common.models.Message
import com.squareup.moshi.JsonClass

/**
 * Chat completion request.
 */
@JsonClass(generateAdapter = true)
data class ChatCompletionRequest(
    val messages: List<Message>,
    val model: String? = null,
    val temperature: Double? = null,
    val topP: Double? = null,
    val n: Int? = null,
    val stream: Boolean = false,
    val maxTokens: Int? = null,
    val presencePenalty: Double? = null,
    val frequencyPenalty: Double? = null,
    val logitBias: Map<String, Double>? = null,
    val user: String? = null
)

/**
 * Chat completion response.
 */
@JsonClass(generateAdapter = true)
data class ChatCompletionResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage?
)

/**
 * Chat completion choice.
 */
@JsonClass(generateAdapter = true)
data class Choice(
    val index: Int,
    val message: Message,
    val finishReason: String?
)

/**
 * Token usage.
 */
@JsonClass(generateAdapter = true)
data class Usage(
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
)

/**
 * Embedding request.
 */
@JsonClass(generateAdapter = true)
data class EmbeddingRequest(
    val input: List<String>,
    val model: String? = null,
    val user: String? = null
)

/**
 * Embedding response.
 */
@JsonClass(generateAdapter = true)
data class EmbeddingResponse(
    val data: List<Embedding>,
    val model: String,
    val usage: Usage
)

/**
 * Embedding.
 */
@JsonClass(generateAdapter = true)
data class Embedding(
    val embedding: List<Double>,
    val index: Int
)