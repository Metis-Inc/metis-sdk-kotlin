# Bots

The Bot client provides methods for creating and managing AI assistants.

## Initialize

```kotlin
val botClient = metis.bot
```

## Create a Bot

```kotlin
val bot = metis.bot.create(
    BotCreationRequest(
        name = "My Assistant",
        instructions = "You are a helpful assistant.",
        providerConfig = ProviderConfig(
            provider = Provider(
                name = "openai_chat_completion", 
                model = "gpt-4o"
            ),
            temperature = 0.7
        ),
        description = "A general-purpose assistant",
        enabled = true,
        memoryEnabled = true
    )
)
```

## Get Bot Details

```kotlin
val bot = metis.bot.get("bot-id")
```

## List All Bots

```kotlin
val bots = metis.bot.list()
```

## Update a Bot

```kotlin
val updatedBot = metis.bot.update(
    "bot-id",
    BotUpdateRequest(
        name = "Updated Assistant",
        instructions = "New instructions for the assistant."
    )
)
```

## Partial Update (Patch)

```kotlin
val patchedBot = metis.bot.patch(
    "bot-id",
    BotUpdateRequest(
        name = "Patched Name"
    )
)
```

## Clone a Bot

```kotlin
val clonedBot = metis.bot.clone(
    "bot-id",
    BotCloneRequest(
        name = "Cloned Assistant",
        description = "A clone of my assistant"
    )
)
```

## Delete a Bot

```kotlin
metis.bot.delete("bot-id")
```

## Request Models

### BotCreationRequest

```kotlin
data class BotCreationRequest(
    val name: String,
    val instructions: String?,
    val providerConfig: ProviderConfig,
    val corpora: List<Corpus>? = null,
    val summarizer: SummarizationConfig? = null,
    val functions: List<BotFunction>? = null,
    val description: String? = null,
    val avatar: StorageFile? = null,
    val enabled: Boolean = true,
    val autoGenerateHeadline: Boolean = false,
    val public: Boolean = false,
    val memoryEnabled: Boolean = true,
    val googleSearchEnabled: Boolean = false
)
```

### BotUpdateRequest

```kotlin
data class BotUpdateRequest(
    val name: String? = null,
    val instructions: String? = null,
    val providerConfig: ProviderConfig? = null,
    val corpora: List<Corpus>? = null,
    val summarizer: SummarizationConfig? = null,
    val functions: List<BotFunction>? = null,
    val description: String? = null,
    val avatar: StorageFile? = null,
    val enabled: Boolean? = null,
    val autoGenerateHeadline: Boolean? = null,
    val public: Boolean? = null,
    val memoryEnabled: Boolean? = null,
    val googleSearchEnabled: Boolean? = null
)
```

### BotCloneRequest

```kotlin
data class BotCloneRequest(
    val name: String,
    val description: String? = null
)
```

## Response Models

### BotOutputModel

```kotlin
data class BotOutputModel(
    val id: String,
    val name: String,
    val instructions: String?,
    val providerConfig: ProviderConfig,
    val corpora: List<Corpus>?,
    val summarizer: SummarizationConfig?,
    val functions: List<BotFunction>?,
    val description: String?,
    val avatar: StorageFile?,
    val enabled: Boolean,
    val autoGenerateHeadline: Boolean,
    val public: Boolean,
    val memoryEnabled: Boolean,
    val googleSearchEnabled: Boolean,
    val createdAt: Date
)
```

## Advanced Configuration

### Bot Functions

You can define custom functions that your bot can call:

```kotlin
val functions = listOf(
    BotFunction(
        name = "get_weather",
        description = "Get the current weather in a location",
        parameters = mapOf(
            "location" to FunctionParameter(
                type = "string",
                description = "The city and state, e.g., San Francisco, CA",
                required = true
            ),
            "unit" to FunctionParameter(
                type = "string",
                description = "The unit of temperature (celsius or fahrenheit)",
                required = false,
                enum = listOf("celsius", "fahrenheit")
            )
        )
    )
)

val bot = metis.bot.create(
    BotCreationRequest(
        name = "Weather Bot",
        instructions = "You help users check the weather.",
        providerConfig = ProviderConfig(
            provider = Provider(name = "openai_chat_completion", model = "gpt-4o")
        ),
        functions = functions
    )
)
```

### Summarization

Enable automatic summarization for chat sessions:

```kotlin
val bot = metis.bot.create(
    BotCreationRequest(
        name = "Summarizing Bot",
        instructions = "You provide summaries of conversations.",
        providerConfig = ProviderConfig(
            provider = Provider(name = "openai_chat_completion", model = "gpt-4o")
        ),
        summarizer = SummarizationConfig(
            provider = "openai_chat_completion",
            enabled = true
        )
    )
)
```

### Knowledge Bases (Corpora)

Connect your bot to knowledge bases:

```kotlin
val bot = metis.bot.create(
    BotCreationRequest(
        name = "Knowledge Bot",
        instructions = "You answer questions based on the provided knowledge base.",
        providerConfig = ProviderConfig(
            provider = Provider(name = "openai_chat_completion", model = "gpt-4o")
        ),
        corpora = listOf(
            Corpus(
                id = "corpus-id",
                name = "Product Documentation",
                type = "TEXT"
            )
        )
    )
)
```
