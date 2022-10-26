package com.ringtones.compose.utils

import com.ringtones.compose.R

class Ringtones {

    val rawSongs = listOf(
        SongDetails(
            R.raw.alarm,
            "Music 1", "Description 1"
        ),
        SongDetails(
            R.raw.music1,
            "Ringtone 1", "Description test"
        ),
        SongDetails(
            R.raw.music2,
            "Music 3", "Description 1"
        ),
    )

    fun getAllRingtone():List<SongDetails>{
        return rawSongs
    }

}


data class SongDetails(
    var raw:Int,
    val title:String,
    val desc:String
)