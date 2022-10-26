package com.ringtones.compose.feature.music_player_sheet

import com.ringtones.compose.data.model.Playlist
import com.ringtones.compose.data.model.Song
import kotlin.time.Duration

sealed interface MusicPlayerSheetAction {
	data class AddToPlaylist(val song: Song, val playlist: Playlist): MusicPlayerSheetAction
	data class SetTimer(val duration: Duration): MusicPlayerSheetAction
}