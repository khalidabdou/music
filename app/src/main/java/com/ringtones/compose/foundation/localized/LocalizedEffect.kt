package com.ringtones.compose.foundation.localized

import com.ringtones.compose.data.preference.Language

sealed class LocalizedEffect {
    data class ApplyLanguage(val language: Language) : LocalizedEffect()
}