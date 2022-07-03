package com.anafthdev.musicompose2.feature.artist_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.anafthdev.musicompose2.data.model.Artist
import com.anafthdev.musicompose2.foundation.uicomponent.ArtistItem

@Composable
fun ArtistListScreen() {
	
	val viewModel = hiltViewModel<ArtistListViewModel>()
	
	val state by viewModel.state.collectAsState()
	
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		
		items(
			items = state.artists,
			key = { artist: Artist -> artist.id }
		) { artist ->
			ArtistItem(
				artist = artist,
				onClick = {
				
				}
			)
		}
	}
	
}