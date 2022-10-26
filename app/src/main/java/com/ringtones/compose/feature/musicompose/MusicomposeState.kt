package com.ringtones.compose.feature.musicompose

import android.os.Parcelable
import androidx.compose.runtime.compositionLocalOf
import com.ringtones.compose.data.PlaybackMode
import com.ringtones.compose.data.SkipForwardBackward
import com.ringtones.compose.data.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicomposeState(
	val songs: List<Song> =  emptyList(),
	val currentSongQueue: List<Song> = emptyList(),
	val currentSongPlayed: Song = Song.default,
	val currentDuration: Long = 0,
	val isPlaying: Boolean = false,
	val isShuffled: Boolean = false,
	val isBottomMusicPlayerShowed: Boolean = false,
	val playbackMode: PlaybackMode = PlaybackMode.REPEAT_OFF,
	val skipForwardBackward: SkipForwardBackward = SkipForwardBackward.FIVE_SECOND
): Parcelable

val LocalMusicomposeState = compositionLocalOf { MusicomposeState() }
