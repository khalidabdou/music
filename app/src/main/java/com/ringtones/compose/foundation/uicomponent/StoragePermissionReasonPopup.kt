package com.ringtones.compose.foundation.uicomponent

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ringtones.compose.R
import com.ringtones.compose.foundation.theme.Inter

@Composable
fun StoragePermissionReasonPopup(
	modifier: Modifier = Modifier,
	onDeny: () -> Unit,
	onAllow: () -> Unit
) {
	
	AlertDialog(
		modifier = modifier,
		onDismissRequest = {},
		title = {
			Text(stringResource(id = R.string.media_access))
		},
		text = {
			Text(stringResource(id = R.string.media_access_message))
		},
		confirmButton = {
			Button(
				onClick = onAllow
			) {
				Text(
					text = stringResource(id = R.string.allow),
					style = LocalTextStyle.current.copy(
						fontFamily = Inter
					)
				)
			}
		},
		dismissButton = {
			TextButton(
				onClick = onDeny
			) {
				Text(
					text = stringResource(id = R.string.deny),
					style = LocalTextStyle.current.copy(
						fontFamily = Inter
					)
				)
			}
		}
	)
}
