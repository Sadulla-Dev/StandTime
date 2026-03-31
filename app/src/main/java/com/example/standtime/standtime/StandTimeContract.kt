package com.example.standtime.standtime

enum class ThemeMode {
    LIGHT,
    DARK
}

enum class StandTimeLanguage {
    ENGLISH,
    UZBEK,
    RUSSIAN
}

enum class AccentPalette {
    LIME,
    SKY,
    CORAL
}

enum class ClockStyle {
    NOTHING,
    PIXEL,
    IPHONE,
    MINIMAL
}

data class PomodoroPreset(
    val minutes: Int,
    val label: String
)

data class CalendarDayCell(
    val label: String,
    val isToday: Boolean,
    val isCurrentMonth: Boolean
)

data class StandTimeUiState(
    val timeText: String = "--:--",
    val dateText: String = "",
    val dayText: String = "",
    val monthTitle: String = "",
    val weekDayLabels: List<String> = emptyList(),
    val calendarCells: List<CalendarDayCell> = emptyList(),
    val batteryLevel: Int = 0,
    val isCharging: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.DARK,
    val language: StandTimeLanguage = StandTimeLanguage.ENGLISH,
    val accentPalette: AccentPalette = AccentPalette.LIME,
    val clockStyle: ClockStyle = ClockStyle.NOTHING,
    val showCalendar: Boolean = true,
    val showBattery: Boolean = true,
    val showPomodoro: Boolean = true,
    val showSeconds: Boolean = true,
    val pomodoroPresets: List<PomodoroPreset> = listOf(
        PomodoroPreset(15, "15"),
        PomodoroPreset(25, "25"),
        PomodoroPreset(50, "50")
    ),
    val selectedPomodoroMinutes: Int = 25,
    val pomodoroRemainingSeconds: Int = 25 * 60,
    val isPomodoroRunning: Boolean = false,
    val mediaPermissionGranted: Boolean = false,
    val mediaSessionAvailable: Boolean = false,
    val mediaAppName: String = "",
    val mediaTitle: String = "",
    val mediaSubtitle: String = "",
    val isMediaPlaying: Boolean = false
)

sealed interface StandTimeIntent {
    data object ToggleTheme : StandTimeIntent
    data class ChangeLanguage(val language: StandTimeLanguage) : StandTimeIntent
    data class ChangeAccent(val palette: AccentPalette) : StandTimeIntent
    data class ChangeClockStyle(val clockStyle: ClockStyle) : StandTimeIntent
    data object ToggleCalendar : StandTimeIntent
    data object ToggleBattery : StandTimeIntent
    data object TogglePomodoro : StandTimeIntent
    data object ToggleSeconds : StandTimeIntent
    data class SelectPomodoroPreset(val minutes: Int) : StandTimeIntent
    data object TogglePomodoroTimer : StandTimeIntent
    data object ResetPomodoro : StandTimeIntent
    data object ToggleMediaPlayback : StandTimeIntent
    data object SkipToNextTrack : StandTimeIntent
}
