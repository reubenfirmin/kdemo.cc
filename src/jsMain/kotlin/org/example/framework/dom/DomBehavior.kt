package org.example.framework.dom

import js.objects.jso
import web.dom.Element
import web.dom.document
import web.dom.observers.IntersectionObserver
import web.dom.observers.MutationObserver
import web.dom.observers.MutationObserverInit
import web.dom.observers.MutationRecordType
import web.events.EventHandler
import web.window.window

/**
 * Bridge between the kotlinx.html dsl and kotlinjs. Attaches events to the DOM once the document is rendered.
 */
object DomBehavior {
    private val behaviors = mutableListOf<Pair<String, (Element) -> Unit>>()
    private val mountCallbacks = mutableMapOf<String, () -> Unit>()
    private val displayCallbacks = mutableListOf<Pair<String, () -> Unit>>()
    private lateinit var observer: IntersectionObserver
    private lateinit var mutationObserver: MutationObserver

    fun queue(id: String, behavior: (Element) -> Unit) {
        behaviors.add(id to behavior)
    }

    fun queueDisplay(id: String, callback: () -> Unit) {
        displayCallbacks.add(id to callback)
    }

    fun queueMount(id: String, callback: () -> Unit) {
        mountCallbacks[id] = callback
    }

    private fun executeMountCallback(id: String) {
        mountCallbacks[id]?.let { callback ->
            callback()
            mountCallbacks.remove(id)
        }
    }

    fun flush() {
        behaviors.forEach { (id, behavior) ->
            document.getElementById(id)?.let { element ->
                behavior(element.unsafeCast<Element>())
            }
        }
        behaviors.clear()

        displayCallbacks.forEach { (id, callback) ->
            document.getElementById(id)?.let { element ->
                observer.observe(element)
            }
        }

        // Execute mount callbacks for elements already in the DOM
        mountCallbacks.keys.toList().forEach { id ->
            document.getElementById(id)?.let {
                executeMountCallback(id)
            }
        }
    }

    private fun applyAll() {
        observer = IntersectionObserver({ entries, _ ->
            entries.forEach { entry ->
                if (entry.isIntersecting) {
                    val id = entry.target.id
                    displayCallbacks.find { it.first == id }?.second?.invoke()
                    observer.unobserve(entry.target)
                    displayCallbacks.removeAll { it.first == id }
                }
            }
        }, jso {
            root = null
            rootMargin = "0px"
            threshold = arrayOf(0.0)
        })

        mutationObserver = MutationObserver { mutations, _ ->
            var shouldFlush = false
            mutations.forEach { mutation ->
                when (mutation.type) {
                    MutationRecordType.childList -> {
                        mutation.addedNodes.forEach { node ->
                            if (node is Element) {
                                executeMountCallback(node.id)
                                shouldFlush = true
                            }
                        }
                        if (mutation.removedNodes.length > 0) {
                            shouldFlush = true
                        }
                    }
                    MutationRecordType.attributes -> {
                        if (mutation.attributeName == "id") {
                            shouldFlush = true
                        }
                    }
                    else -> {
                        console.log(mutation)
                    }
                }
            }
            if (shouldFlush) {
                flush()
            }
        }

        mutationObserver.observe(document, jso<MutationObserverInit> {
            childList = true
            subtree = true
            attributes = true
            attributeFilter = arrayOf("id")
        })

        flush()
    }

    init {
        window.onload = EventHandler {
            applyAll()
        }
    }
}

