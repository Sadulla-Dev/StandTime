package com.standtime.clock.standtime.feature.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import com.standtime.clock.PomodoroAlarmReceiver
import org.json.JSONObject

data class PomodoroSavedState(
    val selectedMinutes: Int,
    val viewedPhase: PomodoroPhase,
    val activePhase: PomodoroPhase?,
    val completedFocusSessions: Int,
    val focusRemainingSeconds: Int,
    val shortBreakRemainingSeconds: Int,
    val longBreakRemainingSeconds: Int,
    val selectedPhaseRemainingSeconds: Int,
    val endsAtMillis: Long?,
    val isRunning: Boolean
)

object PomodoroStateStore {
    private const val KEY_POMODORO_STATE = "pomodoro_state_json"
    private const val REQUEST_CODE = 5201

    fun defaultState(presets: List<PomodoroPreset>, selectedMinutes: Int = 25): PomodoroSavedState {
        val focusMinutes = presets.firstOrNull { it.focusMinutes == selectedMinutes }?.focusMinutes
            ?: presets.firstOrNull()?.focusMinutes
            ?: selectedMinutes
        val shortBreakMinutes = presets.firstOrNull { it.focusMinutes == focusMinutes }?.shortBreakMinutes
            ?: 5
        val longBreakMinutes = presets.firstOrNull { it.focusMinutes == focusMinutes }?.longBreakMinutes
            ?: 15
        return PomodoroSavedState(
            selectedMinutes = focusMinutes,
            viewedPhase = PomodoroPhase.FOCUS,
            activePhase = null,
            completedFocusSessions = 0,
            focusRemainingSeconds = focusMinutes * 60,
            shortBreakRemainingSeconds = shortBreakMinutes * 60,
            longBreakRemainingSeconds = longBreakMinutes * 60,
            selectedPhaseRemainingSeconds = focusMinutes * 60,
            endsAtMillis = null,
            isRunning = false
        )
    }

    fun load(
        prefs: SharedPreferences,
        presets: List<PomodoroPreset>
    ): PomodoroSavedState {
        val raw = prefs.getString(KEY_POMODORO_STATE, null) ?: return defaultState(presets)
        return runCatching {
            val json = JSONObject(raw)
            val selectedMinutes = json.optInt("selectedMinutes", 25)
            val fallback = defaultState(presets, selectedMinutes)
            val viewedPhase = json.optString("viewedPhase").takeIf { it.isNotBlank() }
                ?.let { runCatching { PomodoroPhase.valueOf(it) }.getOrNull() }
                ?: fallback.viewedPhase
            val activePhase = json.optString("activePhase").takeIf { it.isNotBlank() }
                ?.let { runCatching { PomodoroPhase.valueOf(it) }.getOrNull() }
            val focusRemaining = json.optInt("focusRemainingSeconds", fallback.focusRemainingSeconds)
            val shortBreakRemaining = json.optInt(
                "shortBreakRemainingSeconds",
                fallback.shortBreakRemainingSeconds
            )
            val longBreakRemaining = json.optInt(
                "longBreakRemainingSeconds",
                fallback.longBreakRemainingSeconds
            )
            val selectedRemaining = when (viewedPhase) {
                PomodoroPhase.FOCUS -> focusRemaining
                PomodoroPhase.SHORT_BREAK -> shortBreakRemaining
                PomodoroPhase.LONG_BREAK -> longBreakRemaining
            }
            PomodoroSavedState(
                selectedMinutes = selectedMinutes,
                viewedPhase = viewedPhase,
                activePhase = activePhase,
                completedFocusSessions = json.optInt("completedFocusSessions", 0),
                focusRemainingSeconds = focusRemaining,
                shortBreakRemainingSeconds = shortBreakRemaining,
                longBreakRemainingSeconds = longBreakRemaining,
                selectedPhaseRemainingSeconds = selectedRemaining,
                endsAtMillis = json.takeIf { it.has("endsAtMillis") }?.optLong("endsAtMillis"),
                isRunning = json.optBoolean("isRunning", false)
            )
        }.getOrElse {
            defaultState(presets)
        }
    }

    fun save(prefs: SharedPreferences, state: PomodoroSavedState) {
        val json = JSONObject().apply {
            put("selectedMinutes", state.selectedMinutes)
            put("viewedPhase", state.viewedPhase.name)
            put("activePhase", state.activePhase?.name ?: "")
            put("completedFocusSessions", state.completedFocusSessions)
            put("focusRemainingSeconds", state.focusRemainingSeconds)
            put("shortBreakRemainingSeconds", state.shortBreakRemainingSeconds)
            put("longBreakRemainingSeconds", state.longBreakRemainingSeconds)
            put("isRunning", state.isRunning)
            if (state.endsAtMillis != null) put("endsAtMillis", state.endsAtMillis)
        }
        prefs.edit().putString(KEY_POMODORO_STATE, json.toString()).apply()
    }

    fun toUiState(base: StandTimeUiState, state: PomodoroSavedState): StandTimeUiState = base.copy(
        selectedPomodoroMinutes = state.selectedMinutes,
        pomodoroPhase = state.viewedPhase,
        pomodoroActivePhase = state.activePhase,
        pomodoroCompletedFocusSessions = state.completedFocusSessions,
        pomodoroFocusRemainingSeconds = state.focusRemainingSeconds,
        pomodoroShortBreakRemainingSeconds = state.shortBreakRemainingSeconds,
        pomodoroLongBreakRemainingSeconds = state.longBreakRemainingSeconds,
        pomodoroRemainingSeconds = state.selectedPhaseRemainingSeconds,
        pomodoroEndsAtMillis = state.endsAtMillis,
        isPomodoroRunning = state.isRunning
    )

    fun fromUiState(state: StandTimeUiState): PomodoroSavedState = PomodoroSavedState(
        selectedMinutes = state.selectedPomodoroMinutes,
        viewedPhase = state.pomodoroPhase,
        activePhase = state.pomodoroActivePhase,
        completedFocusSessions = state.pomodoroCompletedFocusSessions,
        focusRemainingSeconds = state.pomodoroFocusRemainingSeconds,
        shortBreakRemainingSeconds = state.pomodoroShortBreakRemainingSeconds,
        longBreakRemainingSeconds = state.pomodoroLongBreakRemainingSeconds,
        selectedPhaseRemainingSeconds = state.pomodoroRemainingSeconds,
        endsAtMillis = state.pomodoroEndsAtMillis,
        isRunning = state.isPomodoroRunning
    )

    fun selectPreset(
        presets: List<PomodoroPreset>,
        selectedMinutes: Int
    ): PomodoroSavedState = defaultState(presets, selectedMinutes)

    fun selectViewedPhase(
        state: PomodoroSavedState,
        phase: PomodoroPhase
    ): PomodoroSavedState = state.copy(
        viewedPhase = phase,
        selectedPhaseRemainingSeconds = state.remainingForPhase(phase)
    )

    fun toggleForViewedPhase(
        state: PomodoroSavedState,
        presets: List<PomodoroPreset>,
        nowMillis: Long
    ): PomodoroSavedState {
        val synced = synchronize(state, presets, nowMillis)
        val viewedPhaseRunning = synced.isRunning && synced.activePhase == synced.viewedPhase
        return if (viewedPhaseRunning) {
            pause(synced, nowMillis)
        } else {
            startViewedPhase(synced, nowMillis)
        }
    }

    fun resetViewedPhase(
        state: PomodoroSavedState,
        presets: List<PomodoroPreset>
    ): PomodoroSavedState {
        val duration = durationSeconds(state.selectedMinutes, presets, state.viewedPhase)
        val resetState = state.withRemaining(state.viewedPhase, duration)
        return if (resetState.activePhase == resetState.viewedPhase) {
            resetState.copy(
                activePhase = null,
                isRunning = false,
                endsAtMillis = null,
                selectedPhaseRemainingSeconds = resetState.remainingForPhase(resetState.viewedPhase)
            )
        } else {
            resetState.copy(selectedPhaseRemainingSeconds = resetState.remainingForPhase(resetState.viewedPhase))
        }
    }

    fun skipViewedPhase(
        state: PomodoroSavedState,
        presets: List<PomodoroPreset>,
        nowMillis: Long
    ): PomodoroSavedState {
        val synced = synchronize(state, presets, nowMillis)
        return advance(
            state = synced.copy(
                activePhase = if (synced.activePhase == synced.viewedPhase) synced.viewedPhase else null,
                isRunning = synced.activePhase == synced.viewedPhase && synced.isRunning,
                endsAtMillis = if (synced.activePhase == synced.viewedPhase && synced.isRunning) synced.endsAtMillis else null
            ),
            presets = presets,
            keepRunning = false,
            completedAtMillis = nowMillis,
            sourcePhase = synced.viewedPhase
        )
    }

    fun synchronize(
        state: PomodoroSavedState,
        presets: List<PomodoroPreset>,
        nowMillis: Long
    ): PomodoroSavedState {
        var current = state
        if (!current.isRunning || current.activePhase == null || current.endsAtMillis == null) {
            return current.copy(selectedPhaseRemainingSeconds = current.remainingForPhase(current.viewedPhase))
        }

        while (current.isRunning && current.activePhase != null && current.endsAtMillis != null && current.endsAtMillis <= nowMillis) {
            current = advance(
                state = current,
                presets = presets,
                keepRunning = true,
                completedAtMillis = current.endsAtMillis,
                sourcePhase = current.activePhase
            )
        }

        if (current.isRunning && current.activePhase != null && current.endsAtMillis != null) {
            val remainingSeconds = (((current.endsAtMillis - nowMillis) + 999L) / 1000L)
                .coerceAtLeast(0L)
                .toInt()
            current = current.withRemaining(current.activePhase, remainingSeconds)
        }

        return current.copy(selectedPhaseRemainingSeconds = current.remainingForPhase(current.viewedPhase))
    }

    fun advance(
        state: PomodoroSavedState,
        presets: List<PomodoroPreset>,
        keepRunning: Boolean,
        completedAtMillis: Long,
        sourcePhase: PomodoroPhase? = state.activePhase ?: state.viewedPhase
    ): PomodoroSavedState {
        val finishedPhase = sourcePhase ?: PomodoroPhase.FOCUS
        val nextPhase = when (finishedPhase) {
            PomodoroPhase.FOCUS -> {
                if ((state.completedFocusSessions + 1) % 4 == 0) PomodoroPhase.LONG_BREAK
                else PomodoroPhase.SHORT_BREAK
            }

            PomodoroPhase.SHORT_BREAK,
            PomodoroPhase.LONG_BREAK -> PomodoroPhase.FOCUS
        }
        val nextCompletedFocusSessions = when (finishedPhase) {
            PomodoroPhase.FOCUS -> state.completedFocusSessions + 1
            PomodoroPhase.LONG_BREAK -> 0
            PomodoroPhase.SHORT_BREAK -> state.completedFocusSessions
        }
        val resetFinished = durationSeconds(state.selectedMinutes, presets, finishedPhase)
        val nextRemaining = durationSeconds(state.selectedMinutes, presets, nextPhase)
        val updated = state
            .withRemaining(finishedPhase, resetFinished)
            .withRemaining(nextPhase, nextRemaining)
            .copy(
                viewedPhase = if (state.viewedPhase == finishedPhase) nextPhase else state.viewedPhase,
                activePhase = if (keepRunning) nextPhase else null,
                completedFocusSessions = nextCompletedFocusSessions,
                endsAtMillis = if (keepRunning) completedAtMillis + nextRemaining * 1000L else null,
                isRunning = keepRunning
            )
        return updated.copy(selectedPhaseRemainingSeconds = updated.remainingForPhase(updated.viewedPhase))
    }

    fun durationSeconds(
        selectedMinutes: Int,
        presets: List<PomodoroPreset>,
        phase: PomodoroPhase
    ): Int {
        val preset = presets.firstOrNull { it.focusMinutes == selectedMinutes } ?: presets.first()
        return when (phase) {
            PomodoroPhase.FOCUS -> preset.focusMinutes * 60
            PomodoroPhase.SHORT_BREAK -> preset.shortBreakMinutes * 60
            PomodoroPhase.LONG_BREAK -> preset.longBreakMinutes * 60
        }
    }

    fun scheduleAlarm(context: Context, state: PomodoroSavedState) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val pendingIntent = buildPendingIntent(context)
        if (!state.isRunning || state.activePhase == null || state.endsAtMillis == null) {
            alarmManager.cancel(pendingIntent)
            return
        }

        alarmManager.cancel(pendingIntent)
        val triggerAtMillis = state.endsAtMillis
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        alarmManager.cancel(buildPendingIntent(context))
    }

    private fun startViewedPhase(
        state: PomodoroSavedState,
        nowMillis: Long
    ): PomodoroSavedState {
        val current = if (state.isRunning && state.activePhase != null && state.endsAtMillis != null) {
            val remaining = (((state.endsAtMillis - nowMillis) + 999L) / 1000L).coerceAtLeast(0L).toInt()
            state.withRemaining(state.activePhase, remaining)
        } else {
            state
        }
        val viewedRemaining = current.remainingForPhase(current.viewedPhase).coerceAtLeast(1)
        return current.copy(
            activePhase = current.viewedPhase,
            isRunning = true,
            endsAtMillis = nowMillis + viewedRemaining * 1000L,
            selectedPhaseRemainingSeconds = current.remainingForPhase(current.viewedPhase)
        )
    }

    private fun pause(
        state: PomodoroSavedState,
        nowMillis: Long
    ): PomodoroSavedState {
        val activePhase = state.activePhase ?: state.viewedPhase
        val remaining = (((state.endsAtMillis ?: nowMillis) - nowMillis) + 999L)
            .coerceAtLeast(0L)
            .div(1000L)
            .toInt()
        val updated = state.withRemaining(activePhase, remaining)
        return updated.copy(
            activePhase = null,
            isRunning = false,
            endsAtMillis = null,
            selectedPhaseRemainingSeconds = updated.remainingForPhase(updated.viewedPhase)
        )
    }

    private fun PomodoroSavedState.withRemaining(
        phase: PomodoroPhase,
        remainingSeconds: Int
    ): PomodoroSavedState {
        val clamped = remainingSeconds.coerceAtLeast(0)
        return when (phase) {
            PomodoroPhase.FOCUS -> copy(focusRemainingSeconds = clamped)
            PomodoroPhase.SHORT_BREAK -> copy(shortBreakRemainingSeconds = clamped)
            PomodoroPhase.LONG_BREAK -> copy(longBreakRemainingSeconds = clamped)
        }
    }

    fun PomodoroSavedState.remainingForPhase(phase: PomodoroPhase): Int = when (phase) {
        PomodoroPhase.FOCUS -> focusRemainingSeconds
        PomodoroPhase.SHORT_BREAK -> shortBreakRemainingSeconds
        PomodoroPhase.LONG_BREAK -> longBreakRemainingSeconds
    }

    private fun buildPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, PomodoroAlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
