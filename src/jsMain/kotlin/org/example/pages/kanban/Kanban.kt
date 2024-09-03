package org.example.pages.kanban

import js.objects.jso
import kotlinx.html.*
import kotlinx.html.dom.append

import org.example.framework.dom.onClick
import org.example.framework.dom.onKeyUp
import org.example.framework.dom.onMount
import org.example.framework.interop.appendTo
import org.example.framework.libs.Sortable
import org.example.framework.libs.SortableEvent
import web.dom.document
import web.html.HTMLElement
import web.html.HTMLInputElement
import kotlin.random.Random

class Kanban {
    data class Task(val id: String, val content: String)
    data class Column(val name: String, val tasks: MutableList<Task>)

    private val columns = mutableListOf(
        Column("To Do", mutableListOf(Task("1", "Task 1"), Task("2", "Task 2"), Task("3", "Task 3"))),
        Column("In Progress", mutableListOf(Task("4", "Task 4"), Task("5", "Task 5"))),
        Column("Done", mutableListOf(Task("6", "Task 6")))
    )

    fun TagConsumer<*>.kanbanBoard() {
        div("min-h-screen bg-gradient-to-br from-blue-500 to-purple-600 flex flex-col") {
            main("flex-grow p-8") {
                div("flex space-x-6") {
                    columns.forEachIndexed { index, column ->
                        kanbanColumn(column, index)
                    }
                }
            }
            actionBar()
        }
    }

    private fun FlowContent.kanbanColumn(column: Column, columnIndex: Int) {
        div("w-80 flex flex-col space-y-4 p-6 bg-white bg-opacity-20 backdrop-filter backdrop-blur-lg rounded-lg shadow-lg") {
            h2("text-2xl font-bold mb-4 text-white") {
                +column.name
            }
            div("space-y-3 min-h-[200px] flex flex-col") {
                id = "column-$columnIndex"
                column.tasks.forEach { task ->
                    kanbanCard(task, columnIndex)
                }
                // Add an invisible placeholder to ensure there's always a droppable area
                div("flex-grow") {
                    attributes["data-placeholder"] = "true"
                }

                onMount {
                    val container = document.getElementById(this@div.id) as HTMLElement
                    Sortable.create(container, jso {
                        group = "shared"
                        animation = 150
                        easing = "cubic-bezier(0.25, 1, 0.5, 1)"
                        onStart = { event: SortableEvent ->
                            event.item.classList.apply {
                                add("scale-105", "shadow-xl", "bg-blue-100", "text-red-500", "rotate-[2deg]")
                            }
                        }
                        onEnd = { event: SortableEvent ->
                            event.item.classList.apply {
                                remove("scale-105", "shadow-xl", "bg-blue-100", "text-red-500", "rotate-[2deg]")
                                add("bg-white")
                            }
                            // Update the data model when a card is moved
                            val fromColumnIndex = event.from.id.split("-").last().toInt()
                            val toColumnIndex = event.to.id.split("-").last().toInt()
                            val taskId = event.item.id
                            val fromColumn = columns[fromColumnIndex]
                            val toColumn = columns[toColumnIndex]
                            val taskIndex = fromColumn.tasks.indexOfFirst { it.id == taskId }

                            if (taskIndex != -1) {
                                val task = fromColumn.tasks.removeAt(taskIndex)
                                val newIndex = event.newIndex.coerceAtMost(toColumn.tasks.size)
                                toColumn.tasks.add(newIndex, task)
                            }

                            refreshBoard()
                        }
                    })
                }
            }
        }
    }

    private fun FlowContent.kanbanCard(task: Task, columnIndex: Int) {
        div("bg-white p-4 rounded-lg shadow-md cursor-move border-l-4 border-blue-500 hover:shadow-lg transition-all duration-200 flex justify-between items-center") {
            id = task.id
            +task.content
            button(classes = "text-gray-400 hover:text-red-500 transition-colors duration-200") {
                id = "delete-${task.id}"
                +"×"
                onClick {
                    deleteTask(columnIndex, task.id)
                }
            }
        }
    }

    private fun FlowContent.actionBar() {
        div("bg-white bg-opacity-10 backdrop-filter backdrop-blur-lg p-4 flex justify-between items-center") {
            a(href = "/", classes = "text-white hover:text-blue-200 transition-colors duration-200") {
                +"← Back"
            }
            div("flex-grow mx-4") {
                input(type = InputType.text, classes = "w-full p-3 rounded-full bg-white bg-opacity-20 text-white placeholder-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-300") {
                    id = "add-task-input"
                    placeholder = "Add new task"
                    autoFocus = true
                    onKeyUp { event ->
                        if (event.key == "Enter") {
                            addTask()
                        }
                    }
                }
            }
            button(classes = "bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-full transition-colors duration-200") {
                id = "add-task"
                +"Add Task"
                onClick {
                    addTask()
                }
            }
        }
    }

    private fun addTask() {
        val input = document.getElementById("add-task-input") as HTMLInputElement
        val taskContent = input.value.trim()
        if (taskContent.isNotEmpty()) {
            val newTask = Task(generateUniqueId(), taskContent)
            columns[0].tasks.add(newTask)  // Add to "To Do" column
            input.value = ""
            refreshBoard()
        }
        input.focus()
    }

    private fun deleteTask(columnIndex: Int, taskId: String) {
        val column = columns[columnIndex]
        val taskToRemove = column.tasks.find { it.id == taskId }
        if (taskToRemove != null) {
            column.tasks.remove(taskToRemove)
            refreshBoard()
        }
    }

    private fun refreshBoard() {
        // Re-render the entire board
        val boardContainer = document.querySelector("main") as HTMLElement
        boardContainer.innerHTML = ""
        boardContainer.appendTo().div("flex space-x-6") {
            columns.forEachIndexed { index, column ->
                kanbanColumn(column, index)
            }
        }
    }

    private fun generateUniqueId(): String {
        return "task-${Random.nextInt(100000, 999999)}"
    }
}