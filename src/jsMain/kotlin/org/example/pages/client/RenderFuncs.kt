package org.example.pages.client

import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.serialization.json.*
import org.example.pages.client.component.bookCard
import org.example.pages.client.component.errorMessage
import org.w3c.dom.Element

fun displayBooks(container: Element, result: JsonElement) {
    val docs = result.jsonObject["docs"]?.jsonArray ?: return

    container.innerHTML = ""
    container.appendChild(document.create.div("space-y-4") {
        docs.forEach { book ->
            val title = book.jsonObject["title"]?.jsonPrimitive?.content ?: "Unknown Title"
            val author = book.jsonObject["author_name"]?.jsonArray?.getOrNull(0)?.jsonPrimitive?.content ?: "Unknown Author"
            val year = book.jsonObject["first_publish_year"]?.jsonPrimitive?.content ?: "Unknown Year"

            bookCard(title = title, author = author, year = year)
        }
    })
}

fun displayError(container: Element, message: String) {
    container.innerHTML = ""
    container.appendChild(document.create.div {
        errorMessage(message = message)
    })
}