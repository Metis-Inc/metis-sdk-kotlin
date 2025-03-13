package com.metis.sdk.wrapper

import com.metis.sdk.http.HttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Client for interacting with the Wrapper API.
 */
class WrapperClient(private val httpClient: HttpClient) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Gets chat completions from a provider.
     *
     * @param provider The provider to use
     * @param request The chat completion request
     * @return The chat completion response
     */
    suspend fun getChatCompletions(provider: String, request: ChatCompletionRequest): ChatCompletionResponse {
        val requestJson = moshi.adapter(ChatCompletionRequest::class.java).toJson(request)
        val response = httpClient.post("api/v1/chat/$provider/completions", requestJson)
        return moshi.adapter(ChatCompletionResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse chat completion response")
    }

    /**
     * Streams chat completions from a provider.
     *
     * @param provider The provider to use
     * @param request The chat completion request
     * @return A flow of chat completion chunks
     */
    fun streamChatCompletions(provider: String, request: ChatCompletionRequest): Flow<String> {
        val requestWithStream = request.copy(stream = true)
        val requestJson = moshi.adapter(ChatCompletionRequest::class.java).toJson(requestWithStream)

        return flow {
            // Create a new OkHttpClient with a longer timeout for streaming
            val client = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = requestJson.toRequestBody(mediaType)

            val request = Request.Builder()
                .url("${httpClient.config.baseUrl}/api/v1/chat/$provider/completions")
                .addHeader("Authorization", "Bearer ${httpClient.config.apiKey}")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            val source = response.body?.source()
            if (source != null) {
                while (!source.exhausted()) {
                    val line = source.readUtf8Line() ?: break
                    if (line.startsWith("data:")) {
                        val data = line.substring(5).trim()
                        if (data != "[DONE]") {
                            emit(data)
                        }
                    }
                }
            }

            response.close()
        }
    }

    /**
     * Gets embeddings from a provider.
     *
     * @param provider The provider to use
     * @param request The embedding request
     * @return The embedding response
     */
    suspend fun getEmbeddings(provider: String, request: EmbeddingRequest): EmbeddingResponse {
        val requestJson = moshi.adapter(EmbeddingRequest::class.java).toJson(request)
        val response = httpClient.post("api/v1/wrapper/$provider/embeddings", requestJson)
        return moshi.adapter(EmbeddingResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse embedding response")
    }
}