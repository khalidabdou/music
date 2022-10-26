package com.ringtones.compose.feature.main

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ringtones.compose.R
import com.ringtones.compose.data.MusicomposeDestination
import com.ringtones.compose.feature.home.HomeScreen
import com.ringtones.compose.foundation.common.LocalSongController
import com.ringtones.compose.foundation.uicomponent.BottomMusicPlayerImpl
import com.ringtones.compose.foundation.uicomponent.MoreOptionPopup
import com.ringtones.compose.utils.AppUtil
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    navController: NavController
) {

    val songController = LocalSongController.current
    val context = LocalContext.current

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

                            ),
                            onDismissRequest = {
                                isMoreOptionPopupShowed = false
                            },
                            onClick = { i ->
                                when (i) {
                                    0 -> {
                                        AppUtil.openStore(context)
                                    }
                                    1->{
                                        AppUtil.privacy(context)
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
