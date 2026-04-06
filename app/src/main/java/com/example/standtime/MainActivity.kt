package com.example.standtime

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
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
import com.example.standtime.standtime.feature.PhysicsBubbleScreen
import com.example.standtime.standtime.StandTimeRoute
import com.example.standtime.standtime.feature.utils.StandTimeIntent
import com.example.standtime.standtime.StandTimeViewModel
import com.example.standtime.standtime.feature.utils.ThemeMode
import com.example.standtime.ui.theme.StandTimeTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<StandTimeViewModel>()
    private val introPrefs by lazy {
        getSharedPreferences("stand_time_prefs", MODE_PRIVATE)
    }

    companion object {
        private const val KEY_HAS_SEEN_PHYSICS_BUBBLE_INTRO = "has_seen_physics_bubble_intro"
        private const val KEY_COMPLETED_INITIAL_PERMISSION_FLOW = "completed_initial_permission_flow"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            var shouldShowIntro by rememberSaveable {
                mutableStateOf(
                    !introPrefs.getBoolean(KEY_HAS_SEEN_PHYSICS_BUBBLE_INTRO, false)
                )
            }
            var shouldRunInitialPermissionFlow by rememberSaveable {
                mutableStateOf(
                    !introPrefs.getBoolean(KEY_COMPLETED_INITIAL_PERMISSION_FLOW, false)
                )
            }
            DisposableEffect(shouldShowIntro) {
                requestedOrientation = if (shouldShowIntro) {
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }
                onDispose { }
            }
            DisposableEffect(Unit) {
                AppVisibilityTracker.isAppVisible = true
                onDispose {
                    AppVisibilityTracker.isAppVisible = false
                }
            }
            var hasRequestedNotificationPermission by rememberSaveable {
                mutableStateOf(false)
            }
            var shouldOpenNotificationSettings by rememberSaveable {
                mutableStateOf(false)
            }
            var pendingNotificationAfterLocation by rememberSaveable {
                mutableStateOf(false)
            }
            val notificationPermissionNeeded =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED

            val locationPermissionNeeded =
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            val completeInitialPermissionFlow = {
                introPrefs.edit().putBoolean(KEY_COMPLETED_INITIAL_PERMISSION_FLOW, true).apply()
                shouldRunInitialPermissionFlow = false
            }
            val locationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { grantResults ->
                val granted = grantResults.values.any { it }
                viewModel.onIntent(StandTimeIntent.LocationPermissionChanged(granted))
                if (granted) {
                    viewModel.onIntent(StandTimeIntent.RefreshWeather)
                }
                if (notificationPermissionNeeded) {
                    pendingNotificationAfterLocation = true
                } else {
                    if (!uiState.enableChargingStandMode) {
                        viewModel.onIntent(StandTimeIntent.SetChargingStandMode(true))
                    }
                    completeInitialPermissionFlow()
                }
            }
            val notificationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                hasRequestedNotificationPermission = true
                shouldOpenNotificationSettings =
                    !granted &&
                        !shouldRunInitialPermissionFlow &&
                        uiState.enableChargingStandMode &&
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
                if (granted && !uiState.enableChargingStandMode) {
                    viewModel.onIntent(StandTimeIntent.SetChargingStandMode(true))
                }
                if (shouldRunInitialPermissionFlow) {
                    completeInitialPermissionFlow()
                }
            }

            LaunchedEffect(pendingNotificationAfterLocation, shouldShowIntro) {
                if (pendingNotificationAfterLocation && !shouldShowIntro) {
                    pendingNotificationAfterLocation = false
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            if (!shouldShowIntro) {
                LaunchedEffect(shouldRunInitialPermissionFlow, notificationPermissionNeeded, locationPermissionNeeded) {
                    if (!shouldRunInitialPermissionFlow) return@LaunchedEffect
                    if (locationPermissionNeeded) {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )
                    } else if (notificationPermissionNeeded) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        if (!uiState.enableChargingStandMode) {
                            viewModel.onIntent(StandTimeIntent.SetChargingStandMode(true))
                        }
                        completeInitialPermissionFlow()
                    }
                }

                LaunchedEffect(Unit) {
                    ChargingStandNotificationHelper.ensureChannels(this@MainActivity)
                }

                LaunchedEffect(uiState.enableChargingStandMode, notificationPermissionNeeded, shouldRunInitialPermissionFlow) {
                    if (shouldRunInitialPermissionFlow) return@LaunchedEffect
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
            StandTimeTheme(darkTheme = if (shouldShowIntro) true else uiState.themeMode == ThemeMode.DARK) {
                if (shouldShowIntro) {
                    PhysicsBubbleScreen(
                        language = uiState.language,
                        onLanguageChange = { viewModel.onIntent(StandTimeIntent.ChangeLanguage(it)) },
                        onContinue = {
                            introPrefs.edit()
                                .putBoolean(KEY_HAS_SEEN_PHYSICS_BUBBLE_INTRO, true)
                                .apply()
                            shouldShowIntro = false
                        }
                    )
                } else {
                    StandTimeRoute(
                        state = uiState,
                        onIntent = viewModel::onIntent
                    )
                }
            }
        }
    }
}
