package com.example.standtime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.standtime.standtime.feature.utils.PomodoroStateStore
import com.example.standtime.standtime.feature.utils.PomodoroPhase
import com.example.standtime.standtime.feature.utils.PomodoroPreset

class PomodoroAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (AppVisibilityTracker.isAppVisible) return

        val prefs = context.getSharedPreferences("stand_time_prefs", Context.MODE_PRIVATE)
        val presets = defaultPomodoroPresets()
        val current = PomodoroStateStore.load(prefs, presets)
        if (!current.isRunning || current.activePhase == null || current.endsAtMillis == null) return

        val now = System.currentTimeMillis()
        if (current.endsAtMillis > now) {
            PomodoroStateStore.scheduleAlarm(context, current)
            return
        }

        val finishedPhase = current.activePhase
        val advanced = PomodoroStateStore.advance(
            state = current,
            presets = presets,
            keepRunning = true,
            completedAtMillis = current.endsAtMillis,
            sourcePhase = current.activePhase
        )
        PomodoroStateStore.save(prefs, advanced)
        PomodoroStateStore.scheduleAlarm(context, advanced)
        PomodoroNotificationHelper.notifyPhaseComplete(
            context = context,
            finishedPhase = finishedPhase,
            nextPhase = advanced.activePhase ?: PomodoroPhase.FOCUS
        )
    }

    private fun defaultPomodoroPresets(): List<PomodoroPreset> = listOf(
        PomodoroPreset(15, 5, 12, "15"),
        PomodoroPreset(25, 5, 15, "25"),
        PomodoroPreset(50, 10, 20, "50")
    )
}
