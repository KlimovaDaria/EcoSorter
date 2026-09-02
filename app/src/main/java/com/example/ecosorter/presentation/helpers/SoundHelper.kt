package com.example.ecosorter.presentation.helpers

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.ecosorter.R

class SoundHelper(private val context: Context) {
    private var soundPool: SoundPool? = null
    private var soundDropId: Int = 0

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool?.let { pool ->
            soundDropId = pool.load(context, R.raw.sounddrop, 1)
        }
    }

    fun playDrop() {
        soundPool?.play(
            soundDropId,
            1.0f,
            1.0f,
            1,
            0,
            1.0f
        )
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}