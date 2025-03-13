package com.metis.sdk

import com.metis.sdk.auth.*
import com.metis.sdk.bot.*
import com.metis.sdk.chat.ChatClient
import com.metis.sdk.corpora.CorporaClient
import com.metis.sdk.credit.CreditClient
import com.metis.sdk.http.HttpClient
import com.metis.sdk.meta.MetaClient
import com.metis.sdk.storage.StorageClient
import com.metis.sdk.wrapper.WrapperClient
import com.metis.sdk.chat.*
import com.metis.sdk.common.models.Corpus
import com.metis.sdk.common.models.Message
import com.metis.sdk.common.models.ProviderConfig
import com.metis.sdk.corpora.*
import com.metis.sdk.credit.UserStatement
import com.metis.sdk.meta.MetaResponse
import com.metis.sdk.meta.PricingResponse
import com.metis.sdk.storage.GatewayStorageFile
import com.metis.sdk.wrapper.ChatCompletionRequest
import com.metis.sdk.wrapper.ChatCompletionResponse
import com.metis.sdk.wrapper.EmbeddingRequest
import com.metis.sdk.wrapper.EmbeddingResponse
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * Main entry point for the Metis SDK.
 * Provides access to all API clients.
 *
 * @property config Configuration for the SDK
 */
class MetisClient(private val config: MetisConfig) {
    private val httpClient = HttpClient(config)

    /**
     * Client for authentication operations
     */
    val auth: AuthClient by lazy { AuthClient(httpClient) }

    /**
     * Client for chat operations
     */
    val chat: ChatClient by lazy { ChatClient(httpClient) }

    /**
     * Client for bot operations
     */
    val bot: BotClient by lazy { BotClient(httpClient) }

    /**
     * Client for corpora operations
     */
    val corpora: CorporaClient by lazy { CorporaClient(httpClient) }

    /**
     * Client for storage operations
     */
    val storage: StorageClient by lazy { StorageClient(httpClient) }

    /**
     * Client for credit operations
     */
    val credit: CreditClient by lazy { CreditClient(httpClient) }

    /**
     * Client for meta operations
     */
    val meta: MetaClient by lazy { MetaClient(httpClient) }

    /**
     * Client for wrapper operations
     */
    val wrapper: WrapperClient by lazy { WrapperClient(httpClient) }

    @JvmOverloads
    fun createBot(
        name: String,
        instructions: String? = null,
        providerConfig: ProviderConfig,
        corpora: List<Corpus>? = null,
        summarizer: SummarizationConfig? = null,
        functions: List<BotFunction>? = null,
        description: String? = null,
        avatar: StorageFile? = null,
        enabled: Boolean = true,
        autoGenerateHeadline: Boolean = false,
        public: Boolean = false,
        memoryEnabled: Boolean = true,
        googleSearchEnabled: Boolean = false
    ): BotOutputModel = runBlocking {
        val request = BotCreationRequest(
            name = name,
            instructions = instructions,
            providerConfig = providerConfig,
            corpora = corpora,
            summarizer = summarizer,
            functions = functions,
            description = description,
            avatar = avatar,
            enabled = enabled,
            autoGenerateHeadline = autoGenerateHeadline,
            public = public,
            memoryEnabled = memoryEnabled,
            googleSearchEnabled = googleSearchEnabled,
        )
        bot.create(request)
    }

    fun getBot(botId: String): BotOutputModel = runBlocking {
        bot.get(botId)
    }

    fun deleteBot(botId: String) = runBlocking {
        bot.delete(botId)
    }

    fun listBots(): List<BotOutputModel> = runBlocking {
        bot.list()
    }

    fun updateBot(botId: String, request: BotUpdateRequest): BotOutputModel = runBlocking {
        bot.update(botId, request)
    }

    fun patchBot(botId: String, request: BotUpdateRequest): BotOutputModel = runBlocking {
        bot.patch(botId, request)
    }

    fun cloneBot(botId: String, request: BotCloneRequest): BotOutputModel = runBlocking {
        bot.clone(botId, request)
    }

    // Java-friendly Chat API
    @JvmOverloads
    fun createChatSession(
        botId: String,
        user: ChatUser? = null,
        initialMessages: List<Message>? = null
    ): SessionResponse = runBlocking {
        chat.createSession(botId, user, initialMessages)
    }

    @JvmOverloads
    fun updateChatSession(
        sessionId: String,
        user: ChatUser? = null,
        headline: String? = null
    ): SessionInfo = runBlocking {
        chat.updateSession(sessionId, user, headline)
    }

    fun getChatSession(sessionId: String): SessionResponse = runBlocking {
        chat.getSession(sessionId)
    }

    fun getSessionMessages(
        sessionId: String,
        page: Int,
        size: Int
    ): PaginatedMessageResponse = runBlocking {
        chat.getSessionMessages(sessionId, page, size)
    }

    fun getChatSessionInfo(sessionId: String): SessionInfo = runBlocking {
        chat.getSessionInfo(sessionId)
    }

    @JvmOverloads
    fun getChatSessions(
        userId: String? = null,
        botId: String? = null
    ): List<SessionResponse> = runBlocking {
        chat.getSessions(userId, botId)
    }

    fun getChatSessionsPaginated(
        userId: String?,
        botId: String?,
        page: Int,
        size: Int
    ): PaginatedSessionsResponse = runBlocking {
        chat.getSessionsPaginated(userId, botId, page, size)
    }

    fun deleteChatSession(sessionId: String) = runBlocking {
        chat.deleteSession(sessionId)
    }

    fun sendChatMessage(sessionId: String, message: Message): ChatMessage = runBlocking {
        chat.sendMessage(sessionId, message)
    }

    fun sendAsyncChatMessage(sessionId: String, message: Message): AsyncTaskCreationResponse = runBlocking {
        chat.sendAsyncMessage(sessionId, message)
    }

    fun checkAsyncChatMessageResult(sessionId: String, taskId: String): AsyncMessageResult = runBlocking {
        chat.checkAsyncMessageResult(sessionId, taskId)
    }

    // Java-friendly Auth API
    fun login(username: String, password: String): AuthResponse = runBlocking {
        auth.login(username, password)
    }

    fun register(
        username: String,
        password: String,
        firstName: String,
        lastName: String
    ): UserInfo = runBlocking {
        auth.register(username, password, firstName, lastName)
    }

    fun resendActivation(email: String) = runBlocking {
        auth.resendActivation(email)
    }

    fun generateApiKey(): ApiKey = runBlocking {
        auth.generateApiKey()
    }

    fun getApiKeys(): UserApiKeys = runBlocking {
        auth.getApiKeys()
    }

    fun deleteApiKey(key: String) = runBlocking {
        auth.deleteApiKey(key)
    }

    fun activateUser(code: String): UserInfo = runBlocking {
        auth.activate(code)
    }

    fun getCurrentUser(): UserInfo = runBlocking {
        auth.getUser()
    }

    // Java-friendly Corpora API
    fun createTextCorpus(request: TextCorpusRequest): CorpusResponseDTO = runBlocking {
        corpora.createTextCorpus(request)
    }

    fun createUrlCorpus(request: UrlCorpusRequest): CorpusResponseDTO = runBlocking {
        corpora.createUrlCorpus(request)
    }

    fun createFileCorpus(request: FileCorpusRequest): CorpusResponseDTO = runBlocking {
        corpora.createFileCorpus(request)
    }

    fun updateCorpus(corpusId: String, request: UpdateCorpusRequest): CorpusResponseDTO = runBlocking {
        corpora.updateCorpus(corpusId, request)
    }

    fun getAllCorpora(): List<CorpusResponseDTO> = runBlocking {
        corpora.getAllCorpora()
    }

    fun getCorpus(corpusId: String): CorpusResponseDTO = runBlocking {
        corpora.getCorpus(corpusId)
    }

    fun deleteCorpus(corpusId: String) = runBlocking {
        corpora.deleteCorpus(corpusId)
    }

    fun getCorpusChunks(corpusId: String, page: Int, size: Int): Page<Chunk> = runBlocking {
        corpora.getChunks(corpusId, page, size)
    }

    fun updateChunk(corpusId: String, chunkId: String, request: UpdateChunkRequest): Chunk = runBlocking {
        corpora.updateChunk(corpusId, chunkId, request)
    }

    fun createChunk(corpusId: String, request: CreateChunkRequest): Chunk = runBlocking {
        corpora.createChunk(corpusId, request)
    }

    fun findRelevantChunks(corpusId: String, query: String): List<RelevantChunk> = runBlocking {
        corpora.findRelevant(corpusId, query)
    }

    fun updateCorpusText(corpusId: String, text: String): CorpusResponseDTO = runBlocking {
        corpora.updateText(corpusId, text)
    }

    fun getUrlPages(corpusId: String, page: Int, size: Int): Page<RagWebPage> = runBlocking {
        corpora.getUrlPages(corpusId, page, size)
    }

    fun toggleUrlPage(corpusId: String, pageId: String, enable: Boolean): ToggleUrlPageResponse = runBlocking {
        corpora.toggleUrlPage(corpusId, pageId, enable)
    }

    @JvmOverloads
    fun addFilesToCorpus(corpusId: String, files: List<File>, ocr: Boolean = false): CorpusResponseDTO = runBlocking {
        corpora.addFiles(corpusId, files, ocr)
    }

    fun deleteCorpusFile(corpusId: String, fileId: String) = runBlocking {
        corpora.deleteFile(corpusId, fileId)
    }

    fun rechunkCorpusFile(corpusId: String, fileId: String) = runBlocking {
        corpora.rechunkFile(corpusId, fileId)
    }

    // Java-friendly Credit API
    fun getCreditStatement(): UserStatement = runBlocking {
        credit.getStatement()
    }

    // Java-friendly Meta API
    fun getMeta(): MetaResponse = runBlocking {
        meta.getMeta()
    }

    fun getPricing(): PricingResponse = runBlocking {
        meta.getPricing()
    }

    // Java-friendly Storage API
    fun uploadFile(file: File): GatewayStorageFile = runBlocking {
        storage.uploadFile(file)
    }

    // Java-friendly Wrapper API
    fun getChatCompletions(provider: String, request: ChatCompletionRequest): ChatCompletionResponse = runBlocking {
        wrapper.getChatCompletions(provider, request)
    }

    fun getEmbeddings(provider: String, request: EmbeddingRequest): EmbeddingResponse = runBlocking {
        wrapper.getEmbeddings(provider, request)
    }

    companion object {
        /**
         * Creates a new Metis client with the given API key
         *
         * @param apiKey The API key for authenticating with Metis API
         * @return A new Metis client
         */
        @JvmStatic
        fun create(apiKey: String): MetisClient {
            return MetisClient(MetisConfig(apiKey))
        }

        /**
         * Creates a new Metis client with the given configuration
         *
         * @param config The configuration for the client
         * @return A new Metis client
         */
        @JvmStatic
        fun create(config: MetisConfig): MetisClient {
            return MetisClient(config)
        }
    }
}