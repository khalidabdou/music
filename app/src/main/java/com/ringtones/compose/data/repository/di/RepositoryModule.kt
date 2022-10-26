package com.ringtones.compose.data.repository.di

import com.ringtones.compose.data.datasource.local.LocalDatasource
import com.ringtones.compose.data.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
	
	@Provides
	@Singleton
	fun provideRepository(
		localDatasource: LocalDatasource
	): Repository = Repository(localDatasource)
	
}