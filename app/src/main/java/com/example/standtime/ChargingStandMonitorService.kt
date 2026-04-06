package com.example.standtime

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat

class ChargingStandMonitorService : Service() {
    private var receiverRegistered = false
    private var isCurrentlyCharging = false

    private val powerReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_POWER_CONNECTED -> {
                        if (!isCurrentlyCharging) {
                            isCurrentlyCharging = true
                            postChargingReadyNotification()
                        }
                    }

                    Intent.ACTION_POWER_DISCONNECTED -> {
                        isCurrentlyCharging = false
                    }
                }
            }
        }

    override fun onCreate() {
        super.onCreate()
        ChargingStandNotificationHelper.ensureChannels(this)
        isCurrentlyCharging = isDeviceCharging()
        startInForeground()
        registerPowerReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isStandModeEnabled()) {
            stopSelf()
            return START_NOT_STICKY
        }

        return START_STICKY
    }

    override fun onDestroy() {
        if (receiverRegistered) {
            unregisterReceiver(powerReceiver)
            receiverRegistered = false
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startInForeground() {
        val openAppPendingIntent = buildOpenAppPendingIntent()
        val notification = NotificationCompat.Builder(
            this,
            ChargingStandNotificationHelper.MONITOR_CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .setContentTitle(getString(R.string.charging_stand_monitor_notification_title))
            .setContentText(getString(R.string.charging_stand_monitor_notification_body))
            .setOngoing(true)
            .setAutoCancel(false)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openAppPendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceCompat.startForeground(
                this,
                ChargingStandNotificationHelper.MONITOR_NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(ChargingStandNotificationHelper.MONITOR_NOTIFICATION_ID, notification)
        }
    }

    private fun registerPowerReceiver() {
        if (receiverRegistered) return
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        ContextCompat.registerReceiver(
            this,
            powerReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        receiverRegistered = true
    }

    private fun postChargingReadyNotification() {
        if (AppVisibilityTracker.isAppVisible) return

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val openAppPendingIntent = buildOpenAppPendingIntent()
        val notification = NotificationCompat.Builder(
            this,
            ChargingStandNotificationHelper.ALERT_CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .setContentTitle(getString(R.string.charging_stand_notification_title))
            .setContentText(getString(R.string.charging_stand_notification_body))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(getString(R.string.charging_stand_notification_body))
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                0,
                getString(R.string.charging_stand_notification_action),
                openAppPendingIntent
            )
            .build()

        NotificationManagerCompat.from(this).notify(
            ChargingStandNotificationHelper.ALERT_NOTIFICATION_ID,
            notification
        )
    }

    private fun buildOpenAppPendingIntent(): PendingIntent {
        val launchIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
        }
        return PendingIntent.getActivity(
            this,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun isStandModeEnabled(): Boolean =
        getSharedPreferences("stand_time_prefs", Context.MODE_PRIVATE)
            .getBoolean("enable_charging_stand_mode", false)

    private fun isDeviceCharging(): Boolean {
        val batteryIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: return false
        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
            status == BatteryManager.BATTERY_STATUS_FULL
    }

    companion object {
        fun start(context: Context) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, ChargingStandMonitorService::class.java)
            )
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, ChargingStandMonitorService::class.java))
        }
    }
}
