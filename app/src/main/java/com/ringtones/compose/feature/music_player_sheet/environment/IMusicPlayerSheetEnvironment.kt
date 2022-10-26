package com.ringtones.compose.feature.music_player_sheet.environment

import com.ringtones.compose.data.model.Playlist
import com.ringtones.compose.data.model.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface IMusicPlayerSheetEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getPlaylists(): Flow<List<Playlist>>
	
	fun isTimerActive(): Flow<Boolean>
	
	suspend fun addToPlaylist(song: Song, playlist: Playlist)
	
	suspend fun setTimer(duration: Duration)
	
}