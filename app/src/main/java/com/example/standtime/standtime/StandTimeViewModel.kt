package com.example.standtime.standtime

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StandTimeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(StandTimeUiState())
    val uiState: StateFlow<StandTimeUiState> = _uiState.asStateFlow()

    init {
        startClock()
        startPomodoroTicker()
    }

    fun onIntent(intent: StandTimeIntent) {
        when (intent) {
            StandTimeIntent.ToggleTheme -> _uiState.update { state ->
                state.copy(
                    themeMode = if (state.themeMode == ThemeMode.DARK) {
                        ThemeMode.LIGHT
                    } else {
                        ThemeMode.DARK
                    }
                )
            }

            is StandTimeIntent.ChangeLanguage -> _uiState.update { state ->
                state.copy(language = intent.language)
            }

            is StandTimeIntent.ChangeAccent -> _uiState.update { state ->
                state.copy(accentPalette = intent.palette)
            }

            is StandTimeIntent.ChangeClockStyle -> _uiState.update { state ->
                state.copy(clockStyle = intent.clockStyle)
            }

            StandTimeIntent.ToggleCalendar -> _uiState.update { state ->
                state.copy(showCalendar = !state.showCalendar)
            }

            StandTimeIntent.ToggleBattery -> _uiState.update { state ->
                state.copy(showBattery = !state.showBattery)
            }

            StandTimeIntent.TogglePomodoro -> _uiState.update { state ->
                state.copy(showPomodoro = !state.showPomodoro)
            }

            StandTimeIntent.ToggleSeconds -> _uiState.update { state ->
                state.copy(showSeconds = !state.showSeconds)
            }

            is StandTimeIntent.SelectPomodoroPreset -> _uiState.update { state ->
                state.copy(
                    selectedPomodoroMinutes = intent.minutes,
                    pomodoroRemainingSeconds = intent.minutes * 60,
                    isPomodoroRunning = false
                )
            }

            StandTimeIntent.TogglePomodoroTimer -> _uiState.update { state ->
                state.copy(isPomodoroRunning = !state.isPomodoroRunning)
            }

            StandTimeIntent.ResetPomodoro -> _uiState.update { state ->
                state.copy(
                    pomodoroRemainingSeconds = state.selectedPomodoroMinutes * 60,
                    isPomodoroRunning = false
                )
            }

            StandTimeIntent.ToggleMediaPlayback -> StandTimeMediaService.togglePlayback()
            StandTimeIntent.SkipToNextTrack -> StandTimeMediaService.skipToNext()
        }
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                val state = _uiState.value
                val locale = state.language.toLocale()
                val now = Date()
                val timePattern = if (state.showSeconds) "HH:mm:ss" else "HH:mm"
                val timeText = SimpleDateFormat(timePattern, locale).format(now)
                val dateText = SimpleDateFormat("d MMMM yyyy", locale).format(now)
                val dayText = SimpleDateFormat("EEEE", locale).format(now)
                val calendarState = buildCalendarState(locale)
                val batterySnapshot = readBatterySnapshot()
                val mediaSnapshot = StandTimeMediaService.snapshot(getApplication())

                _uiState.update {
                    it.copy(
                        timeText = timeText,
                        dateText = dateText,
                        dayText = dayText.replaceFirstChar { char ->
                            if (char.isLowerCase()) char.titlecase(locale) else char.toString()
                        },
                        monthTitle = calendarState.monthTitle,
                        weekDayLabels = calendarState.weekHeaders,
                        calendarCells = calendarState.cells,
                        batteryLevel = batterySnapshot.level,
                        isCharging = batterySnapshot.isCharging,
                        mediaPermissionGranted = mediaSnapshot.permissionGranted,
                        mediaSessionAvailable = mediaSnapshot.sessionAvailable,
                        mediaAppName = mediaSnapshot.appName,
                        mediaTitle = mediaSnapshot.title,
                        mediaSubtitle = mediaSnapshot.subtitle,
                        isMediaPlaying = mediaSnapshot.isPlaying
                    )
                }
                delay(1_000)
            }
        }
    }

    private fun startPomodoroTicker() {
        viewModelScope.launch {
            while (true) {
                delay(1_000)
                _uiState.update { state ->
                    if (!state.isPomodoroRunning) {
                        state
                    } else if (state.pomodoroRemainingSeconds <= 1) {
                        state.copy(
                            pomodoroRemainingSeconds = state.selectedPomodoroMinutes * 60,
                            isPomodoroRunning = false
                        )
                    } else {
                        state.copy(
                            pomodoroRemainingSeconds = state.pomodoroRemainingSeconds - 1
                        )
                    }
                }
            }
        }
    }

    private fun readBatterySnapshot(): BatterySnapshot {
        val context = getApplication<Application>()
        val batteryStatus = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) ?: 0
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100
        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val percentage = if (scale > 0) (level * 100) / scale else 0
        val charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
            status == BatteryManager.BATTERY_STATUS_FULL
        return BatterySnapshot(
            level = percentage.coerceIn(0, 100),
            isCharging = charging
        )
    }

    private fun buildCalendarState(locale: Locale): CalendarState {
        val today = Calendar.getInstance(locale)
        val working = Calendar.getInstance(locale).apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val monthTitle = SimpleDateFormat("MMMM yyyy", locale).format(working.time)
        val firstDayOfWeek = working.firstDayOfWeek
        val weekHeaders = buildList {
            repeat(7) { index ->
                val headerCalendar = Calendar.getInstance(locale).apply {
                    set(Calendar.DAY_OF_WEEK, ((firstDayOfWeek - 1 + index) % 7) + 1)
                }
                add(
                    SimpleDateFormat("EE", locale)
                        .format(headerCalendar.time)
                        .replaceFirstChar { char ->
                            if (char.isLowerCase()) char.titlecase(locale) else char.toString()
                        }
                )
            }
        }

        val leadingBlanks = (7 + (working.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek)) % 7
        val daysInMonth = working.getActualMaximum(Calendar.DAY_OF_MONTH)
        val cells = buildList {
            repeat(leadingBlanks) {
                add(CalendarDayCell(label = "", isToday = false, isCurrentMonth = false))
            }
            for (day in 1..daysInMonth) {
                add(
                    CalendarDayCell(
                        label = day.toString(),
                        isToday = today.get(Calendar.YEAR) == working.get(Calendar.YEAR) &&
                            today.get(Calendar.MONTH) == working.get(Calendar.MONTH) &&
                            today.get(Calendar.DAY_OF_MONTH) == day,
                        isCurrentMonth = true
                    )
                )
            }
            while (size % 7 != 0) {
                add(CalendarDayCell(label = "", isToday = false, isCurrentMonth = false))
            }
        }

        return CalendarState(
            monthTitle = monthTitle.replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase(locale) else char.toString()
            },
            weekHeaders = weekHeaders,
            cells = cells
        )
    }

    private fun StandTimeLanguage.toLocale(): Locale = when (this) {
        StandTimeLanguage.ENGLISH -> Locale.ENGLISH
        StandTimeLanguage.UZBEK -> Locale.forLanguageTag("uz")
        StandTimeLanguage.RUSSIAN -> Locale.forLanguageTag("ru")
    }
}

private data class BatterySnapshot(
    val level: Int,
    val isCharging: Boolean
)

private data class CalendarState(
    val monthTitle: String,
    val weekHeaders: List<String>,
    val cells: List<CalendarDayCell>
)
