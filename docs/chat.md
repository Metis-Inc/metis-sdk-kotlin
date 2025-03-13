# Chat

The Chat client provides methods for creating chat sessions and exchanging messages with bots.

## Initialize

```kotlin
val chatClient = metis.chat
```

## Create a Chat Session

```kotlin
val session = metis.chat.createSession(
    botId = "bot-id",
    user = ChatUser(
        id = "user123",
        name = "John Doe",
        email = "john@example.com"
    ),
    initialMessages = listOf(
        Message(
            type = MessageType.SYSTEM,
            content = "The conversation is starting now."
        )
    )
)
```

## Send a Message

```kotlin
val message = Message(
    type = MessageType.USER,
    content = "Hello, how are you?"
)

val response = metis.chat.sendMessage(
    sessionId = session.id,
    message = message
)
```

## Stream a Message

For a better user experience with long responses, you can stream the bot's response:

```kotlin
val message = Message(
    type = MessageType.USER,
    content = "Tell me a story about a brave knight."
)

metis.chat.streamMessage(session.id, message)
    .collect { chunk ->
        print(chunk.content)
    }
```

## Send Asynchronous Message

For potentially long-running operations:

```kotlin
val message = Message(
    type = MessageType.USER,
    content = "Analyze this complex data."
)

val task = metis.chat.sendAsyncMessage(session.id, message)

// Later, check the result
val result = metis.chat.checkAsyncMessageResult(session.id, task.taskId)
```

## Get Session Messages

```kotlin
val messages = metis.chat.getSessionMessages(
    sessionId = session.id,
    page = 0,
    size = 20
)
```

## Update Session

```kotlin
val updatedSession = metis.chat.updateSession(
    sessionId = session.id,
    user = ChatUser(id = "user123", name = "John Updated"),
    headline = "Important conversation"
)
```

## Get Session Info

```kotlin
val sessionInfo = metis.chat.getSessionInfo(session.id)
```

## List Sessions

```kotlin
// Get all sessions
val allSessions = metis.chat.getSessions()

// Filter by user or bot
val userSessions = metis.chat.getSessions(userId = "user123")
val botSessions = metis.chat.getSessions(botId = "bot-id")

// Paginated sessions
val paginatedSessions = metis.chat.getSessionsPaginated(
    userId = "user123",
    page = 0,
    size = 10
)
```

## Delete a Session

```kotlin
metis.chat.deleteSession("session-id")
```

## Request Models

### ChatUser

```kotlin
data class ChatUser(
    val id: String,
    val name: String? = null,
    val email: String? = null,
    val metadata: Map<String, Any>? = null
)
```

### Message

```kotlin
data class Message(
    val type: MessageType,
    val content: String? = null,
    val attachments: List<Attachment>? = null,
    val metadata: Map<String, Any>? = null
)
```

### MessageType

```kotlin
enum class MessageType {
    USER,
    ASSISTANT,
    SYSTEM
}
```

### Attachment

```kotlin
data class Attachment(
    val type: String,
    val url: String,
    val name: String? = null,
    val metadata: Map<String, Any>? = null
)
```

## Response Models

### SessionResponse

```kotlin
data class SessionResponse(
    val id: String,
    val botId: String,
    val user: ChatUser?,
    val messages: List<ChatMessage>,
    val startDate: Date,
    val headline: String?
)
```

### ChatMessage

```kotlin
data class ChatMessage(
    val id: String,
    val sessionId: String,
    val message: Message,
    val timestamp: Date
)
```

### PaginatedMessageResponse

```kotlin
data class PaginatedMessageResponse(
    val messages: List<ChatMessage>,
    val totalPages: Int,
    val totalElements: Int,
    val currentPage: Int
)
```

### ChatStreamChunk

```kotlin
data class ChatStreamChunk(
    val id: String,
    val content: String?,
    val done: Boolean
)
```

## Advanced Usage

### Sending Files

```kotlin
val message = Message(
    type = MessageType.USER,
    content = "Please analyze this image.",
    attachments = listOf(
        Attachment(
            type = "image",
            url = "https://example.com/image.jpg",
            name = "image.jpg"
        )
    )
)

val response = metis.chat.sendMessage(
    sessionId = session.id,
    message = message
)
```

### User Metadata

You can include custom metadata with the user:

```kotlin
val session = metis.chat.createSession(
    botId = "bot-id",
    user = ChatUser(
        id = "user123",
        name = "John Doe",
        metadata = mapOf(
            "subscription" to "premium",
            "preferences" to mapOf(
                "language" to "en",
                "theme" to "dark"
            )
        )
    )
)
```
