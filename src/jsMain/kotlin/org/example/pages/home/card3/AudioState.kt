package org.example.pages.home.card3

import web.audio.*

class AudioState {

    private var audioContext = AudioContext()
    var kickDrum = KickDrum(audioContext)
    var hiHat = HiHat(audioContext)
    var tb303 = TB303(audioContext)

//    private var connected = true

    fun currentTime() = audioContext.currentTime

    fun disconnect() {
        kickDrum.disconnect()
        hiHat.disconnect()
        tb303.disconnect()
        audioContext.closeAsync()
//        connected = false
    }

    fun resume() {
        if (audioContext.state == AudioContextState.closed) {
            audioContext = AudioContext()
            kickDrum = KickDrum(audioContext)
            hiHat = HiHat(audioContext)
            tb303 = TB303(audioContext)
        } else {
            audioContext.resumeAsync()
        }
    }
}