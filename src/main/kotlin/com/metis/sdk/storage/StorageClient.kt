package com.metis.sdk.storage

import com.metis.sdk.http.HttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

/**
 * Client for interacting with the Storage API.
 */
class StorageClient(private val httpClient: HttpClient) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Uploads files to storage.
     *
     * @param files The files to upload
     * @return The uploaded storage files
     */
    private suspend fun uploadFiles(files: List<File>): List<GatewayStorageFile> {
        val parts = mutableMapOf<String, Any>()

        // Add files to the multipart request
        files.forEachIndexed { index, file ->
            parts["files[$index]"] = file
        }

        val response = httpClient.postMultipart("api/v1/storage", parts)
        return moshi.adapter(GatewayStorageFiles::class.java).fromJson(response)?.files
            ?: throw IllegalStateException("Failed to parse storage files response")
    }

    /**
     * Uploads a single file to storage.
     *
     * @param file The file to upload
     * @return The uploaded storage file
     */
    suspend fun uploadFile(file: File): GatewayStorageFile {
        return uploadFiles(listOf(file)).first()
    }
}