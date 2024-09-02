package org.example.pages.client.component

import kotlinx.html.*

class BookCard(classes: String, consumer: TagConsumer<*>) : DIV(
    mapOf("class" to "$classes bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4"), consumer
) {
    fun render(title: String, author: String, year: String) {
        h2("text-xl font-bold mb-2") { +title }
        p("text-gray-700") { +"Author: $author" }
        p("text-gray-600") { +"First Published: $year" }
    }
}

fun FlowContent.bookCard(classes: String = "", title: String, author: String, year: String) {
    BookCard(classes, consumer).visit {
        render(title, author, year)
    }
}
