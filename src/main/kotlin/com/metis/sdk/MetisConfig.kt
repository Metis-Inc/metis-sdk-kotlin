package com.metis.sdk

/**
 * Configuration for the Metis SDK client.
 *
 * @property apiKey The API key for authenticating with Metis API
 * @property baseUrl The base URL of the Metis API (defaults to production)
 * @property timeout Timeout for API requests in seconds (defaults to 30 seconds)
 */
data class MetisConfig(
    val apiKey: String,
    val baseUrl: String = "https://api.metisai.ir",
    val timeout: Long = 70
)