# Metis SDK for Kotlin/Java

The official Metis SDK for Kotlin and Java applications.

## Installation

### Gradle

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Metis-Inc:metis-sdk-kotlin:1.0.0'
}
```

### Gradle Kotlin DSL

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.Metis-Inc:metis-sdk-kotlin:1.0.0")
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

### Initialize the SDK

```kotlin
// Kotlin
val metis = MetisClient.create("your-api-key")
```

```java
// Java
MetisClient metis = MetisClient.create("your-api-key");
```

### Create a Bot

```kotlin
// Kotlin
val bot = metis.bot.create(
    BotCreationRequest(
        name = "My Assistant",
        instructions = "You are a helpful assistant.",
        providerConfig = ProviderConfig(
            provider = Provider(name = "openai_chat_completion", model = "gpt-4o")
        )
    )
)
```

```java
// Java
ProviderConfig providerConfig = new ProviderConfig(
    new Provider("openai_chat_completion", "gpt-4o", false, false),
    0.7,
    null,
    null,
    null,
    null
);

BotCreationRequest request = new BotCreationRequest(
    "My Assistant",
    "You are a helpful assistant.",
    providerConfig,
    null,
    null,
    null,
    null,
    null,
    true,
    false,
    false,
    true,
    false
);

BotOutputModel bot = metis.getBot().create(request);
```

### Create a Chat Session

```kotlin
// Kotlin
val session = metis.chat.createSession(
    botId = bot.id,
    user = ChatUser(id = "user123", name = "John Doe")
)
```

```java
// Java
SessionResponse session = metis.getChat().createSession(
    bot.getId(),
    new ChatUser("user123", "John Doe", null, null),
    null
);
```

### Send a Message

```kotlin
// Kotlin
val message = Message(
    type = MessageType.USER,
    content = "Hello, how are you?"
)

val response = metis.chat.sendMessage(
    sessionId = session.id,
    message = message
)
```

```java
// Java
Message message = new Message(
    MessageType.USER,
    "Hello, how are you?",
    null,
    null
);

ChatMessage response = metis.getChat().sendMessage(
    session.getId(),
    message
);
```

### Stream a Message

```kotlin
// Kotlin
val message = Message(
    type = MessageType.USER,
    content = "Tell me a story about a brave knight."
)

metis.chat.streamMessage(session.id, message)
    .collect { chunk ->
        print(chunk.content)
    }
```

```java
// Java
Message message = new Message(
    MessageType.USER,
    "Tell me a story about a brave knight.",
    null,
    null
);

metis.getChat().streamMessage(session.getId(), message)
    .subscribe(chunk -> {
        System.out.print(chunk.getContent());
    });
```

## Documentation

For complete documentation, visit [https://docs.metisai.ir](https://docs.metisai.ir).

## License

This SDK is distributed under the MIT License. See LICENSE for more information.