package org.example.audio

import org.example.audio.grid.GridEvent
import org.example.audio.instrument.*
import org.example.audio.instrument.InstrumentType.RHYTHM
import org.example.audio.instrument.InstrumentType.TONAL
import web.audio.*
import kotlin.math.abs

class AudioState {
    private var audioContext = AudioContext()
    private var isClosing = false

    var analyser = audioContext.createAnalyser().apply {
        fftSize = 256
        smoothingTimeConstant = 0.4
    }

    private val activeInstruments = mutableMapOf<InstrumentId, Instrument>()

    fun currentTime() = audioContext.currentTime

    fun disconnect() {
        if (audioContext.state != AudioContextState.closed && !isClosing) {
            isClosing = true
            activeInstruments.forEach {
                it.value.disconnect()
            }
            audioContext.closeAsync().then { isClosing = false }
        }
    }

    fun setParameter(instrumentId: InstrumentId, parameter: Parameter, value: Double) {
        activeInstruments[instrumentId]?.setParameterValue(parameter, value)
    }

    fun availableParameters(instrument: InstrumentId): List<AvailableParameter> {
        return activeInstruments[instrument]?.availableParameters() ?: listOf()
    }

    fun sequence(instrument: InstrumentId, time: Double, event: GridEvent): Boolean {
        val instrumentInstance = when (instrument.type) {
            RHYTHM -> activeInstruments.getOrPut(instrument) {
                InstrumentFactory.rhythm(instrument, audioContext, analyser)
            }

            TONAL -> activeInstruments.getOrPut(instrument) {
                InstrumentFactory.tonal(instrument, audioContext, analyser)
            }
        }

        // Check if the proposed time clashes with any scheduled notes
        if (instrumentInstance.scheduledEventTimes().none { abs(it - time) < 0.001 }) { // 1ms tolerance
            when (instrument.type) {
                // TODO the casting is janky
                RHYTHM -> (activeInstruments[instrument] as RhythmInstrument).play(time, event.velocity)
                TONAL -> (activeInstruments[instrument] as TonalInstrument).play(time, event.note, event.octave, event.velocity)
            }
            return true
        }
        return false
    }

    fun resume() {
        if (audioContext.state == AudioContextState.closed || isClosing) {
            activeInstruments.clear()
            audioContext = AudioContext()
            isClosing = false
            analyser = audioContext.createAnalyser().apply {
                fftSize = 256
                smoothingTimeConstant = 0.4
            }
        } else {
            audioContext.resumeAsync()
        }
    }
}