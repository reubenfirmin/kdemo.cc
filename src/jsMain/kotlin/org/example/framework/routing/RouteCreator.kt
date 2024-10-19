package org.example.framework.routing

import kotlinx.html.TagConsumer

object RouteCreator {

    fun addRoute(path: String, handler: TagConsumer<*>.(Params) -> Any): Route {
        val segments = path.split("/").filter {
            it.isNotEmpty()
        }.map { segment ->
            if (segment.startsWith("{") && segment.endsWith("}")) {
                RouteSegment.Parameter(segment.trim('{', '}'))
            } else {
                RouteSegment.Static(segment)
            }
        }
        val route = Route(path, segments, handler)
        RouteTrie.addRoute(route)
        return route
    }
}