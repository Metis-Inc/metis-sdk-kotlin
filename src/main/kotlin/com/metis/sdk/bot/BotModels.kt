package com.metis.sdk.bot

import com.metis.sdk.common.models.Corpus
import com.metis.sdk.common.models.ProviderConfig
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * Request to create a new bot.
 */
@JsonClass(generateAdapter = true)
data class BotCreationRequest(
    val name: String,
    val instructions: String?,
    val providerConfig: ProviderConfig,
    val corpora: List<Corpus>? = null,
    val summarizer: SummarizationConfig? = null,
    val functions: List<BotFunction>? = null,
    val description: String? = null,
    val avatar: StorageFile? = null,
    val enabled: Boolean = true,
    val autoGenerateHeadline: Boolean = false,
    val public: Boolean = false,
    val memoryEnabled: Boolean = true,
    val googleSearchEnabled: Boolean = false
)

/**
 * Request to update a bot.
 */
@JsonClass(generateAdapter = true)
data class BotUpdateRequest(
    val name: String? = null,
    val instructions: String? = null,
    val providerConfig: ProviderConfig? = null,
    val corpora: List<Corpus>? = null,
    val summarizer: SummarizationConfig? = null,
    val functions: List<BotFunction>? = null,
    val description: String? = null,
    val avatar: StorageFile? = null,
    val enabled: Boolean? = null,
    val autoGenerateHeadline: Boolean? = null,
    val public: Boolean? = null,
    val memoryEnabled: Boolean? = null,
    val googleSearchEnabled: Boolean? = null
)

/**
 * Request to clone a bot.
 */
@JsonClass(generateAdapter = true)
data class BotCloneRequest(
    val name: String,
    val description: String? = null
)

/**
 * Bot output model.
 */
@JsonClass(generateAdapter = true)
data class BotOutputModel(
    val id: String,
    val name: String,
    val instructions: String?,
    val providerConfig: ProviderConfig,
    val corpora: List<Corpus>?,
    val summarizer: SummarizationConfig?,
    val functions: List<BotFunction>?,
    val description: String?,
    val avatar: StorageFile?,
    val enabled: Boolean,
    val autoGenerateHeadline: Boolean,
    val public: Boolean,
    val memoryEnabled: Boolean,
    val googleSearchEnabled: Boolean,
    val createdAt: Date
)

/**
 * Summarization configuration.
 */
@JsonClass(generateAdapter = true)
data class SummarizationConfig(
    val provider: String,
    val enabled: Boolean = true
)

/**
 * Bot function.
 */
@JsonClass(generateAdapter = true)
data class BotFunction(
    val name: String,
    val description: String,
    val parameters: Map<String, FunctionParameter>
)

/**
 * Function parameter.
 */
@JsonClass(generateAdapter = true)
data class FunctionParameter(
    val type: String,
    val description: String,
    val required: Boolean = false,
    val enum: List<String>? = null
)

/**
 * Storage file.
 */
@JsonClass(generateAdapter = true)
data class StorageFile(
    val objectName: String,
    val url: String,
    val size: Long,
    val contentType: String
)