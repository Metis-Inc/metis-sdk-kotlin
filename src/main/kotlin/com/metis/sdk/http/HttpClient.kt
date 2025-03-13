package com.metis.sdk.http

import com.metis.sdk.MetisConfig
import com.metis.sdk.common.exceptions.MetisApiException
import com.metis.sdk.common.exceptions.MetisAuthException
import com.metis.sdk.common.exceptions.MetisNetworkException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * HTTP client for making requests to the Metis API.
 */
class HttpClient(val config: MetisConfig) {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(config.timeout, TimeUnit.SECONDS)
        .readTimeout(config.timeout, TimeUnit.SECONDS)
        .writeTimeout(config.timeout, TimeUnit.SECONDS)
        .build()
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Makes a GET request to the specified endpoint.
     *
     * @param endpoint The API endpoint to call
     * @param queryParams Optional query parameters
     * @return The response body as a string
     */
    suspend fun get(endpoint: String, queryParams: Map<String, String> = emptyMap()): String {
        val url = buildUrl(endpoint, queryParams)
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .get()
            .build()

        return executeRequest(request)
    }

    /**
     * Makes a POST request to the specified endpoint.
     *
     * @param endpoint The API endpoint to call
     * @param body The request body as a JSON string
     * @param queryParams Optional query parameters
     * @return The response body as a string
     */
    suspend fun post(endpoint: String, body: String, queryParams: Map<String, String> = emptyMap()): String {
        val url = buildUrl(endpoint, queryParams)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = body.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .post(requestBody)
            .build()

        return executeRequest(request)
    }

    /**
     * Makes a POST request with multipart form data to the specified endpoint.
     *
     * @param endpoint The API endpoint to call
     * @param parts The multipart form data parts
     * @param queryParams Optional query parameters
     * @return The response body as a string
     */
    suspend fun postMultipart(
        endpoint: String,
        parts: Map<String, Any>,
        queryParams: Map<String, String> = emptyMap()
    ): String {
        val url = buildUrl(endpoint, queryParams)
        val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

        parts.forEach { (name, value) ->
            when (value) {
                is File -> {
                    val mediaType = getMediaType(value.name)
                    val requestBody = value.asRequestBody(mediaType)
                    multipartBuilder.addFormDataPart(name, value.name, requestBody)
                }
                is String -> {
                    multipartBuilder.addFormDataPart(name, value)
                }
                else -> {
                    val jsonAdapter = moshi.adapter(Any::class.java)
                    multipartBuilder.addFormDataPart(name, jsonAdapter.toJson(value))
                }
            }
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .post(multipartBuilder.build())
            .build()

        return executeRequest(request)
    }

    /**
     * Makes a PUT request to the specified endpoint.
     *
     * @param endpoint The API endpoint to call
     * @param body The request body as a JSON string
     * @param queryParams Optional query parameters
     * @return The response body as a string
     */
    suspend fun put(endpoint: String, body: String, queryParams: Map<String, String> = emptyMap()): String {
        val url = buildUrl(endpoint, queryParams)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = body.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .put(requestBody)
            .build()

        return executeRequest(request)
    }

    /**
     * Makes a PATCH request to the specified endpoint.
     *
     * @param endpoint The API endpoint to call
     * @param body The request body as a JSON string
     * @param queryParams Optional query parameters
     * @return The response body as a string
     */
    suspend fun patch(endpoint: String, body: String, queryParams: Map<String, String> = emptyMap()): String {
        val url = buildUrl(endpoint, queryParams)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = body.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .patch(requestBody)
            .build()

        return executeRequest(request)
    }

    /**
     * Makes a DELETE request to the specified endpoint.
     *
     * @param endpoint The API endpoint to call
     * @param queryParams Optional query parameters
     * @return The response body as a string
     */
    suspend fun delete(endpoint: String, queryParams: Map<String, String> = emptyMap()): String {
        val url = buildUrl(endpoint, queryParams)
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .delete()
            .build()

        return executeRequest(request)
    }

    /**
     * Executes an HTTP request and returns the response body as a string.
     *
     * @param request The HTTP request to execute
     * @return The response body as a string
     */
    private suspend fun executeRequest(request: Request): String = suspendCoroutine { continuation ->
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(MetisNetworkException("Network error: ${e.message}", e))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val body = it.body?.string() ?: ""

                    when {
                        it.isSuccessful -> continuation.resume(body)
                        it.code == 401 -> continuation.resumeWithException(
                            MetisAuthException("Authentication failed: ${it.message}")
                        )
                        else -> {
                            val errorMessage = try {
                                val errorAdapter = moshi.adapter(ErrorResponse::class.java)
                                val errorBody = errorAdapter.fromJson(body)
                                errorBody?.message ?: "Unknown error"
                            } catch (e: Exception) {
                                "Error ${it.code}: ${it.message}"
                            }

                            continuation.resumeWithException(
                                MetisApiException(errorMessage, it.code)
                            )
                        }
                    }
                }
            }
        })
    }

    /**
     * Builds a URL for the specified endpoint and query parameters.
     *
     * @param endpoint The API endpoint
     * @param queryParams The query parameters
     * @return The complete URL
     */
    private fun buildUrl(endpoint: String, queryParams: Map<String, String>): HttpUrl {
        val urlBuilder = "${config.baseUrl}/$endpoint".toHttpUrlOrNull()!!.newBuilder()

        queryParams.forEach { (key, value) ->
            urlBuilder.addQueryParameter(key, value)
        }

        return urlBuilder.build()
    }

    /**
     * Gets the media type for a file based on its extension.
     *
     * @param fileName The name of the file
     * @return The media type
     */
    private fun getMediaType(fileName: String): MediaType {
        val extension = fileName.substringAfterLast('.', "")
        return when (extension.lowercase(Locale.getDefault())) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "pdf" -> "application/pdf"
            "txt" -> "text/plain"
            "html", "htm" -> "text/html"
            "json" -> "application/json"
            "xml" -> "application/xml"
            "mp3" -> "audio/mpeg"
            "mp4" -> "video/mp4"
            else -> "application/octet-stream"
        }.toMediaType()
    }

    /**
     * Data class for parsing error responses.
     */
    private data class ErrorResponse(
        val message: String
    )
}