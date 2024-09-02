package org.example.pages.booksearch

import kotlinx.html.*
import kotlinx.html.js.onSubmitFunction
import web.dom.document
import web.html.HTMLElement
import web.html.HTMLInputElement

class BookSearch {
    private val bookApi = BookApi()

    fun TagConsumer<*>.render() {
        main("ml-8") {
            h1("text-xl") {
                +"Book Search"
            }

            div {
                +"Type a book title and press enter!"
            }

            div("mt-4") {
                id = "container"
            }

            form {
                id = "search-form"
                onSubmitFunction = { event ->
                    event.preventDefault()
                    handleSearch()
                }

                input(classes = "border-solid border-2 p-2 mt-8") {
                    id = "search-input"
                    type = InputType.text
                    name = "query"
                    autoFocus = true
                    placeholder = "Enter book title"
                }
            }

            a(classes = "pt-4 block") {
                href = "/"
                +"Back"
            }
        }
    }

    private fun handleSearch() {
        val containerEl = document.getElementById("container") as HTMLElement
        val inputEl = document.getElementById("search-input") as HTMLInputElement

        val query = inputEl.value

        if (query.isNotBlank()) {
            containerEl.innerHTML = "Searching..."
            bookApi.searchBooks(query)
                .then { result ->
                    displayBooks(containerEl, result)
                }
                .catch { error ->
                    displayError(containerEl, error.message ?: "An error occurred")
                }

            inputEl.value = ""
        }
    }
}
