package com.metis.sdk.chat

import com.metis.sdk.common.models.Message
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * Represents a user in a chat session.
 */
@JsonClass(generateAdapter = true)
data class ChatUser(
    val id: String,
    val name: String? = null,
    val email: String? = null,
    val metadata: Map<String, Any>? = null
)

/**
 * Represents a chat message.
 */
@JsonClass(generateAdapter = true)
data class ChatMessage(
    val id: String,
    val sessionId: String,
    val message: Message,
    val timestamp: Date
)

/**
 * Request to create a new chat session.
 */
@JsonClass(generateAdapter = true)
data class CreateSessionRequest(
    val botId: String,
    val user: ChatUser? = null,
    val initialMessages: List<Message>? = null
)

/**
 * Request to update a chat session.
 */
@JsonClass(generateAdapter = true)
data class UpdateSessionRequest(
    val user: ChatUser? = null,
    val headline: String? = null
)

/**
 * Response containing session information.
 */
@JsonClass(generateAdapter = true)
data class SessionResponse(
    val id: String,
    val botId: String,
    val user: ChatUser?,
    val messages: List<ChatMessage>,
    val startDate: Date,
    val headline: String?
)

/**
 * Basic session information.
 */
@JsonClass(generateAdapter = true)
data class SessionInfo(
    val id: String,
    val botId: String,
    val user: ChatUser?,
    val startDate: Date,
    val headline: String?
)

/**
 * Request to send a message to a chat session.
 */
@JsonClass(generateAdapter = true)
data class SendMessageRequest(
    val message: Message
)

/**
 * Response for paginated messages.
 */
@JsonClass(generateAdapter = true)
data class PaginatedMessageResponse(
    val messages: List<ChatMessage>,
    val totalPages: Int,
    val totalElements: Int,
    val currentPage: Int
)

/**
 * Response for paginated sessions.
 */
@JsonClass(generateAdapter = true)
data class PaginatedSessionsResponse(
    val sessions: List<SessionResponse>,
    val totalPages: Int,
    val totalElements: Int,
    val currentPage: Int
)

/**
 * Response for async task creation.
 */
@JsonClass(generateAdapter = true)
data class AsyncTaskCreationResponse(
    val taskId: String,
    val status: String
)

/**
 * Result of an async message task.
 */
@JsonClass(generateAdapter = true)
data class AsyncMessageResult(
    val taskId: String,
    val status: String,
    val message: ChatMessage?
)

/**
 * Chunk of a streaming chat response.
 */
@JsonClass(generateAdapter = true)
data class ChatStreamChunk(
    val id: String,
    val content: String?,
    val done: Boolean
)