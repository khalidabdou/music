package com.anafthdev.musicompose2.feature.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.musicompose2.R
import com.anafthdev.musicompose2.data.MusicomposeDestination
import com.anafthdev.musicompose2.data.SortType
import com.anafthdev.musicompose2.feature.admob.AdvertView
import com.anafthdev.musicompose2.feature.home.HomeScreen
import com.anafthdev.musicompose2.feature.musicompose.LocalMusicomposeState
import com.anafthdev.musicompose2.foundation.common.LocalSongController
import com.anafthdev.musicompose2.foundation.uicomponent.BottomMusicPlayerImpl
import com.anafthdev.musicompose2.foundation.uicomponent.MoreOptionPopup
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    navController: NavController
) {

    val tabPages = listOf(
        stringResource(id = R.string.song),
        stringResource(id = R.string.album),
        stringResource(id = R.string.artist),
        stringResource(id = R.string.playlist)
    )

    val songController = LocalSongController.current
    val musicomposeState = LocalMusicomposeState.current

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    var isMoreOptionPopupShowed by remember { mutableStateOf(false) }

    val scrollToPage: (Int) -> Unit = { page ->
        scope.launch { pagerState.animateScrollToPage(page) }
        Unit
    }

    BackHandler {
        when {
            pagerState.currentPage != 0 -> scrollToPage(0)
            else -> navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Column {
            SmallTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(MusicomposeDestination.Setting.route)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_setting),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isMoreOptionPopupShowed = !isMoreOptionPopupShowed
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = null
                        )
                    }

                    if (isMoreOptionPopupShowed) {
                        MoreOptionPopup(
                            options = listOf(
                                stringResource(id = R.string.rate),
                                stringResource(id = R.string.privacy),
                                stringResource(id = R.string.sort_by)
                            ),
                            onDismissRequest = {
                                isMoreOptionPopupShowed = false
                            },
                            onClick = { i ->
                                when (i) {
                                    0 -> {
                                        scope.launch {
                                            songController?.hideBottomMusicPlayer()
                                            delay(800)
                                            navController.navigate(
                                                MusicomposeDestination.BottomSheet.Sort.createRoute(
                                                    type = when (pagerState.currentPage) {
                                                        0 -> SortType.SONG
                                                        1 -> SortType.ALBUM
                                                        2 -> SortType.ARTIST
                                                        3 -> SortType.PLAYLIST
                                                        else -> SortType.SONG
                                                    }
                                                )
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            )

            HomeScreen(navController)


        }

        BottomMusicPlayerImpl(navController = navController)
    }

}
