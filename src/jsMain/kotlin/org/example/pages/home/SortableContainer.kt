package org.example.pages.home

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
            val container = document.getElementById(this@div.id) as HTMLElement
            Sortable.create(container, jso {
                group = "shared"
                animation = 150
                easing = "cubic-bezier(1, 0, 0, 1)"
                onChoose = { _ ->
                    console.log("Element chosen")
                }
                onUnchoose = { _ ->
                    console.log("Element unchosen")
                }
                onStart = { event: SortableEvent ->
                    event.item.classList.apply {
                        remove("bg-white")
                        add("bg-blue-100", "text-red-500", "rotate-[5deg]")
                    }
                    console.log("Drag started")
                }
                onEnd = { event: SortableEvent ->
                    event.item.classList.apply {
                        remove("bg-blue-100", "text-red-500", "rotate-[5deg]")
                        add("bg-white")
                    }
                    console.log("Moved element from index ${event.oldIndex} to ${event.newIndex}")
                }
            })
        }
    }
}