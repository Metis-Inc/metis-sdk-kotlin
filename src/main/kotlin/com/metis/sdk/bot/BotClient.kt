package com.metis.sdk.bot

import com.metis.sdk.http.HttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Types

/**
 * Client for interacting with the Bot API.
 */
class BotClient(private val httpClient: HttpClient) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Creates a new bot.
     *
     * @param request The bot creation request
     * @return The created bot
     */
    suspend fun create(request: BotCreationRequest): BotOutputModel {
        val requestJson = moshi.adapter(BotCreationRequest::class.java).toJson(request)
        val response = httpClient.post("api/v1/bots", requestJson)
        return moshi.adapter(BotOutputModel::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse bot response")
    }

    /**
     * Gets a bot by ID.
     *
     * @param botId The ID of the bot to get
     * @return The bot
     */
    suspend fun get(botId: String): BotOutputModel {
        val response = httpClient.get("api/v1/bots/$botId")
        return moshi.adapter(BotOutputModel::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse bot response")
    }

    /**
     * Deletes a bot.
     *
     * @param botId The ID of the bot to delete
     */
    suspend fun delete(botId: String) {
        httpClient.delete("api/v1/bots/$botId")
    }

    /**
     * Lists all bots.
     *
     * @return List of bots
     */
    suspend fun list(): List<BotOutputModel> {
        val response = httpClient.get("api/v1/bots/all")
        val listType = Types.newParameterizedType(List::class.java, BotOutputModel::class.java)
        return moshi.adapter<List<BotOutputModel>>(listType).fromJson(response) ?: emptyList()
    }

    /**
     * Updates a bot.
     *
     * @param botId The ID of the bot to update
     * @param request The update request
     * @return The updated bot
     */
    suspend fun update(botId: String, request: BotUpdateRequest): BotOutputModel {
        val requestJson = moshi.adapter(BotUpdateRequest::class.java).toJson(request)
        val response = httpClient.put("api/v1/bots/$botId", requestJson)
        return moshi.adapter(BotOutputModel::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse bot response")
    }

    /**
     * Partially updates a bot.
     *
     * @param botId The ID of the bot to update
     * @param request The update request
     * @return The updated bot
     */
    suspend fun patch(botId: String, request: BotUpdateRequest): BotOutputModel {
        val requestJson = moshi.adapter(BotUpdateRequest::class.java).toJson(request)
        val response = httpClient.patch("api/v1/bots/$botId", requestJson)
        return moshi.adapter(BotOutputModel::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse bot response")
    }

    /**
     * Clones a bot.
     *
     * @param botId The ID of the bot to clone
     * @param request The clone request
     * @return The cloned bot
     */
    suspend fun clone(botId: String, request: BotCloneRequest): BotOutputModel {
        val requestJson = moshi.adapter(BotCloneRequest::class.java).toJson(request)
        val response = httpClient.post("api/v1/bots/$botId/clone", requestJson)
        return moshi.adapter(BotOutputModel::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse bot response")
    }
}