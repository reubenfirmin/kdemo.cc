package org.example.framework.dom

import js.objects.jso
import kotlinx.html.CommonAttributeGroupFacade
import kotlinx.html.id
import web.dom.Element
import web.dom.document
import web.dom.observers.IntersectionObserver
import web.dom.observers.MutationObserver
import web.dom.observers.MutationObserverInit
import web.events.Event
import web.events.EventHandler
import web.events.EventType
import web.events.addEventListener
import web.uievents.MouseEvent
import web.window.window
import kotlin.random.Random

/**
 * Bridge between the kotlinx.html dsl and kotlinjs. Attaches events to the DOM once the document is rendered.
 * This will probably be extracted to a library.
 *
 * TODO possibly doesn't handle dynamic UIs.
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

    private fun applyAll() {
        behaviors.forEach { (id, behavior) ->
            document.getElementById(id)?.let { element ->
                behavior(element.unsafeCast<Element>())
            }
        }
        behaviors.clear()

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
            mutations.forEach { mutation ->
                mutation.addedNodes.forEach { node ->
                    if (node is Element) {
                        executeMountCallback(node.id)
                    }
                }
            }
        }

        displayCallbacks.forEach { (id, _) ->
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

        mutationObserver.observe(document.body, jso<MutationObserverInit> {
            childList = true
            subtree = true
        })
    }

    init {
        window.onload = EventHandler {
            applyAll()
        }
    }
}

private fun generateRandomString(length: Int = 7): String {
    val charPool : List<Char> = ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

private fun CommonAttributeGroupFacade.ensureId(): String {
    if (id.isBlank()) {
        id = "element-${generateRandomString()}"
    }
    return id
}

private inline fun <reified E : Event> CommonAttributeGroupFacade.attachEvent(
    type: EventType<E>,
    noinline handler: (E) -> Unit
) {
    val id = ensureId()
    DomBehavior.queue(id) { element ->
        element.addEventListener(type, handler)
    }
}

// Standard event listeners
fun CommonAttributeGroupFacade.onClick(handler: (MouseEvent) -> Unit) = attachEvent<MouseEvent>(EventType("click"), handler)
fun CommonAttributeGroupFacade.onMouseEnter(handler: (MouseEvent) -> Unit) = attachEvent<MouseEvent>(EventType("mouseenter"), handler)
fun CommonAttributeGroupFacade.onMouseLeave(handler: (MouseEvent) -> Unit) = attachEvent<MouseEvent>(EventType("mouseleave"), handler)
fun CommonAttributeGroupFacade.onSubmit(handler: (Event) -> Unit) = attachEvent<Event>(EventType("submit"), handler)
fun CommonAttributeGroupFacade.onChange(handler: (Event) -> Unit) = attachEvent<Event>(EventType("change"), handler)

/**
 * Executed when element becomes visible
 */
fun CommonAttributeGroupFacade.onDisplay(handler: () -> Unit) {
    val id = ensureId()
    DomBehavior.queueDisplay(id, handler)
}

/**
 * Executed when element is added to dom
 */
fun CommonAttributeGroupFacade.onMount(handler: () -> Unit) {
    val id = ensureId()
    DomBehavior.queueMount(id, handler)
}
