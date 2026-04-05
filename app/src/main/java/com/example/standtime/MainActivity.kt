package com.example.standtime

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.standtime.standtime.StandTimeRoute
import com.example.standtime.standtime.StandTimeViewModel
import com.example.standtime.standtime.feature.utils.ThemeMode
import com.example.standtime.ui.theme.StandTimeTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<StandTimeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            var hasRequestedNotificationPermission by rememberSaveable {
                mutableStateOf(false)
            }
            var shouldOpenNotificationSettings by rememberSaveable {
                mutableStateOf(false)
            }
            val notificationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                hasRequestedNotificationPermission = true
                shouldOpenNotificationSettings =
                    !granted &&
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
            }
            val notificationPermissionNeeded =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED

            LaunchedEffect(Unit) {
                ChargingStandNotificationHelper.ensureChannels(this@MainActivity)
            }

            LaunchedEffect(uiState.enableChargingStandMode, notificationPermissionNeeded) {
                if (!uiState.enableChargingStandMode) {
                    hasRequestedNotificationPermission = false
                    shouldOpenNotificationSettings = false
                    ChargingStandMonitorService.stop(this@MainActivity)
                } else if (notificationPermissionNeeded && !hasRequestedNotificationPermission) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    ChargingStandMonitorService.start(this@MainActivity)
                } else {
                    ChargingStandMonitorService.start(this@MainActivity)
                }
            }

            LaunchedEffect(shouldOpenNotificationSettings) {
                if (!shouldOpenNotificationSettings) return@LaunchedEffect
                startActivity(
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                        data = "package:$packageName".toUri()
                    }
                )
                shouldOpenNotificationSettings = false
            }

            val keepScreenOn = uiState.enableChargingStandMode && uiState.isCharging
            DisposableEffect(keepScreenOn) {
                if (keepScreenOn) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
                onDispose {
                    if (keepScreenOn) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }
            }
            StandTimeTheme(darkTheme = uiState.themeMode == ThemeMode.DARK) {
                StandTimeRoute(
                    state = uiState,
                    onIntent = viewModel::onIntent
                )
            }
        }
    }
}
