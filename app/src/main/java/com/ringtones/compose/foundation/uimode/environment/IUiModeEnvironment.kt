package com.ringtones.compose.foundation.uimode.environment

import com.ringtones.compose.foundation.uimode.data.UiMode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IUiModeEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getUiMode(): Flow<UiMode>
	
	suspend fun setUiMode(uiMode: UiMode)
	
}