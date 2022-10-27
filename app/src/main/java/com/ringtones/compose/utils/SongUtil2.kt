package com.ringtones.compose.utils


import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.ringtones.compose.data.model.Song


object SongUtil2 {

    fun getSong(
        context: Context,
        isTracksSmallerThan100KBSkipped: Boolean = true,
        isTracksShorterThan60SecondsSkipped: Boolean = true
    ): List<Song> {

        val songList = ArrayList<Song>()
        val rawSongs=Ringtones().getAllRingtone()

        rawSongs.forEach { Mysong->
            val mediaPath =
                Uri.parse("android.resource://" + context.packageName.toString() + "/" + Mysong.raw)
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(context, mediaPath)
            val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val sponsorArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            val audioTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val image = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)

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
                path = "android.resource://${context.packageName}/" + Mysong.raw,
                dateAdded = 10000
            )
            songList.add(song)
        }

        return songList
    }


}