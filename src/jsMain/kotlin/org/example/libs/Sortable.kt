@file:JsModule("sortablejs")
@file:JsNonModule
package org.example.libs

import org.w3c.dom.Element

@JsName("default")
external object Sortable {
    fun create(el: Element, options: SortableOptions)
}

external interface DragEvent {
    val item: Element
    val to: Element
    val from: Element
    val newIndex: Int
    val oldIndex: Int
}

external interface SortableOptions {
    var group: String
    var animation: Int
    var forceFallback: Boolean
    var dragClass: String
    var ghostClass: String
    var easing: String
    var onEnd: (DragEvent) -> Unit
}