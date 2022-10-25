package com.anafthdev.musicompose2.utils

import com.anafthdev.musicompose2.R
import com.anafthdev.musicompose2.data.model.Song
import java.util.ArrayList

class Ringtones {


    val songList = ArrayList<Song>()

    val rawSongs=listOf<SongDetails>(
        SongDetails(
            R.raw.alarm,
            "Music 1","Description 1"),
        SongDetails(
            R.raw.music1,
            "Ringtone 1","Description test"),
        SongDetails(
            R.raw.music2,
            "Music 3","Description 1"),


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