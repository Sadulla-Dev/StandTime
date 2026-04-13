package com.example.standtime.standtime

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import com.example.standtime.R
import com.example.standtime.standtime.feature.CustomClockStudioPage
import com.example.standtime.standtime.feature.components.GalleryClockContent
import com.example.standtime.standtime.feature.components.accentColor
import com.example.standtime.standtime.feature.components.galleryParts
import com.example.standtime.standtime.feature.components.galleryStyleAt
import com.example.standtime.standtime.feature.components.galleryStyleCount
import com.example.standtime.standtime.feature.components.galleryStyles
import com.example.standtime.standtime.feature.components.isViewedPomodoroRunning
import com.example.standtime.standtime.feature.components.pomodoroProgress
import com.example.standtime.standtime.feature.components.pomodoroTotalSeconds
import com.example.standtime.standtime.feature.components.remainingPomodoroText
import com.example.standtime.standtime.feature.utils.AccentPalette
import com.example.standtime.standtime.feature.utils.CalendarDayCell
import com.example.standtime.standtime.feature.utils.PomodoroPhase
import com.example.standtime.standtime.feature.utils.StandTimeIntent
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.StandTimeUiState
import com.example.standtime.standtime.feature.utils.ThemeMode
import com.example.standtime.standtime.feature.utils.localizedStringResource
import com.example.standtime.ui.theme.CoralAccent
import com.example.standtime.ui.theme.LimeAccent
import com.example.standtime.ui.theme.SkyAccent
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// Root
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun StandTimeRoute(
    state: StandTimeUiState,
    onIntent: (StandTimeIntent) -> Unit,
    waveToWakeRevealActive: Boolean,
    ambientLightSensorAvailable: Boolean,
    proximitySensorAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    val language = state.language
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val accentColor = state.accentColor()
    var isCustomStudioOpen by rememberSaveable { mutableStateOf(false) }
    var showCustomCreateChoice by rememberSaveable { mutableStateOf(false) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grantResults ->
        val granted = grantResults.values.any { it }
        onIntent(StandTimeIntent.LocationPermissionChanged(granted))
        if (granted) onIntent(StandTimeIntent.RefreshWeather)
    }
    val requestLocationPermission = remember(locationPermissionLauncher, onIntent) {
        {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    val background = Brush.radialGradient(
        colors = listOf(
            accentColor.copy(alpha = if (state.themeMode == ThemeMode.DARK) 0.28f else 0.16f),
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surface
        )
    )

    val rootPagerState = rememberPagerState(pageCount = { 3 })
    val isChargingStandModeActive = state.enableChargingStandMode && state.isCharging

    LaunchedEffect(isChargingStandModeActive, isCustomStudioOpen) {
        if (isChargingStandModeActive && !isCustomStudioOpen && rootPagerState.currentPage != 0) {
            rootPagerState.animateScrollToPage(0)
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (showCustomCreateChoice) {
            val targetCustomStyle =
                state.savedCustomClockStyles.getOrNull(state.selectedGalleryStyleIndex - galleryStyles.size)
                    ?: state.savedCustomClockStyles.lastOrNull()
            AlertDialog(onDismissRequest = { showCustomCreateChoice = false }, title = {
                Text(
                    localizedStringResource(
                        R.string.custom_create_dialog_title, language
                    )
                )
            }, text = {
                Text(
                    localizedStringResource(
                        R.string.custom_create_dialog_message, language
                    )
                )
            }, confirmButton = {
                TextButton(
                    onClick = {
                        targetCustomStyle?.let {
                            onIntent(StandTimeIntent.EditSavedCustomClockStyle(it.id))
                        }
                        showCustomCreateChoice = false
                        isCustomStudioOpen = true
                    }) {
                    Text(localizedStringResource(R.string.custom_edit_existing, language))
                }
            }, dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = {
                            onIntent(StandTimeIntent.StartNewCustomClockStyle)
                            showCustomCreateChoice = false
                            isCustomStudioOpen = true
                        }) {
                        Text(localizedStringResource(R.string.custom_create_new, language))
                    }
                    TextButton(onClick = { showCustomCreateChoice = false }) {
                        Text(localizedStringResource(R.string.custom_cancel, language))
                    }
                }
            })
        }

        if (isCustomStudioOpen) {
            CustomClockStudioPage(
                state = state,
                language = language,
                accentColor = accentColor,
                onIntent = onIntent,
                onClose = { isCustomStudioOpen = false },
                onSave = {
                    onIntent(StandTimeIntent.SaveCustomClockStyle)
                    isCustomStudioOpen = false
                })
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
            ) {
                HorizontalPager(
                    state = rootPagerState, modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> ClockStylesPage(
                            state = state,
                            language = language,
                            accentColor = accentColor,
                            onIntent = onIntent,
                            waveToWakeRevealActive = waveToWakeRevealActive,
                            onOpenCustomStudio = {
                                if (state.savedCustomClockStyles.isNotEmpty()) {
                                    showCustomCreateChoice = true
                                } else {
                                    onIntent(StandTimeIntent.StartNewCustomClockStyle)
                                    isCustomStudioOpen = true
                                }
                            })

                        1 -> DashboardPage(
                            state = state,
                            language = language,
                            accentColor = accentColor,
                            onIntent = onIntent,
                            onRequestLocationPermission = requestLocationPermission,
                            isLandscape = isLandscape
                        )

                        else -> SetupPage(
                            state = state,
                            language = language,
                            accentColor = accentColor,
                            onIntent = onIntent,
                            onRequestLocationPermission = requestLocationPermission,
                            ambientLightSensorAvailable = ambientLightSensorAvailable,
                            proximitySensorAvailable = proximitySensorAvailable
                        )
                    }
                }

            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Page 0 — Clock Styles Gallery (full‑screen vertical pager)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ClockStylesPage(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    waveToWakeRevealActive: Boolean,
    onOpenCustomStudio: () -> Unit
) {
    val stylesCount = galleryStyleCount(state.savedCustomClockStyles)
    val lastStyleIndex = (stylesCount - 1).coerceAtLeast(0)
    val parts = state.galleryParts()
    val scope = rememberCoroutineScope()
    var pendingDeleteStyleId by rememberSaveable { mutableStateOf<String?>(null) }
    var overlaysVisible by rememberSaveable { mutableStateOf(true) }
    var overlayAutoHideToken by rememberSaveable { mutableStateOf(0) }
    val galleryPagerState = rememberPagerState(
        initialPage = state.selectedGalleryStyleIndex.coerceIn(0, lastStyleIndex),
        pageCount = { stylesCount })
    val currentIndex = galleryPagerState.currentPage.coerceIn(0, lastStyleIndex)
    val currentStyle = galleryStyleAt(currentIndex, state.savedCustomClockStyles)
    val styleName = currentStyle.label ?: localizedStringResource(
        currentStyle.nameRes ?: R.string.gallery_style_custom, language
    )
    val hasGalleryBottomOverlay = state.enableTapRevealInfo && (
        state.mediaSessionAvailable ||
            listOf(state.dayText, state.dateText).any { it.isNotBlank() } ||
            (state.showWeather && listOf(state.weatherTemperature, state.weatherSummary).any { it.isNotBlank() }) ||
            state.showBattery
        )
    val overlayAlpha by animateFloatAsState(
        targetValue = if (overlaysVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 850),
        label = "galleryOverlayAlpha"
    )
    val waveMaskAlpha by animateFloatAsState(
        targetValue = if (state.enableWaveToWake && !waveToWakeRevealActive) 1f else 0f,
        animationSpec = tween(durationMillis = 450),
        label = "waveMaskAlpha"
    )
    val latestOverlayVisibility by rememberUpdatedState(overlaysVisible)

    LaunchedEffect(stylesCount, galleryPagerState.currentPage) {
        if (galleryPagerState.currentPage > lastStyleIndex) {
            galleryPagerState.scrollToPage(lastStyleIndex)
        }
    }

    // Persist selected style to state
    LaunchedEffect(currentIndex) {
        onIntent(StandTimeIntent.ChangeGalleryStyleIndex(currentIndex))
        overlaysVisible = true
        overlayAutoHideToken++
    }

    LaunchedEffect(overlayAutoHideToken) {
        if (overlayAutoHideToken == 0) return@LaunchedEffect
        kotlinx.coroutines.delay(5000)
        if (latestOverlayVisibility) {
            overlaysVisible = false
        }
    }

    val customStyleToDelete =
        state.savedCustomClockStyles.firstOrNull { it.id == pendingDeleteStyleId }
    if (customStyleToDelete != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteStyleId = null },
            title = { Text(localizedStringResource(R.string.custom_delete_title, language)) },
            text = {
                Text(
                    localizedStringResource(
                        R.string.custom_delete_message, language, customStyleToDelete.name
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onIntent(StandTimeIntent.DeleteSavedCustomClockStyle(customStyleToDelete.id))
                        pendingDeleteStyleId = null
                    }) {
                    Text(localizedStringResource(R.string.custom_delete_confirm, language))
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteStyleId = null }) {
                    Text(localizedStringResource(R.string.custom_cancel, language))
                }
            })
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Full‑screen vertical pager — each page IS the clock style
        VerticalPager(
            state = galleryPagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(modifier = Modifier
                .fillMaxSize()
                .combinedClickable(onClick = {
                    if (overlaysVisible) {
                        overlaysVisible = false
                    } else {
                        overlaysVisible = true
                        overlayAutoHideToken++
                    }
                }, onLongClick = {
                    state.savedCustomClockStyles.getOrNull(page - galleryStyles.size)
                        ?.let { pendingDeleteStyleId = it.id }
                })) {
                GalleryClockContent(
                    index = page,
                    parts = parts,
                    language = language,
                    accentColor = accentColor,
                    customStyles = state.savedCustomClockStyles,
                    burnInProtectionEnabled = state.enableBurnInProtection,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .fillMaxWidth()
                .graphicsLayer { alpha = overlayAlpha },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                text = localizedStringResource(
                    R.string.gallery_style_counter,
                    language,
                    styleName,
                    currentIndex + 1,
                    stylesCount
                ),
                modifier = Modifier
                    .galleryOverlaySurface(RoundedCornerShape(999.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp,
                color = GalleryOverlayContentColor
            )
        }

        if (state.enableTapRevealInfo) {
            GalleryBottomOverlay(
                state = state,
                language = language,
                overlayAlpha = overlayAlpha,
                onIntent = onIntent,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, end = 96.dp, bottom = 26.dp)
                    .fillMaxWidth(0.74f)
            )
        }

//        Box(
//            modifier = Modifier
//                .align(Alignment.CenterStart)
//                .fillMaxHeight()
//                .padding(
//                    start = 16.dp,
//                    top = 24.dp,
//                    bottom = if (hasGalleryBottomOverlay) 118.dp else 24.dp
//                )
//                .graphicsLayer { alpha = overlayAlpha },
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                modifier = Modifier.verticalScroll(rememberScrollState()),
//                verticalArrangement = Arrangement.spacedBy(7.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                repeat(stylesCount) { index ->
//                    Box(
//                        modifier = Modifier
//                            .width(if (index == currentIndex) 8.dp else 6.dp)
//                            .height(if (index == currentIndex) 24.dp else 8.dp)
//                            .clip(RoundedCornerShape(50))
//                            .background(
//                                GalleryOverlayContentColor.copy(
//                                    alpha = if (index == currentIndex) 0.95f else 0.25f
//                                )
//                            )
//                            .combinedClickable(
//                                onClick = {
//                                    if (galleryPagerState.currentPage != index) {
//                                        scope.launch {
//                                            galleryPagerState.animateScrollToPage(index)
//                                        }
//                                    } else {
//                                        overlaysVisible = !overlaysVisible
//                                        if (overlaysVisible) {
//                                            overlayAutoHideToken++
//                                        }
//                                    }
//                                }
//                            )
//                    )
//                }
//            }
//        }

        Button(
            onClick = onOpenCustomStudio,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = GalleryOverlayChipColor, contentColor = GalleryOverlayContentColor
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 26.dp)
                .graphicsLayer { alpha = overlayAlpha }
                .galleryOverlaySurface(CircleShape)) {
            Text(
                text = "+", fontSize = 25.sp, fontFamily = FontFamily.Monospace
            )
        }

        if (waveMaskAlpha > 0.01f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .graphicsLayer { alpha = waveMaskAlpha }
            )
        }
    }
}

private val GalleryOverlayChipColor = Color.Black.copy(alpha = 0.28f)
private val GalleryOverlayContentColor = Color.White

@Composable
private fun GalleryInfoChip(text: String) {
    if (text.isBlank()) return
    Text(
        text = text,
        modifier = Modifier
            .galleryOverlaySurface(RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        letterSpacing = 0.4.sp,
        color = GalleryOverlayContentColor
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GalleryBottomOverlay(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    overlayAlpha: Float,
    onIntent: (StandTimeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateInfo = listOf(state.dayText, state.dateText)
        .filter { it.isNotBlank() }
        .joinToString("  •  ")
    val weatherInfo = listOf(state.weatherTemperature, state.weatherSummary)
        .filter { it.isNotBlank() }
        .joinToString("  •  ")
    val batteryInfo = if (state.showBattery) {
        localizedStringResource(
            if (state.isCharging) R.string.gallery_info_charging_value else R.string.gallery_info_battery_value,
            language,
            state.batteryLevel
        )
    } else {
        ""
    }

    val hasMetaItems = dateInfo.isNotBlank() ||
        (state.showWeather && weatherInfo.isNotBlank()) ||
        batteryInfo.isNotBlank()
    if (!hasMetaItems && !state.mediaSessionAvailable) return

    Column(
        modifier = modifier
            .widthIn(max = 520.dp)
            .graphicsLayer { alpha = overlayAlpha },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (hasMetaItems) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GalleryInfoChip(dateInfo)
                if (state.showWeather) {
                    GalleryInfoChip(weatherInfo)
                }
                GalleryInfoChip(batteryInfo)
            }
        }

        if (state.mediaSessionAvailable) {
            GalleryMediaChip(
                state = state,
                language = language,
                onIntent = onIntent,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun GalleryMediaChip(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    onIntent: (StandTimeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = state.mediaTitle.ifBlank {
        localizedStringResource(R.string.media_now_playing_fallback, language)
    }
    val subtitle = state.mediaSubtitle.ifBlank {
        state.mediaAppLabel.ifBlank { state.mediaAppName }
    }
    Row(
        modifier = modifier
            .galleryOverlaySurface(RoundedCornerShape(999.dp))
            .padding(start = 10.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.GraphicEq,
                contentDescription = null,
                tint = GalleryOverlayContentColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = GalleryOverlayContentColor
            )
            Text(
                text = subtitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                fontSize = 9.sp,
                color = GalleryOverlayContentColor.copy(alpha = 0.72f)
            )
        }
        IconButton(
            onClick = { onIntent(StandTimeIntent.ToggleMediaPlayback) },
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        ) {
            Icon(
                imageVector = if (state.isMediaPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = localizedStringResource(
                    if (state.isMediaPlaying) R.string.media_pause else R.string.media_play,
                    language
                ),
                tint = GalleryOverlayContentColor,
                modifier = Modifier.size(18.dp)
            )
        }
        IconButton(
            onClick = { onIntent(StandTimeIntent.SkipToNextTrack) },
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        ) {
            Icon(
                imageVector = Icons.Rounded.SkipNext,
                contentDescription = localizedStringResource(R.string.media_next, language),
                tint = GalleryOverlayContentColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun Modifier.galleryOverlaySurface(shape: Shape): Modifier = this
    .shadow(
        elevation = 16.dp,
        shape = shape,
        ambientColor = Color.Black.copy(alpha = 0.26f),
        spotColor = Color.Black.copy(alpha = 0.20f)
    )
    .border(
        width = 1.dp, color = Color.White.copy(alpha = 0.08f), shape = shape
    )
    .clip(shape)
    .background(GalleryOverlayChipColor)

// ─────────────────────────────────────────────────────────────────────────────
// Page 1 — Dashboard
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DashboardPage(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    onRequestLocationPermission: () -> Unit,
    isLandscape: Boolean
) {
    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left: gallery clock panel (same styles as page 0)
            GalleryClockPanel(
                state = state,
                language = language,
                accentColor = accentColor,
                onIntent = onIntent,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            // Right: swipeable info panels
            InfoPanelStack(
                state = state,
                language = language,
                accentColor = accentColor,
                onIntent = onIntent,
                onRequestLocationPermission = onRequestLocationPermission,
                modifier = Modifier
                    .weight(1.05f)
                    .fillMaxHeight()
            )
        }
    } else {
        PortraitDashboard(
            state = state,
            language = language,
            accentColor = accentColor,
            onIntent = onIntent,
            onRequestLocationPermission = onRequestLocationPermission,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        )
    }
}

// ── Gallery clock panel for Dashboard ────────────────────────────────────────

/**
 * Shows the same gallery clock styles as page 0 inside a rounded card.
 * User swipes vertically to browse & implicitly select a style.
 * The clock's own background fills the card — no extra tinting applied.
 */
@Composable
private fun GalleryClockPanel(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val stylesCount = galleryStyleCount(state.savedCustomClockStyles)
    val lastStyleIndex = (stylesCount - 1).coerceAtLeast(0)
    val parts = state.galleryParts()
    // Keep pager in sync with the selected style from page 0
    val initialPage = state.selectedGalleryStyleIndex.coerceIn(0, lastStyleIndex)
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { stylesCount })
    val currentIndex = pagerState.currentPage.coerceIn(0, lastStyleIndex)
    val currentStyle = galleryStyleAt(currentIndex, state.savedCustomClockStyles)

    LaunchedEffect(stylesCount, pagerState.currentPage) {
        if (pagerState.currentPage > lastStyleIndex) {
            pagerState.scrollToPage(lastStyleIndex)
        }
    }

    LaunchedEffect(currentIndex) {
        onIntent(StandTimeIntent.ChangeGalleryStyleIndex(currentIndex))
    }

    Box(
        modifier = modifier.clip(RoundedCornerShape(28.dp))
    ) {
        VerticalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                GalleryClockContent(
                    index = page,
                    parts = parts,
                    language = language,
                    accentColor = accentColor,
                    customStyles = state.savedCustomClockStyles,
                    burnInProtectionEnabled = state.enableBurnInProtection,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Subtle style name label at bottom
        Text(
            text = currentStyle.label ?: localizedStringResource(
                currentStyle.nameRes ?: R.string.gallery_style_custom, language
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(999.dp))
                .background(Color.Black.copy(alpha = 0.28f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            color = Color.White
        )
    }
}

// ── Info panels (calendar / weather / pomodoro / media) ──────────────────────

@Composable
private fun InfoPanelStack(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    onRequestLocationPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = buildList<DashboardPanel> {
        if (state.showCalendar) add(DashboardPanel.Calendar)
        if (state.showPomodoro) add(DashboardPanel.Pomodoro)
        if (state.showWeather) add(DashboardPanel.Weather)
        add(DashboardPanel.Media)
    }
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 6.dp),
            pageSpacing = 14.dp
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                when (pages[page]) {
                    DashboardPanel.Calendar -> CalendarCard(state, language, accentColor)
                    DashboardPanel.Weather -> WeatherCard(
                        state, language, accentColor, onIntent, onRequestLocationPermission
                    )

                    DashboardPanel.Pomodoro -> PomodoroCard(state, language, accentColor, onIntent)
                    DashboardPanel.Media -> MediaCard(state, language, accentColor, onIntent)
                }
            }
        }
    }
}

// ── Portrait fallback for dashboard ──────────────────────────────────────────

@Composable
private fun PortraitDashboard(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    onRequestLocationPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val adaptiveClockHeight = (screenHeight * 0.34f).coerceIn(220.dp, 380.dp)

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Gallery clock card with adaptive height for different phone sizes.
        GalleryClockPanel(
            state = state,
            language = language,
            accentColor = accentColor,
            onIntent = onIntent,
            modifier = Modifier
                .fillMaxWidth()
                .height(adaptiveClockHeight)
        )

        CalendarCard(state, language, accentColor)

        if (state.showPomodoro) {
            PomodoroCard(state, language, accentColor, onIntent)
        }
        if (state.showWeather) {
            WeatherCard(state, language, accentColor, onIntent, onRequestLocationPermission)
        }
        MediaCard(state, language, accentColor, onIntent)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Page 2 — Setup / Settings
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SetupPage(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    onRequestLocationPermission: () -> Unit,
    ambientLightSensorAvailable: Boolean,
    proximitySensorAvailable: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingsCard(
            state = state,
            language = language,
            accentColor = accentColor,
            onIntent = onIntent,
            onRequestLocationPermission = onRequestLocationPermission,
            ambientLightSensorAvailable = ambientLightSensorAvailable,
            proximitySensorAvailable = proximitySensorAvailable
        )
    }
}

@Composable
private fun SettingsCard(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    onRequestLocationPermission: () -> Unit,
    ambientLightSensorAvailable: Boolean,
    proximitySensorAvailable: Boolean
) {
    PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

            // ── Language ──────────────────────────────────────────────────
            SettingSection(
                title = localizedStringResource(R.string.language_title, language)
            ) {
                ChipRow {
                    LanguageChip(
                        selected = state.language == StandTimeLanguage.ENGLISH,
                        label = "English",
                        onClick = { onIntent(StandTimeIntent.ChangeLanguage(StandTimeLanguage.ENGLISH)) })
                    LanguageChip(
                        selected = state.language == StandTimeLanguage.UZBEK,
                        label = "O'zbek",
                        onClick = { onIntent(StandTimeIntent.ChangeLanguage(StandTimeLanguage.UZBEK)) })
                    LanguageChip(
                        selected = state.language == StandTimeLanguage.RUSSIAN,
                        label = "Русский",
                        onClick = { onIntent(StandTimeIntent.ChangeLanguage(StandTimeLanguage.RUSSIAN)) })
                }
            }

            // ── Theme ─────────────────────────────────────────────────────
            SettingSection(
                title = localizedStringResource(R.string.theme_title, language)
            ) {
                ChipRow {
                    FilterChip(
                        selected = state.themeMode == ThemeMode.DARK,
                        onClick = { if (state.themeMode != ThemeMode.DARK) onIntent(StandTimeIntent.ToggleTheme) },
                        label = {
                            Text(
                                localizedStringResource(
                                    R.string.dark_theme_label, language
                                )
                            )
                        })
                    FilterChip(
                        selected = state.themeMode == ThemeMode.LIGHT,
                        onClick = { if (state.themeMode != ThemeMode.LIGHT) onIntent(StandTimeIntent.ToggleTheme) },
                        label = {
                            Text(
                                localizedStringResource(
                                    R.string.light_theme_label, language
                                )
                            )
                        })
                }
            }

            // ── Accent colour ─────────────────────────────────────────────
            SettingSection(
                title = localizedStringResource(R.string.accent_title, language)
            ) {
                ChipRow {
                    AccentChip(
                        label = localizedStringResource(R.string.accent_lime_label, language),
                        color = LimeAccent,
                        selected = state.accentPalette == AccentPalette.LIME,
                        onClick = { onIntent(StandTimeIntent.ChangeAccent(AccentPalette.LIME)) })
                    AccentChip(
                        label = localizedStringResource(R.string.accent_sky_label, language),
                        color = SkyAccent,
                        selected = state.accentPalette == AccentPalette.SKY,
                        onClick = { onIntent(StandTimeIntent.ChangeAccent(AccentPalette.SKY)) })
                    AccentChip(
                        label = localizedStringResource(R.string.accent_coral_label, language),
                        color = CoralAccent,
                        selected = state.accentPalette == AccentPalette.CORAL,
                        onClick = { onIntent(StandTimeIntent.ChangeAccent(AccentPalette.CORAL)) })
                }
            }

            // ── Widgets toggle ────────────────────────────────────────────
            SettingSection(
                title = localizedStringResource(R.string.customize_title, language)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    SettingRow(
                        label = localizedStringResource(R.string.show_calendar_label, language),
                        checked = state.showCalendar,
                        onCheckedChange = { onIntent(StandTimeIntent.ToggleCalendar) })
                    SettingRow(
                        label = localizedStringResource(R.string.show_weather_label, language),
                        checked = state.showWeather,
                        onCheckedChange = { onIntent(StandTimeIntent.ToggleWeather) })
                    SettingRow(
                        label = localizedStringResource(R.string.show_battery_label, language),
                        checked = state.showBattery,
                        onCheckedChange = { onIntent(StandTimeIntent.ToggleBattery) })
                    SettingRow(
                        label = localizedStringResource(R.string.show_pomodoro_label, language),
                        checked = state.showPomodoro,
                        onCheckedChange = { onIntent(StandTimeIntent.TogglePomodoro) })
                    SettingRow(
                        label = localizedStringResource(R.string.show_seconds_label, language),
                        checked = state.showSeconds,
                        onCheckedChange = { onIntent(StandTimeIntent.ToggleSeconds) })
                }
            }

            SettingSection(
                title = localizedStringResource(R.string.stand_features_title, language)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    SettingRow(
                        label = localizedStringResource(R.string.charging_stand_mode_label, language),
                        checked = state.enableChargingStandMode,
                        onCheckedChange = { onIntent(StandTimeIntent.SetChargingStandMode(it)) },
                        supportingText = localizedStringResource(
                            R.string.charging_stand_mode_hint,
                            language
                        )
                    )
                    SettingRow(
                        label = localizedStringResource(R.string.burn_in_protection_label, language),
                        checked = state.enableBurnInProtection,
                        onCheckedChange = { onIntent(StandTimeIntent.SetBurnInProtection(it)) }
                    )
                    SettingRow(
                        label = localizedStringResource(R.string.tap_reveal_info_label, language),
                        checked = state.enableTapRevealInfo,
                        onCheckedChange = { onIntent(StandTimeIntent.SetTapRevealInfo(it)) }
                    )
                    SettingRow(
                        label = localizedStringResource(R.string.auto_dim_night_mode_label, language),
                        checked = state.enableAutoDimNightMode,
                        onCheckedChange = { onIntent(StandTimeIntent.SetAutoDimNightMode(it)) },
                        supportingText = localizedStringResource(
                            if (ambientLightSensorAvailable) {
                                R.string.auto_dim_night_mode_hint
                            } else {
                                R.string.auto_dim_night_mode_unavailable
                            },
                            language
                        )
                    )
//                    SettingRow(
//                        label = localizedStringResource(R.string.sleep_color_filter_label, language),
//                        checked = state.enableSleepColorFilter,
//                        onCheckedChange = { onIntent(StandTimeIntent.SetSleepColorFilter(it)) },
//                        supportingText = localizedStringResource(
//                            if (ambientLightSensorAvailable) {
//                                R.string.sleep_color_filter_hint
//                            } else {
//                                R.string.auto_dim_night_mode_unavailable
//                            },
//                            language
//                        )
//                    )
//                    SettingRow(
//                        label = localizedStringResource(R.string.wave_to_wake_label, language),
//                        checked = state.enableWaveToWake,
//                        onCheckedChange = { onIntent(StandTimeIntent.SetWaveToWake(it)) },
//                        supportingText = localizedStringResource(
//                            if (proximitySensorAvailable) {
//                                R.string.wave_to_wake_hint
//                            } else {
//                                R.string.wave_to_wake_unavailable
//                            },
//                            language
//                        )
//                    )
                }
            }

            // ── Location / weather refresh ────────────────────────────────
            if (!state.locationPermissionGranted) {
                Button(
                    onClick = onRequestLocationPermission, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(localizedStringResource(R.string.weather_enable_location, language))
                }
            } else {
                FilterChip(
                    selected = false,
                    onClick = { onIntent(StandTimeIntent.RefreshWeather) },
                    label = { Text(localizedStringResource(R.string.weather_refresh, language)) })
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Info cards
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun WeatherCard(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    onRequestLocationPermission: () -> Unit
) {
    PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = localizedStringResource(R.string.weather_title, language),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            when {
                !state.locationPermissionGranted -> {
                    Text(
                        text = localizedStringResource(R.string.weather_permission_body, language),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(onClick = onRequestLocationPermission) {
                        Text(localizedStringResource(R.string.weather_enable_location, language))
                    }
                }

                state.isWeatherLoading -> {
                    Text(
                        text = localizedStringResource(R.string.weather_loading, language),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                state.weatherError.isNotBlank() -> {
                    Text(
                        text = state.weatherError,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    FilterChip(
                        selected = false,
                        onClick = { onIntent(StandTimeIntent.RefreshWeather) },
                        label = {
                            Text(
                                localizedStringResource(
                                    R.string.weather_refresh, language
                                )
                            )
                        })
                }

                else -> {
                    Text(
                        text = state.locationName.ifBlank {
                            localizedStringResource(R.string.weather_location_unknown, language)
                        }, style = MaterialTheme.typography.titleLarge, color = accentColor
                    )
                    Text(
                        text = state.weatherTemperature.ifBlank { "--" },
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = state.weatherSummary.ifBlank {
                            localizedStringResource(R.string.weather_unavailable, language)
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (state.weatherWind.isNotBlank()) {
                        Text(
                            text = localizedStringResource(
                                R.string.weather_wind_value, language, state.weatherWind
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    FilterChip(
                        selected = false,
                        onClick = { onIntent(StandTimeIntent.RefreshWeather) },
                        label = {
                            Text(
                                localizedStringResource(
                                    R.string.weather_refresh, language
                                )
                            )
                        })
                }
            }
        }
    }
}

@Composable
private fun CalendarCard(
    state: StandTimeUiState, language: StandTimeLanguage, accentColor: Color
) {
    PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = localizedStringResource(R.string.calendar_title, language),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = state.monthTitle,
                style = MaterialTheme.typography.titleMedium,
                color = accentColor
            )
            CalendarGrid(
                weekDayLabels = state.weekDayLabels,
                cells = state.calendarCells,
                accentColor = accentColor
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    weekDayLabels: List<String>, cells: List<CalendarDayCell>, accentColor: Color
) {
    BoxWithConstraints {
        val compact = maxWidth < 340.dp
        val rowSpacing = if (compact) 4.dp else 8.dp
        val itemSpacing = if (compact) 3.dp else 6.dp
        Column(verticalArrangement = Arrangement.spacedBy(rowSpacing)) {
            CalendarRow(itemSpacing = itemSpacing) {
                weekDayLabels.forEach { label ->
                    CalendarTextCell(
                        text = label,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        compact = compact
                    )
                }
            }
            cells.chunked(7).forEach { week ->
                CalendarRow(itemSpacing = itemSpacing) {
                    week.forEach { cell ->
                        CalendarTextCell(
                            text = cell.label,
                            color = if (cell.isCurrentMonth) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
                            backgroundColor = if (cell.isToday) accentColor.copy(alpha = 0.20f)
                            else Color.Transparent,
                            compact = compact
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarRow(
    itemSpacing: androidx.compose.ui.unit.Dp = 6.dp, content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        content = content
    )
}

@Composable
private fun RowScope.CalendarTextCell(
    text: String, color: Color, backgroundColor: Color = Color.Transparent, compact: Boolean = false
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(vertical = if (compact) 5.dp else 8.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            style = if (compact) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PomodoroCard(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit
) {
    val isViewedPhaseRunning = state.isViewedPomodoroRunning()
    val progress by animateFloatAsState(
        targetValue = state.pomodoroProgress(),
        animationSpec = tween(durationMillis = 500),
        label = "pomodoroProgress"
    )
    val currentPhaseLabel = localizedStringResource(pomodoroPhaseLabelRes(state.pomodoroPhase), language)
    val nextPhase = when (state.pomodoroPhase) {
        PomodoroPhase.FOCUS -> {
            if ((state.pomodoroCompletedFocusSessions + 1) % 4 == 0) PomodoroPhase.LONG_BREAK
            else PomodoroPhase.SHORT_BREAK
        }

        PomodoroPhase.SHORT_BREAK,
        PomodoroPhase.LONG_BREAK -> PomodoroPhase.FOCUS
    }
    val nextPhaseLabel = localizedStringResource(pomodoroPhaseLabelRes(nextPhase), language)
    val cycleIndex = when (state.pomodoroPhase) {
        PomodoroPhase.FOCUS -> (state.pomodoroCompletedFocusSessions % 4) + 1
        PomodoroPhase.SHORT_BREAK -> state.pomodoroCompletedFocusSessions.coerceIn(1, 4)
        PomodoroPhase.LONG_BREAK -> 4
    }
    val statusText = when {
        isViewedPhaseRunning && state.pomodoroPhase == PomodoroPhase.FOCUS ->
            localizedStringResource(R.string.pomodoro_running, language)

        isViewedPhaseRunning ->
            localizedStringResource(R.string.pomodoro_break_running, language)

        state.isPomodoroRunning && state.pomodoroActivePhase != null ->
            localizedStringResource(
                R.string.pomodoro_background_running,
                language,
                localizedStringResource(pomodoroPhaseLabelRes(state.pomodoroActivePhase), language)
            )

        else -> localizedStringResource(R.string.pomodoro_ready, language)
    }

    PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = localizedStringResource(R.string.pomodoro_title, language),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = accentColor.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = currentPhaseLabel,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PomodoroProgressRing(
                    progress = progress,
                    accentColor = accentColor,
                    modifier = Modifier.size(112.dp)
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        color = accentColor,
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = state.remainingPomodoroText(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = accentColor,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = localizedStringResource(
                            R.string.pomodoro_next_up,
                            language,
                            nextPhaseLabel
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CompactInfoPill(
                            text = localizedStringResource(
                                R.string.pomodoro_cycle_progress,
                                language,
                                cycleIndex,
                                4
                            )
                        )
                        CompactInfoPill(
                            text = localizedStringResource(
                                R.string.pomodoro_preset_label,
                                language,
                                (state.pomodoroTotalSeconds() / 60).toString()
                            )
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentPhaseLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(modifier = Modifier.weight(1f)) {
                    ChipRow {
                        listOf(
                            PomodoroPhase.FOCUS,
                            PomodoroPhase.SHORT_BREAK,
                            PomodoroPhase.LONG_BREAK
                        ).forEach { phase ->
                            FilterChip(
                                selected = state.pomodoroPhase == phase,
                                onClick = { onIntent(StandTimeIntent.SetPomodoroPhase(phase)) },
                                label = {
                                    Text(
                                        localizedStringResource(
                                            pomodoroPhaseLabelRes(phase),
                                            language
                                        ),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = localizedStringResource(R.string.presets_title, language),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(modifier = Modifier.weight(1f)) {
                    ChipRow {
                        state.pomodoroPresets.forEach { preset ->
                            FilterChip(
                                selected = state.selectedPomodoroMinutes == preset.focusMinutes,
                                onClick = { onIntent(StandTimeIntent.SelectPomodoroPreset(preset.focusMinutes)) },
                                label = {
                                    Text(
                                        localizedStringResource(
                                            R.string.pomodoro_preset_label,
                                            language,
                                            preset.label
                                        ),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onIntent(StandTimeIntent.TogglePomodoroTimer) },
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        if (isViewedPhaseRunning) {
                            localizedStringResource(R.string.pomodoro_pause, language)
                        } else {
                            localizedStringResource(R.string.pomodoro_start, language)
                        }
                    )
                }

                TextButton(
                    onClick = { onIntent(StandTimeIntent.ResetPomodoro) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(localizedStringResource(R.string.pomodoro_reset, language))
                }

                TextButton(
                    onClick = { onIntent(StandTimeIntent.SkipPomodoroPhase) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(localizedStringResource(R.string.pomodoro_skip, language))
                }
            }
        }
    }
}

@Composable
private fun CompactInfoPill(text: String) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PomodoroProgressRing(
    progress: Float,
    accentColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 12.dp.toPx()
            drawArc(
                color = accentColor.copy(alpha = 0.14f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = accentColor,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Box(
            modifier = Modifier
                .size(118.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}

private fun pomodoroPhaseLabelRes(phase: PomodoroPhase): Int = when (phase) {
    PomodoroPhase.FOCUS -> R.string.pomodoro_focus
    PomodoroPhase.SHORT_BREAK -> R.string.pomodoro_short_break
    PomodoroPhase.LONG_BREAK -> R.string.pomodoro_long_break
}

@Composable
private fun MediaCard(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit
) {
    val context = LocalContext.current
    PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = localizedStringResource(R.string.media_title, language),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!state.mediaPermissionGranted) {
                Text(
                    text = localizedStringResource(R.string.media_permission_body, language),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    }) {
                    Text(localizedStringResource(R.string.media_enable_access, language))
                }
            } else if (!state.mediaSessionAvailable) {
                Text(
                    text = localizedStringResource(R.string.media_unavailable, language),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            accentColor.copy(alpha = 0.95f),
                                            accentColor.copy(alpha = 0.30f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.GraphicEq,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = state.mediaTitle.ifBlank {
                                    localizedStringResource(R.string.media_now_playing_fallback, language)
                                },
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = state.mediaSubtitle.ifBlank {
                                    state.mediaAppLabel.ifBlank { state.mediaAppName }
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            CompactInfoPill(
                                text = localizedStringResource(
                                    R.string.media_source_value,
                                    language,
                                    state.mediaAppLabel.ifBlank { state.mediaAppName }
                                )
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onIntent(StandTimeIntent.ToggleMediaPlayback) },
                        modifier = Modifier.weight(1.15f),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Icon(
                            imageVector = if (state.isMediaPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (state.isMediaPlaying) {
                                localizedStringResource(R.string.media_pause, language)
                            } else {
                                localizedStringResource(R.string.media_play, language)
                            }
                        )
                    }

                    Button(
                        onClick = { onIntent(StandTimeIntent.SkipToNextTrack) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SkipNext,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(localizedStringResource(R.string.media_next, language))
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Shared UI atoms
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PageIndicatorBar(
    currentPage: Int,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val label = when (currentPage) {
        1 -> localizedStringResource(R.string.dashboard_label, language)
        else -> localizedStringResource(R.string.setup_label, language)
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 9.dp else 6.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage) accentColor
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
            )
        }
    }
}

/**
 * Unified card shell used by all info cards and settings on pages 1 & 2.
 * Keeps visual language consistent with a subtle accent tint.
 */
@Composable
private fun PanelCard(
    accentColor: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f),
        tonalElevation = 8.dp,
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.0f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingSection(
    title: String, content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp
        )
        content()
    }
}

@Composable
private fun SettingRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    supportingText: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!supportingText.isNullOrBlank()) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun AccentChip(
    label: String, color: Color, selected: Boolean, onClick: () -> Unit
) {
    FilterChip(
        selected = selected, onClick = onClick, label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Text(label)
            }
        })
}

@Composable
private fun ColorOptionChip(
    label: String, color: Color, selected: Boolean, onClick: () -> Unit
) {
    FilterChip(
        selected = selected, onClick = onClick, label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Text(label)
            }
        })
}

@Composable
private fun LanguageChip(selected: Boolean, label: String, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(label) })
}

@Composable
private fun ChipRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Internal enums
// ─────────────────────────────────────────────────────────────────────────────

private enum class DashboardPanel {
    Calendar, Weather, Pomodoro, Media
}
