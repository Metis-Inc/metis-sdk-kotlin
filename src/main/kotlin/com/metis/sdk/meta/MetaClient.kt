package com.metis.sdk.meta

import com.metis.sdk.http.HttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Client for interacting with the Meta API.
 */
class MetaClient(private val httpClient: HttpClient) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Gets metadata about available providers and models.
     *
     * @return Metadata response
     */
    suspend fun getMeta(): MetaResponse {
        val response = httpClient.get("api/v1/meta")
        return moshi.adapter(MetaResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse meta response")
    }

    /**
     * Gets pricing information.
     *
     * @return Pricing response
     */
    suspend fun getPricing(): PricingResponse {
        val response = httpClient.get("api/v1/meta/pricing")
        return moshi.adapter(PricingResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse pricing response")
    }
}