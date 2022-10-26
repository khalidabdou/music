package com.ringtones.compose.feature.musicompose.environment

import com.ringtones.compose.data.PlaybackMode
import com.ringtones.compose.data.SkipForwardBackward
import com.ringtones.compose.data.model.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IMusicomposeEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getSongs(): Flow<List<Song>>
	
	fun isPlaying(): Flow<Boolean>
	
	fun isShuffled(): Flow<Boolean>
	
	fun getCurrentDuration(): Flow<Long>
	
	fun getCurrentSongQueue(): Flow<List<Song>>
	
	fun getCurrentPlayedSong(): Flow<Song>
	
	fun getSkipForwardBackward(): Flow<SkipForwardBackward>
	
	fun getPlaybackMode(): Flow<PlaybackMode>
	
	fun isBottomMusicPlayerShowed(): Flow<Boolean>
	
	fun snapTo(duration: Long, fromUser: Boolean = true)
	
	fun stop()
	
	suspend fun play(song: Song)
	
	suspend fun pause()
	
	suspend fun resume()
	
	suspend fun previous()
	
	suspend fun next()
	
	suspend fun forward()
	
	suspend fun backward()
	
	suspend fun changePlaybackMode()
	
	suspend fun updateSong(song: Song)
	
	suspend fun setShuffle(shuffle: Boolean)
	
	suspend fun playAll(songs: List<Song>)
	
	suspend fun updateQueueSong(songs: List<Song>)
	
	suspend fun checkScannedSong(songList: List<Song>)
	
	suspend fun setShowBottomMusicPlayer(show: Boolean)
	
	suspend fun playLastSongPlayed()
	
}