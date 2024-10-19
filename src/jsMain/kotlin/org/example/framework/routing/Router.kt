package org.example.framework.routing

import kotlinx.browser.window
import kotlinx.html.*
import org.example.framework.dom.DomBehavior
import org.example.framework.interop.appendTo
import org.example.framework.interop.clear
import web.dom.document
import kotlin.js.Promise

/**
 * Set up new routes either using the Routes builder, or the route() helper functions (found in Route).
 */
object Router {

    private fun handleRoute() {
        val path = window.location.pathname

        RouteTrie.findRoute(path) ?.apply {
            val (route, params) = this

            document.body.apply {
                clear()
                val consumer = appendTo()

                when (val result = route.handler(consumer, Params(params))) {
                    is Promise<*> -> result.then { DomBehavior.flush() }
                    else -> DomBehavior.flush()
                }
            }

            console.log("Navigating to ${window.location.pathname}")
        } ?: notFound(path)
    }

    private fun notFound(path: String) {
        document.body.apply {
            clear()
            appendTo().h1 {
                +"Sorry, couldn't find what you are looking for"
                console.log("Was looking for $path")
                console.log(RouteTrie.visualize())
            }
        }
        DomBehavior.flush()
    }

    fun navigate(path: String) {
        window.history.pushState(null, "", path)
        handleRoute()
    }

    fun start() {
        window.onpopstate = {
            handleRoute()
        }
        handleRoute()
    }
}

