package com.metis.sdk.auth

import com.squareup.moshi.JsonClass

/**
 * Authentication request.
 */
@JsonClass(generateAdapter = true)
data class AuthRequest(
    val username: String,
    val password: String
)

/**
 * Authentication response.
 */
@JsonClass(generateAdapter = true)
data class AuthResponse(
    val token: String
)

/**
 * Registration request.
 */
@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

/**
 * Request to resend activation email.
 */
@JsonClass(generateAdapter = true)
data class ResendActivationRequest(
    val email: String
)

/**
 * User API keys.
 */
@JsonClass(generateAdapter = true)
data class UserApiKeys(
    val userId: String,
    val keys: List<ApiKey>
)

/**
 * API key.
 */
@JsonClass(generateAdapter = true)
data class ApiKey(
    val id: String,
    val key: String
)

/**
 * User information.
 */
@JsonClass(generateAdapter = true)
data class UserInfo(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val activated: Boolean,
    val roles: List<String>
)