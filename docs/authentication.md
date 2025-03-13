# Authentication

The Authentication client provides methods for user management and API key operations.

## Initialize

```kotlin
val authClient = metis.auth
```

## User Authentication

### Login

```kotlin
val authResponse = metis.auth.login("user@example.com", "password")
// The response contains a JWT token
val token = authResponse.token
```

### Register

```kotlin
val userInfo = metis.auth.register(
    username = "user@example.com",
    password = "password",
    firstName = "John",
    lastName = "Doe"
)
```

### Get Current User

```kotlin
val user = metis.auth.getUser()
```

### Activate Account

```kotlin
val activatedUser = metis.auth.activate("activation-code")
```

### Resend Activation Email

```kotlin
metis.auth.resendActivation("user@example.com")
```

## API Key Management

### Generate a New API Key

```kotlin
val apiKey = metis.auth.generateApiKey()
```

### Get All API Keys

```kotlin
val keys = metis.auth.getApiKeys()
```

### Delete an API Key

```kotlin
metis.auth.deleteApiKey("api-key-to-delete")
```

## Response Models

### UserInfo

```kotlin
data class UserInfo(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val activated: Boolean,
    val roles: List<String>
)
```

### AuthResponse

```kotlin
data class AuthResponse(
    val token: String
)
```

### ApiKey

```kotlin
data class ApiKey(
    val id: String,
    val key: String
)
```

### UserApiKeys

```kotlin
data class UserApiKeys(
    val userId: String,
    val keys: List<ApiKey>
)
```
