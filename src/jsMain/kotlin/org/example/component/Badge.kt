package org.example.component

import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.TagConsumer
import kotlinx.html.visit

// example of a reusable component
class Badge(classes: String, consumer: TagConsumer<*>): DIV(
    mapOf("class" to "$classes flex justify-center items-center px-3 text-sm font-medium text-purple-800 bg-purple-100 " +
            "rounded-lg dark:bg-purple-200"), consumer) {

    fun render(block: Badge.() -> Unit) {
        block()
    }
}

fun FlowContent.badge(classes: String = "", block: Badge.() -> Unit) {
    Badge(classes, consumer).visit {
        render(block)
    }
}