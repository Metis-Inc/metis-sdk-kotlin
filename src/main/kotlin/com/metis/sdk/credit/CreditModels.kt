package com.metis.sdk.credit

import com.squareup.moshi.JsonClass
import java.util.*

/**
 * Request to add credit to a user's account.
 */
@JsonClass(generateAdapter = true)
data class AddCreditRequest(
    val userId: String,
    val amount: Double,
    val agent: String,
    val reason: String
)

/**
 * User credit statement.
 */
@JsonClass(generateAdapter = true)
data class UserStatement(
    val userId: String,
    val balance: Double,
    val transactions: List<Transaction>
)

/**
 * Credit transaction.
 */
@JsonClass(generateAdapter = true)
data class Transaction(
    val id: String,
    val userId: String,
    val amount: Double,
    val balance: Double,
    val agent: String,
    val reason: String,
    val timestamp: Date
)