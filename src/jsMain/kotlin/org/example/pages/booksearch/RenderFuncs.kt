package org.example.pages.booksearch

import kotlinx.html.*
import org.example.framework.dom.onLoad
import org.example.framework.interop.appendTo
import org.example.framework.tags.svg
import org.example.pages.booksearch.component.errorMessage
import web.dom.document
import web.html.HTMLElement
import web.html.HTMLImageElement
import kotlin.random.Random


fun FlowContent.bookImage(book: Book, idx: Int) {
    val isbn = book.isbn?.firstOrNull()
    if (isbn != null) {
        img("w-full h-full object-cover") {
            id = "img-$idx"
            src = "https://covers.openlibrary.org/b/isbn/$isbn-M.jpg"
            alt = book.title

            onLoad {
                val element = document.getElementById(this@img.id) as HTMLImageElement

                if (element.width <= 1 && element.height <= 1) {
                    element.style.display = "none"
                    element.parentElement!!.appendTo().div("h-64") {
                        renderSvgCover(book, idx)
                    }
                }
            }

        }
    } else {
        div("h-64") {
            renderSvgCover(book, idx)
        }
    }
}

private fun FlowContent.renderSvgCover(book: Book, idx: Int) {
    val title = book.title.take(50)
    val author = book.author_name?.firstOrNull()?.take(40) ?: "Unknown"
    val bgColor = generateContrastingColor()
    val textColor = if (isColorLight(bgColor)) "#000000" else "#FFFFFF"

    svg("w-full h-full") {
        id = "svg-cover-$idx"
        attributes["viewBox"] = "0 0 100 100"
        attributes["preserveAspectRatio"] = "xMidYMid meet"

        // Background
        rect {
            fill = bgColor
            this.width = "100"
            this.height = "100"
        }

        // Border
        rect {
            fill = "none"
            stroke = textColor
            strokeWidth = "2"
            x = "2"
            y = "2"
            this.width = "96"
            this.height = "96"
        }

        // Title
        text {
            x = "50"
            y = "30"
            textAnchor = "middle"
            fontFamily = "Arial, sans-serif"
            fontSize = "10"
            fill = textColor
            attributes["font-weight"] = "bold"

            val words = title.split(" ")
            var line = ""
            var yOffset = 0
            words.forEach { word ->
                if ((line + word).length > 15) {
                    tspan {
                        x = "50"
                        dy = if (yOffset == 0) "0" else "1.2em"
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
                    x = "50"
                    dy = if (yOffset == 0) "0" else "1.2em"
                    +line.trim()
                }
            }
        }

        // "by" text
        text {
            x = "50"
            y = "70"
            textAnchor = "middle"
            fontFamily = "Arial, sans-serif"
            fontSize = "8"
            fill = textColor
            +"by"
        }

        // Author
        text {
            x = "50"
            y = "80"
            textAnchor = "middle"
            fontFamily = "Arial, sans-serif"
            fontSize = "9"
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


fun displayError(container: HTMLElement, message: String) {
    container.innerHTML = ""
    container.appendTo().div {
        errorMessage(message = message)
    }
}