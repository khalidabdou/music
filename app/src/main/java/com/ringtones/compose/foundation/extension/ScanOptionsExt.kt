package com.ringtones.compose.foundation.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ringtones.compose.R
import com.ringtones.compose.data.ScanOptions

@Composable
fun ScanOptions.getLabel(): String {
	return when (this) {
		ScanOptions.SKIP_TRACKS_SMALLER_THAN_100_KB -> stringResource(id = R.string.skip_tracks_smaller_than_100_kb)
		ScanOptions.SKIP_TRACKS_SHORTER_THAN_60_SECONDS -> stringResource(id = R.string.skip_tracks_shorter_than_60_seconds)
	}
}
