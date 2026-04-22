package com.standtime.clock

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.standtime.clock.standtime.feature.PhysicsBubbleScreen
import com.standtime.clock.standtime.StandTimeRoute
import com.standtime.clock.standtime.feature.utils.StandTimeIntent
import com.standtime.clock.standtime.StandTimeViewModel
import com.standtime.clock.standtime.feature.utils.ThemeMode
import com.standtime.clock.ui.theme.StandTimeTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<StandTimeViewModel>()
    private val introPrefs by lazy {
        getSharedPreferences("stand_time_prefs", MODE_PRIVATE)
    }

    companion object {
        private const val KEY_HAS_SEEN_PHYSICS_BUBBLE_INTRO = "has_seen_physics_bubble_intro"
        private const val KEY_COMPLETED_INITIAL_PERMISSION_FLOW = "completed_initial_permission_flow"
    }

    override fun onStart() {
        super.onStart()
        AppVisibilityTracker.isAppVisible = true
    }

    override fun onStop() {
        AppVisibilityTracker.isAppVisible = false
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val sensorManager = remember { getSystemService(SensorManager::class.java) }
            val ambientLightSensor = remember(sensorManager) {
                sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)
            }
            val proximitySensor = remember(sensorManager) {
                sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            }
            val automationScope = rememberCoroutineScope()
            var ambientLux by remember { mutableStateOf<Float?>(null) }
            var waveRevealActive by rememberSaveable { mutableStateOf(true) }
            var waveHideJob by remember { mutableStateOf<Job?>(null) }
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
            DisposableEffect(
                shouldShowIntro,
                uiState.enableAutoDimNightMode,
                uiState.enableSleepColorFilter,
                uiState.enableWaveToWake,
                ambientLightSensor,
                proximitySensor
            ) {
                val needsLightSensor =
                    !shouldShowIntro && (uiState.enableAutoDimNightMode || uiState.enableSleepColorFilter)
                val needsProximitySensor = !shouldShowIntro && uiState.enableWaveToWake
                val listener = object : SensorEventListener {
                    var lastProximityNear = false

                    override fun onSensorChanged(event: SensorEvent) {
                        when (event.sensor.type) {
                            Sensor.TYPE_LIGHT -> {
                                ambientLux = event.values.firstOrNull()
                            }

                            Sensor.TYPE_PROXIMITY -> {
                                val value = event.values.firstOrNull() ?: return
                                val isNear = value < (proximitySensor?.maximumRange ?: value)
                                if (needsProximitySensor && isNear && !lastProximityNear) {
                                    waveRevealActive = true
                                    waveHideJob?.cancel()
                                    waveHideJob = automationScope.launch {
                                        delay(8_000)
                                        waveRevealActive = false
                                    }
                                }
                                lastProximityNear = isNear
                            }
                        }
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
                }

                if (needsLightSensor && ambientLightSensor != null) {
                    sensorManager?.registerListener(
                        listener,
                        ambientLightSensor,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                }
                if (needsProximitySensor && proximitySensor != null) {
                    sensorManager?.registerListener(
                        listener,
                        proximitySensor,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                }
                if (!needsProximitySensor) {
                    waveHideJob?.cancel()
                    waveRevealActive = true
                } else if (waveRevealActive) {
                    waveHideJob?.cancel()
                    waveHideJob = automationScope.launch {
                        delay(8_000)
                        waveRevealActive = false
                    }
                }

                onDispose {
                    sensorManager?.unregisterListener(listener)
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
                    PomodoroNotificationHelper.ensureChannel(this@MainActivity)
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
            val isDarkRoom = (ambientLux ?: Float.MAX_VALUE) <= 8f
            val targetBrightness = when {
                shouldShowIntro -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                !uiState.enableAutoDimNightMode -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                ambientLightSensor == null -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                ambientLux == null -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                ambientLux!! <= 2f -> 0.015f
                ambientLux!! <= 8f -> 0.03f
                ambientLux!! <= 18f -> 0.08f
                else -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            }
            DisposableEffect(targetBrightness) {
                window.attributes = window.attributes.apply {
                    screenBrightness = targetBrightness
                }
                onDispose {
                    window.attributes = window.attributes.apply {
                        screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                    }
                }
            }
            val sleepFilterActive =
                !shouldShowIntro && uiState.enableSleepColorFilter && ambientLightSensor != null && isDarkRoom
            StandTimeTheme(darkTheme = if (shouldShowIntro) true else uiState.themeMode == ThemeMode.DARK) {
                Box(modifier = Modifier.fillMaxSize()) {
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
                            onIntent = viewModel::onIntent,
                            waveToWakeRevealActive = !uiState.enableWaveToWake || waveRevealActive,
                            ambientLightSensorAvailable = ambientLightSensor != null,
                            proximitySensorAvailable = proximitySensor != null
                        )
                    }

                    if (sleepFilterActive) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0x66FF4D1A),
                                            Color(0x88C81D00)
                                        )
                                    )
                                )
                                .graphicsLayer(alpha = 0.82f)
                        )
                    }
                }
            }
        }
    }
}
