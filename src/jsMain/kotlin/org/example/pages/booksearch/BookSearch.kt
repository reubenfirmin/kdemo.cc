package org.example.pages.booksearch

import kotlinx.html.*
import kotlinx.html.js.onSubmitFunction
import org.example.framework.dom.onClick
import org.example.framework.interop.appendTo
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
                div("max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8 flex flex-row gap-16 items-end") {
                    h1("text-3xl font-bold text-gray-900") {
                        +"BookShop"
                    }
                    a {
                        href = "/"
                        +"Back"
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
                                    id = "search"
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
        val inputEl = document.getElementById("search-input") as HTMLInputElement
        val query = inputEl.value

        if (query.isNotBlank()) {
            val containerEl = document.getElementById("results-container") as HTMLElement
            containerEl.appendTo().div("col-span-full text-center") {
                +"Searching..."
            }
            bookApi.searchBooks(query)
                .then { result ->
                    displayBooks(containerEl, result)
                }
                .catch { error ->
                    displayError(containerEl, error.message ?: " An error occurred")
                    console.error(error)
                }

            inputEl.value = ""
        }
    }

    private fun displayBooks(container: HTMLElement, result: BookSearchResponse) {
        container.innerHTML = ""
        if (result.docs.isEmpty()) {
            container.appendTo().p("col-span-full text-center text-gray-700") {
                +"No books found."
            }
        } else {
            result.docs.forEachIndexed { idx, book ->
                container.appendTo().div("bg-white overflow-hidden shadow rounded-lg flex flex-col") {
                    div("relative flex-grow h-64") {
                        bookImage(book, idx)
                        div("absolute top-0 right-0 bg-indigo-500 text-white px-2 py-1 m-2 rounded") {
                            +"$${round(Random.nextDouble(9.99, 29.99) * 100) / 100}"
                        }
                    }
                    div("p-4 flex-grow flex flex-col justify-between") {
                        div {
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
                        div("mt-4") {
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
    }
}