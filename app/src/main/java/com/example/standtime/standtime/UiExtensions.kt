package com.example.standtime.standtime

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.standtime.R
import com.example.standtime.ui.theme.CoralAccent
import com.example.standtime.ui.theme.LimeAccent
import com.example.standtime.ui.theme.SkyAccent

internal fun StandTimeUiState.remainingPomodoroText(): String {
    val minutes = pomodoroRemainingSeconds / 60
    val seconds = pomodoroRemainingSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

internal fun StandTimeUiState.accentColor(): Color = when (accentPalette) {
    AccentPalette.LIME -> LimeAccent
    AccentPalette.SKY -> SkyAccent
    AccentPalette.CORAL -> CoralAccent
}

@Composable
internal fun ClockStyle.label(language: StandTimeLanguage): String = when (this) {
    ClockStyle.NOTHING -> localizedStringResource(R.string.nothing_style_label, language)
    ClockStyle.PIXEL -> localizedStringResource(R.string.pixel_style_label, language)
    ClockStyle.IPHONE -> localizedStringResource(R.string.iphone_style_label, language)
    ClockStyle.MINIMAL -> localizedStringResource(R.string.minimal_style_label, language)
}
