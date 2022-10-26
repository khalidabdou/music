package com.ringtones.compose.data.datasource.local

import com.ringtones.compose.data.datasource.local.db.playlist.PlaylistDao
import com.ringtones.compose.data.datasource.local.db.song.SongDao
import com.ringtones.compose.data.model.Playlist
import com.ringtones.compose.data.model.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDatasource @Inject constructor(
	private val songDao: SongDao,
	private val playlistDao: PlaylistDao
) {
	
	fun getAllSong(): Flow<List<Song>> {
		return songDao.getAllSong()
	}
	
	fun getSong(audioID: Long): Song? {
		return songDao.get(audioID)
	}
	
	suspend fun updateSongs(vararg song: Song) {
		songDao.update(*song)
	}
	
	suspend fun deleteSongs(vararg song: Song) {
		songDao.delete(*song)
	}
	
	suspend fun insertSongs(vararg song: Song) {
		songDao.insert(*song)
	}
	
	
	
	fun getAllPlaylist(): Flow<List<Playlist>> {
		return playlistDao.getAllPlaylist()
	}
	
	fun getPlaylist(id: Int): Playlist? {
		return playlistDao.get(id)
	}
	
	suspend fun updatePlaylist(id: Int, name: String, iconID: Int) {
		playlistDao.update(id, name, iconID)
	}
	
	suspend fun deletePlaylists(vararg playlist: Playlist) {
		playlistDao.delete(*playlist)
	}
	
	suspend fun updatePlaylists(vararg playlist: Playlist) {
		playlistDao.update(*playlist)
	}
	
	suspend fun insertPlaylists(vararg playlist: Playlist) {
		playlistDao.insert(*playlist)
	}
	
}