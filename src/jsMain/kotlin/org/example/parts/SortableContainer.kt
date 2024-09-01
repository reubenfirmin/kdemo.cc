package org.example.parts

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.id
import org.example.libs.DragEvent
import org.example.libs.Sortable
import org.example.libs.SortableOptions
import org.w3c.dom.HTMLElement

// example of how to break the ui up into chunks. this is not a reusable component, it's just a fragment of a ui
fun FlowContent.sortableContainer() {

    div {
        id = "sortable-container"
        classes = setOf("flex", "flex-col", "space-y-4", "p-4", "bg-gray-100", "min-h-[300px]")

        div("bg-white p-4 rounded shadow cursor-move") {
            +"Draggable Item 1"
        }
        div("bg-white p-4 rounded shadow cursor-move") {
            +"Draggable Item 2"
        }
        div("bg-white p-4 rounded shadow cursor-move") {
            +"Draggable Item 3"
        }

        window.requestAnimationFrame {
            console.log("Hello world!")
            val container = document.getElementById("sortable-container") as HTMLElement
            Sortable.create(container, object : SortableOptions {
                override var group = "shared"
                override var animation = 150
                override var forceFallback = true
                override var dragClass = "bg-blue-100"
                override var ghostClass = "bg-blue-200 opacity-80"
                override var easing = "cubic-bezier(1, 0, 0, 1)"
                override var onEnd = { event: DragEvent ->
                    console.log("Moved element from index ${event.oldIndex} to ${event.newIndex}")
                }
            })
        }
    }
}