# Java Interoperability

The Metis SDK is written in Kotlin but is fully compatible with Java. This guide explains how to use the SDK from Java code.

## Basic Usage

```java
import com.metis.sdk.MetisClient;
import com.metis.sdk.models.bot.*;
import com.metis.sdk.models.chat.*;

public class JavaExample {
    public static void main(String[] args) {
        // Initialize the SDK
        MetisClient metis = MetisClient.create("your-api-key");
        
        // Create a bot
        Provider provider = new Provider("openai_chat_completion", "gpt-4o");
        ProviderConfig providerConfig = new ProviderConfig(provider);
        
        BotCreationRequest botRequest = new BotCreationRequest(
            "Java Assistant",
            "You are a helpful assistant for Java developers.",
            providerConfig,
            null, null, null,
            "A bot for Java developers",
            null, true, false, false, true, false
        );
        
        BotOutputModel bot = metis.getBot().create(botRequest);
        
        // Create a chat session
        ChatUser user = new ChatUser("java-user-123", "Java User", null, null);
        SessionResponse session = metis.getChat().createSession(bot.getId(), user, null);
        
        // Send a message
        Message message = new Message(MessageType.USER, "How do I handle exceptions in Java?", null, null);
        ChatMessage response = metis.getChat().sendMessage(session.getId(), message);
        
        // Print the response
        System.out.println(response.getMessage().getContent());
    }
}
```

## Handling Kotlin Data Classes

Kotlin data classes are compiled to Java classes with getters and setters. Use the appropriate getters to access properties:

```java
// Accessing properties of a bot
String botId = bot.getId();
String botName = bot.getName();
boolean isEnabled = bot.getEnabled();
```

## Handling Nullable Types

Kotlin's nullable types are represented as `@Nullable` annotations in Java. Always check for null:

```java
String instructions = bot.getInstructions();
if (instructions != null) {
    System.out.println(instructions);
}
```

## Working with Collections

Kotlin's immutable collections are converted to Java's read-only collections:

```java
// Get all bots
List<BotOutputModel> bots = metis.getBot().list();

// Iterate through bots
for (BotOutputModel bot : bots) {
    System.out.println(bot.getName());
}
```

## Handling Default Parameters

Kotlin functions with default parameters are compiled to multiple Java methods with different signatures:

```java
// Using a method with all parameters
metis.getChat().getSessions("user-id", "bot-id", 0, 10);

// Using a method with fewer parameters (others use defaults)
metis.getChat().getSessions("user-id", null);
```

## Working with Kotlin Extensions

Kotlin extension functions are compiled to static methods in Java:

```java
// Instead of using Kotlin extension function:
// myBot.clone(cloneRequest)

// In Java, use the static method:
BotOutputModel clonedBot = BotClientKt.clone(myBot, cloneRequest);
```

## Handling Coroutines

If the SDK exposes suspending functions, they will be accessible in Java through callbacks or futures:

```java
// For a Kotlin suspending function:
// suspend fun sendAsyncMessage(sessionId: String, message: Message): AsyncTaskResponse

// In Java, use the callback version:
metis.getChat().sendAsyncMessage(
    sessionId,
    message,
    new Continuation<AsyncTaskResponse>() {
        @Override
        public void resumeWith(Object result) {
            if (result instanceof Result.Success) {
                AsyncTaskResponse response = (AsyncTaskResponse) ((Result.Success) result).getValue();
                System.out.println("Task ID: " + response.getTaskId());
            } else {
                Throwable error = ((Result.Failure) result).getException();
                System.err.println("Error: " + error.getMessage());
            }
        }
    }
);
```

## Builder Pattern for Request Objects

For Java convenience, you can use the builder pattern for complex request objects:

```java
import com.metis.sdk.models.bot.BotCreationRequest;
import com.metis.sdk.models.bot.Provider;
import com.metis.sdk.models.bot.ProviderConfig;

public class JavaBuilderExample {
    public static void main(String[] args) {
        // Create a bot using builder pattern
        BotCreationRequest botRequest = new BotCreationRequest.Builder()
            .name("Java Assistant")
            .instructions("You are a helpful assistant for Java developers.")
            .providerConfig(
                new ProviderConfig.Builder()
                    .provider(
                        new Provider.Builder()
                            .name("openai_chat_completion")
                            .model("gpt-4o")
                            .build()
                    )
                    .temperature(0.7)
                    .build()
            )
            .description("A bot for Java developers")
            .enabled(true)
            .memoryEnabled(true)
            .build();
            
        BotOutputModel bot = metis.getBot().create(botRequest);
    }
}
```

## Exception Handling

Handle exceptions the Java way:

```java
try {
    BotOutputModel bot = metis.getBot().get("non-existent-id");
} catch (MetisApiException e) {
    System.err.println("API Error: " + e.getMessage());
    System.err.println("Status Code: " + e.getStatusCode());
} catch (MetisAuthException e) {
    System.err.println("Authentication Error: " + e.getMessage());
} catch (Exception e) {
    System.err.println("Unexpected Error: " + e.getMessage());
}
```

## Complete Java Example

```java
import com.metis.sdk.MetisClient;
import com.metis.sdk.models.bot.*;
import com.metis.sdk.models.chat.*;
import com.metis.sdk.exceptions.*;

import java.util.List;

public class CompleteJavaExample {
    public static void main(String[] args) {
        try {
            // Initialize the SDK
            MetisClient metis = MetisClient.create("your-api-key");
            
            // Create a bot
            BotCreationRequest botRequest = new BotCreationRequest(
                "Java Assistant",
                "You are a helpful assistant for Java developers.",
                new ProviderConfig(
                    new Provider("openai_chat_completion", "gpt-4o")
                ),
                null, null, null, "A bot for Java developers",
                null, true, false, false, true, false
            );
            
            BotOutputModel bot = metis.getBot().create(botRequest);
            System.out.println("Created bot: " + bot.getName() + " (ID: " + bot.getId() + ")");
            
            // Create a chat session
            ChatUser user = new ChatUser("java-user-123", "Java User", null, null);
            SessionResponse session = metis.getChat().createSession(bot.getId(), user, null);
            System.out.println("Created session: " + session.getId());
            
            // Send a message
            Message message = new Message(MessageType.USER, "What are the best practices for Java exception handling?", null, null);
            ChatMessage response = metis.getChat().sendMessage(session.getId(), message);
            
            // Print the response
            System.out.println("\nBot Response:");
            System.out.println(response.getMessage().getContent());
            
            // List all bots
            List<BotOutputModel> bots = metis.getBot().list();
            System.out.println("\nAll Bots:");
            for (BotOutputModel b : bots) {
                System.out.println("- " + b.getName() + " (ID: " + b.getId() + ")");
            }
            
            // Get session messages
            PaginatedMessageResponse messages = metis.getChat().getSessionMessages(session.getId(), 0, 10);
            System.out.println("\nSession Messages:");
            for (ChatMessage msg : messages.getMessages()) {
                String role = msg.getMessage().getType().name();
                System.out.println(role + ": " + msg.getMessage().getContent());
            }
            
        } catch (MetisApiException e) {
            System.err.println("API Error: " + e.getMessage());
            System.err.println("Status Code: " + e.getStatusCode());
        } catch (MetisAuthException e) {
            System.err.println("Authentication Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```
