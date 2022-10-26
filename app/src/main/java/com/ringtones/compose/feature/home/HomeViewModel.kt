package com.ringtones.compose.feature.home

import com.ringtones.compose.feature.home.environment.IHomeEnvironment
import com.ringtones.compose.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	environment: IHomeEnvironment
): StatefulViewModel<HomeState, Unit, HomeAction, IHomeEnvironment>(
	HomeState,
	environment
) {
	
	override fun dispatch(action: HomeAction) {}
	
}