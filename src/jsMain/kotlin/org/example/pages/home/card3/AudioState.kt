package org.example.pages.home.card3

import web.audio.*

class AudioState {
    private var audioContext = AudioContext()
    private var isClosing = false

    var analyser = audioContext.createAnalyser().apply {
        fftSize = 256
        smoothingTimeConstant = 0.4
    }
    var kickDrum = KickDrum(audioContext, analyser)
    var hiHat = HiHat(audioContext, analyser)
    var tb303 = TB303(audioContext, analyser)

    fun currentTime() = audioContext.currentTime

    fun disconnect() {
        if (audioContext.state != AudioContextState.closed && !isClosing) {
            isClosing = true
            kickDrum.disconnect()
            hiHat.disconnect()
            tb303.disconnect()
            audioContext.closeAsync().then { isClosing = false }
        }
    }

    fun resume() {
        if (audioContext.state == AudioContextState.closed || isClosing) {
            audioContext = AudioContext()
            isClosing = false
            analyser = audioContext.createAnalyser().apply {
                fftSize = 256
                smoothingTimeConstant = 0.4
            }
            kickDrum = KickDrum(audioContext, analyser)
            hiHat = HiHat(audioContext, analyser)
            tb303 = TB303(audioContext, analyser)
        } else {
            audioContext.resumeAsync()
        }
    }
}