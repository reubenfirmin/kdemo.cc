package org.example.framework.routing

import kotlinx.html.TagConsumer

sealed class RouteSegment {
    data class Static(val value: String) : RouteSegment()
    data class Parameter(val name: String) : RouteSegment()
}

class Route(
    val pattern: String,
    val segments: List<RouteSegment>,
    val handler: TagConsumer<*>.(Params) -> Any
)

/**
 * Not typesafe, but lower boilerplate than the Routes approach
 * ```
 * with (MyClass()) {
 *     route("{path}") { it.renderFunc() }
 * }
 * ```
 */
inline fun <reified T : Any> T.route(path: String, crossinline block: T.(TagConsumer<*>) -> Unit) =
    RouteCreator.addRoute(path) {
        block(this@route, this)
    }


/**
 * Not typesafe, but lower boilerplate than the Routes approach
 *
 * ```
 * route (MyClass()) {
 *     "{path}" to { renderFunc() }
 * }
 * ```
 */
fun <T : Any> route(instance: T, block: T.() -> Pair<String, TagConsumer<*>.(Params) -> Any>): Route {
    val (path, handler) = instance.block()
    return RouteCreator.addRoute(path, handler)
}