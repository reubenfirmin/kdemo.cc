package org.example.pages.client

import kotlinx.html.*
import org.example.component.badge
import org.example.framework.dom.onSubmit
import web.dom.document
import web.html.HTMLInputElement
import kotlin.js.Promise
import kotlinx.browser.window
import kotlinx.serialization.json.*

class ClientIndex {

    fun TagConsumer<*>.clientIndex() {
        main("ml-8") {
            h1("text-xl") {
                +"Book Search"
            }

            badge {
                +"Type a book title and press enter!"
            }

            div("mt-4") {
                id = "container"
            }

            form {
                id = "search-form"
                input(classes = "border-solid border-2 p-2") {
                    id = "search-input"
                    type = InputType.text
                    autoFocus = true
                    name = "query"
                    placeholder = "Enter book title"
                }

                onSubmit { event ->
                    event.preventDefault()

                    val containerEl = document.getElementById("container")!!
                    val inputEl = document.getElementById("search-input") as HTMLInputElement

                    val query = inputEl.value
                    if (query.isNotBlank()) {
                        // this grossness is because kotlinx.html hasn't caught up with the kotlin-browser wrapper yet...
                        fetchBooks(query)
                            .then { result -> displayBooks(containerEl.unsafeCast<org.w3c.dom.HTMLElement>(), result) }
                            .catch { error -> displayError(containerEl.unsafeCast<org.w3c.dom.HTMLElement>(), error.message ?: "An error occurred") }

                        inputEl.value = ""
                    }
                }
            }

            a(classes = "pt-4 block") {
                href = "/"
                +"Back"
            }
        }
    }

    private fun fetchBooks(query: String): Promise<JsonElement> {
        val encodedQuery = js("encodeURIComponent")(query) as String
        return window.fetch("https://openlibrary.org/search.json?q=$encodedQuery&limit=5")
            .then { response ->
                if (!response.ok) {
                    throw Exception("HTTP error! status: ${response.status}")
                }
                response.text()
            }
            .then { text -> Json.parseToJsonElement(text) }
    }
}