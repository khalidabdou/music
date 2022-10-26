package com.ringtones.compose.foundation.localized.environment

import com.ringtones.compose.data.datastore.AppDatastore
import com.ringtones.compose.data.preference.Language
import com.ringtones.compose.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class LocalizedEnvironment @Inject constructor(
	@Named(DiName.IO) override val dispatcher: CoroutineDispatcher,
	private val appDatastore: AppDatastore
): ILocalizedEnvironment {
	
	override suspend fun setLanguage(lang: Language) {
		appDatastore.setLanguage(lang)
	}
	
	override fun getLanguage(): Flow<Language> {
		return appDatastore.getLanguage
	}
	
	
}