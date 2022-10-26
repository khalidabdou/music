package com.ringtones.compose.feature.language

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ringtones.compose.R
import com.ringtones.compose.data.preference.Language
import com.ringtones.compose.foundation.localized.LocalizedAction
import com.ringtones.compose.foundation.localized.LocalizedViewModel
import com.ringtones.compose.foundation.uicomponent.LanguageItem

@Composable
fun LanguageScreen(
	navController: NavController
) {
	
	val localizedViewModel = hiltViewModel<LocalizedViewModel>()
	
	val localizedState by localizedViewModel.state.collectAsState()
	
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
					text = stringResource(id = R.string.language),
					style = MaterialTheme.typography.titleLarge.copy(
						fontWeight = FontWeight.Bold
					),
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
		
		for (lang in Language.values()) {
			LanguageItem(
				selected = localizedState.language == lang,
				language = lang,
				onClick = {
					localizedViewModel.dispatch(
						LocalizedAction.SetLanguage(lang)
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
		
		Spacer(modifier = Modifier.height(16.dp))
	}
}
