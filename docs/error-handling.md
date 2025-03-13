# Error Handling

The Metis SDK uses exceptions to handle errors. This guide explains how to properly handle these exceptions in your application.

## Exception Types

### MetisApiException

This is the base exception for all API-related errors. It contains:

- `statusCode`: HTTP status code
- `errorCode`: Specific error code from the API
- `message`: Human-readable error message

```kotlin
try {
    val bot = metis.bot.get("non-existent-id")
} catch (e: MetisApiException) {
    println("API Error: ${e.message}")
    println("Status Code: ${e.statusCode}")
    println("Error Code: ${e.errorCode}")
}
```

### MetisAuthException

Thrown when there are authentication issues, such as invalid API keys or expired tokens.

```kotlin
try {
    val user = metis.auth.getUser()
} catch (e: MetisAuthException) {
    println("Authentication Error: ${e.message}")
    // Handle by re-authenticating or prompting for credentials
}
```

### MetisRateLimitException

Thrown when you've exceeded the rate limits for the API.

```kotlin
try {
    val response = metis.chat.sendMessage(sessionId, message)
} catch (e: MetisRateLimitException) {
    println("Rate Limit Exceeded: ${e.message}")
    println("Retry After: ${e.retryAfterSeconds} seconds")
    
    // Implement exponential backoff
    delay(e.retryAfterSeconds * 1000L)
    // Retry the request
}
```

### MetisNetworkException

Thrown when there are network-related issues.

```kotlin
try {
    val bot = metis.bot.create(botRequest)
} catch (e: MetisNetworkException) {
    println("Network Error: ${e.message}")
    // Check connectivity and retry
}
```

### MetisValidationException

Thrown when the request data fails validation.

```kotlin
try {
    val bot = metis.bot.create(
        BotCreationRequest(
            name = "",  // Empty name will fail validation
            instructions = "You are a helpful assistant.",
            providerConfig = ProviderConfig(
                provider = Provider(name = "openai_chat_completion", model = "gpt-4o")
            )
        )
    )
} catch (e: MetisValidationException) {
    println("Validation Error: ${e.message}")
    println("Field: ${e.field}")
    println("Rejected Value: ${e.rejectedValue}")
}
```

## Best Practices

### Structured Error Handling

Use structured error handling to provide a better user experience:

```kotlin
fun createBot(name: String, instructions: String): BotOutputModel? {
    try {
        return metis.bot.create(
            BotCreationRequest(
                name = name,
                instructions = instructions,
                providerConfig = ProviderConfig(
                    provider = Provider(name = "openai_chat_completion", model = "gpt-4o")
                )
            )
        )
    } catch (e: MetisValidationException) {
        println("Invalid input: ${e.message}")
        // Prompt user to correct input
    } catch (e: MetisAuthException) {
        println("Authentication failed: ${e.message}")
        // Redirect to login
    } catch (e: MetisRateLimitException) {
        println("Rate limit exceeded. Please try again in ${e.retryAfterSeconds} seconds.")
        // Implement retry with backoff
    } catch (e: MetisNetworkException) {
        println("Network error: ${e.message}")
        // Check connectivity
    } catch (e: MetisApiException) {
        println("API error: ${e.message}")
        // General error handling
    }
    return null
}
```

### Retry Logic

Implement retry logic for transient errors:

```kotlin
suspend fun sendMessageWithRetry(
    sessionId: String,
    message: Message,
    maxRetries: Int = 3
): ChatMessage? {
    var retries = 0
    var backoffMs = 1000L
    
    while (retries < maxRetries) {
        try {
            return metis.chat.sendMessage(sessionId, message)
        } catch (e: MetisRateLimitException) {
            retries++
            if (retries >= maxRetries) throw e
            
            val waitTime = e.retryAfterSeconds * 1000L
            println("Rate limit exceeded. Retrying in ${waitTime/1000} seconds...")
            delay(waitTime)
        } catch (e: MetisNetworkException) {
            retries++
            if (retries >= maxRetries) throw e
            
            println("Network error. Retrying in ${backoffMs/1000} seconds...")
            delay(backoffMs)
            backoffMs *= 2  // Exponential backoff
        } catch (e: MetisApiException) {
            // Don't retry for other API errors
            throw e
        }
    }
    return null
}
```

### Logging

Implement proper logging for errors:

```kotlin
try {
    val bot = metis.bot.get(botId)
} catch (e: MetisApiException) {
    logger.error("Failed to get bot $botId", e)
    // Additional error handling
}
```

## Common Error Scenarios

### Invalid API Key

```
MetisAuthException: Invalid API key provided
```

Solution: Check that you're using the correct API key and that it hasn't been revoked.

### Resource Not Found

```
MetisApiException: Bot with ID 'xyz' not found (Status: 404, Code: RESOURCE_NOT_FOUND)
```

Solution: Verify that the resource ID exists and that you have permission to access it.

### Rate Limiting

```
MetisRateLimitException: Rate limit exceeded. Retry after 30 seconds. (Status: 429, Code: RATE_LIMIT_EXCEEDED)
```

Solution: Implement rate limiting on your side and use exponential backoff for retries.

### Validation Errors

```
MetisValidationException: Bot name cannot be empty (Field: name, Value: "")
```

Solution: Validate user input before sending requests to the API.

### Insufficient Credits

```
MetisApiException: Insufficient credits (Status: 402, Code: INSUFFICIENT_CREDITS)
```

Solution: Check the user's credit balance and prompt them to add more credits if needed.
