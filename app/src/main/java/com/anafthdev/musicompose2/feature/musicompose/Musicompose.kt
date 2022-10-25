package com.anafthdev.musicompose2.feature.musicompose

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.anafthdev.musicompose2.R
import com.anafthdev.musicompose2.data.datastore.AppDatastore
import com.anafthdev.musicompose2.data.datastore.LocalAppDatastore
import com.anafthdev.musicompose2.foundation.common.LocalSongController
import com.anafthdev.musicompose2.foundation.common.MusicomposeRippleTheme
import com.anafthdev.musicompose2.foundation.common.SongController
import com.anafthdev.musicompose2.foundation.extension.isDark
import com.anafthdev.musicompose2.foundation.extension.isDynamicDark
import com.anafthdev.musicompose2.foundation.extension.isDynamicLight
import com.anafthdev.musicompose2.foundation.extension.toast
import com.anafthdev.musicompose2.foundation.theme.Musicompose2
import com.anafthdev.musicompose2.foundation.theme.black01
import com.anafthdev.musicompose2.foundation.theme.black10
import com.anafthdev.musicompose2.foundation.uicomponent.LocalBottomMusicPlayerAlbumImageAngle
import com.anafthdev.musicompose2.foundation.uicomponent.PermissionDeniedPermanentlyPopup
import com.anafthdev.musicompose2.foundation.uicomponent.StoragePermissionReasonPopup
import com.anafthdev.musicompose2.foundation.uimode.UiModeViewModel
import com.anafthdev.musicompose2.foundation.uimode.data.LocalUiMode
import com.anafthdev.musicompose2.runtime.navigation.MusicomposeNavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun Musicompose(
    appDatastore: AppDatastore,
    songController: SongController,
    viewModel: MusicomposeViewModel,
) {

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val uiModeViewModel = hiltViewModel<UiModeViewModel>()

    val state by viewModel.state.collectAsState()
    val uiModeState by uiModeViewModel.state.collectAsState()

    val useDynamicColor = uiModeState.uiMode.isDynamicDark() or uiModeState.uiMode.isDynamicLight()
    val isSystemInDarkTheme = uiModeState.uiMode.isDark() or uiModeState.uiMode.isDynamicDark()
    val scope = rememberCoroutineScope()
    val bottomMusicPlayerInfiniteTransition = rememberInfiniteTransition()

    val angle by bottomMusicPlayerInfiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = FastOutSlowInEasing
            )
        )
    )

    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    viewModel.dispatch(MusicomposeAction.PlayLastSongPlayed)

                    scope.launch {
                        songController.pause()
                        delay(800)
                        songController.showBottomMusicPlayer()
                    }
                }
                else -> {}
            }
        }

        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }

    CompositionLocalProvider(
        LocalUiMode provides uiModeState.uiMode,
        LocalRippleTheme provides MusicomposeRippleTheme,
        LocalAppDatastore provides appDatastore,
        LocalContentColor provides if (isSystemInDarkTheme) black10 else black01,
        LocalSongController provides songController,
        LocalMusicomposeState provides state,
        LocalOverscrollConfiguration provides null,
        LocalBottomMusicPlayerAlbumImageAngle provides angle
    ) {
        Musicompose2(
            darkTheme = isSystemInDarkTheme,
            dynamicColor = useDynamicColor
        ) {
           /* PermissionRequired(
                permissionState = storagePermissionState,
                permissionNotGrantedContent = {
                    StoragePermissionReasonPopup(
                        onDeny = {
                            context.getString(R.string.permission_denied_by_user).toast(
                                context = context,
                                length = Toast.LENGTH_LONG
                            )

                            (context as Activity).finishAffinity()
                        },
                        onAllow = {
                            storagePermissionState.launchPermissionRequest()
                        }
                    )
                },
                permissionNotAvailableContent = {
                    PermissionDeniedPermanentlyPopup(
                        onClose = {
                            (context as Activity).finishAffinity()
                        },
                        onOpen = {
                            context.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                            )
                        }
                    )
                }
            ) {*/

                //youDesirePermissionCode(context as Activity)
                MusicomposeScreen()
           // }
        }
    }

}


@Composable
private fun MusicomposeScreen() {
    val backgroundColor = MaterialTheme.colorScheme.background

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = backgroundColor.luminance() > 0.5f
        )
    }

    MusicomposeNavHost(
        modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
    )
}
