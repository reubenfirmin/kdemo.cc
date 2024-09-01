package org.example.parts

import js.objects.jso
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.id
import org.example.framework.dom.onMount
import org.example.framework.libs.Sortable
import org.example.framework.libs.SortableEvent
import web.dom.document
import web.html.HTMLElement

// example of how to break the ui up into chunks. this is not a reusable component, it's just a fragment of a ui
fun FlowContent.sortableContainer() {

    div("text-black w-64 flex flex-col space-y-4 p-4 bg-gray-100 min-h-[200px] border-solid border-4 border-sky-500 mt-4 ml-4") {
        id = "sortable-container"

        val itemClasses = "bg-white p-4 rounded drop-shadow-lg cursor-move border-solid border-2 border-sky-500"

        div(itemClasses) {
            +"Draggable Item 1"
        }
        div(itemClasses) {
            +"Draggable Item 2"
        }
        div(itemClasses) {
            +"Draggable Item 3"
        }

        onMount {
            val container = document.getElementById("sortable-container") as HTMLElement
            Sortable.create(container, jso {
                group = "shared"
                animation = 150
                easing = "cubic-bezier(1, 0, 0, 1)"
                onChoose = { event: SortableEvent ->
                    console.log("Element chosen")
                }
                onUnchoose = { event: SortableEvent ->
                    console.log("Element unchosen")
                }
                onStart = { event: SortableEvent ->
                    console.log("Drag started")
                    event.item.classList.remove("bg-white")
                    event.item.classList.add("bg-blue-100", "text-red-500", "rotate-[5deg]")
                }
                onEnd = { event: SortableEvent ->
                    console.log("Drag ended")
                    event.item.classList.remove("bg-blue-100", "text-red-500", "rotate-[5deg]")
                    event.item.classList.add("bg-white")
                    console.log("Moved element from index ${event.oldIndex} to ${event.newIndex}")
                }
            })
        }
    }
}