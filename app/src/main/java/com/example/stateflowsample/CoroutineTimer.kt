package com.example.stateflowsample

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CoroutineTimer(private val scope: CoroutineScope, private val duration: Int) {
    var job: Job? = null
    fun start(onTick: (seconds: Int) -> Unit) {
        job?.cancel()
        job = scope.launch {
            (duration downTo 0)
                .asFlow()
                .onStart { emit(duration) }
                .onEach { delay(1000) }
                .conflate()
                .collectLatest { seconds ->
                    onTick(seconds)
                }
        }
    }
}