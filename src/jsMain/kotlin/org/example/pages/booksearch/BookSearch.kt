package org.example.pages.booksearch

import org.example.framework.tags.svg
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onSubmitFunction
import org.example.framework.dom.onClick
import org.example.framework.dom.onError
import web.dom.document
import web.html.HTMLElement
import web.html.HTMLImageElement
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
        val containerEl = document.getElementById("results-container") as HTMLElement
        val inputEl = document.getElementById("search-input") as HTMLInputElement

        val query = inputEl.value

        if (query.isNotBlank()) {
            // TODO use dsl
            containerEl.innerHTML = "<div class='col-span-full text-center'>Searching...</div>"
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
            container.unsafeCast<org.w3c.dom.HTMLElement>().append.p("col-span-full text-center text-gray-700") {
                +"No books found."
            }
        } else {
            result.docs.forEachIndexed { idx, book ->
                container.unsafeCast<org.w3c.dom.HTMLElement>().append.div("bg-white overflow-hidden shadow rounded-lg flex flex-col") {
                    div("relative flex-grow") {
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

    private fun FlowContent.bookImage(book: Book, idx: Int) {
        val isbn = book.isbn?.firstOrNull()
        if (isbn != null) {
            img("w-full h-48 object-cover") {
                id = "img-$idx"
                src = "https://covers.openlibrary.org/b/isbn/$isbn-M.jpg"
                alt = book.title

                onError {
                    val element = document.getElementById(this@img.id) as HTMLImageElement
                    element.style.display = "none"
                    val svgCompanion = document.getElementById("svg-$idx") as HTMLElement
                    svgCompanion.style.display = "block"
                }
            }
            renderSvgCover(book, idx, "display: none")
        } else {
            renderSvgCover(book, idx)
        }
    }

    private fun FlowContent.renderSvgCover(book: Book, idx: Int, style: String = "") {
        val width = 192
        val height = 192
        val title = book.title.take(50)
        val author = book.author_name?.firstOrNull()?.take(40) ?: "Unknown"
        val bgColor = generateContrastingColor()
        val textColor = if (isColorLight(bgColor)) "#000000" else "#FFFFFF"

        svg("w-full h-48") {
            id = "svg-cover-$idx"
            attributes["style"] = style
            attributes["width"] = "$width"
            attributes["height"] = "$height"
            viewBox = "0 0 $width $height"
            attributes["xmlns"] = "http://www.w3.org/2000/svg"

            // Background
            rect {
                fill = bgColor
                this.width = "100%"
                this.height = "100%"
            }

            // Border
            rect {
                fill = "none"
                stroke = textColor
                strokeWidth = "4"
                x = "4"
                y = "4"
                this.width = "${width - 8}"
                this.height = "${height - 8}"
            }

            // Title
            text {
                x = "50%"
                y = "35%"
                textAnchor = "middle"
                fontFamily = "Arial, sans-serif"
                fontSize = "18"
                fill = textColor
                attributes["font-weight"] = "bold"

                val words = title.split(" ")
                var line = ""
                var yOffset = 0
                words.forEach { word ->
                    if ((line + word).length > 15) {
                        tspan {
                            x = "50%"
                            dy = "${if (yOffset == 0) "0" else "1.2em"}"
                            +line.trim()
                        }
                        line = "$word "
                        yOffset++
                    } else {
                        line += "$word "
                    }
                }
                if (line.isNotEmpty()) {
                    tspan {
                        x = "50%"
                        dy = "${if (yOffset == 0) "0" else "1.2em"}"
                        +line.trim()
                    }
                }
            }

            // "by" text
            text {
                x = "50%"
                y = "70%"
                textAnchor = "middle"
                fontFamily = "Arial, sans-serif"
                fontSize = "14"
                fill = textColor
                +"by"
            }

            // Author
            text {
                x = "50%"
                y = "80%"
                textAnchor = "middle"
                fontFamily = "Arial, sans-serif"
                fontSize = "16"
                fill = textColor
                +author.escapeXml()
            }
        }
    }

    private fun generateContrastingColor(): String {
        val hue = Random.nextInt(360)
        val saturation = Random.nextInt(70, 100)
        val lightness = Random.nextInt(25, 75)
        return "hsl($hue, $saturation%, $lightness%)"
    }

    private fun isColorLight(color: String): Boolean {
        val hsl = color.substringAfter("hsl(").substringBefore(")").split(",")
        val lightness = hsl[2].trim().removeSuffix("%").toInt()
        return lightness > 50
    }

    private fun String.escapeXml(): String {
        return replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }
}