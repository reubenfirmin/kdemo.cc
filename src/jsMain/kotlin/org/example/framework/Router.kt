package org.example.framework

import kotlinx.browser.window
import kotlinx.html.*
import org.example.framework.dom.DomBehavior
import org.example.framework.interop.appendTo
import org.example.framework.interop.clear
import web.dom.document
import kotlin.js.Promise

// TODO this is primitive. doesn't allow for templated paths or parameters yet.
object Router {

    private val routes = mutableMapOf<String, TagConsumer<*>.() -> Any>()

    fun addRoute(path: String, handler: TagConsumer<*>.() -> Any): Route {
        routes[path] = handler
        return Route(path)
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
        document.body.apply {
            clear()
            appendTo().h1 {
                +"Sorry, couldn't find what you are looking for"
            }
        }
        DomBehavior.flush()
    }

    fun start() {
        window.onpopstate = {
            handleRoute()
        }

        handleRoute()
    }
}

open class Routes<T>(private val resource: T, private val basePath: String) {
    /**
     * ```
     * with (MyClass()) {
     *     route("{path}") { it.renderFunc() }
     * }
     * ```
     */
    fun route(path: String, block: T.(TagConsumer<*>) -> Unit) =
        with (resource) {
            Router.addRoute((basePath + path).replace("//", "/")) {
                block(this@with, this)
            }
        }


    /**
     * ```
     * route (MyClass()) {
     *     "{path}" to { renderFunc() }
     * }
     * ```
     */
    fun route(block: T.() -> Pair<String, TagConsumer<*>.() -> Any>): Route {
        val (path, handler) = resource.block()
        return Router.addRoute(path, handler)
    }
}

inline fun <reified T : Any> routes(resource: T, basePath: String, block: Routes<T>.() -> Unit) = Routes(resource, basePath).apply { block() }


// TODO handle params and such
data class Route(val path: String)