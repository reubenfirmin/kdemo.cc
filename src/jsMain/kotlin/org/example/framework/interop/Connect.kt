package org.example.framework.interop

import kotlinx.html.TagConsumer
import web.dom.Element
import kotlinx.html.dom.append
import web.html.HTMLCollection
import web.html.HTMLElement

fun Element.clear() {
    while (firstChild != null) {
        removeChild(firstChild!!)
    }
}

// bridge between kotlinx.html (which is still on org.w3c.dom) and the kotlin-browser wrapper, which uses the web.dom api
fun HTMLElement.appendTo(): TagConsumer<*> = this.unsafeCast<org.w3c.dom.HTMLElement>().append

fun <T : Element> HTMLCollection<T>.firstOrNull(): T? {
    return if (length > 0) get(0)  else null
}