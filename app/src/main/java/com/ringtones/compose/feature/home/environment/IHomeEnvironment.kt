package com.ringtones.compose.feature.home.environment

import kotlinx.coroutines.CoroutineDispatcher

interface IHomeEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
}