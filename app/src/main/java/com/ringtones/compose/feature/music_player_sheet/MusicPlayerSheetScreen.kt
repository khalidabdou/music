package com.ringtones.compose.feature.music_player_sheet

import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ringtones.compose.R
import com.ringtones.compose.data.PlaybackMode
import com.ringtones.compose.data.SkipForwardBackward
import com.ringtones.compose.feature.admob.AdvertView
import com.ringtones.compose.feature.admob.loadInterstitial
import com.ringtones.compose.feature.admob.showInterstitial
import com.ringtones.compose.feature.musicompose.LocalMusicomposeState
import com.ringtones.compose.feature.musicompose.MusicomposeState
import com.ringtones.compose.foundation.common.BottomSheetLayoutConfig
import com.ringtones.compose.foundation.common.LocalSongController
import com.ringtones.compose.foundation.extension.toast
import com.ringtones.compose.foundation.theme.Inter
import com.ringtones.compose.utils.AppUtil.IS_RINGTONE
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerSheetScreen(
    navController: NavController,
    bottomSheetLayoutConfig: BottomSheetLayoutConfig
) {

    val context = LocalContext.current
    val musicomposeState = LocalMusicomposeState.current

    val viewModel = hiltViewModel<MusicPlayerSheetViewModel>()

    val state by viewModel.state.collectAsState()
    loadInterstitial(context)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val songController = LocalSongController.current

    val moreOptionSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    BackHandler {
        when {
            scaffoldState.bottomSheetState.isExpanded -> scope.launch {
                scaffoldState.bottomSheetState.collapse()
            }

            moreOptionSheetState.isVisible -> scope.launch {
                moreOptionSheetState.hide()
            }
            else -> navController.popBackStack()
        }
    }

    Scaffold(
        bottomBar = {
            Column {
                AdvertView()
                Spacer(modifier = Modifier.height(30.dp))
            }

        }
    ) { innerPadding ->
//        LazyColumn(
//            contentPadding = innerPadding,
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            item {
//                MotionContent(
//                    musicomposeState = musicomposeState,
//                    background = bottomSheetLayoutConfig.sheetBackgroundColor,
//                )
//            }
//        }
        Column(
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(bottomSheetLayoutConfig.sheetBackgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(top = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                AlbumImage(
                    albumPath = musicomposeState.currentSongPlayed.albumPath,
                    modifier = Modifier
                        .size(150.dp)
                        .aspectRatio(1f, true)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()

            ) {


                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = musicomposeState.currentSongPlayed.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize

                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                )

                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = musicomposeState.currentSongPlayed.artist,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Inter,
                        fontSize =
                        MaterialTheme.typography.titleMedium.fontSize
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1.0f)

            ) {

                SongProgress(
                    maxDuration = musicomposeState.currentSongPlayed.duration,
                    currentDuration = musicomposeState.currentDuration,
                    onChange = { progress ->
                        val duration =
                            progress * musicomposeState.currentSongPlayed.duration
                        songController?.snapTo(duration.toLong())
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                SongControlButtons(
                    isPlaying = musicomposeState.isPlaying,
                    onPrevious = {
                        showInterstitial(context)
                        songController?.previous()
                    },
                    onPlayPause = {
                        showInterstitial(context)
                        if (musicomposeState.isPlaying) songController?.pause()
                        else songController?.resume()
                    },
                    onNext = {
                        showInterstitial(context)
                        songController?.next()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OtherButtons(
                    musicomposeState = musicomposeState,
                    onPlaybackModeClicked = {
                        showInterstitial(context)
                        songController?.changePlaybackMode()
                    },
                    onFavoriteClicked = {
                        showInterstitial(context)
                        songController?.updateSong(
                            musicomposeState.currentSongPlayed.copy(
                                isFavorite = !musicomposeState.currentSongPlayed.isFavorite
                            )
                        )
                    },
                    onBackwardClicked = {

                        showInterstitial(context)
                        songController?.backward()
                    },
                    onForwardClicked = {
                        showInterstitial(context)
                        songController?.forward()
                    },
                    onShuffleClicked = {
                        showInterstitial(context)
                        songController?.setShuffled(!musicomposeState.isShuffled)
                    },
                    onSet = {
                        //Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (Settings.System.canWrite(context)) {
                                //val k = File(musicomposeState.currentSongPlayed.path)

                                RingtoneManager.setActualDefaultRingtoneUri(
                                    context,
                                    it,
                                    musicomposeState.currentSongPlayed.path.toUri()
                                )

                                context.getString(R.string.success_alarm).toast(
                                    context = context,
                                    length = Toast.LENGTH_LONG
                                )
                            } else {
                                openAndroidPermissionsMenu(context)
                            }
                        } else {
                            RingtoneManager.setActualDefaultRingtoneUri(
                                context,
                                RingtoneManager.TYPE_ALARM,
                                musicomposeState.currentSongPlayed.path.toUri()
                            )

                        }
                    },

                    context = context
                )
            }
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumImage(
    albumPath: String,
    modifier: Modifier = Modifier
) {

    Card(
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = modifier
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(albumPath.toUri())
                    .error(R.drawable.ic_music_unknown)
                    .placeholder(R.drawable.ic_music_unknown)
                    .build()
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun SongProgress(
    maxDuration: Long,
    currentDuration: Long,
    onChange: (Float) -> Unit
) {

    val progress = remember(maxDuration, currentDuration) {
        currentDuration.toFloat() / maxDuration.toFloat()
    }

    val maxDurationInMinute = remember(maxDuration) {
        maxDuration.milliseconds.inWholeMinutes
    }

    val maxDurationInSecond = remember(maxDuration) {
        maxDuration.milliseconds.inWholeSeconds % 60
    }

    val currentDurationInMinute = remember(currentDuration) {
        currentDuration.milliseconds.inWholeMinutes
    }

    val currentDurationInSecond = remember(currentDuration) {
        currentDuration.milliseconds.inWholeSeconds % 60
    }

    val maxDurationString = remember(maxDurationInMinute, maxDurationInSecond) {
        val minute = if (maxDurationInMinute < 10) "0$maxDurationInMinute"
        else maxDurationInMinute.toString()

        val second = if (maxDurationInSecond < 10) "0$maxDurationInSecond"
        else maxDurationInSecond.toString()

        return@remember "$minute:$second"
    }

    val currentDurationString =
        remember(currentDurationInMinute, currentDurationInSecond) {
            val minute = if (currentDurationInMinute < 10) "0$currentDurationInMinute"
            else currentDurationInMinute.toString()

            val second = if (currentDurationInSecond < 10) "0$currentDurationInSecond"
            else currentDurationInSecond.toString()

            return@remember "$minute:$second"
        }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
    ) {
        Slider(
            value = progress,
            onValueChange = onChange,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = currentDurationString,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = Inter
                )
            )

            Text(
                text = maxDurationString,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = Inter
                )
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongControlButtons(
    isPlaying: Boolean,
    onPrevious: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth(0.8f)
    ) {
        IconButton(
            onClick = onPrevious
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_previous_filled_rounded),
                contentDescription = null
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = MaterialTheme.shapes.large,
            onClick = onPlayPause,
            modifier = Modifier
                .size(64.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(
                        id = if (!isPlaying) R.drawable.ic_play_filled_rounded else R.drawable.ic_pause_filled_rounded
                    ),
                    contentDescription = null
                )
            }
        }

        IconButton(
            onClick = onNext
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_next_filled_rounded),
                contentDescription = null
            )
        }
    }
}

@Composable
fun OtherButtons(
    musicomposeState: MusicomposeState,

    onPlaybackModeClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
    onBackwardClicked: () -> Unit,
    onForwardClicked: () -> Unit,
    onShuffleClicked: () -> Unit,
    onSet: (Int) -> Unit,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBackwardClicked
            ) {
                Icon(
                    painter = painterResource(
                        id = when (musicomposeState.skipForwardBackward) {
                            SkipForwardBackward.FIVE_SECOND -> R.drawable.ic_backward_5_sec
                            SkipForwardBackward.TEN_SECOND -> R.drawable.ic_backward_10_sec
                            SkipForwardBackward.FIFTEEN_SECOND -> R.drawable.ic_backward_15_sec
                        }
                    ),
                    contentDescription = null
                )
            }

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = IconButtonDefaults.iconButtonColors().contentColor(
                        enabled = musicomposeState.playbackMode != PlaybackMode.REPEAT_OFF
                    ).value
                ),
                onClick = onPlaybackModeClicked
            ) {
                Icon(
                    painter = painterResource(
                        id = when (musicomposeState.playbackMode) {
                            PlaybackMode.REPEAT_ONE -> R.drawable.ic_repeate_one
                            PlaybackMode.REPEAT_ALL -> R.drawable.ic_repeate_on
                            PlaybackMode.REPEAT_OFF -> R.drawable.ic_repeate_on
                        }
                    ),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = onFavoriteClicked
            ) {
                Image(
                    painter = painterResource(
                        id = if (musicomposeState.currentSongPlayed.isFavorite) R.drawable.ic_favorite_selected
                        else R.drawable.ic_favorite_unselected
                    ),
                    contentDescription = null
                )
            }

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = IconButtonDefaults.iconButtonColors().contentColor(
                        enabled = musicomposeState.isShuffled
                    ).value
                ),
                onClick = onShuffleClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_shuffle),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = onForwardClicked
            ) {
                Icon(
                    painter = painterResource(
                        id = when (musicomposeState.skipForwardBackward) {
                            SkipForwardBackward.FIVE_SECOND -> R.drawable.ic_forward_5_sec
                            SkipForwardBackward.TEN_SECOND -> R.drawable.ic_forward_10_sec
                            SkipForwardBackward.FIFTEEN_SECOND -> R.drawable.ic_forward_15_sec
                        }
                    ),
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        if (IS_RINGTONE)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 4.dp)

            ) {
                BoxSetAs(R.drawable.ring, title = stringResource(R.string.Ringtone)) {
                    onSet(RingtoneManager.TYPE_RINGTONE)
                    showInterstitial(context)
                }
                BoxSetAs(R.drawable.alarm, title = stringResource(R.string.Alarm)) {
                    onSet(RingtoneManager.TYPE_ALARM)
                    showInterstitial(context)
                }
                BoxSetAs(R.drawable.notifications, title = stringResource(R.string.Notifications)) {
                    onSet(RingtoneManager.TYPE_NOTIFICATION)
                    showInterstitial(context)
                }
            }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxSetAs(
    icon: Int,
    title: String,
    onlClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.width(90.dp).padding(2.dp)
            .clickable {
                onlClick()
            }
    ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(
                        id = icon
                    ),
                    contentDescription = null
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = Inter
                    )
                )
        }
    }
}


@OptIn(
    ExperimentalMotionApi::class,
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
)
/*
@Composable
private fun MotionContent(

    background: Color,
    musicomposeState: MusicomposeState,
    modifier: Modifier = Modifier,

    ) {
    val context = LocalContext.current
    val songController = LocalSongController.current

    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.motion_scene)
            .readBytes()
            .decodeToString()
    }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.background(Color.White)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .background(Color.Red)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                AlbumImage(
                    albumPath = musicomposeState.currentSongPlayed.albumPath,
                    modifier = Modifier
                        .size(150.dp)
                        .aspectRatio(1f, true)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()

            ) {


                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = musicomposeState.currentSongPlayed.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize

                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                )

                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = musicomposeState.currentSongPlayed.artist,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Inter,
                        fontSize =
                        MaterialTheme.typography.titleMedium.fontSize
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier

            ) {
                Spacer(modifier = Modifier.height(24.dp))
                SongProgress(
                    maxDuration = musicomposeState.currentSongPlayed.duration,
                    currentDuration = musicomposeState.currentDuration,
                    onChange = { progress ->
                        val duration =
                            progress * musicomposeState.currentSongPlayed.duration
                        songController?.snapTo(duration.toLong())
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                SongControlButtons(
                    isPlaying = musicomposeState.isPlaying,
                    onPrevious = {
                        showInterstitial(context)
                        songController?.previous()
                    },
                    onPlayPause = {
                        showInterstitial(context)
                        if (musicomposeState.isPlaying) songController?.pause()
                        else songController?.resume()
                    },
                    onNext = {
                        showInterstitial(context)
                        songController?.next()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OtherButtons(
                    musicomposeState = musicomposeState,
                    onPlaybackModeClicked = {
                        showInterstitial(context)
                        songController?.changePlaybackMode()
                    },
                    onFavoriteClicked = {
                        showInterstitial(context)
                        songController?.updateSong(
                            musicomposeState.currentSongPlayed.copy(
                                isFavorite = !musicomposeState.currentSongPlayed.isFavorite
                            )
                        )
                    },
                    onBackwardClicked = {

                        showInterstitial(context)
                        songController?.backward()
                    },
                    onForwardClicked = {
                        showInterstitial(context)
                        songController?.forward()
                    },
                    onShuffleClicked = {
                        showInterstitial(context)
                        songController?.setShuffled(!musicomposeState.isShuffled)
                    },
                    onSet = {
                        //Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (Settings.System.canWrite(context)) {
                                //val k = File(musicomposeState.currentSongPlayed.path)

                                RingtoneManager.setActualDefaultRingtoneUri(
                                    context,
                                    it,
                                    musicomposeState.currentSongPlayed.path.toUri()
                                )

                                context.getString(R.string.success_alarm).toast(
                                    context = context,
                                    length = Toast.LENGTH_LONG
                                )
                            } else {
                                openAndroidPermissionsMenu(context)
                            }
                        } else {
                            RingtoneManager.setActualDefaultRingtoneUri(
                                context,
                                RingtoneManager.TYPE_ALARM,
                                musicomposeState.currentSongPlayed.path.toUri()
                            )

                        }
                    },

                    context = context
                )
            }
        }
    }
}
*/


private fun openAndroidPermissionsMenu(context: Context) {
    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
    intent.data = Uri.parse("package:" + context.packageName)
    context.startActivity(intent)
}

