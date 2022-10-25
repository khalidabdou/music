package com.anafthdev.musicompose2.feature.home

import android.content.ContentValues
import android.media.RingtoneManager
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.musicompose2.data.MusicomposeDestination
import com.anafthdev.musicompose2.data.model.Song
import com.anafthdev.musicompose2.feature.admob.AdvertView
import com.anafthdev.musicompose2.feature.admob.loadInterstitial
import com.anafthdev.musicompose2.feature.admob.showInterstitial
import com.anafthdev.musicompose2.feature.musicompose.LocalMusicomposeState
import com.anafthdev.musicompose2.foundation.common.LocalSongController
import com.anafthdev.musicompose2.foundation.extension.isNotDefault
import com.anafthdev.musicompose2.foundation.extension.isPlaying
import com.anafthdev.musicompose2.foundation.extension.isSelected
import com.anafthdev.musicompose2.foundation.uicomponent.BottomMusicPlayerDefault
import com.anafthdev.musicompose2.foundation.uicomponent.SongItem


@Composable
fun HomeScreen(navController: NavController) {
	
	val songController = LocalSongController.current
	val musicomposeState = LocalMusicomposeState.current
	val context= LocalContext.current
	
	val viewModel = hiltViewModel<HomeViewModel>()
	//loadInterstitial(context)
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		
		/*item {
			PlayAll(
				songs = musicomposeState.songs,
				modifier = Modifier
					.padding(horizontal = 16.dp)
			)
		}*/

		items(
			items = musicomposeState.songs,
			key = { song: Song -> song.audioID }
		) { song ->
			SongItem(
				song = song,
				selected = song.isSelected(),
				isMusicPlaying = song.isPlaying(),
				onClick = {
				songController?.play(song)
				showInterstitial(context)
					if (musicomposeState.currentSongPlayed.isNotDefault()) {
						navController.navigate(
							MusicomposeDestination.BottomSheet.MusicPlayer.route
						)
					}
				},
				onFavoriteClicked = { isFavorite ->
					songController?.updateSong(
						song.copy(
							isFavorite = isFavorite
						)
					)
				}
			)
		}

		
		// BottomMusicPlayer padding
		item {
			Spacer(modifier = Modifier.height(BottomMusicPlayerDefault.Height))
		}
	}
}
