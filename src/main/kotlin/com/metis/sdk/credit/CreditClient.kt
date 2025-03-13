package com.metis.sdk.credit

import com.metis.sdk.http.HttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Types

/**
 * Client for interacting with the Credit API.
 */
class CreditClient(private val httpClient: HttpClient) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Gets the user's credit statement.
     *
     * @return The user's credit statement
     */
    suspend fun getStatement(): UserStatement {
        val response = httpClient.get("api/v1/credit/statement")
        return moshi.adapter(UserStatement::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse user statement")
    }
}