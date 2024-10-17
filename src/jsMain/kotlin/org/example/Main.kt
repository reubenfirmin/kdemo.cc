package org.example

import org.example.framework.Router
import org.example.framework.route
import org.example.pages.blog.BlogIndex
import org.example.pages.booksearch.BookSearch
import org.example.pages.Index
import org.example.pages.kanban.Kanban
import org.example.pages.oscilloscope.Oscilloscope

fun main() {

    route(Index()) {
        "/" to { home() }
    }

    route(BlogIndex()) {
        "/blog" to { blogIndex() }
    }

    route(BookSearch()) {
        "/book-search" to { render() }
    }

    route(Oscilloscope()) {
        "/scope" to { oscilloscopeDemo() }
    }

    route(Kanban()) {
        "/kanban" to { kanbanBoard() }
    }

    Router.start()
}

