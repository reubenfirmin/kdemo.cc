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
            } ?: notFound(path)
        }

        console.log("Navigating to ${window.location.pathname}")
    }

    private fun notFound(path: String) {
        document.body.apply {
            clear()
            appendTo().h1 {
                +"Sorry, couldn't find what you are looking for"
                console.log("Was looking for $path in:")
                for (route in routes) {
                    console.log(route.key)
                }
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
 * Type safe routing.
 *
 *     object myresource: Routes<MyResource>(MyResource(), "/resource") {
 *         val main = route { "/view" to { someView() }}
 *     }
 *
 * This gives you the ability elsewhere in the app to call:
 *
 *    myresource.main.path
 *
 */
open class Routes<T : Any>(val resource: T, private val basePath: String) {
    fun route(block: T.() -> Pair<String, TagConsumer<*>.() -> Any>): Route {
        val (path, handler) = resource.block()
        return Router.addRoute((basePath + path).replace("//", "/"), handler)
    }
}


// TODO handle params and such
data class Route(val path: String)


/**
 * Not typesafe, but lower boilerplate
 * ```
 * with (MyClass()) {
 *     route("{path}") { it.renderFunc() }
 * }
 * ```
 */
inline fun <reified T : Any> T.route(path: String, crossinline block: T.(TagConsumer<*>) -> Unit) =
    Router.addRoute(path) {
        block(this@route, this)
    }


/**
 * Not typesafe, but lower boilerplate
 *
 * ```
 * route (MyClass()) {
 *     "{path}" to { renderFunc() }
 * }
 * ```
 */
fun <T : Any> route(instance: T, block: T.() -> Pair<String, TagConsumer<*>.() -> Any>): Route {
    val (path, handler) = instance.block()
    return Router.addRoute(path, handler)
}