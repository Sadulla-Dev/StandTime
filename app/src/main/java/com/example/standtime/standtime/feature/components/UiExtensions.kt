package com.standtime.clock.standtime.feature.components

import androidx.compose.ui.graphics.Color
import com.standtime.clock.standtime.feature.utils.AccentPalette
import com.standtime.clock.standtime.feature.utils.PomodoroPhase
import com.standtime.clock.standtime.feature.utils.PomodoroPreset
import com.standtime.clock.standtime.feature.utils.StandTimeUiState
import com.standtime.clock.ui.theme.CoralAccent
import com.standtime.clock.ui.theme.LimeAccent
import com.standtime.clock.ui.theme.SkyAccent

internal fun StandTimeUiState.remainingPomodoroText(): String {
    val totalSeconds = pomodoroRemainingForViewedPhase()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

internal fun StandTimeUiState.selectedPomodoroPreset(): PomodoroPreset =
    pomodoroPresets.firstOrNull { it.focusMinutes == selectedPomodoroMinutes }
        ?: pomodoroPresets.first()

internal fun StandTimeUiState.pomodoroTotalSeconds(phase: PomodoroPhase = pomodoroPhase): Int {
    val preset = selectedPomodoroPreset()
    return when (phase) {
        PomodoroPhase.FOCUS -> preset.focusMinutes * 60
        PomodoroPhase.SHORT_BREAK -> preset.shortBreakMinutes * 60
        PomodoroPhase.LONG_BREAK -> preset.longBreakMinutes * 60
    }
}

internal fun StandTimeUiState.pomodoroProgress(): Float {
    val totalSeconds = pomodoroTotalSeconds().coerceAtLeast(1)
    val elapsedSeconds = (totalSeconds - pomodoroRemainingForViewedPhase()).coerceIn(0, totalSeconds)
    return elapsedSeconds / totalSeconds.toFloat()
}

internal fun StandTimeUiState.pomodoroRemainingForPhase(phase: PomodoroPhase): Int = when (phase) {
    PomodoroPhase.FOCUS -> pomodoroFocusRemainingSeconds
    PomodoroPhase.SHORT_BREAK -> pomodoroShortBreakRemainingSeconds
    PomodoroPhase.LONG_BREAK -> pomodoroLongBreakRemainingSeconds
}

internal fun StandTimeUiState.pomodoroRemainingForViewedPhase(): Int =
    pomodoroRemainingForPhase(pomodoroPhase)

internal fun StandTimeUiState.isViewedPomodoroRunning(): Boolean =
    isPomodoroRunning && pomodoroActivePhase == pomodoroPhase

internal fun StandTimeUiState.accentColor(): Color = when (accentPalette) {
    AccentPalette.LIME -> LimeAccent
    AccentPalette.SKY -> SkyAccent
    AccentPalette.CORAL -> CoralAccent
}
