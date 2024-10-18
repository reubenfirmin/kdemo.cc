package org.example.framework.interop

import kotlinx.css.CssBuilder
import kotlinx.html.CommonAttributeGroupFacade
import kotlinx.html.classes
import kotlinx.html.style
import web.dom.document
import web.html.HTMLStyleElement

/**
 * Worried about performance of inlining css? Don't be. https://danielnagy.me/posts/Post_tsr8q6sx37pl
 * (kotlin-css does not support classname based styles; kotlin-styled-next is required for that.)
 */
fun CommonAttributeGroupFacade.css(block: CssBuilder.() -> Unit) {
    style = CssBuilder().apply {
        block()
    }.toString().removeSuffix("\n")
}

var counter = 0

/**
 * Creates a class and attaches it to the head. Sets the current element's classname either to something specific if supplied,
 * or a generated value.
 * According to the video above, this is not as performant as inlining styles, or tailwind.
 */
fun CommonAttributeGroupFacade.cssClass(className: String = "clzz${counter++}", block: CssBuilder.() -> Unit) {
    val rawCss = CssBuilder().apply(block).toString()
    val formattedCss = rawCss.split(";")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .joinToString(";\n        ")
    val style = """
    .$className {
        $formattedCss;
    }
    """.trimIndent()

    val head = document.head
    val styleTag = head.getElementsByTagName("style").firstOrNull() as? HTMLStyleElement
        ?: document.createElement("style").also { head.appendChild(it) } as HTMLStyleElement

    styleTag.innerHTML += "\n$style"

    this.classes = setOf(className) + this.classes
}

