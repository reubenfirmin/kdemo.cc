package org.example

import org.example.framework.Router
import org.example.framework.Routes
import org.example.framework.routes
import org.example.pages.Index
import org.example.pages.blog.BlogIndex
import org.example.pages.booksearch.BookSearch
import org.example.pages.kanban.Kanban
import org.example.pages.oscilloscope.Oscilloscope

object App {

    object index: Routes<Index>(Index(), "/") {
        val home = route { "" to { home() }}
    }

    object books: Routes<BookSearch>(BookSearch(), "/books") {
        val search = route { "/search" to { render() }}
    }

    val blog = routes(BlogIndex(), "/blog") {
        val main = route { "/posts" to { blogIndex() }}
    }

    val scope = routes(Oscilloscope(), "/scope") {
        val main = route { "/view" to { oscilloscopeDemo() }}
    }

    val kanban = routes(Kanban(), "/kanban") {
        val board = route { "/board" to { kanbanBoard() }}
    }
}

fun main() {
    App
    Router.start()
}

