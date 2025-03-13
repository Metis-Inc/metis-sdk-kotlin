# Credit

The Credit client provides methods for managing user credits and transactions.

## Initialize

```kotlin
val creditClient = metis.credit
```

## Get Credit Statement

```kotlin
val statement = metis.credit.getStatement()
```

## Response Models

### UserStatement

```kotlin
data class UserStatement(
    val userId: String,
    val balance: Double,
    val transactions: List<Transaction>
)
```

### Transaction

```kotlin
data class Transaction(
    val id: String,
    val userId: String,
    val amount: Double,
    val balance: Double,
    val agent: String,
    val reason: String,
    val timestamp: Date
)
```

## Understanding Credits

Credits are used to pay for API usage. Different operations consume different amounts of credits:

- Chat messages: Based on input and output tokens
- File processing: Based on file size and complexity
- Embedding generation: Based on text length
- Knowledge base queries: Based on query complexity

You can monitor your credit usage through the statement endpoint to manage costs effectively.
