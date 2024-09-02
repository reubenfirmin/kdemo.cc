package org.example.framework

import kotlinx.browser.window
import kotlinx.dom.clear
import kotlinx.html.*
import kotlinx.html.dom.append
import web.dom.document

object Router {
    private val routes = mutableMapOf<String, TagConsumer<*>.() -> Unit>()

    fun route(path: String, handler: TagConsumer<*>.() -> Unit) {
        routes[path] = handler
    }

    fun navigate(path: String) {
        window.history.pushState(null, "", path)
        handleRoute()
    }

    private fun handleRoute() {
        val path = window.location.pathname

        kotlinx.browser.document.body!!.apply {
            clear()
            val consumer = append

            routes[path]?.invoke(consumer) ?: notFound()
        }

        console.log("Navigated to ${window.location.pathname}")
    }

    private fun notFound() {
        document.getElementById("root")?.innerHTML = "<h1>404 - Page Not Found</h1>"
    }

    fun start() {
        window.onpopstate = {
            handleRoute()
        }

        handleRoute()
    }
}