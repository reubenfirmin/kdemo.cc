package org.example.framework.dom

import kotlinx.html.CommonAttributeGroupFacade
import kotlinx.html.id
import web.events.Event
import web.events.EventType
import web.events.addEventListener
import web.uievents.InputEvent
import web.uievents.KeyboardEvent
import web.uievents.MouseEvent
import kotlin.random.Random

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
// XXX these all require that id is defined on the attribute first; the logic above that tries to generate an id isn't working
fun CommonAttributeGroupFacade.onClick(handler: (MouseEvent) -> Unit) = attachEvent<MouseEvent>(EventType("click"), handler)
fun CommonAttributeGroupFacade.onMouseEnter(handler: (MouseEvent) -> Unit) = attachEvent<MouseEvent>(EventType("mouseenter"), handler)
fun CommonAttributeGroupFacade.onMouseLeave(handler: (MouseEvent) -> Unit) = attachEvent<MouseEvent>(EventType("mouseleave"), handler)
fun CommonAttributeGroupFacade.onSubmit(handler: (Event) -> Unit) = attachEvent<Event>(EventType("submit"), handler)
fun CommonAttributeGroupFacade.onChange(handler: (Event) -> Unit) = attachEvent<Event>(EventType("change"), handler)
fun CommonAttributeGroupFacade.onKeyUp(handler: (KeyboardEvent) -> Unit) = attachEvent<KeyboardEvent>(EventType("keyup"), handler)
fun CommonAttributeGroupFacade.onKeyDown(handler: (KeyboardEvent) -> Unit) = attachEvent<KeyboardEvent>(EventType("keydown"), handler)
fun CommonAttributeGroupFacade.onLoad(handler: (Event) -> Unit) = attachEvent<Event>(EventType("load"), handler)
fun CommonAttributeGroupFacade.onError(handler: (Event) -> Unit) = attachEvent<Event>(EventType("error"), handler)
fun CommonAttributeGroupFacade.onInput(handler: (InputEvent) -> Unit) = attachEvent<InputEvent>(EventType("input"), handler)


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
