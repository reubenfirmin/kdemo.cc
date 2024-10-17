package org.example.framework.interop

import kotlinx.css.CssBuilder
import kotlinx.html.CommonAttributeGroupFacade
import kotlinx.html.style

/**
 * Worried about performance of inlining css? Don't be. https://danielnagy.me/posts/Post_tsr8q6sx37pl
 * (kotlin-css does not support classname based styles; kotlin-styled-next is required for that.)
 */
fun CommonAttributeGroupFacade.css(block: CssBuilder.() -> Unit) {
    style = CssBuilder().apply {
        block()
    }.toString().removeSuffix("\n")
}
