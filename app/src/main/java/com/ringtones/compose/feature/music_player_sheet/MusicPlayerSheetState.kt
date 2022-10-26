package com.ringtones.compose.feature.music_player_sheet

import com.ringtones.compose.data.model.Playlist

data class MusicPlayerSheetState(
	val playlists: List<Playlist> = emptyList(),
	val isTimerActive: Boolean = false
)
