package com.metis.sdk.storage

import com.squareup.moshi.JsonClass

/**
 * Response containing storage files.
 */
@JsonClass(generateAdapter = true)
data class GatewayStorageFiles(
    val files: List<GatewayStorageFile>
)

/**
 * Storage file.
 */
@JsonClass(generateAdapter = true)
data class GatewayStorageFile(
    val objectName: String,
    val url: String,
    val size: Long,
    val contentType: String,
    val name: String? = null
)