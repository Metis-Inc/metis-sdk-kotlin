package com.metis.sdk.common.models

import com.squareup.moshi.JsonClass

/**
 * Represents a provider configuration in the Metis system.
 */
@JsonClass(generateAdapter = true)
data class ProviderConfig(
    val provider: Provider,
    val temperature: Double? = null,
    val maxTokens: Int? = null,
    val topP: Double? = null,
    val frequencyPenalty: Double? = null,
    val presencePenalty: Double? = null
)