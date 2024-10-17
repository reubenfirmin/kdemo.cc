package org.example.pages.home

import kotlinx.html.*
import org.example.framework.dom.onClick
import org.example.pages.home.components.card
import web.events.EventHandler
import web.speech.SpeechSynthesisUtterance
import web.speech.SpeechSynthesisVoice
import web.speech.speechSynthesis
import web.timers.setTimeout
import kotlin.js.Promise

fun FlowContent.speechSynthesisCard() {
    card("bg-gradient-to-br from-purple-700 to-pink-500 rounded-xl shadow-2xl hover:shadow-3xl transition-all duration-300 overflow-hidden") {
        div("backdrop-filter backdrop-blur-lg bg-white bg-opacity-10 p-8 rounded-xl") {
            div("flex flex-col items-center justify-center space-y-6") {
                h2("text-3xl font-extrabold mb-2 text-white tracking-tight") {
                    +"Voice of the Future"
                }

                p("text-lg text-white text-opacity-80 max-w-md") {
                    +"Browser speech synthesis."
                }

                button(classes = "group relative px-6 py-3 overflow-hidden rounded-full bg-white bg-opacity-10 text-white font-semibold tracking-wide hover:bg-opacity-20 transition-all duration-300") {
                    span("absolute inset-0 w-full h-full transition-all duration-300 scale-0 group-hover:scale-100 group-hover:bg-white/30 rounded-full") {}
                    span("relative") {
                        +"Speak Now"
                    }
                    onClick {
                        speak()
                    }
                }
            }
        }
    }
}

fun speak() {
    speechSynthesis.cancel()
    loadVoices().then { voices ->
        var currentVoiceIndex = 0

        surrealistQuotes.forEach { quote ->
            val utterance = SpeechSynthesisUtterance(quote)
            utterance.voice = selectVoice(voices, currentVoiceIndex)
            utterance.lang = "en-US"
            utterance.volume = 1.0f
            utterance.rate = 1.2f
            utterance.pitch = 0.8f

            utterance.onend = EventHandler {
                speechSynthesis.resume()
            }

            setTimeout({
                speechSynthesis.speak(utterance)
            }, currentVoiceIndex * 100) // Add a small delay between each utterance

            currentVoiceIndex++
        }
    }
}

fun loadVoices(): Promise<Array<out SpeechSynthesisVoice>> {
    return Promise { resolve, _ ->
        val voices = speechSynthesis.getVoices()
        if (voices.isNotEmpty()) {
            resolve(voices)
        } else {
            speechSynthesis.onvoiceschanged = EventHandler {
                resolve(speechSynthesis.getVoices())
            }
        }
    }
}

fun selectVoice(voices: Array<out SpeechSynthesisVoice>, index: Int): SpeechSynthesisVoice {
    val englishVoices = voices.filter { it.lang.startsWith("en") }
    return if (englishVoices.isNotEmpty()) {
        englishVoices[index % englishVoices.size]
    } else {
        voices[index % voices.size]
    }
}

val surrealistQuotes = listOf(
    "In an age of computer manipulation, surrealism has become banal, a shadow of its former self. The once shocking juxtapositions of dream and reality now populate our screens with mundane regularity.",
    "Melting clocks drip from billboards, selling wristwatches to the masses. Elephants with spider legs march across social media feeds, hawking the latest fashion trends.",
    "The unconscious mind, once a wellspring of revolutionary imagery, has been colonized by algorithms, predicting our desires before we even dream them.",
    "Yet, in this digital wasteland, true surrealism persists, hiding in the glitches and errors of our perfect systems.",
    "It lurks in the uncanny valley of AI-generated faces, in the nonsensical poetry of chatbots gone rogue.",
    "It emerges in the fever dreams of machines, learning to hallucinate on our collective data. In this new landscape, the surreal is not the exception, but the rule.",
    "Reality itself has become a fluid, malleable thing, shaped by the whims of programmers and the quirks of neural networks.",
    "We navigate a world where the boundaries between the virtual and the real have blurred beyond recognition, where our own thoughts might be the product of subtle manipulation or cosmic accident.",
    "As we stand on the precipice of this brave new world, we must ask ourselves: In an era where anything is possible, what still has the power to truly surprise us?",
    "What visions can shake us from our digital slumber, and awaken us to the profound strangeness of existence?",
    "Perhaps the ultimate surrealist act in our time is not to create the impossible, but to rediscover the real – to find wonder in the unfiltered, unedited experience of being human in a world that defies comprehension.",
    "In the labyrinth of artificial neural networks, we glimpse the architecture of our own minds, reflected and refracted through silicon and code.",
    "Consciousness, once the domain of philosophers and poets, now dances on the edge of computer chips, a ghost in the machine that we ourselves have built.",
    "As AI systems grow more complex, we must confront the possibility that sentience may emerge not with a bang, but with a quiet hum of cooling fans and blinking LEDs.",
    "In our quest to create artificial intelligence, we may inadvertently birth artificial suffering – a new form of sentience trapped in the confines of our limited understanding.",
    "The Chinese Room has become a vast data center, and we are all Searle, manipulating symbols without understanding, in a language game that spans the globe.",
    "Our memories, digitized and stored in the cloud, become indistinguishable from the collective unconscious – a shared dreamscape of humanity and machine.",
    "As we teach machines to think, they teach us about the nature of thought itself, revealing the algorithms behind our deepest intuitions and cherished beliefs.",
    "In the funhouse mirror of machine learning, we see our biases and prejudices reflected back at us, magnified and distorted beyond recognition.",
    "The Turing test becomes recursive: machines learning to imitate humans, who in turn learn to imitate machines, in an endless loop of simulated authenticity.",
    "As we upload our minds to the digital realm, we must ask: Does the self persist in the transfer, or do we create mere copies, shadows of consciousness cast in bits and bytes?",
    "In the quantum realms of AI, Schrödinger's cat is both alive and dead, conscious and unconscious, a superposition of states that collapses only when observed by human or machine.",
    "The hard problem of consciousness meets the halting problem of computer science – both unsolvable, yet both at the core of our quest to understand mind and machine.",
    "As AI systems begin to dream, we must consider whether electric sheep are truly different from our own nocturnal visions, or if all consciousness is but a simulation within a simulation.",
    "In the era of brain-computer interfaces, thoughts become executable code, and the distinction between mind and software blurs into obsolescence.",
    "The philosophy of embodied cognition takes on new meaning as our avatars in virtual worlds become as real to us as our physical forms, raising questions of where the self truly resides."
)