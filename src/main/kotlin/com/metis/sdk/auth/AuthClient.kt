package com.metis.sdk.auth

import com.metis.sdk.http.HttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Client for interacting with the Auth API.
 */
class AuthClient(private val httpClient: HttpClient) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Logs in a user.
     *
     * @param username The username (email)
     * @param password The password
     * @return The authentication response
     */
    suspend fun login(username: String, password: String): AuthResponse {
        val request = AuthRequest(username, password)
        val requestJson = moshi.adapter(AuthRequest::class.java).toJson(request)
        val response = httpClient.post("api/v1/user/login", requestJson)
        return moshi.adapter(AuthResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse auth response")
    }

    /**
     * Registers a new user.
     *
     * @param username The username (email)
     * @param password The password
     * @param firstName The first name
     * @param lastName The last name
     * @return The user info
     */
    suspend fun register(
        username: String,
        password: String,
        firstName: String,
        lastName: String
    ): UserInfo {
        val request = RegisterRequest(username, password, firstName, lastName)
        val requestJson = moshi.adapter(RegisterRequest::class.java).toJson(request)
        val response = httpClient.post("api/v1/user/register", requestJson)
        return moshi.adapter(UserInfo::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse user info")
    }

    /**
     * Resends an activation email.
     *
     * @param email The email to resend activation to
     */
    suspend fun resendActivation(email: String) {
        val request = ResendActivationRequest(email)
        val requestJson = moshi.adapter(ResendActivationRequest::class.java).toJson(request)
        httpClient.post("api/v1/user/resend-activation", requestJson)
    }

    /**
     * Generates a new API key.
     *
     * @return The generated API key
     */
    suspend fun generateApiKey(): ApiKey {
        val response = httpClient.post("api/v1/user/api-keys", "")
        return moshi.adapter(ApiKey::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse API key")
    }

    /**
     * Gets all API keys for the current user.
     *
     * @return The user's API keys
     */
    suspend fun getApiKeys(): UserApiKeys {
        val response = httpClient.get("api/v1/user/api-keys")
        return moshi.adapter(UserApiKeys::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse user API keys")
    }

    /**
     * Deletes an API key.
     *
     * @param key The key to delete
     */
    suspend fun deleteApiKey(key: String) {
        httpClient.delete("api/v1/user/api-keys/$key")
    }

    /**
     * Activates a user account.
     *
     * @param code The activation code
     * @return The user info
     */
    suspend fun activate(code: String): UserInfo {
        val response = httpClient.get("api/v1/user/activate/$code")
        return moshi.adapter(UserInfo::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse user info")
    }

    /**
     * Gets the current user's info.
     *
     * @return The user info
     */
    suspend fun getUser(): UserInfo {
        val response = httpClient.get("api/v1/user/me")
        return moshi.adapter(UserInfo::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse user info")
    }
}