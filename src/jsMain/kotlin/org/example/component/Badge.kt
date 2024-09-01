package org.example.component

import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.TagConsumer
import kotlinx.html.visit

class Badge(consumer: TagConsumer<*>): DIV(
    mapOf("class" to "flex justify-center items-center px-3 text-sm font-medium text-purple-800 bg-purple-100 " +
            "rounded-lg dark:bg-purple-200"), consumer) {

    fun render(block: Badge.() -> Unit) {
        block()
    }
}

fun FlowContent.badge(block: Badge.() -> Unit) {
    Badge(consumer).visit {
        render(block)
    }
}