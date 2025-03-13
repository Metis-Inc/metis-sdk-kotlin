package com.metis.sdk.meta

import com.metis.sdk.common.models.Provider
import com.squareup.moshi.JsonClass

/**
 * Metadata response.
 */
@JsonClass(generateAdapter = true)
data class MetaResponse(
    val summarizers: List<Provider>,
    val chunkers: List<Provider>,
    val chatProviders: List<Provider>,
    val embeddingProviders: List<Provider>,
    val rerankerProviders: List<Provider>,
    val generationProviders: List<ProviderWithTags>
)

/**
 * Provider with tags.
 */
@JsonClass(generateAdapter = true)
data class ProviderWithTags(
    val name: String,
    val model: String,
    val tags: List<String>
)

/**
 * Pricing response.
 */
@JsonClass(generateAdapter = true)
data class PricingResponse(
    val chatProviders: List<PricingProviderDTO>,
    val imageProviders: List<PricingProviderDTO>
)

/**
 * Pricing provider DTO.
 */
@JsonClass(generateAdapter = true)
data class PricingProviderDTO(
    val name: String,
    val model: String,
    val currency: String,
    val fixedCallIncome: Double,
    val inputTokenUnitIncome: Double,
    val outputTokenUnitIncome: Double
)