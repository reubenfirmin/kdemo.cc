package org.example.pages.client

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.js.Promise

@Serializable
data class Book(
    val title: String,
    val author_name: List<String>? = null,
    val first_publish_year: Int? = null
)

@Serializable
data class BookSearchResponse(
    val numFound: Int,
    val start: Int,
    val docs: List<Book>
)

class BookApi {
    private val httpClient = HttpClient("https://openlibrary.org")
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun searchBooks(query: String, limit: Int = 10): Promise<BookSearchResponse> {
        val encodedQuery = js("encodeURIComponent")(query) as String

        return httpClient.get(
            endpoint = "/search.json?q=$encodedQuery&limit=$limit",
            parse = { responseText ->
                json.decodeFromString<BookSearchResponse>(responseText)
            }
        )
    }
}