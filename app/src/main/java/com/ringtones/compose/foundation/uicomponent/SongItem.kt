package com.ringtones.compose.foundation.uicomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ringtones.compose.R
import com.ringtones.compose.data.model.Song
import com.ringtones.compose.foundation.common.MusicomposeRippleTheme
import com.ringtones.compose.foundation.theme.Inter
import com.ringtones.compose.foundation.theme.black01
import com.ringtones.compose.foundation.theme.black10

@Preview(showBackground = true)
@Composable
fun SongItemPreview() {
	CompositionLocalProvider(
		LocalRippleTheme provides MusicomposeRippleTheme,
		LocalContentColor provides if (isSystemInDarkTheme()) black10 else black01,
	) {
		SongItem(song = Song.default, selected = true, isMusicPlaying = false, onClick = {}, onFavoriteClicked = {})
	}
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SongItem(
	song: Song,
	selected: Boolean,
	isMusicPlaying: Boolean,
	modifier: Modifier = Modifier,
	showAlbumImage: Boolean = true,
	showFavorite: Boolean = true,
	onFavoriteClicked: (Boolean) -> Unit = {},
	onClick: () -> Unit,
) {
	Card(
		onClick = onClick,
		colors = CardDefaults.cardColors(
			containerColor = Color.Transparent
		),
		modifier = modifier
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(72.dp)
		) {
			
			if (showAlbumImage) {
				Image(
					painter = rememberAsyncImagePainter(
						ImageRequest.Builder(LocalContext.current)
							.data(song.albumPath.toUri())
							.error(R.drawable.ic_music_unknown)
							.placeholder(R.drawable.ic_music_unknown)
							.build()
					),
					contentDescription = null,
					modifier = Modifier
                        .padding(8.dp)
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
				)
			}
			
			Column(
				verticalArrangement = Arrangement.SpaceEvenly,
				modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
			) {
				Text(
					text = song.title,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyMedium.copy(
						color = if (selected) MaterialTheme.colorScheme.primary
						else LocalContentColor.current,
						fontWeight = FontWeight.Bold
					)
				)
				
				Text(
					text = "${song.artist} • ${song.album}",
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodySmall.copy(
						color = if (selected) MaterialTheme.colorScheme.primary
						else LocalContentColor.current.copy(alpha = 0.7f),
						fontFamily = Inter
					)
				)
			}
			
			AnimatedVisibility(
				visible = selected,
				enter = scaleIn(),
				exit = scaleOut()
			) {
				AudioWave(isMusicPlaying = isMusicPlaying)
			}
			
			if (showFavorite) {
				IconButton(
					onClick = {
						onFavoriteClicked(!song.isFavorite)
					}
				) {
					Image(
						painter = painterResource(
							id = if (song.isFavorite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite_unselected
						),
						contentDescription = null
					)
				}
			}
		}
	}
}
