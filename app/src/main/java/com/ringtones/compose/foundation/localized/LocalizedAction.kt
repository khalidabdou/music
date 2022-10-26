package com.ringtones.compose.foundation.localized

import com.ringtones.compose.data.preference.Language

sealed class LocalizedAction {
    data class SetLanguage(val lang: Language) : LocalizedAction()
}