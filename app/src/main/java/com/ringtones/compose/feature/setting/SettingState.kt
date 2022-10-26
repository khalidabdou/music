package com.ringtones.compose.feature.setting

import com.ringtones.compose.data.SkipForwardBackward

data class SettingState(
    val skipForwardBackward: SkipForwardBackward = SkipForwardBackward.FIVE_SECOND
)
