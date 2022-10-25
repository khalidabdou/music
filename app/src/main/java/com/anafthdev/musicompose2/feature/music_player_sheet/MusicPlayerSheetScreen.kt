package com.anafthdev.musicompose2.feature.music_player_sheet

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.anafthdev.musicompose2.R
import com.anafthdev.musicompose2.data.PlaybackMode
import com.anafthdev.musicompose2.data.SkipForwardBackward
import com.anafthdev.musicompose2.feature.admob.loadInterstitial
import com.anafthdev.musicompose2.feature.admob.showInterstitial
import com.anafthdev.musicompose2.feature.more_option_music_player_sheet.data.MoreOptionMusicPlayerSheetType
import com.anafthdev.musicompose2.feature.musicompose.LocalMusicomposeState
import com.anafthdev.musicompose2.feature.musicompose.MusicomposeState
import com.anafthdev.musicompose2.foundation.common.BottomSheetLayoutConfig
import com.anafthdev.musicompose2.foundation.common.LocalSongController
import com.anafthdev.musicompose2.foundation.extension.toast
import com.anafthdev.musicompose2.foundation.theme.Inter
import com.anafthdev.musicompose2.foundation.uicomponent.PermissionDeniedPermanentlyPopup
import com.anafthdev.musicompose2.foundation.uicomponent.StoragePermissionReasonPopup
import com.anafthdev.musicompose2.foundation.uiextension.currentFraction
import com.anafthdev.musicompose2.utils.AppUtil
import com.anafthdev.musicompose2.utils.Download
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@OptIn(ExperimentalMaterialApi::class)
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

    val moreOptionSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    var moreOptionType by remember { mutableStateOf(MoreOptionMusicPlayerSheetType.SET_ALARM) }

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

    MotionContent(
        musicomposeState = musicomposeState,
        fraction = scaffoldState.currentFraction,
        background = bottomSheetLayoutConfig.sheetBackgroundColor,
        /*  onMoreClicked = {
              scope.launch {
                  moreOptionSheetState.show()
              }
          }*/
    )
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

    val currentDurationString = remember(currentDurationInMinute, currentDurationInSecond) {
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
    modifier: Modifier = Modifier,
    onPlaybackModeClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
    onBackwardClicked: () -> Unit,
    onForwardClicked: () -> Unit,
    onShuffleClicked: () -> Unit,
    onSetAlarm: () -> Unit,
    onSetRingtone: () -> Unit,
    onSetNotifications: () -> Unit,
    onDownload: () -> Unit,
    context: Context
) {
    Column(
        Modifier.fillMaxHeight(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
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

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)

        ) {
            BoxSetAs(R.drawable.ring, title = "Ringtone") {
                onSetRingtone()
                showInterstitial(context)
            }
            BoxSetAs(R.drawable.alarm, title = "Alarm") {
                onSetAlarm()
                showInterstitial(context)
            }
            BoxSetAs(R.drawable.notifications, title = "Notifications") {
                onSetNotifications()
                showInterstitial(context)
            }
            BoxSetAs(R.drawable.download, title = "Download") {
                onDownload()
                showInterstitial(context)
            }
        }
        //AdvertView()
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f, true)

        ) {
            Text(text = "kdsjf")
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
        modifier = Modifier
            .size(width = 80.dp, height = 50.dp)
            .clickable {
                onlClick()
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
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
}


@OptIn(
    ExperimentalMotionApi::class,
    ExperimentalPermissionsApi::class,
   )
@Composable
private fun MotionContent(
    fraction: Float,
    background: Color,
    musicomposeState: MusicomposeState,
    modifier: Modifier = Modifier,

    ) {

    val context = LocalContext.current
    val songController = LocalSongController.current
    val storagePermissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.motion_scene)
            .readBytes()
            .decodeToString()
    }

    Row(
        modifier = modifier
            .background(background)
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        MotionLayout(
            motionScene = MotionScene(content = motionScene),
            progress = fraction,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Spacer(modifier = Modifier.layoutId("top_bar"))


            AlbumImage(
                albumPath = musicomposeState.currentSongPlayed.albumPath,
                modifier = Modifier
                    .layoutId("album_image")
                    .aspectRatio(1f, true)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .layoutId("column_title_artist")
            ) {
                AnimatedVisibility(visible = fraction < 0.8f) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = musicomposeState.currentSongPlayed.title,
                    textAlign = if (fraction > 0.8f) TextAlign.Start else TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (fraction > 0.8f) MaterialTheme.typography.titleMedium.fontSize
                        else MaterialTheme.typography.titleLarge.fontSize
                    ),
                    modifier = Modifier
                        .fillMaxWidth(if (fraction > 0.8f) 1f else 0.7f)
                )

                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = musicomposeState.currentSongPlayed.artist,
                    textAlign = if (fraction > 0.8f) TextAlign.Start else TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = Inter,
                        fontSize = if (fraction > 0.8f) MaterialTheme.typography.titleSmall.fontSize
                        else MaterialTheme.typography.titleMedium.fontSize
                    ),
                    modifier = Modifier
                        .fillMaxWidth(if (fraction > 0.8) 1f else 0.7f)
                )
            }

            Row(
                modifier = Modifier
                    .layoutId("row_buttons")
            ) {
                IconButton(
                    onClick = {
                        if (musicomposeState.isPlaying) songController?.pause()
                        else songController?.resume()
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (!musicomposeState.isPlaying) R.drawable.ic_play_filled_rounded else R.drawable.ic_pause_filled_rounded
                        ),
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = {
                        songController?.next()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_next_filled_rounded),
                        contentDescription = null
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .layoutId("column_other")
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                SongProgress(
                    maxDuration = musicomposeState.currentSongPlayed.duration,
                    currentDuration = musicomposeState.currentDuration,
                    onChange = { progress ->
                        val duration = progress * musicomposeState.currentSongPlayed.duration

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

                if (AppUtil.ENABLE_BUTTONS)
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
                        onSetAlarm = {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (Settings.System.canWrite(context)) {
                                    RingtoneManager.setActualDefaultRingtoneUri(
                                        context,
                                        RingtoneManager.TYPE_ALARM,
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
                        onSetRingtone = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (Settings.System.canWrite(context)) {
                                    RingtoneManager.setActualDefaultRingtoneUri(
                                        context,
                                        RingtoneManager.TYPE_ALARM,
                                        musicomposeState.currentSongPlayed.path.toUri()
                                    )

                                    context.getString(R.string.success_ringtone).toast(
                                        context = context,
                                        length = Toast.LENGTH_LONG
                                    )
                                } else {
                                    openAndroidPermissionsMenu(context)
                                }
                            } else {
                                RingtoneManager.setActualDefaultRingtoneUri(
                                    context,
                                    RingtoneManager.TYPE_RINGTONE,
                                    musicomposeState.currentSongPlayed.path.toUri()
                                )

                            }
                        },
                        onSetNotifications = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (Settings.System.canWrite(context)) {
                                    RingtoneManager.setActualDefaultRingtoneUri(
                                        context,
                                        RingtoneManager.TYPE_ALARM,
                                        musicomposeState.currentSongPlayed.path.toUri()
                                    )

                                    context.getString(R.string.success_ringtone).toast(
                                        context = context,
                                        length = Toast.LENGTH_LONG
                                    )
                                } else {
                                    openAndroidPermissionsMenu(context)
                                }
                            } else {
                                RingtoneManager.setActualDefaultRingtoneUri(
                                    context,
                                    RingtoneManager.TYPE_NOTIFICATION,
                                    musicomposeState.currentSongPlayed.path.toUri()
                                )

                            }
                        },
                        onDownload ={
                                    
                        },

                        modifier = Modifier
                            .fillMaxWidth(),
                        context = context
                    )


            }
        }
    }
}


private fun openAndroidPermissionsMenu(context: Context) {
    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
    intent.data = Uri.parse("package:" + context.getPackageName())
    context.startActivity(intent)
}






