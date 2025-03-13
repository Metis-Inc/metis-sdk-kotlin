# Storage

The Storage client provides methods for uploading and managing files.

## Initialize

```kotlin
val storageClient = metis.storage
```

## Upload File

```kotlin
val file = metis.storage.uploadFile(File("image.jpg"))
```

## Response Models

### GatewayStorageFile

```kotlin
data class GatewayStorageFile(
    val objectName: String,
    val url: String,
    val size: Long,
    val contentType: String,
    val name: String? = null
)
```

## Usage with Bots

You can use the Storage client to upload avatar images for bots:

```kotlin
// Upload an avatar image
val avatarFile = metis.storage.uploadFile(File("avatar.png"))

// Create a bot with the avatar
val bot = metis.bot.create(
    BotCreationRequest(
        name = "My Assistant",
        instructions = "You are a helpful assistant.",
        providerConfig = ProviderConfig(
            provider = Provider(name = "openai_chat_completion", model = "gpt-4o")
        ),
        avatar = StorageFile(
            objectName = avatarFile.objectName,
            url = avatarFile.url,
            size = avatarFile.size,
            contentType = avatarFile.contentType
        )
    )
)
```

## Usage with Chat

You can upload files to include as attachments in messages:

```kotlin
// Upload a file
val uploadedFile = metis.storage.uploadFile(File("document.pdf"))

// Send a message with the file as an attachment
val message = Message(
    type = MessageType.USER,
    content = "Please analyze this document.",
    attachments = listOf(
        Attachment(
            type = "file",
            url = uploadedFile.url,
            name = uploadedFile.name ?: "document.pdf"
        )
    )
)

val response = metis.chat.sendMessage(
    sessionId = "session-id",
    message = message
)
```

## Supported File Types

The Storage client supports various file types, including:

- Images (JPG, PNG, GIF)
- Documents (PDF, DOCX, TXT)
- Audio files (MP3, WAV)
- Video files (MP4)

The file type is automatically detected based on the file extension.
