package com.ringtones.compose.foundation.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ringtones.compose.R
import com.ringtones.compose.data.SortPlaylistOption

@Composable
fun SortPlaylistOption.optionToString(): String {
	return stringResource(
		id = when (this) {
			SortPlaylistOption.PLAYLIST_NAME -> R.string.playlist_name
			SortPlaylistOption.NUMBER_OF_SONGS -> R.string.number_of_songs
		}
	)
}
