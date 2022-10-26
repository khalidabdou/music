package com.ringtones.compose.foundation.uimode

import com.ringtones.compose.foundation.uimode.data.UiMode

sealed class UiModeAction {
    data class SetUiMode(val uiMode: UiMode) : UiModeAction()
}
