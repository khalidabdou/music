package com.ringtones.compose.data.model

data class Album(
	val id: String,
	val name: String,
	val artist: String,
	val artistID: String,
	val songs: List<Song>
) {
	companion object {
		val default = Album(
			id = "-1",
			name = "-",
			artist = Artist.default.name,
			artistID = Artist.default.id,
			songs = emptyList()
		)
	}
}
