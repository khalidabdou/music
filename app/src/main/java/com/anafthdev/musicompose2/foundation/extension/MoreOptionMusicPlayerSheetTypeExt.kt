package com.anafthdev.musicompose2.foundation.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.anafthdev.musicompose2.R
import com.anafthdev.musicompose2.feature.more_option_music_player_sheet.data.MoreOptionMusicPlayerSheetType

fun MoreOptionMusicPlayerSheetType.isAlbum() = this == MoreOptionMusicPlayerSheetType.SET_ALARM
fun MoreOptionMusicPlayerSheetType.isArtist() = this == MoreOptionMusicPlayerSheetType.SET_RINGTONE

@Composable
fun MoreOptionMusicPlayerSheetType.getLabel(s: String): String {
	return when (this) {
		MoreOptionMusicPlayerSheetType.SET_ALARM -> stringResource(
			id = R.string.x_album,
			s
		)
		MoreOptionMusicPlayerSheetType.SET_RINGTONE -> stringResource(
			id = R.string.x_album,
			s
		)
	}
}

@Composable
fun MoreOptionMusicPlayerSheetType.getIcon(): Int {
	return when (this) {
		MoreOptionMusicPlayerSheetType.SET_ALARM -> R.drawable.ic_cd
		MoreOptionMusicPlayerSheetType.SET_RINGTONE -> R.drawable.ic_cd
	}
}
