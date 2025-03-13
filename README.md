# Metis SDK for Kotlin/Java

The official Metis SDK for Kotlin and Java applications, providing a seamless interface to interact with the Metis AI platform.

## Installation

### Gradle

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Metis-Inc:metis-sdk-kotlin:main-SNAPSHOT'
}
```

### Gradle Kotlin DSL

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.Metis-Inc:metis-sdk-kotlin:main-SNAPSHOT")
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Metis-Inc</groupId>
        <artifactId>metis-sdk-kotlin</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Quick Start

#### Kotlin
```kotlin
// Initialize the SDK
val metis = MetisClient.create("your-api-key")

// Create a bot
val bot = metis.bot.create(
    BotCreationRequest(
        name = "My Assistant",
        instructions = "You are a helpful assistant.",
        providerConfig = ProviderConfig(
            provider = Provider(name = "openai_chat_completion", model = "gpt-4o")
        )
    )
)

// Create a chat session
val session = metis.chat.createSession(
    botId = bot.id,
    user = ChatUser(id = "user123", name = "John Doe")
)

// Send a message
val message = Message(
    type = MessageType.USER,
    content = "Hello, how are you?"
)

val response = metis.chat.sendMessage(
    sessionId = session.id,
    message = message
)

// Print the bot's response
println(response.message.content)
```

#### Java
```java
// Initialize the SDK
MetisClient metis = MetisClient.Companion.create("your-api-key");

// Create a bot
ProviderConfig providerConfig = new ProviderConfig(
    new Provider("openai_chat_completion", "gpt-4o")
);

BotCreationRequest botRequest = new BotCreationRequest(
    "My Assistant",
    "You are a helpful assistant.",
    providerConfig
);

Bot bot = metis.getBot().create(botRequest);

// Create a chat session
ChatUser user = new ChatUser("user123", "John Doe");
ChatSession session = metis.getChat().createSession(bot.getId(), user);

// Send a message
Message message = new Message(
    MessageType.USER,
    "Hello, how are you?"
);

ChatResponse response = metis.getChat().sendMessage(
    session.getId(),
    message
);

// Print the bot's response
System.out.println(response.getMessage().getContent());
```

## Documentation

The SDK provides access to all Metis API features through specialized clients:

- [**Authentication**](docs/authentication.md): User management, API keys
- [**Bots**](docs/bots.md): Create and manage AI assistants
- [**Chat**](docs/chat.md): Interact with bots through chat sessions
- [**Corpora**](docs/corpora.md): Manage knowledge bases for retrieval-augmented generation (RAG)
- [**Storage**](docs/storage.md): Upload and manage files
- [**Credit**](docs/credit.md): Manage user credits and transactions
- [**Meta**](docs/meta.md): Access metadata about available models and providers
- [**Wrapper**](docs/wrapper.md): Direct access to underlying AI providers

Additional resources:
- [**Error Handling**](docs/error-handling.md): How to handle errors in the SDK
- [**Java Interoperability**](docs/java-interop.md): Using the SDK from Java
- [**Advanced Configuration**](docs/advanced-config.md): Customizing the SDK behavior

## Best Practices

1. **API Key Security**: Never hardcode your API key in your application. Use environment variables or secure storage.

2. **Error Handling**: Always implement proper error handling to gracefully manage API errors.

3. **Resource Management**: Delete unused resources (bots, sessions, corpora) to optimize costs.

4. **Streaming**: For long responses, use streaming to provide a better user experience.

5. **Asynchronous Operations**: For time-consuming operations, use asynchronous methods.

## License

This SDK is distributed under the MIT License. See LICENSE for more information.

## Support

For support, please contact [info@metisai.ir](mailto:info@metisai.ir) or visit [https://docs.metisai.ir](https://docs.metisai.ir).
