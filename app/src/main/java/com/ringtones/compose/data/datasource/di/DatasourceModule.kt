package com.ringtones.compose.data.datasource.di

import com.ringtones.compose.data.datasource.local.LocalDatasource
import com.ringtones.compose.data.datasource.local.db.playlist.PlaylistDao
import com.ringtones.compose.data.datasource.local.db.song.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatasourceModule {
	
	@Provides
	@Singleton
	fun provideLocalDatasource(
		songDao: SongDao,
		playlistDao: PlaylistDao
	): LocalDatasource = LocalDatasource(
		songDao = songDao,
		playlistDao = playlistDao
	)
	
}