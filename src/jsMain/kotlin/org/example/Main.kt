package org.example

import org.example.framework.Router
import org.example.framework.Routes
import org.example.pages.Index
import org.example.pages.blog.BlogIndex
import org.example.pages.booksearch.BookSearch
import org.example.pages.kanban.Kanban
import org.example.pages.oscilloscope.Oscilloscope

class App {

    // type safe routing demonstrated here; by this i mean that you can, from elsewhere in the app, call
    // `App.index.home` to get a handle on the Route object. this is pretty sweet, as it means the only
    // magic strings involved in routing live in one place. but, there's a couple bits of boilerplate here
    // that i'd like to get rid of, so, contributions welcome:

    // a) the "<Index>(Index()" - this is just redundant. (b is 20+ lines down)
    object index: Routes<Index>(Index(), "/") {
        val home = route { "" to { home() }}
        // this is an easter egg. (just to demo that you can have multiple routes per path)
        val fish = route { "/fish" to { fish() }}
    }

    object books: Routes<BookSearch>(BookSearch(), "/books") {
        val search = route { "/search" to { render() }}
    }

    object blog: Routes<BlogIndex>(BlogIndex(), "/blog") {
        val main = route { "/posts" to { blogIndex() }}
    }

    object scope: Routes<Oscilloscope>(Oscilloscope(), "/scope") {
        val main = route { "/view" to { oscilloscopeDemo() }}
    }

    object kanban: Routes<Kanban>(Kanban(), "/kanban") {
        val board = route { "/board" to { kanbanBoard() }}
    }

    init {
        // b) and, crucially, we have to do something with these, otherwise the routes don't actually get initialized
        listOf(index, books, blog, scope, kanban).forEach { _ ->
            console
        }
    }
}

fun main() {
    App()
    Router.start()
}

