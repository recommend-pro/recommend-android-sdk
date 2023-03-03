package com.recommend.sdk.core

import android.content.Context
import com.recommend.sdk.core.data.model.CurrentState
import com.recommend.sdk.core.data.repository.CurrentStateRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Current state manager
 *
 * @property context
 * @constructor Create Current state manager
 */
class CurrentStateManager(val context: Context) {
    private val currentStateRepository = CurrentStateRepository(context)
    private lateinit var currentState: CurrentState
    private val mutex = Mutex()

    suspend fun getCurrentState(): CurrentState {
        mutex.withLock {
            if (!this::currentState.isInitialized) {
                this.currentState = currentStateRepository.getCurrentState()
            }
            return currentState
        }
    }

    suspend fun onFirstLaunch() {
        val currentState = getCurrentState()
        currentState.isFirstLaunch = false
        currentStateRepository.saveCurrentState(currentState)
    }

    suspend fun saveCurrentState(currentState: CurrentState) {
        mutex.withLock {
            this@CurrentStateManager.currentState = currentState
            currentStateRepository.saveCurrentState(currentState)
        }
    }
}
