package com.anafthdev.musicompose2.utils


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.anafthdev.musicompose2.R
import com.anafthdev.musicompose2.data.model.Song
import com.anafthdev.musicompose2.runtime.MainActivity
import java.util.*

data class SongDetails(
    var raw:Int,
    val title:String,
    val desc:String
)
object SongUtil2 {

    fun getSong(
        context: Context,
        isTracksSmallerThan100KBSkipped: Boolean = true,
        isTracksShorterThan60SecondsSkipped: Boolean = true
    ): List<Song> {

        val songList = ArrayList<Song>()

        val rawSongs=listOf<SongDetails>(
            SongDetails(R.raw.alarm,
            "Music 1","Description 1"),
            SongDetails(R.raw.music1,
                "Ringtone 1","Description test"),
            SongDetails(R.raw.music2,
                "Music 3","Description 1"),


        )
        //youDesirePermissionCode(context as Activity)




        rawSongs.forEach { Mysong->
            val mediaPath =
                Uri.parse("android.resource://" + context.getPackageName().toString() + "/" +Mysong.raw)
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(context, mediaPath)
            val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val sponsorArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            val audioTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val image = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)

            Log.d("MUSICURLL",duration.toString())
            Log.d("MUSICURLL",sponsorArtist.toString())



            val song = Song(
                audioID = Mysong.raw.toLong(),
                displayName = Mysong.title,
                title = Mysong.title,
                artist =  Mysong.desc ,
                artistID = "artistID",
                album = "album",
                albumID = "albumId",
                duration = duration!!.toLong(),
                albumPath = "albumPath.toString()",
                path = "android.resource://com.anafthdev.musicompose2/" + Mysong.raw,
                dateAdded = 10000
            )


            songList.add(song)

        }

        //RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, Uri.parse("android.resource://" + context.getPackageName().toString() + "/raw/music2.mp3" ));

        return songList
    }


}