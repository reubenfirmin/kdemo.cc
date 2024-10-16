package org.example.pages.booksearch

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.js.Promise
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Headers
import kotlinx.browser.window

// once better tested, move to framework package
class HttpClient(private val baseUrl: String) {
    private val json = Json { ignoreUnknownKeys = true }

    // TODO maybe convert to kotlin.web wrapper. however, that uses coroutines, which maybe worth avoiding
    private fun <T> request(
        method: String,
        endpoint: String,
        body: Any? = null,
        headers: Map<String, String> = emptyMap(),
        parse: (String) -> T
    ): Promise<T> {
        val url = baseUrl + endpoint
        val requestInit = RequestInit().apply {
            this.method = method
            this.headers = Headers().apply {
                if (body != null) {
                    append("Content-Type", "application/json")
                }
                headers.forEach { (key, value) -> append(key, value) }
            }
            body?.let { this.body = json.encodeToString(it) }
        }

        return window.fetch(url, requestInit)
            .then { response ->
                if (!response.ok) {
                    throw Exception("HTTP error! status: ${response.status}")
                }
                response.text()
            }
            .then { text ->
                parse(text)
            }
    }

    fun <T> get(endpoint: String, parse: (String) -> T, headers: Map<String, String> = emptyMap()): Promise<T> =
        request("GET", endpoint, headers = headers, parse = parse)

    fun <T> post(endpoint: String, body: Any, parse: (String) -> T, headers: Map<String, String> = emptyMap()): Promise<T> =
        request("POST", endpoint, body, headers, parse)

    fun <T> put(endpoint: String, body: Any, parse: (String) -> T, headers: Map<String, String> = emptyMap()): Promise<T> =
        request("PUT", endpoint, body, headers, parse)

    fun <T> patch(endpoint: String, body: Any, parse: (String) -> T, headers: Map<String, String> = emptyMap()): Promise<T> =
        request("PATCH", endpoint, body, headers, parse)

    fun <T> delete(endpoint: String, parse: (String) -> T, headers: Map<String, String> = emptyMap()): Promise<T> =
        request("DELETE", endpoint, headers = headers, parse = parse)
}