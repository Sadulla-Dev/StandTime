package com.standtime.clock.standtime.feature.utils

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

enum class CustomClockFont {
    MONO,
    MONO_WIDE,
    SERIF_CLASSIC,
    SERIF_SOFT,
    SANS_CLEAN,
    SANS_BOLD,
    CONDENSED,
    CURSIVE,
    TECH,
    POSTER,
    ELEGANT,
    MINIMAL
}

enum class CustomClockLayout {
    HORIZONTAL,
    VERTICAL
}

enum class ClockStyle {
    NOTHING,
    PIXEL,
    IPHONE,
    MINIMAL
}

enum class PomodoroPhase {
    FOCUS,
    SHORT_BREAK,
    LONG_BREAK
}

data class PomodoroPreset(
    val focusMinutes: Int,
    val shortBreakMinutes: Int,
    val longBreakMinutes: Int,
    val label: String
)

data class CustomColorValue(
    val argb: Long
)

data class CustomClockStyleSettings(
    val font: CustomClockFont = CustomClockFont.MONO,
    val textColor: CustomColorValue = CustomColorValue(0xFFFFFFFF),
    val backgroundStartColor: CustomColorValue = CustomColorValue(0xFF020617),
    val showBackgroundCenterColor: Boolean = false,
    val backgroundCenterColor: CustomColorValue = CustomColorValue(0xFF0F172A),
    val showBackgroundEndColor: Boolean = false,
    val backgroundEndColor: CustomColorValue = CustomColorValue(0xFF1E293B),
    val scale: Float = 1f,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val layout: CustomClockLayout = CustomClockLayout.VERTICAL,
    val showSeconds: Boolean = false,
    val showDate: Boolean = true,
    val showWeather: Boolean = false,
    val recentColors: List<CustomColorValue> = listOf(
        CustomColorValue(0xFFFFFFFF),
        CustomColorValue(0xFF111827),
        CustomColorValue(0xFFF97316),
        CustomColorValue(0xFF38BDF8),
        CustomColorValue(0xFF22C55E),
        CustomColorValue(0xFFA855F7)
    )
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
    val locationPermissionGranted: Boolean = false,
    val isWeatherLoading: Boolean = false,
    val weatherSummary: String = "",
    val weatherTemperature: String = "",
    val weatherWind: String = "",
    val locationName: String = "",
    val latitudeText: String = "",
    val longitudeText: String = "",
    val weatherError: String = "",
    val themeMode: ThemeMode = ThemeMode.DARK,
    val language: StandTimeLanguage = StandTimeLanguage.ENGLISH,
    val accentPalette: AccentPalette = AccentPalette.LIME,
    val clockStyle: ClockStyle = ClockStyle.NOTHING,
    val showCalendar: Boolean = true,
    val showWeather: Boolean = true,
    val showBattery: Boolean = true,
    val showPomodoro: Boolean = true,
    val showSeconds: Boolean = true,
    val pomodoroPresets: List<PomodoroPreset> = listOf(
        PomodoroPreset(15, 5, 12, "15"),
        PomodoroPreset(25, 5, 15, "25"),
        PomodoroPreset(50, 10, 20, "50")
    ),
    val selectedPomodoroMinutes: Int = 25,
    val pomodoroPhase: PomodoroPhase = PomodoroPhase.FOCUS,
    val pomodoroActivePhase: PomodoroPhase? = null,
    val pomodoroCompletedFocusSessions: Int = 0,
    val pomodoroFocusRemainingSeconds: Int = 25 * 60,
    val pomodoroShortBreakRemainingSeconds: Int = 5 * 60,
    val pomodoroLongBreakRemainingSeconds: Int = 15 * 60,
    val pomodoroRemainingSeconds: Int = 25 * 60,
    val pomodoroEndsAtMillis: Long? = null,
    val isPomodoroRunning: Boolean = false,
    val mediaPermissionGranted: Boolean = false,
    val mediaSessionAvailable: Boolean = false,
    val mediaAppName: String = "",
    val mediaAppLabel: String = "",
    val mediaTitle: String = "",
    val mediaSubtitle: String = "",
    val selectedGalleryStyleIndex: Int = 0,
    val isMediaPlaying: Boolean = false,
    val enableChargingStandMode: Boolean = false,
    val enableBurnInProtection: Boolean = true,
    val enableTapRevealInfo: Boolean = true,
    val enableAutoDimNightMode: Boolean = false,
    val enableSleepColorFilter: Boolean = false,
    val enableWaveToWake: Boolean = false,
    val customClockStyle: CustomClockStyleSettings = CustomClockStyleSettings(),
    val savedCustomClockStyles: List<SavedCustomClockStyle> = emptyList(),
    val editingCustomClockStyleId: String? = null
)

sealed interface StandTimeIntent {
    data object ToggleTheme : StandTimeIntent
    data class ChangeLanguage(val language: StandTimeLanguage) : StandTimeIntent
    data class ChangeAccent(val palette: AccentPalette) : StandTimeIntent
    data class ChangeGalleryStyleIndex(val index: Int) : StandTimeIntent
    data class ChangeClockStyle(val clockStyle: ClockStyle) : StandTimeIntent
    data object ToggleCalendar : StandTimeIntent
    data object ToggleWeather : StandTimeIntent
    data object ToggleBattery : StandTimeIntent
    data object TogglePomodoro : StandTimeIntent
    data object ToggleSeconds : StandTimeIntent
    data class SetChargingStandMode(val enabled: Boolean) : StandTimeIntent
    data class SetBurnInProtection(val enabled: Boolean) : StandTimeIntent
    data class SetTapRevealInfo(val enabled: Boolean) : StandTimeIntent
    data class SetAutoDimNightMode(val enabled: Boolean) : StandTimeIntent
    data class SetSleepColorFilter(val enabled: Boolean) : StandTimeIntent
    data class SetWaveToWake(val enabled: Boolean) : StandTimeIntent
    data class LocationPermissionChanged(val granted: Boolean) : StandTimeIntent
    data object RefreshWeather : StandTimeIntent
    data class SelectPomodoroPreset(val minutes: Int) : StandTimeIntent
    data class SetPomodoroPhase(val phase: PomodoroPhase) : StandTimeIntent
    data object TogglePomodoroTimer : StandTimeIntent
    data object ResetPomodoro : StandTimeIntent
    data object SkipPomodoroPhase : StandTimeIntent
    data object ToggleMediaPlayback : StandTimeIntent
    data object SkipToNextTrack : StandTimeIntent
    data class ChangeCustomClockFont(val font: CustomClockFont) : StandTimeIntent
    data class ChangeCustomClockTextColor(val color: CustomColorValue) : StandTimeIntent
    data class ChangeCustomClockBackgroundStart(val color: CustomColorValue) : StandTimeIntent
    data class ToggleCustomClockBackgroundCenter(val enabled: Boolean) : StandTimeIntent
    data class ChangeCustomClockBackgroundCenter(val color: CustomColorValue) : StandTimeIntent
    data class ToggleCustomClockBackgroundEnd(val enabled: Boolean) : StandTimeIntent
    data class ChangeCustomClockBackgroundEnd(val color: CustomColorValue) : StandTimeIntent
    data class ChangeCustomClockScale(val scale: Float) : StandTimeIntent
    data class ChangeCustomClockOffset(val x: Float, val y: Float) : StandTimeIntent
    data class ChangeCustomClockLayout(val layout: CustomClockLayout) : StandTimeIntent
    data class ToggleCustomClockSeconds(val enabled: Boolean) : StandTimeIntent
    data class ToggleCustomClockDate(val enabled: Boolean) : StandTimeIntent
    data class ToggleCustomClockWeather(val enabled: Boolean) : StandTimeIntent
    data object StartNewCustomClockStyle : StandTimeIntent
    data class EditSavedCustomClockStyle(val id: String) : StandTimeIntent
    data object SaveCustomClockStyle : StandTimeIntent
    data class DeleteSavedCustomClockStyle(val id: String) : StandTimeIntent
}
