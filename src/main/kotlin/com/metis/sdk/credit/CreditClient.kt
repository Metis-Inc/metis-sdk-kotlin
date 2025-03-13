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

    /**
     * Adds credit to a user's account (admin only).
     *
     * @param userId The ID of the user to add credit to
     * @param amount The amount of credit to add
     * @param agent The agent adding the credit
     * @param reason The reason for adding the credit
     */
    suspend fun addCredit(userId: String, amount: Double, agent: String, reason: String) {
        val request = AddCreditRequest(userId, amount, agent, reason)
        val requestJson = moshi.adapter(AddCreditRequest::class.java).toJson(request)
        httpClient.post("api/v1/credit/admin/add", requestJson)
    }

    /**
     * Gets users with low balance (admin only).
     *
     * @return List of users with low balance
     */
    suspend fun getUsersWithLowBalance(): List<UserStatement> {
        val response = httpClient.get("api/v1/credit/low-balances")
        val listType = Types.newParameterizedType(List::class.java, UserStatement::class.java)
        return moshi.adapter<List<UserStatement>>(listType).fromJson(response) ?: emptyList()
    }
}