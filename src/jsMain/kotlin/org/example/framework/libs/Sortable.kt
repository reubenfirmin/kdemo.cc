@file:JsModule("sortablejs")
@file:JsNonModule
package org.example.framework.libs

import web.dom.Element
import web.geometry.DOMRect


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

external interface MoveEvent {
    val dragged: Element
    val draggedRect: DOMRect
    val related: Element
    val relatedRect: DOMRect
    val willInsertAfter: Boolean
}

external interface SortableEvent {
    val item: Element
    val from: Element
    val to: Element
    val oldIndex: Int
    val newIndex: Int
    val oldDraggableIndex: Int
    val newDraggableIndex: Int
    val clone: Element
    val pullMode: String?
}

external interface SortableOptions {
    var group: String?
    var sort: Boolean?
    var delay: Int?
    var delayOnTouchOnly: Boolean?
    var touchStartThreshold: Int?
    var disabled: Boolean?
    var store: Any?
    var animation: Int?
    var easing: String?
    var handle: String?
    var filter: String?
    var preventOnFilter: Boolean?
    var draggable: String?
    var dataIdAttr: String?
    var ghostClass: String?
    var chosenClass: String?
    var dragClass: String?
    var swapThreshold: Double?
    var invertSwap: Boolean?
    var invertedSwapThreshold: Double?
    var direction: String?
    var forceFallback: Boolean?
    var fallbackClass: String?
    var fallbackOnBody: Boolean?
    var fallbackTolerance: Int?
    var dragoverBubble: Boolean?
    var removeCloneOnHide: Boolean?
    var emptyInsertThreshold: Int?
    var onChoose: ((event: SortableEvent) -> Unit)?
    var onUnchoose: ((event: SortableEvent) -> Unit)?
    var onStart: ((event: SortableEvent) -> Unit)?
    var onEnd: ((event: SortableEvent) -> Unit)?
    var onAdd: ((event: SortableEvent) -> Unit)?
    var onUpdate: ((event: SortableEvent) -> Unit)?
    var onSort: ((event: SortableEvent) -> Unit)?
    var onRemove: ((event: SortableEvent) -> Unit)?
    var onMove: ((event: MoveEvent) -> Boolean?)?
    var onClone: ((event: SortableEvent) -> Unit)?
    var onChange: ((event: SortableEvent) -> Unit)?
}