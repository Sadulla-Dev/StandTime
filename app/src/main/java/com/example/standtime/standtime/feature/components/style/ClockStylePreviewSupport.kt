package com.standtime.clock.standtime.feature.components.style

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.CustomClockFont
import com.standtime.clock.standtime.feature.utils.CustomClockLayout
import com.standtime.clock.standtime.feature.utils.CustomClockStyleSettings
import com.standtime.clock.standtime.feature.utils.CustomColorValue
import com.standtime.clock.ui.theme.StandTimeTheme

internal val ClockStylePreviewParts = GalleryClockParts(
    hours = "18",
    minutes = "24",
    seconds = "36",
    dayText = "Friday",
    dateText = "Apr 4",
    kanjiHours = "一八",
    kanjiMinutes = "二四",
    weatherTemperature = "24°",
    weatherSummary = "Sunny",
    locationName = "Tashkent",
    batteryInfo = "82"
)

internal val ClockStylePreviewAccent = Color(0xFFE67E22)

internal val ClockStylePreviewCustomStyle = CustomClockStyleSettings(
    font = CustomClockFont.CONDENSED,
    textColor = CustomColorValue(0xFFF8FAFC),
    backgroundStartColor = CustomColorValue(0xFF0F172A),
    showBackgroundCenterColor = true,
    backgroundCenterColor = CustomColorValue(0xFF1D4ED8),
    showBackgroundEndColor = true,
    backgroundEndColor = CustomColorValue(0xFF7C3AED),
    scale = 1.12f,
    layout = CustomClockLayout.HORIZONTAL,
    showSeconds = true,
    showDate = true,
    showWeather = true
)

@Composable
internal fun ClockStylePreviewFrame(content: @Composable (Modifier) -> Unit) {
    StandTimeTheme(darkTheme = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            content(Modifier.fillMaxSize())
        }
    }
}
