package com.example.standtime

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.standtime.standtime.feature.utils.PomodoroPhase

object PomodoroNotificationHelper {
    const val CHANNEL_ID = "pomodoro_alerts"
    const val NOTIFICATION_ID = 5202

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.pomodoro_notification_channel),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.pomodoro_notification_channel_desc)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 220, 120, 220)
                setSound(soundUri, audioAttributes)
            }
        )
    }

    fun notifyPhaseComplete(
        context: Context,
        finishedPhase: PomodoroPhase,
        nextPhase: PomodoroPhase
    ) {
        ensureChannel(context)
        vibrate(context)

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val openIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
        }
        val pendingIntent = android.app.PendingIntent.getActivity(
            context,
            0,
            openIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(context.getString(phaseFinishedTitleRes(finishedPhase)))
            .setContentText(context.getString(R.string.pomodoro_notification_body, context.getString(phaseLabelRes(nextPhase))))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(context.getString(R.string.pomodoro_notification_body, context.getString(phaseLabelRes(nextPhase))))
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun vibrate(context: Context) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Vibrator::class.java)
        } ?: return

        if (!vibrator.hasVibrator()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 220, 120, 220), -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 220, 120, 220), -1)
        }
    }

    private fun phaseFinishedTitleRes(phase: PomodoroPhase): Int = when (phase) {
        PomodoroPhase.FOCUS -> R.string.pomodoro_focus_complete
        PomodoroPhase.SHORT_BREAK -> R.string.pomodoro_short_break_complete
        PomodoroPhase.LONG_BREAK -> R.string.pomodoro_long_break_complete
    }

    private fun phaseLabelRes(phase: PomodoroPhase): Int = when (phase) {
        PomodoroPhase.FOCUS -> R.string.pomodoro_focus
        PomodoroPhase.SHORT_BREAK -> R.string.pomodoro_short_break
        PomodoroPhase.LONG_BREAK -> R.string.pomodoro_long_break
    }
}
