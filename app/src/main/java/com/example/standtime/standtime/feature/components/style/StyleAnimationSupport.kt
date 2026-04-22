package com.standtime.clock.standtime.feature.components.style

import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberUpdatedState

internal const val FullTurnRadians = 6.2831855f

@Composable
internal fun rememberContinuousAnimationSeconds(speed: Float = 1f): Float {
    val speedState by rememberUpdatedState(speed)
    val elapsedSeconds by produceState(initialValue = 0f) {
        var startTimeNanos = 0L
        while (true) {
            withInfiniteAnimationFrameNanos { frameTimeNanos ->
                if (startTimeNanos == 0L) {
                    startTimeNanos = frameTimeNanos
                }
                value = ((frameTimeNanos - startTimeNanos) / 1_000_000_000f) * speedState
            }
        }
    }
    return elapsedSeconds
}
