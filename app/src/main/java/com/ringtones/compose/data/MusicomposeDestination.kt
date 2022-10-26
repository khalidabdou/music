package com.ringtones.compose.data

sealed class MusicomposeDestination(open val route: String) {

	object Main: MusicomposeDestination("main")
	
	//object Search: MusicomposeDestination("search")
	
	object Setting: MusicomposeDestination("setting")
	
	object Language: MusicomposeDestination("language")
	
	object Theme: MusicomposeDestination("theme")
	
	//object ScanOptions: MusicomposeDestination("scan-options")
	
	/*object Album: MusicomposeDestination("album/{albumID}") {
		fun createRoute(albumID: String): String {
			return "album/$albumID"
		}
	}
	
	object Artist: MusicomposeDestination("artist/{artistID}") {
		fun createRoute(artistID: String): String {
			return "artist/$artistID"
		}
	}
	
	object Playlist: MusicomposeDestination("playlist/{playlistID}") {
		fun createRoute(playlistID: Int): String {
			return "playlist/$playlistID"
		}
	}
	
	object SongSelector: MusicomposeDestination("song-selector/{type}/{playlistID}") {
		fun createRoute(type: SongSelectorType, playlistID: Int): String {
			return "song-selector/${type.ordinal}/$playlistID"
		}
	}*/
	
	class BottomSheet {
		object MusicPlayer: MusicomposeDestination("music-player")
		
		object Sort: MusicomposeDestination("bottom-sheet/sort/{type}") {
			fun createRoute(type: SortType): String {
				return "bottom-sheet/sort/${type.ordinal}"
			}
		}
		
		object Playlist: MusicomposeDestination("bottom-sheet/playlist/{option}/{playlistID}") {
			fun createRoute(
                option: PlaylistOption,
                playlistID: Int = com.ringtones.compose.data.model.Playlist.default.id
			): String {
				return "bottom-sheet/playlist/${option.ordinal}/$playlistID"
			}
		}
		
		object DeletePlaylist: MusicomposeDestination("delete-playlist/{playlistID}") {
			fun createRoute(playlistID: Int): String {
				return "delete-playlist/$playlistID"
			}
		}
	}

}
