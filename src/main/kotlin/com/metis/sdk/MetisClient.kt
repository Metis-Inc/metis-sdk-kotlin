package com.metis.sdk

import com.metis.sdk.auth.AuthClient
import com.metis.sdk.bot.BotClient
import com.metis.sdk.chat.ChatClient
import com.metis.sdk.corpora.CorporaClient
import com.metis.sdk.credit.CreditClient
import com.metis.sdk.http.HttpClient
import com.metis.sdk.meta.MetaClient
import com.metis.sdk.storage.StorageClient
import com.metis.sdk.wrapper.WrapperClient

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