package com.ringtones.compose.foundation.localized.di

import com.ringtones.compose.foundation.localized.environment.ILocalizedEnvironment
import com.ringtones.compose.foundation.localized.environment.LocalizedEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocalizedModule {
	
	@Binds
	abstract fun provideEnvironment(
		localizedEnvironment: LocalizedEnvironment
	): ILocalizedEnvironment
	
}