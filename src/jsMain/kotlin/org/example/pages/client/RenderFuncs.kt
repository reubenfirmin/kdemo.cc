package org.example.pages.client

import kotlinx.html.*
import kotlinx.html.dom.append
import org.example.framework.dom.onClick
import org.example.framework.dom.onMouseEnter
import org.example.framework.dom.onMouseLeave
import org.example.pages.client.component.errorMessage
import web.html.HTMLElement
import web.uievents.MouseEvent

fun displayBooks(container: HTMLElement, result: BookSearchResponse) {
    container.innerHTML = ""
    if (result.docs.isEmpty()) {
        container.unsafeCast<org.w3c.dom.HTMLElement>().append.p {
            +"No books found."
        }
    } else {
        container.unsafeCast<org.w3c.dom.HTMLElement>().append.ul {
            result.docs.forEachIndexed() { idx, book ->
                li("hover:underline hover:cursor-pointer") {
                    id = "book-$idx"
                    +"${book.title} by ${book.author_name?.joinToString(", ") ?: "Unknown"} (${book.first_publish_year ?: "Year unknown"})"
                    val toggleOrange = fun (e: MouseEvent) {
                        val el = e.target as HTMLElement
                        el.classList.toggle("text-orange-500")
                    }

                    onClick(toggleOrange)
                    onMouseEnter(toggleOrange)
                    onMouseLeave(toggleOrange)
                }
            }
        }
    }
}

fun displayError(container: HTMLElement, message: String) {
    container.innerHTML = ""
    container.unsafeCast<org.w3c.dom.HTMLElement>().append.div {
        errorMessage(message = message)
    }
}