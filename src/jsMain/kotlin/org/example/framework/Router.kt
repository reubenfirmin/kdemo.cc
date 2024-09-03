package org.example.framework

import kotlinx.browser.window
import kotlinx.html.dom.append
import kotlinx.html.*
import org.example.framework.dom.DomBehavior
import org.example.framework.interop.appendTo
import org.example.framework.interop.clear
import web.dom.document
import kotlin.js.Promise

object Router {
    private val routes = mutableMapOf<String, TagConsumer<*>.() -> Any>()

    fun route(path: String, handler: TagConsumer<*>.() -> Any): Router {
        routes[path] = handler
        return this
    }

    fun navigate(path: String) {
        window.history.pushState(null, "", path)
        handleRoute()
    }

    private fun handleRoute() {
        val path = window.location.pathname

        document.body.apply {
            clear()
            val consumer = appendTo()

            routes[path]?.let { handler ->
                when (val result = handler(consumer)) {
                    is Promise<*> -> result.then { DomBehavior.flush() }
                    else -> DomBehavior.flush()
                }
            } ?: notFound()
        }

        console.log("Navigating to ${window.location.pathname}")
    }

    private fun notFound() {
        document.getElementById("root")?.innerHTML = "<h1>404 - Page Not Found</h1>"
        DomBehavior.flush()
    }

    fun start() {
        window.onpopstate = {
            handleRoute()
        }

        handleRoute()
    }
}