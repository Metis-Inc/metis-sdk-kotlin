package com.metis.sdk.common.models

import com.squareup.moshi.JsonClass

/**
 * Represents a provider in the Metis system.
 */
@JsonClass(generateAdapter = true)
data class Provider(
    val name: String,
    val model: String? = null,
    val acceptImageAttachment: Boolean = false,
    val acceptFileAttachment: Boolean = false
)

/**
 * Represents a provider with tags in the Metis system.
 */
@JsonClass(generateAdapter = true)
data class ProviderWithTags(
    val name: String,
    val model: String,
    val tags: List<String>
)