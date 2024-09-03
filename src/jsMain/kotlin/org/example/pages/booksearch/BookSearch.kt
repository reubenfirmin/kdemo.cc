package org.example.pages.booksearch

import js.uri.encodeURIComponent
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onSubmitFunction
import org.example.framework.dom.onClick
import web.dom.document
import web.html.HTMLElement
import web.html.HTMLInputElement
import kotlin.random.Random
import kotlin.math.round

class BookSearch {
    private val bookApi = BookApi()

    fun TagConsumer<*>.render() {
        div("min-h-screen bg-gray-100") {
            header("bg-white shadow") {
                div("max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8") {
                    h1("text-3xl font-bold text-gray-900") {
                        +"BookShop"
                    }
                }
            }

            main("max-w-7xl mx-auto py-6 sm:px-6 lg:px-8") {
                div("px-4 py-6 sm:px-0") {
                    div("mb-8") {
                        p("text-xl text-center text-gray-700") {
                            +"Discover your next favorite book!"
                        }
                        div("mt-4 flex justify-center") {
                            form {
                                id = "search-form"
                                onSubmitFunction = { event ->
                                    event.preventDefault()
                                    handleSearch()
                                }

                                input(classes = "w-full sm:w-64 px-4 py-2 border border-gray-300 rounded-l-md focus:ring-indigo-500 focus:border-indigo-500") {
                                    id = "search-input"
                                    type = InputType.text
                                    name = "query"
                                    autoFocus = true
                                    placeholder = "Enter book title"
                                }
                                button(classes = "px-4 py-2 border border-transparent text-sm font-medium rounded-r-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500") {
                                    type = ButtonType.submit
                                    +"Search"
                                }
                            }
                        }

                    }

                    div("grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6") {
                        id = "results-container"
                    }
                }
            }

            footer("bg-white") {
                div("max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8") {
                    p("text-center text-gray-500") {
                        +"© 2024 BookShop. All rights reserved."
                    }
                }
            }
        }
    }

    private fun handleSearch() {
        val containerEl = document.getElementById("results-container") as HTMLElement
        val inputEl = document.getElementById("search-input") as HTMLInputElement

        val query = inputEl.value

        if (query.isNotBlank()) {
            containerEl.innerHTML = "<div class='col-span-full text-center'>Searching...</div>"
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

    private fun displayBooks(container: HTMLElement, result: BookSearchResponse) {
        container.innerHTML = ""
        if (result.docs.isEmpty()) {
            container.unsafeCast<org.w3c.dom.HTMLElement>().append.p("col-span-full text-center text-gray-700") {
                +"No books found."
            }
        } else {
            result.docs.forEachIndexed { idx, book ->
                container.unsafeCast<org.w3c.dom.HTMLElement>().append.div("bg-white overflow-hidden shadow rounded-lg") {
                    div("relative") {
                        img("w-full h-48 object-cover") {
                            src = getBookCoverUrl(book)
                            alt = book.title
                        }
                        div("absolute top-0 right-0 bg-indigo-500 text-white px-2 py-1 m-2 rounded") {
                            +"$${round(Random.nextDouble(9.99, 29.99) * 100) / 100}"
                        }
                    }
                    div("p-4") {
                        h3("text-lg font-semibold text-gray-900 truncate") {
                            +book.title
                        }
                        p("text-sm text-gray-600") {
                            +(book.author_name?.firstOrNull() ?: "Unknown Author")
                        }
                        div("flex items-center mt-2") {
                            val rating = Random.nextDouble(3.5, 5.0)
                            for (i in 1..5) {
                                span("text-yellow-400") {
                                    +if (i <= rating.toInt()) "★" else "☆"
                                }
                            }
                            span("ml-1 text-sm text-gray-600") {
                                +"(${round(rating * 10) / 10})"
                            }
                        }
                    }
                    div("px-4 py-3 bg-gray-50") {
                        button(classes="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded") {
                            id = "add-$idx"
                            +"Add to Cart"
                            onClick { event ->
                                val element = event.target as HTMLElement
                                element.innerText = "Added!"
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getBookCoverUrl(book: Book): String {
        val isbn = book.isbn?.firstOrNull()
        return if (isbn != null) {
            "https://covers.openlibrary.org/b/isbn/$isbn-M.jpg"
        } else {
            generateBookCover(book)
        }
    }

    private fun generateBookCover(book: Book): String {
        val width = 200
        val height = 300
        val title = book.title.take(20)
        val author = book.author_name?.firstOrNull()?.take(20) ?: "Unknown"
        val bgColor = Random.nextInt(0xFFFFFF)
        val textColor = if (bgColor and 0xFFFFFF > 0x7FFFFF) 0 else 0xFFFFFF

        // TODO svg
        return "data:image/svg+xml," + encodeURIComponent("""
            <svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height">
                <rect width="100%" height="100%" fill="#${bgColor.toString(16).padStart(6, '0')}"/>
                <text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" font-family="Arial, sans-serif" font-size="16" fill="#${textColor.toString(16).padStart(6, '0')}">
                    <tspan x="50%" dy="-1em">$title</tspan>
                    <tspan x="50%" dy="1.5em">by</tspan>
                    <tspan x="50%" dy="1.5em">$author</tspan>
                </text>
            </svg>
        """.trimIndent())
    }
}