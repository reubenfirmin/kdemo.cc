package org.example.pages.kanban

import js.objects.jso
import kotlinx.html.*
import org.example.framework.dom.onClick
import org.example.framework.dom.onKeyUp
import org.example.framework.dom.onMount
import org.example.framework.libs.Sortable
import org.example.framework.libs.SortableEvent
import web.dom.document
import web.html.HTMLElement
import web.html.HTMLInputElement

class Kanban {
    private val columns = listOf(
        Column("To Do", mutableListOf("Task 1", "Task 2", "Task 3")),
        Column("In Progress", mutableListOf("Task 4", "Task 5")),
        Column("Done", mutableListOf("Task 6"))
    )

    fun TagConsumer<*>.kanbanBoard() {
        main {
            div("flex space-x-4 p-4") {
                columns.forEachIndexed { index, column ->
                    kanbanColumn(column, index)
                }
            }
        }
    }

    private fun FlowContent.kanbanColumn(column: Column, columnIndex: Int) {
        div("w-64 flex flex-col space-y-4 p-4 bg-gray-100 min-h-[200px] border-solid border-2 border-gray-300 rounded") {
            h2("text-lg font-bold mb-2") {
                +column.name
            }
            div("space-y-2 min-h-[50px]") {
                id = "column-$columnIndex"
                column.tasks.forEach { task ->
                    kanbanCard(task, columnIndex)
                }

                onMount {
                    val container = document.getElementById(this@div.id) as HTMLElement
                    Sortable.create(container, jso {
                        group = "shared"
                        animation = 150
                        easing = "cubic-bezier(1, 0, 0, 1)"
                        onStart = { event: SortableEvent ->
                            event.item.classList.apply {
                                remove("bg-white")
                                add("bg-blue-100", "rotate-[2deg]", "scale-105")
                            }
                        }
                        onEnd = { event: SortableEvent ->
                            event.item.classList.apply {
                                remove("bg-blue-100", "rotate-[2deg]", "scale-105")
                                add("bg-white")
                            }
                            console.log("Moved element from column ${event.from.id} to ${event.to.id}")
                        }
                    })
                }
            }
            div("mt-4") {
                input(type = InputType.text, classes = "w-full p-2 border rounded") {
                    id = "$columnIndex-add"
                    placeholder = "Add new task"
                    onKeyUp { event ->
                        if (event.key == "Enter") {
                            onAddTask(columnIndex)
                        }
                    }
                }
            }
        }
    }

    private fun FlowContent.kanbanCard(task: String, columnIndex: Int) {
        div("bg-white p-3 rounded shadow cursor-move border-solid border border-gray-200 transition-all duration-200 flex justify-between items-center") {
            +task
            button(classes = "text-red-500 hover:text-red-700") {
                id = "$task-$columnIndex-del"
                +"×"
                onClick {
                    onDeleteTask(columnIndex, task)
                }
            }
        }
    }

    private data class Column(val name: String, val tasks: MutableList<String>)

    private fun onAddTask(columnIndex: Int) {
        val input = document.querySelector("#column-$columnIndex input") as HTMLInputElement
        val task = input.value.trim()
        if (task.isNotEmpty()) {
            columns[columnIndex].tasks.add(task)
            input.value = ""
            // Trigger a re-render of the column
            val column = document.getElementById("column-$columnIndex")
            // Implementation of updateColumn function would depend on your framework
            updateColumn(column, columns[columnIndex])
        }
    }

    private fun onDeleteTask(columnIndex: Int, task: String) {
        columns[columnIndex].tasks.remove(task)
        // Trigger a re-render of the column
        val column = document.getElementById("column-$columnIndex")
        // Implementation of updateColumn function would depend on your framework
        updateColumn(column, columns[columnIndex])
    }

    // This function would need to be implemented based on your specific framework
    private fun updateColumn(columnElement: HTMLElement?, column: Column) {
        // Clear existing tasks
        columnElement?.innerHTML = ""
        // Re-render tasks
        column.tasks.forEach { task ->
            // Add task to columnElement
        }
    }
}