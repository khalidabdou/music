package com.ringtones.compose.foundation.localized.environment

import com.ringtones.compose.data.preference.Language
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ILocalizedEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun setLanguage(lang: Language)
	
	fun getLanguage(): Flow<Language>
	
}