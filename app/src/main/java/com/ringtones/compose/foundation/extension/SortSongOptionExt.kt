package com.ringtones.compose.foundation.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ringtones.compose.R
import com.ringtones.compose.data.SortSongOption

@Composable
fun SortSongOption.optionToString(): String {
	return stringResource(
		id = when (this) {
			SortSongOption.SONG_NAME -> R.string.song_name
			SortSongOption.DATE_ADDED -> R.string.date_added
			SortSongOption.ARTIST_NAME -> R.string.artist_name
		}
	)
}
