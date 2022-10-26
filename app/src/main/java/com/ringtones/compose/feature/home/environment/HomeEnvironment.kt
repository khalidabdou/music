package com.ringtones.compose.feature.home.environment

import com.ringtones.compose.data.repository.Repository
import com.ringtones.compose.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class HomeEnvironment @Inject constructor(
    @Named(DiName.IO) override val dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : IHomeEnvironment