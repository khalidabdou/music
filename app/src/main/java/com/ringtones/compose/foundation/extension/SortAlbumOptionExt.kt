package com.ringtones.compose.foundation.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ringtones.compose.R
import com.ringtones.compose.data.SortAlbumOption

@Composable
fun SortAlbumOption.optionToString(): String {
	return stringResource(
		id = when (this) {
			SortAlbumOption.ALBUM_NAME -> R.string.album_name
			SortAlbumOption.ARTIST_NAME -> R.string.artist_name
		}
	)
}
