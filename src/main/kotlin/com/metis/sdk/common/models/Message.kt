package com.metis.sdk.common.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * Represents a message type in the Metis system.
 */
enum class MessageType {
    @Json(name = "USER")
    USER,

    @Json(name = "ASSISTANT")
    ASSISTANT,

    @Json(name = "SYSTEM")
    SYSTEM
}

/**
 * Represents a message in the Metis system.
 */
@JsonClass(generateAdapter = true)
data class Message(
    val type: MessageType,
    val content: String? = null,
    val attachments: List<Attachment>? = null,
    val metadata: Map<String, Any>? = null
)

/**
 * Represents an attachment in a message.
 */
@JsonClass(generateAdapter = true)
data class Attachment(
    val type: String,
    val url: String,
    val name: String? = null,
    val metadata: Map<String, Any>? = null
)