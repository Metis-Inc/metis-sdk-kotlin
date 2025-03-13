# Wrapper

The Wrapper client provides direct access to underlying AI providers.

## Initialize

```kotlin
val wrapperClient = metis.wrapper
```

## Direct Chat Completions

```kotlin
val completionRequest = ChatCompletionRequest(
    messages = listOf(
        Message(
            type = MessageType.USER,
            content = "Explain quantum computing"
        )
    ),
    temperature = 0.7,
    maxTokens = 1000
)

val completion = metis.wrapper.getChatCompletions(
    provider = "openai_chat_completion",
    request = completionRequest
)
```

## Stream Chat Completions

```kotlin
val completionRequest = ChatCompletionRequest(
    messages = listOf(
        Message(
            type = MessageType.USER,
            content = "Write a poem about the ocean"
        )
    )
)

metis.wrapper.streamChatCompletions(
    provider = "openai_chat_completion",
    request = completionRequest
).collect { chunk ->
    print(chunk)
}
```

## Get Embeddings

```kotlin
val embeddingRequest = EmbeddingRequest(
    input = listOf("This is a sentence to embed.")
)

val embeddings = metis.wrapper.getEmbeddings(
    provider = "openai",
    request = embeddingRequest
)
```

## Request Models

### ChatCompletionRequest

```kotlin
data class ChatCompletionRequest(
    val messages: List<Message>,
    val model: String? = null,
    val temperature: Double? = null,
    val topP: Double? = null,
    val n: Int? = null,
    val stream: Boolean = false,
    val maxTokens: Int? = null,
    val presencePenalty: Double? = null,
    val frequencyPenalty: Double? = null,
    val logitBias: Map<String, Double>? = null,
    val user: String? = null,
    val functions: List<BotFunction>? = null,
    val functionCall: String? = null
)
```

### EmbeddingRequest

```kotlin
data class EmbeddingRequest(
    val input: List<String>,
    val model: String? = null
)
```

## Response Models

### ChatCompletionResponse

```kotlin
data class ChatCompletionResponse(
    val id: String,
    val choices: List<ChatCompletionChoice>,
    val created: Long,
    val model: String,
    val usage: TokenUsage?
)
```

### ChatCompletionChoice

```kotlin
data class ChatCompletionChoice(
    val index: Int,
    val message: Message,
    val finishReason: String?
)
```

### EmbeddingResponse

```kotlin
data class EmbeddingResponse(
    val data: List<Embedding>,
    val model: String,
    val usage: TokenUsage?
)
```

### Embedding

```kotlin
data class Embedding(
    val embedding: List<Double>,
    val index: Int
)
```

### TokenUsage

```kotlin
data class TokenUsage(
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
)
```

## Use Cases

### Direct Provider Access

The Wrapper client is useful when you need direct access to provider capabilities without the additional features of the Bot and Chat clients:

```kotlin
// Generate text directly from a provider
val completionRequest = ChatCompletionRequest(
    messages = listOf(
        Message(
            type = MessageType.SYSTEM,
            content = "You are a helpful assistant specialized in mathematics."
        ),
        Message(
            type = MessageType.USER,
            content = "Solve the equation: 2x + 5 = 15"
        )
    )
)

val completion = metis.wrapper.getChatCompletions(
    provider = "openai_chat_completion",
    request = completionRequest
)

println(completion.choices.first().message.content)
```

### Custom Embeddings

Generate embeddings for semantic search or other vector operations:

```kotlin
val embeddingRequest = EmbeddingRequest(
    input = listOf(
        "The quick brown fox jumps over the lazy dog",
        "A fast auburn canine leaps above the inactive hound"
    )
)

val embeddings = metis.wrapper.getEmbeddings(
    provider = "openai",
    request = embeddingRequest
)

// Calculate similarity between embeddings
val embedding1 = embeddings.data[0].embedding
val embedding2 = embeddings.data[1].embedding

// Cosine similarity calculation
val dotProduct = embedding1.zip(embedding2).sumOf { (a, b) -> a * b }
val magnitude1 = sqrt(embedding1.sumOf { it * it })
val magnitude2 = sqrt(embedding2.sumOf { it * it })
val similarity = dotProduct / (magnitude1 * magnitude2)

println("Similarity: $similarity")
```
