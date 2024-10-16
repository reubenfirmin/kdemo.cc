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

    fun addRoute(path: String, handler: TagConsumer<*>.() -> Any): Router {
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

/**
 * ```
 * with (MyClass()) {
 *     route("{path}") { it.renderFunc() }
 * }
 * ```
 */
inline fun <reified T : Any> T.route(path: String, crossinline block: T.(TagConsumer<*>) -> Unit) {
    Router.addRoute(path) {
        block(this@route, this)
    }
}

/**
 * ```
 * route (MyClass()) {
 *     "{path}" to { renderFunc() }
 * }
 * ```
 */
fun <T : Any> route(instance: T, block: T.() -> Pair<String, TagConsumer<*>.() -> Any>) {
    val (path, handler) = instance.block()
    Router.addRoute(path, handler)
}