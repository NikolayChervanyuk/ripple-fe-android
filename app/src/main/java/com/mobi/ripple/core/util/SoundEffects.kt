package com.mobi.ripple.core.util

import android.content.Context
import android.media.MediaPlayer
import com.mobi.ripple.R

class SoundEffects {
    data object LikeSound {
        fun play(context: Context) {
            MediaPlayer.create(context, R.raw.like_sound).start()
        }
    }

    data object NewMessage {
        fun play(context: Context) {
            MediaPlayer.create(context, R.raw.new_message_sound).start()
        }
    }


}