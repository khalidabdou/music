package com.ringtones.compose.feature.theme

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ringtones.compose.R
import com.ringtones.compose.foundation.theme.DarkColorScheme
import com.ringtones.compose.foundation.theme.LightColorScheme
import com.ringtones.compose.foundation.uicomponent.ThemeItem
import com.ringtones.compose.foundation.uimode.UiModeAction
import com.ringtones.compose.foundation.uimode.UiModeViewModel
import com.ringtones.compose.foundation.uimode.data.UiMode

@Composable
fun ThemeScreen(
	navController: NavController
) {
	
	val uiModeViewModel = hiltViewModel<UiModeViewModel>()
	
	val uiModeState by uiModeViewModel.state.collectAsState()
	
	val uiModes = remember {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) UiMode.values()
		else arrayOf(
			UiMode.DARK,
			UiMode.LIGHT
		)
	}
	
	Column(
		modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
	) {
		SmallTopAppBar(
			colors = TopAppBarDefaults.smallTopAppBarColors(
				containerColor = MaterialTheme.colorScheme.background
			),
			title = {
				Text(
					text = stringResource(id = R.string.theme),
					style = MaterialTheme.typography.titleLarge,
				)
			},
			navigationIcon = {
				IconButton(
					onClick = {
						navController.popBackStack()
					}
				) {
					Icon(
						imageVector = Icons.Rounded.ArrowBack,
						contentDescription = null
					)
				}
			}
		)
		
		for (mUiMode in uiModes) {
			ThemeItem(
				selected = uiModeState.uiMode == mUiMode,
				uiMode = mUiMode,
				colorScheme = when (mUiMode) {
					UiMode.DARK -> DarkColorScheme
					UiMode.LIGHT -> LightColorScheme
					UiMode.DYNAMIC_DARK -> dynamicDarkColorScheme(LocalContext.current)
					UiMode.DYNAMIC_LIGHT -> dynamicLightColorScheme(LocalContext.current)
				},
				onClick = {
					uiModeViewModel.dispatch(
						UiModeAction.SetUiMode(mUiMode)
					)
					
					navController.popBackStack()
				},
				modifier = Modifier
                    .padding(
                        vertical = 4.dp,
                        horizontal = 8.dp
                    )
                    .fillMaxWidth()
			)
		}
	}
}
