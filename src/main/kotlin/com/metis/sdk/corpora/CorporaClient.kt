package com.metis.sdk.corpora

import com.metis.sdk.http.HttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Types
import java.io.File

/**
 * Client for interacting with the Corpora API.
 */
class CorporaClient(private val httpClient: HttpClient) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Creates a new text corpus.
     *
     * @param request The text corpus creation request
     * @return The created corpus
     */
    suspend fun createTextCorpus(request: TextCorpusRequest): CorpusResponseDTO {
        val parts = mutableMapOf<String, Any>(
            "name" to request.name,
            "type" to "TEXT",
            "text" to request.text
        )

        addCommonCorpusParams(parts, request)

        val response = httpClient.postMultipart("api/v1/corpora", parts)
        return moshi.adapter(CorpusResponseDTO::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse corpus response")
    }

    /**
     * Creates a new URL corpus.
     *
     * @param request The URL corpus creation request
     * @return The created corpus
     */
    suspend fun createUrlCorpus(request: UrlCorpusRequest): CorpusResponseDTO {
        val parts = mutableMapOf<String, Any>(
            "name" to request.name,
            "type" to "URL",
            "url" to request.url,
            "crawlingDepth" to request.crawlingDepth.toString()
        )

        addCommonCorpusParams(parts, request)

        val response = httpClient.postMultipart("api/v1/corpora", parts)
        return moshi.adapter(CorpusResponseDTO::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse corpus response")
    }

    /**
     * Creates a new file corpus.
     *
     * @param request The file corpus creation request
     * @return The created corpus
     */
    suspend fun createFileCorpus(request: FileCorpusRequest): CorpusResponseDTO {
        val parts = mutableMapOf<String, Any>(
            "name" to request.name,
            "type" to "FILE"
        )

        addCommonCorpusParams(parts, request)
        request.ocr?.let { parts["ocr"] = it.toString() }

        // Add files to the multipart request
        request.files.forEachIndexed { index, file ->
            parts["files[$index]"] = file
        }

        val response = httpClient.postMultipart("api/v1/corpora", parts)
        return moshi.adapter(CorpusResponseDTO::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse corpus response")
    }

    /**
     * Helper method to add common corpus parameters to a request.
     */
    private fun addCommonCorpusParams(parts: MutableMap<String, Any>, request: CreateCorpusRequest) {
        request.description?.let { parts["description"] = it }
        request.chunking?.let { parts["chunking"] = moshi.adapter(ChunkingMeta::class.java).toJson(it) }
        request.embedding?.let { parts["embedding"] = moshi.adapter(EmbeddingMeta::class.java).toJson(it) }
        request.ranking?.let { parts["ranking"] = moshi.adapter(RankingMeta::class.java).toJson(it) }
        request.reranking?.let { parts["reranking"] = moshi.adapter(RerankingMeta::class.java).toJson(it) }
    }

    /**
     * Updates a corpus.
     *
     * @param corpusId The ID of the corpus to update
     * @param request The update request
     * @return The updated corpus
     */
    suspend fun updateCorpus(corpusId: String, request: UpdateCorpusRequest): CorpusResponseDTO {
        val requestJson = moshi.adapter(UpdateCorpusRequest::class.java).toJson(request)
        val response = httpClient.put("api/v1/corpora/$corpusId", requestJson)
        return moshi.adapter(CorpusResponseDTO::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse corpus response")
    }

    /**
     * Gets all corpora.
     *
     * @return List of corpora
     */
    suspend fun getAllCorpora(): List<CorpusResponseDTO> {
        val response = httpClient.get("api/v1/corpora/all")
        val listType = Types.newParameterizedType(List::class.java, CorpusResponseDTO::class.java)
        return moshi.adapter<List<CorpusResponseDTO>>(listType).fromJson(response) ?: emptyList()
    }

    /**
     * Gets a corpus by ID.
     *
     * @param corpusId The ID of the corpus to get
     * @return The corpus
     */
    suspend fun getCorpus(corpusId: String): CorpusResponseDTO {
        val response = httpClient.get("api/v1/corpora/$corpusId")
        return moshi.adapter(CorpusResponseDTO::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse corpus response")
    }

    /**
     * Deletes a corpus.
     *
     * @param corpusId The ID of the corpus to delete
     */
    suspend fun deleteCorpus(corpusId: String) {
        httpClient.delete("api/v1/corpora/$corpusId")
    }

    /**
     * Gets chunks for a corpus.
     *
     * @param corpusId The ID of the corpus
     * @param page The page number (0-based)
     * @param size The page size
     * @return Paginated chunks
     */
    suspend fun getChunks(corpusId: String, page: Int, size: Int): Page<Chunk> {
        val queryParams = mapOf(
            "page" to page.toString(),
            "size" to size.toString()
        )

        val response = httpClient.get("api/v1/corpora/$corpusId/chunks", queryParams)
        val type = Types.newParameterizedType(Page::class.java, Chunk::class.java)
        return moshi.adapter<Page<Chunk>>(type).fromJson(response)
            ?: throw IllegalStateException("Failed to parse chunks response")
    }

    /**
     * Updates a chunk.
     *
     * @param corpusId The ID of the corpus
     * @param chunkId The ID of the chunk to update
     * @param request The update request
     * @return The updated chunk
     */
    suspend fun updateChunk(corpusId: String, chunkId: String, request: UpdateChunkRequest): Chunk {
        val requestJson = moshi.adapter(UpdateChunkRequest::class.java).toJson(request)
        val response = httpClient.put("api/v1/corpora/$corpusId/chunks/$chunkId", requestJson)
        return moshi.adapter(Chunk::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse chunk response")
    }

    /**
     * Creates a new chunk.
     *
     * @param corpusId The ID of the corpus
     * @param request The creation request
     * @return The created chunk
     */
    suspend fun createChunk(corpusId: String, request: CreateChunkRequest): Chunk {
        val requestJson = moshi.adapter(CreateChunkRequest::class.java).toJson(request)
        val response = httpClient.post("api/v1/corpora/$corpusId/chunks", requestJson)
        return moshi.adapter(Chunk::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse chunk response")
    }

    /**
     * Finds relevant chunks for a query.
     *
     * @param corpusId The ID of the corpus
     * @param query The query to search for
     * @return List of relevant chunks
     */
    suspend fun findRelevant(corpusId: String, query: String): List<RelevantChunk> {
        val queryParams = mapOf("query" to query)
        val response = httpClient.get("api/v1/corpora/$corpusId/relevant", queryParams)
        val listType = Types.newParameterizedType(List::class.java, RelevantChunk::class.java)
        return moshi.adapter<List<RelevantChunk>>(listType).fromJson(response) ?: emptyList()
    }

    /**
     * Updates the text of a text corpus.
     *
     * @param corpusId The ID of the corpus
     * @param text The new text
     * @return The updated corpus
     */
    suspend fun updateText(corpusId: String, text: String): CorpusResponseDTO {
        val request = UpdateTextRequest(text)
        val requestJson = moshi.adapter(UpdateTextRequest::class.java).toJson(request)
        val response = httpClient.put("api/v1/corpora/$corpusId/text", requestJson)
        return moshi.adapter(CorpusResponseDTO::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse corpus response")
    }

    /**
     * Gets URL pages for a URL corpus.
     *
     * @param corpusId The ID of the corpus
     * @param page The page number (0-based)
     * @param size The page size
     * @return Paginated URL pages
     */
    suspend fun getUrlPages(corpusId: String, page: Int, size: Int): Page<RagWebPage> {
        val queryParams = mapOf(
            "page" to page.toString(),
            "size" to size.toString()
        )

        val response = httpClient.get("api/v1/corpora/$corpusId/url/pages", queryParams)
        val type = Types.newParameterizedType(Page::class.java, RagWebPage::class.java)
        return moshi.adapter<Page<RagWebPage>>(type).fromJson(response)
            ?: throw IllegalStateException("Failed to parse URL pages response")
    }

    /**
     * Toggles the enable status of a URL page.
     *
     * @param corpusId The ID of the corpus
     * @param pageId The ID of the page
     * @param enable Whether to enable or disable the page
     * @return The toggle response
     */
    suspend fun toggleUrlPage(corpusId: String, pageId: String, enable: Boolean): ToggleUrlPageResponse {
        val request = ToggleUrlPageRequest(enable)
        val requestJson = moshi.adapter(ToggleUrlPageRequest::class.java).toJson(request)
        val response = httpClient.post("api/v1/corpora/$corpusId/url/page/$pageId/toggle", requestJson)
        return moshi.adapter(ToggleUrlPageResponse::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse toggle response")
    }

    /**
     * Adds files to a file corpus.
     *
     * @param corpusId The ID of the corpus
     * @param files The files to add
     * @param ocr Whether to enable OCR for the files
     * @return The updated corpus
     */
    suspend fun addFiles(corpusId: String, files: List<File>, ocr: Boolean = false): CorpusResponseDTO {
        val parts = mutableMapOf<String, Any>()

        if (ocr) {
            parts["ocr"] = "true"
        }

        // Add files to the multipart request
        files.forEachIndexed { index, file ->
            parts["files[$index]"] = file
        }

        val response = httpClient.postMultipart("api/v1/corpora/$corpusId/files", parts)
        return moshi.adapter(CorpusResponseDTO::class.java).fromJson(response)
            ?: throw IllegalStateException("Failed to parse corpus response")
    }

    /**
     * Deletes a file from a file corpus.
     *
     * @param corpusId The ID of the corpus
     * @param fileId The ID of the file to delete
     */
    suspend fun deleteFile(corpusId: String, fileId: String) {
        httpClient.delete("api/v1/corpora/$corpusId/files/$fileId")
    }

    /**
     * Rechunks a file in a file corpus.
     *
     * @param corpusId The ID of the corpus
     * @param fileId The ID of the file to rechunk
     */
    suspend fun rechunkFile(corpusId: String, fileId: String) {
        httpClient.post("api/v1/corpora/$corpusId/files/$fileId/rechunk", "")
    }
}