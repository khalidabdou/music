package com.ringtones.compose.foundation.extension

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> SnapshotStateList<T>.swap(newList: List<T>): SnapshotStateList<T> {
	clear()
	addAll(newList)
	
	return this
}