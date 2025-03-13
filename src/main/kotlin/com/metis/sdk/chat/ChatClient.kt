package com.metis.sdk.chat

import com.metis.sdk.common.models.Message
import com.metis.sdk.http.HttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import java.util.concurrent.TimeUnit

/**
 * Client for interacting with the Chat API.
 */
class ChatClient(private val httpClient: HttpClient) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Creates a new chat session.
     *
     * @param botId The ID of the bot to create a session with
     * @param user Optional user information
     * @param initialMessages Optional initial messages for the session
     * @return The created session
     */
    suspend fun createSession(
        botId: String,
        user: ChatUser? = null,
        initialMessages: List<Message>? = null
    ): SessionResponse {
        val request = CreateSessionRequest(botId, user, initialMessages)
        val requestJson = moshi.adapter(CreateSessionRequest::class.java).toJson(request)

        val response = httpClient.post("api/v1/chat/session", requestJson)
        return moshi.adapter(SessionResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse session response")
    }

    /**
     * Updates an existing chat session.
     *
     * @param sessionId The ID of the session to update
     * @param user Optional user information to update
     * @param headline Optional headline to update
     * @return The updated session info
     */
    suspend fun updateSession(
        sessionId: String,
        user: ChatUser? = null,
        headline: String? = null
    ): SessionInfo {
        val request = UpdateSessionRequest(user, headline)
        val requestJson = moshi.adapter(UpdateSessionRequest::class.java).toJson(request)

        val response = httpClient.post("api/v1/chat/session/$sessionId", requestJson)
        return moshi.adapter(SessionInfo::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse session info response")
    }

    /**
     * Gets a chat session by ID.
     *
     * @param sessionId The ID of the session to get
     * @return The session
     */
    suspend fun getSession(sessionId: String): SessionResponse {
        val response = httpClient.get("api/v1/chat/session/$sessionId")
        return moshi.adapter(SessionResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse session response")
    }

    /**
     * Gets paginated messages for a chat session.
     *
     * @param sessionId The ID of the session
     * @param page The page number (0-based)
     * @param size The page size
     * @return Paginated messages
     */
    suspend fun getSessionMessages(
        sessionId: String,
        page: Int,
        size: Int
    ): PaginatedMessageResponse {
        val queryParams = mapOf(
            "page" to page.toString(),
            "size" to size.toString()
        )

        val response = httpClient.get("api/v1/chat/session/$sessionId/messages/paginated", queryParams)
        return moshi.adapter(PaginatedMessageResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse paginated message response")
    }

    /**
     * Gets info about a chat session.
     *
     * @param sessionId The ID of the session
     * @return The session info
     */
    suspend fun getSessionInfo(sessionId: String): SessionInfo {
        val response = httpClient.get("api/v1/chat/session/$sessionId/info")
        return moshi.adapter(SessionInfo::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse session info response")
    }

    /**
     * Gets all sessions, optionally filtered by user ID or bot ID.
     *
     * @param userId Optional user ID to filter by
     * @param botId Optional bot ID to filter by
     * @return List of sessions
     */
    suspend fun getSessions(userId: String? = null, botId: String? = null): List<SessionResponse> {
        val queryParams = mutableMapOf<String, String>()
        userId?.let { queryParams["userId"] = it }
        botId?.let { queryParams["botId"] = it }

        val response = httpClient.get("api/v1/chat/sessions", queryParams)
        val listType = Types.newParameterizedType(List::class.java, SessionResponse::class.java)
        return moshi.adapter<List<SessionResponse>>(listType).fromJson(response)
            ?: emptyList()
    }

    /**
     * Gets paginated sessions, optionally filtered by user ID or bot ID.
     *
     * @param userId Optional user ID to filter by
     * @param botId Optional bot ID to filter by
     * @param page The page number (0-based)
     * @param size The page size
     * @return Paginated sessions
     */
    suspend fun getSessionsPaginated(
        userId: String? = null,
        botId: String? = null,
        page: Int,
        size: Int
    ): PaginatedSessionsResponse {
        val queryParams = mutableMapOf(
            "page" to page.toString(),
            "size" to size.toString()
        )
        userId?.let { queryParams["userId"] = it }
        botId?.let { queryParams["botId"] = it }

        val response = httpClient.get("api/v1/chat/session/paginated", queryParams)
        return moshi.adapter(PaginatedSessionsResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse paginated sessions response")
    }

    /**
     * Deletes a chat session.
     *
     * @param sessionId The ID of the session to delete
     */
    suspend fun deleteSession(sessionId: String) {
        httpClient.delete("api/v1/chat/session/$sessionId")
    }

    /**
     * Sends a message to a chat session.
     *
     * @param sessionId The ID of the session
     * @param message The message to send
     * @return The sent message with the bot's response
     */
    suspend fun sendMessage(sessionId: String, message: Message): ChatMessage {
        val request = SendMessageRequest(message)
        val requestJson = moshi.adapter(SendMessageRequest::class.java).toJson(request)

        val response = httpClient.post("api/v1/chat/session/$sessionId/message", requestJson)
        return moshi.adapter(ChatMessage::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse chat message response")
    }

    /**
     * Sends a message asynchronously to a chat session.
     *
     * @param sessionId The ID of the session
     * @param message The message to send
     * @return The async task creation response
     */
    suspend fun sendAsyncMessage(sessionId: String, message: Message): AsyncTaskCreationResponse {
        val request = SendMessageRequest(message)
        val requestJson = moshi.adapter(SendMessageRequest::class.java).toJson(request)

        val response = httpClient.post("api/v1/chat/session/$sessionId/message/async", requestJson)
        return moshi.adapter(AsyncTaskCreationResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse async task creation response")
    }

    /**
     * Checks the result of an asynchronous message.
     *
     * @param sessionId The ID of the session
     * @param taskId The ID of the async task
     * @return The async message result
     */
    suspend fun checkAsyncMessageResult(sessionId: String, taskId: String): AsyncMessageResult {
        val response = httpClient.get("api/v1/chat/session/$sessionId/message/async/$taskId")
        return moshi.adapter(AsyncMessageResult::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse async message result")
    }

    /**
     * Streams a message to a chat session.
     *
     * @param sessionId The ID of the session
     * @param message The message to send
     * @return A flow of chat stream chunks
     */
    fun streamMessage(sessionId: String, message: Message): Flow<ChatStreamChunk> {
        val requestJson = moshi.adapter(SendMessageRequest::class.java).toJson(SendMessageRequest(message))

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
                .url("${httpClient.config.baseUrl}/api/v1/chat/session/$sessionId/message/stream")
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
                            val chunk = moshi.adapter(ChatStreamChunk::class.java).fromJson(data)
                            if (chunk != null) {
                                emit(chunk)
                            }
                        }
                    }
                }
            }

            response.close()
        }
    }
}