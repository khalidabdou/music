package com.anafthdev.musicompose2.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.anafthdev.musicompose2.R
import com.anafthdev.musicompose2.data.model.Song
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

object SongUtil {
	
	fun getSong(
		context: Context,
		isTracksSmallerThan100KBSkipped: Boolean = true,
		isTracksShorterThan60SecondsSkipped: Boolean = true
	): List<Song> {
		
		val songList = ArrayList<Song>()
		
		val audioUriExternal = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
		//val audioUriExternal = MediaStore.Audio.Media.getContentUri(ContentResolver.SCHEME_ANDROID_RESOURCE + File.pathSeparator + File.separator + File.separator+ context.getPackageName()+ File.separator+"raw" )
		//val audioUriExternal =
			//Uri.parse("android.resource://" + context.getPackageName().toString() )

		//val audioUriExternal = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + File.pathSeparator + File.separator + context.getPackageName() + "/raw/"+R.raw.alarm );
		Log.d("MUSICURL",audioUriExternal.toString())



		val songProjection = listOf(
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.ARTIST_ID,
			MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.DURATION,
			MediaStore.Audio.Media.ALBUM_ID,
			MediaStore.Audio.Media.DATE_ADDED,
			MediaStore.Audio.Media.SIZE
		)
		
		val cursorIndexSongID: Int
		val cursorIndexSongDisplayName: Int
		val cursorIndexSongTitle: Int
		val cursorIndexSongArtist: Int
		val cursorIndexSongArtistID: Int
		val cursorIndexSongAlbum: Int
		val cursorIndexSongDuration: Int
		val cursorIndexSongAlbumID: Int
		val cursorIndexSongDateAdded: Int
		val cursorIndexSongSize: Int
		
		val songCursor = context.contentResolver.query(
			audioUriExternal,
			songProjection.toTypedArray(),
			null,
			null,
			null
		)
		Log.d("MUSICURL",songCursor.toString())


		
		if (songCursor != null) {

			cursorIndexSongID = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
			cursorIndexSongDisplayName = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
			cursorIndexSongTitle = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
			cursorIndexSongArtist = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
			cursorIndexSongArtistID = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)
			cursorIndexSongAlbum = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
			cursorIndexSongDuration = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
			cursorIndexSongAlbumID = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
			cursorIndexSongDateAdded = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
			cursorIndexSongSize = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
			
			while (songCursor.moveToNext()) {
				val audioID = songCursor.getLong(cursorIndexSongID)
				val displayName = songCursor.getString(cursorIndexSongDisplayName)
				val title = songCursor.getString(cursorIndexSongTitle)
				val artist = songCursor.getString(cursorIndexSongArtist)
				val artistID = songCursor.getString(cursorIndexSongArtistID)
				val album = songCursor.getString(cursorIndexSongAlbum)
				val duration = songCursor.getLong(cursorIndexSongDuration)
				val albumId = songCursor.getString(cursorIndexSongAlbumID)
				val dateAdded = songCursor.getLong(cursorIndexSongDateAdded)
				val size = songCursor.getInt(cursorIndexSongSize)
				
				val albumPath = Uri.withAppendedPath(Uri.parse("content://media/external/audio/albumart"), albumId)
				val path = Uri.withAppendedPath(audioUriExternal, "" + audioID)
				
				val durationGreaterThan60Sec = duration.milliseconds.inWholeSeconds > 60
				val sizeGreaterThan100KB = (size / 1024) > 100
				
				val song = Song(
					audioID = audioID,
					displayName = displayName,
					title = title,
					artist = if (artist.equals("<unknown>", true)) context.getString(R.string.unknown) else artist,
					artistID = artistID,
					album = album,
					albumID = albumId,
					duration = duration,
					albumPath = albumPath.toString(),
					path = path.toString(),
					dateAdded = dateAdded
				)
				Log.d("MUSICURL",song.path.toString())
				
				when {
					isTracksSmallerThan100KBSkipped and isTracksShorterThan60SecondsSkipped -> {
						if (sizeGreaterThan100KB and durationGreaterThan60Sec) songList.add(song)
					}
					!isTracksSmallerThan100KBSkipped and isTracksShorterThan60SecondsSkipped -> {
						if (durationGreaterThan60Sec) songList.add(song)
					}
					isTracksSmallerThan100KBSkipped and !isTracksShorterThan60SecondsSkipped -> {
						if (sizeGreaterThan100KB) songList.add(song)
					}
					!isTracksSmallerThan100KBSkipped and !isTracksShorterThan60SecondsSkipped -> {
						songList.add(song)
					}
				}
			}
			
			songCursor.close()
		}
		
		return songList
	}

	
}