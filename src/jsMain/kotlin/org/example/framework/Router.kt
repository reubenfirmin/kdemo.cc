package org.example.framework

import kotlinx.browser.window
import kotlinx.dom.clear
import kotlinx.html.*
import kotlinx.html.dom.append
import org.example.framework.dom.DomBehavior
import web.dom.document
import kotlin.js.Promise

object Router {

    private val routes = mutableMapOf<String, TagConsumer<*>.() -> Promise<Unit>>()

    fun route(path: String, handler: TagConsumer<*>.() -> Promise<Unit>): Router {
        routes[path] = handler
        return this
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

            routes[path]?.let { handler ->
                handler(consumer).then {
                    console.log("Rendered page for $path")
                    DomBehavior.flush()
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