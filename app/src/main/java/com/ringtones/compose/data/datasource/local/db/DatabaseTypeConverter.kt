package com.ringtones.compose.data.datasource.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ringtones.compose.data.model.Song

object DatabaseTypeConverter {

	@TypeConverter
	fun songsToJson(songs: List<Song>) = Gson().toJson(songs)!!
	
	@TypeConverter
	fun songsFromJson(json: String): List<Song> {
		return (Gson().fromJson(json, Array<Song>::class.java) ?: emptyArray()).toList()
	}
	
	@TypeConverter
	fun longListToJson(songIDs: List<Long>) = Gson().toJson(songIDs)!!
	
	@TypeConverter
	fun longListFromJson(json: String): List<Long> {
		return (Gson().fromJson(json, Array<Long>::class.java) ?: emptyArray()).toList()
	}
	
}