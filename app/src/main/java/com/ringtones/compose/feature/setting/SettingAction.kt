package com.ringtones.compose.feature.setting

import com.ringtones.compose.data.SkipForwardBackward

sealed interface SettingAction {
	data class SetSkipForwardBackward(val skipForwardBackward: SkipForwardBackward): SettingAction
}