package org.example.framework.routing

class Params(private val map: Map<String, String> = emptyMap()) {
    operator fun get(key: String): String? = map[key]
}