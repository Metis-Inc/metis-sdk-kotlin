# Meta

The Meta client provides methods for accessing metadata about available models and providers.

## Initialize

```kotlin
val metaClient = metis.meta
```

## Get Available Providers

```kotlin
val meta = metis.meta.getMeta()
```

This returns information about all available providers and models, including:
- Chat providers
- Embedding providers
- Chunking providers
- Summarization providers
- Reranking providers
- Generation providers

## Get Pricing

```kotlin
val pricing = metis.meta.getPricing()
```

This returns pricing information for different providers and models.

## Response Models

### MetaResponse

```kotlin
data class MetaResponse(
    val summarizers: List<Provider>,
    val chunkers: List<Provider>,
    val chatProviders: List<Provider>,
    val embeddingProviders: List<Provider>,
    val rerankerProviders: List<Provider>,
    val generationProviders: List<ProviderWithTags>
)
```

### Provider

```kotlin
data class Provider(
    val name: String,
    val model: String? = null,
    val acceptImageAttachment: Boolean = false,
    val acceptFileAttachment: Boolean = false
)
```

### ProviderWithTags

```kotlin
data class ProviderWithTags(
    val name: String,
    val model: String,
    val tags: List<String>
)
```

### PricingResponse

```kotlin
data class PricingResponse(
    val chatProviders: List<PricingProviderDTO>,
    val imageProviders: List<PricingProviderDTO>
)
```

### PricingProviderDTO

```kotlin
data class PricingProviderDTO(
    val name: String,
    val model: String,
    val currency: String,
    val fixedCallIncome: Double,
    val inputTokenUnitIncome: Double,
    val outputTokenUnitIncome: Double
)
```

## Usage Examples

### Selecting the Best Model

You can use the Meta client to dynamically select the best model for your use case:

```kotlin
// Get available models
val meta = metis.meta.getMeta()

// Find models that support image attachments
val imageCapableModels = meta.chatProviders.filter { it.acceptImageAttachment }

// Find the cheapest model
val pricing = metis.meta.getPricing()
val cheapestModel = pricing.chatProviders.minByOrNull { it.outputTokenUnitIncome }

// Create a bot with the selected model
val bot = metis.bot.create(
    BotCreationRequest(
        name = "Cost-Effective Assistant",
        instructions = "You are a helpful assistant.",
        providerConfig = ProviderConfig(
            provider = Provider(
                name = cheapestModel?.name ?: "openai_chat_completion",
                model = cheapestModel?.model ?: "gpt-3.5-turbo"
            )
        )
    )
)
```

### Finding Models with Specific Capabilities

```kotlin
val meta = metis.meta.getMeta()

// Find models with specific tags
val creativeModels = meta.generationProviders.filter { 
    "creative" in it.tags 
}

// Find models that support file attachments
val fileCapableModels = meta.chatProviders.filter { 
    it.acceptFileAttachment 
}
```
