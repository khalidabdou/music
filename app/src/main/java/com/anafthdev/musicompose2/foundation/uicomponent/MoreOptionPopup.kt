package com.anafthdev.musicompose2.foundation.uicomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.anafthdev.musicompose2.foundation.theme.Inter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionPopup(
	options: List<String>,
	modifier: Modifier = Modifier,
	onDismissRequest: () -> Unit,
	onClick: (Int) -> Unit
) {
	
	Popup(
		alignment = Alignment.BottomCenter,
		onDismissRequest = onDismissRequest
	) {
		Card(
			shape = MaterialTheme.shapes.medium,
			modifier = modifier
		) {
			options.forEachIndexed { i, label ->
				Row() {
					Text(
						text = label,
						style = MaterialTheme.typography.titleMedium.copy(
							fontFamily = Inter
						),
						modifier = Modifier
							.padding(8.dp)
							.clickable {
								onDismissRequest()
								onClick(i)
							}
					)
				}

			}
		}
	}
}
