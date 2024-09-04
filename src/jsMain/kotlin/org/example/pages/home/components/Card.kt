package org.example.pages.home.components

import kotlinx.html.*

class Card(classes: String, consumer: TagConsumer<*>): DIV(mapOf("class" to classes), consumer) {

    fun render(block: DIV.() -> Unit) {
        block()
    }
}

fun FlowContent.card(classes: String = "bg-gray-800 rounded-lg shadow-md p-6", block: DIV.() -> Unit) = Card(classes, consumer).visit {
    render(block)
}