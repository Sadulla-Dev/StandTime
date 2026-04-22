package com.standtime.clock

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object ChargingStandNotificationHelper {
    const val ALERT_CHANNEL_ID = "charging_stand_mode_alerts"
    const val MONITOR_CHANNEL_ID = "charging_stand_mode_monitor"
    const val ALERT_NOTIFICATION_ID = 4102
    const val MONITOR_NOTIFICATION_ID = 4101

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        if (manager.getNotificationChannel(ALERT_CHANNEL_ID) == null) {
            manager.createNotificationChannel(
                NotificationChannel(
                    ALERT_CHANNEL_ID,
                    context.getString(R.string.charging_stand_notification_channel),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description =
                        context.getString(R.string.charging_stand_notification_channel_desc)
                }
            )
        }
        if (manager.getNotificationChannel(MONITOR_CHANNEL_ID) == null) {
            manager.createNotificationChannel(
                NotificationChannel(
                    MONITOR_CHANNEL_ID,
                    context.getString(R.string.charging_stand_monitor_channel),
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description =
                        context.getString(R.string.charging_stand_monitor_channel_desc)
                }
            )
        }
    }
}
