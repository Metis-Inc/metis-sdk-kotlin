# Advanced Configuration

This guide covers advanced configuration options for the Metis SDK.

## Custom HTTP Client

You can customize the HTTP client used by the SDK:

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    baseUrl = "https://api.metisai.ir",
    httpClientConfig = {
        // Configure OkHttp client
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        
        // Add custom interceptors
        addInterceptor(LoggingInterceptor())
        
        // Configure proxy if needed
        proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("proxy.example.com", 8080)))
    }
)
```

## Custom JSON Serialization

You can customize the JSON serialization:

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    jsonConfig = {
        // Configure Gson
        setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        registerTypeAdapter(Date::class.java, DateTypeAdapter())
    }
)
```

## Logging Configuration

Configure logging levels:

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    logLevel = LogLevel.BODY  // Log request and response bodies
)
```

Available log levels:
- `LogLevel.NONE`: No logging
- `LogLevel.BASIC`: Log request method and URL
- `LogLevel.HEADERS`: Log request and response headers
- `LogLevel.BODY`: Log request and response bodies (including headers)

## Retry Configuration

Configure automatic retries for failed requests:

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    retryConfig = RetryConfig(
        maxRetries = 3,
        retryCondition = { response, exception ->
            // Retry on 429 (rate limit) and 5xx errors
            exception is MetisNetworkException || 
            (response != null && (response.code == 429 || response.code >= 500))
        },
        backoffStrategy = ExponentialBackoff(
            initialDelayMs = 1000,
            maxDelayMs = 10000,
            factor = 2.0
        )
    )
)
```

## Timeout Configuration

Configure timeouts for different types of requests:

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    timeoutConfig = TimeoutConfig(
        connectTimeoutMs = 5000,  // 5 seconds
        readTimeoutMs = 30000,    // 30 seconds
        writeTimeoutMs = 10000    // 10 seconds
    )
)
```

## Custom Base URL

If you're using a custom deployment or a different environment:

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    baseUrl = "https://api.custom-deployment.example.com"
)
```

## Thread Pool Configuration

Configure the thread pools used for asynchronous operations:

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    threadPoolConfig = ThreadPoolConfig(
        corePoolSize = 5,
        maxPoolSize = 10,
        keepAliveTimeMs = 60000,
        queueCapacity = 100
    )
)
```

## Custom Headers

Add custom headers to all requests:

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    headers = mapOf(
        "X-Custom-Header" to "custom-value",
        "User-Agent" to "MyApp/1.0"
    )
)
```

## Proxy Configuration

Configure a proxy for all requests:

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    proxyConfig = ProxyConfig(
        host = "proxy.example.com",
        port = 8080,
        username = "proxyuser",  // Optional
        password = "proxypass"   // Optional
    )
)
```

## Certificate Pinning

Enhance security with certificate pinning:

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    certificatePinning = CertificatePinningConfig(
        pins = listOf(
            "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
            "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB="
        )
    )
)
```

## Complete Advanced Configuration Example

```kotlin
val client = MetisClient.create(
    apiKey = "your-api-key",
    baseUrl = "https://api.metisai.ir",
    httpClientConfig = {
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        addInterceptor(LoggingInterceptor())
    },
    jsonConfig = {
        setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    },
    logLevel = LogLevel.BODY,
    retryConfig = RetryConfig(
        maxRetries = 3,
        retryCondition = { response, exception ->
            exception is MetisNetworkException || 
            (response != null && (response.code == 429 || response.code >= 500))
        },
        backoffStrategy = ExponentialBackoff(
            initialDelayMs = 1000,
            maxDelayMs = 10000,
            factor = 2.0
        )
    ),
    headers = mapOf(
        "X-Custom-Header" to "custom-value"
    ),
    proxyConfig = ProxyConfig(
        host = "proxy.example.com",
        port = 8080
    )
)
```
