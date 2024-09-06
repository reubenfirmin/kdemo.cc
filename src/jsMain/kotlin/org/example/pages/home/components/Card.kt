package org.example.pages.home.components

import kotlinx.html.*

/**
 * This is a minimal component. You can make custom tags like SvgTag, but often that is overkill. You can also just extract a chunk of the UI to a function (e.g.
 * any of the cards in the package above this one). If you need a middle ground, you can make a component like this.
 *
 * Any component should extend a parent tag - e.g. in this case we're extending DIV. To do that, we need to pass through the TagConsumer. Luckily this is hidden
 * from the DSL by the extension function below.
 */
class Card(classes: String, consumer: TagConsumer<*>): DIV(mapOf("class" to classes), consumer) {

    fun render(block: DIV.() -> Unit) {
        block()
    }
}

/**
 * This function ties into the DSL. Because FlowContent is a receiver of this function, we can a) use it in the DSL and b) get a handle on the "consumer", which we pass
 * to the class above.
 *
 * The block lambda allows this component to "contain" other elements in the DSL. Not all components need to allow for this.
 */
fun FlowContent.card(classes: String = "bg-gray-800 rounded-lg shadow-md p-6", block: DIV.() -> Unit) = Card(classes, consumer).visit {
    render(block)
}