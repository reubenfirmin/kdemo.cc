package org.example.pages.home.card3

import kotlinx.browser.window
import web.audio.AnalyserNode
import kotlin.math.log10
import kotlin.math.pow

class TechnoPlayer {

    private var audioState: AudioState? = null

    private var rhythmIntervalId: Int? = null
    private var beatCount = 0

    private var tb303Cutoff = logToLinear(1000)
    private var tb303Delay = 70
    private var activeGrid = 0
    private var started = false

    fun startTechno(): AnalyserNode {

        if (started) {
            return audioState!!.analyser
        }

        if (audioState == null) {
            audioState = AudioState()
        } else {
            audioState!!.resume()
        }

        with (audioState!!) {

            val interval = (60.0 / 130 * 1000 / 4).toInt() // 130bpm 16th notes...I think

            val song = HitTheClub(kickDrum, hiHat, tb303)

            activeGrid++

            started = true
            // TODO is there a better clock? surely
            rhythmIntervalId = window.setInterval({
                // adjust these each tick just for state simplicity
                tb303.setDelayTime(this@TechnoPlayer.tb303Delay)
                tb303.setFilterCutoff(this@TechnoPlayer.tb303Cutoff)
                song.grid(beatCount++, currentTime())
            }, interval)
            return analyser
        }
    }

    fun stopTechno() {
        if (audioState != null) {
            audioState!!.disconnect()
        }
        started = false
        rhythmIntervalId?.let { window.clearInterval(it) }
        beatCount = 0

        // We're not closing the AudioContext here to allow for quick restart
    }

    fun isPlaying() = started

    /**
     * Set cutoff - 0-1000 (mapped to 20-20000hz)
     */
    fun setCutoff(cutoff: Int) {
        require(cutoff in 0..1000) {
            "cutoff must be between 0 and 1000."
        }
        tb303Cutoff = linearToLog(cutoff).toInt()
    }

    fun getCutoffDisplay() = logToLinear(tb303Cutoff)

    /**
     * Set delay - 0 to 100
     */
    fun setDelay(delay: Int) {
        tb303Delay = delay
    }

    fun getDelayDisplay() = tb303Delay

    private fun linearToLog(value: Int): Float {
        val minFreq = 20f
        val maxFreq = 20000f
        return minFreq * 10.0.pow((value / 1000.0) * log10(maxFreq / minFreq)).toFloat()
    }

    private fun logToLinear(freq: Int): Int {
        val minFreq = 20f
        val maxFreq = 20000f
        return ((log10(freq / minFreq) / log10(maxFreq / minFreq)) * 1000).toInt().coerceIn(0, 1000)
    }
}