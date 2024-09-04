# Kotlin.js + Tailwind + Kotlinx.html demos

This is a set of frontend experiments using Kotlin.js, Tailwind and Kotlinx.html. Typesafe UIs with a convenient DSL, and a better IDE experience - what's not to love? (This also uses kotlin-browser, a new-i sh library that provides an update to the older org.w3c.dom classes provided in kotlin.js.)

Status: weekend code, not yet used in production.

[Hosted Demo](http://kdemo.cc)

## To Run Locally

`./gradlew watch`

This will open a browser tab which will hot reload on changes to code. Tailwind is automatically rerun when new styles are added.

## To Build For Production

`./gradlew deploy`

Deploy the contents of `dist`.

## The Code

[Kotlinx.html](https://github.com/Kotlin/kotlinx.html) is a library that deserves more attention. It gives you a typesafe DSL that generates HTML, and can be used on the backend or 
(as in the case of this project) frontend. As an example of this, the SvgTag class which is included in this project is lifted as-is (and only slightly enhanced) from a backend HTMX 
oriented project.

To give you a taste:

```kotlin
fun FlowContent.blogDemoCard() {
    card("bg-blue-700 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-300 overflow-hidden flex flex-col justify-between") {
        div("h-2 bg-gradient-to-r from-purple-400 via-red-500 to-pink-500")

        div("p-6") {
            h2("text-xl font-semibold mb-4 text-gray-100") {
                +"Blog Demo"
            }
        ...
```

## Debug in IDE

Check out https://kotlinlang.org/docs/js-debugging.html for instructions on debugging in IntelliJ.

# Demos

## Dashboard

### 1) Text Styling

Showcases applying tailwind styles. 

### 2) Button Demo

Example of using SVG, combined with tailwind animations.

### 3) Chart Demo

SVG charting + web audio API.

## Blog

Rudimentary demo right now - just showcases events.

## Book Search

Simple ecommerce layout. Implements simple axios-like http client which is used to interact with an API.

## Scope

Full screen charting using Canvas.

## Kanban

Demonstrates integrating an npm library (in this case sortable.js).

# Worth Checking Out

## Event Handling 

Events.kt (under org.example.framework.dom) provides common event handlers which can be plugged into the DSL. Because the elements don't exist at the time the DSL is being executed,
calling any of these functions queues up an event handler to be added as soon as the DOM has settled. This seems to work pretty robustly, though I haven't tried this on a large scale app.

## Routing

Router.kt implements a very simple router. Any navigation is intercepted, and the body is swapped out with the new "page". Main.kt wires this up for this app.

## Calling APIs

The booksearch demo implements a simple HttpClient - just a wrapper around fetch with some convenience. BookApi demonstrates deserializing responses.
