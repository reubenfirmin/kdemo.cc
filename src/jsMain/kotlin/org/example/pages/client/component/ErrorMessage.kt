package org.example.pages.client.component

import kotlinx.html.*

class ErrorMessage(classes: String, consumer: TagConsumer<*>) : DIV(
    mapOf("class" to "$classes bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative"), consumer
) {
    fun render(message: String) {
        strong("font-bold") { +"Error!" }
        span("block sm:inline") { +message }
    }
}

fun FlowContent.errorMessage(classes: String = "", message: String) {
    ErrorMessage(classes, consumer).visit {
        render(message)
    }
}