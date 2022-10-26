package com.ringtones.compose.feature.setting.environment

import com.ringtones.compose.data.SkipForwardBackward
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ISettingEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getSkipForwardBackward(): Flow<SkipForwardBackward>
	
	suspend fun setSkipForwardBackward(skipForwardBackward: SkipForwardBackward)
	
}