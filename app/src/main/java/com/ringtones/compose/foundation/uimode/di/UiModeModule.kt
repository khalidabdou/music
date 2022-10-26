package com.ringtones.compose.foundation.uimode.di

import com.ringtones.compose.foundation.uimode.environment.IUiModeEnvironment
import com.ringtones.compose.foundation.uimode.environment.UiModeEnvironment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UiModeModule {
	
	@Binds
	abstract fun provideEnvironment(
		uiModeEnvironment: UiModeEnvironment
	): IUiModeEnvironment
	
}