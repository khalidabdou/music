package com.ringtones.compose.feature.setting.environment

import com.ringtones.compose.data.SkipForwardBackward
import com.ringtones.compose.data.datastore.AppDatastore
import com.ringtones.compose.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class SettingEnvironment @Inject constructor(
	@Named(DiName.IO) override val dispatcher: CoroutineDispatcher,
	private val appDatastore: AppDatastore
): ISettingEnvironment {
	
	override fun getSkipForwardBackward(): Flow<SkipForwardBackward> {
		return appDatastore.getSkipForwardBackward
	}
	
	override suspend fun setSkipForwardBackward(skipForwardBackward: SkipForwardBackward) {
		appDatastore.setSkipForwardBackward(skipForwardBackward)
	}
	
}