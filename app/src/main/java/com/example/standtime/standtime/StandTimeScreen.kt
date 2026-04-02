package com.example.standtime.standtime

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.CustomClockStudioPage
import com.example.standtime.standtime.feature.components.accentColor
import com.example.standtime.standtime.feature.components.remainingPomodoroText
import com.example.standtime.standtime.feature.components.GalleryClockContent
import com.example.standtime.standtime.feature.components.galleryParts
import com.example.standtime.standtime.feature.components.galleryStyles
import com.example.standtime.standtime.feature.components.galleryStyleAt
import com.example.standtime.standtime.feature.components.galleryStyleCount
import com.example.standtime.standtime.feature.utils.AccentPalette
import com.example.standtime.standtime.feature.utils.CalendarDayCell
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
    modifier: Modifier = Modifier
) {
    val language = state.language
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val accentColor = state.accentColor()
    var isCustomStudioOpen by rememberSaveable { mutableStateOf(false) }

    var hasRequestedLocationPermission by rememberSaveable { mutableStateOf(false) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grantResults ->
        val granted = grantResults.values.any { it }
        hasRequestedLocationPermission = true
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

    LaunchedEffect(state.locationPermissionGranted, hasRequestedLocationPermission) {
        if (!state.locationPermissionGranted && !hasRequestedLocationPermission) {
            hasRequestedLocationPermission = true
            requestLocationPermission()
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
    val scope = rememberCoroutineScope()

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
            ) {
                HorizontalPager(
                    state = rootPagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> ClockStylesPage(
                            state = state,
                            language = language,
                            accentColor = accentColor,
                            onIntent = onIntent,
                            onOpenCustomStudio = { isCustomStudioOpen = true }
                        )
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
                            onRequestLocationPermission = requestLocationPermission
                        )
                    }
                }

                if (rootPagerState.currentPage != 0) {
                    PageIndicatorBar(
                        currentPage = rootPagerState.currentPage,
                        language = language,
                        accentColor = accentColor,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 6.dp)
                    )
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
    onOpenCustomStudio: () -> Unit
) {
    val stylesCount = galleryStyleCount(state.savedCustomClockStyles)
    val lastStyleIndex = (stylesCount - 1).coerceAtLeast(0)
    val parts = state.galleryParts()
    var pendingDeleteStyleId by rememberSaveable { mutableStateOf<String?>(null) }
    val galleryPagerState = rememberPagerState(
        initialPage = state.selectedGalleryStyleIndex.coerceIn(0, lastStyleIndex),
        pageCount = { stylesCount }
    )
    val currentIndex = galleryPagerState.currentPage.coerceIn(0, lastStyleIndex)
    val currentStyle = galleryStyleAt(currentIndex, state.savedCustomClockStyles)
    val styleName = currentStyle.label ?: localizedStringResource(currentStyle.nameRes ?: R.string.gallery_style_custom, language)

    LaunchedEffect(stylesCount, galleryPagerState.currentPage) {
        if (galleryPagerState.currentPage > lastStyleIndex) {
            galleryPagerState.scrollToPage(lastStyleIndex)
        }
    }

    // Persist selected style to state
    LaunchedEffect(currentIndex) {
        onIntent(StandTimeIntent.ChangeGalleryStyleIndex(currentIndex))
    }

    val customStyleToDelete = state.savedCustomClockStyles.firstOrNull { it.id == pendingDeleteStyleId }
    if (customStyleToDelete != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteStyleId = null },
            title = { Text(localizedStringResource(R.string.custom_delete_title, language)) },
            text = {
                Text(
                    localizedStringResource(
                        R.string.custom_delete_message,
                        language,
                        customStyleToDelete.name
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onIntent(StandTimeIntent.DeleteSavedCustomClockStyle(customStyleToDelete.id))
                        pendingDeleteStyleId = null
                    }
                ) {
                    Text(localizedStringResource(R.string.custom_delete_confirm, language))
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteStyleId = null }) {
                    Text(localizedStringResource(R.string.custom_cancel, language))
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Full‑screen vertical pager — each page IS the clock style
        VerticalPager(
            state = galleryPagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            state.savedCustomClockStyles
                                .getOrNull(page - galleryStyles.size)
                                ?.let { pendingDeleteStyleId = it.id }
                        }
                    )
            ) {
                GalleryClockContent(
                    index = page,
                    parts = parts,
                    language = language,
                    accentColor = accentColor,
                    customStyles = state.savedCustomClockStyles,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // ── Top overlay: battery + style counter ──────────────────────────
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = localizedStringResource(
                    R.string.gallery_charging_status,
                    language,
                    state.batteryLevel
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.Black.copy(alpha = 0.28f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp,
                color = Color.White
            )
            Text(
                text = localizedStringResource(
                    R.string.gallery_style_counter,
                    language,
                    styleName,
                    currentIndex + 1,
                    stylesCount
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.Black.copy(alpha = 0.28f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onOpenCustomStudio) {
                Text(localizedStringResource(R.string.custom_create_button, language))
            }
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(stylesCount) { index ->
                    Box(
                        modifier = Modifier
                            .width(if (index == currentIndex) 22.dp else 6.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                Color.White.copy(
                                    alpha = if (index == currentIndex) 0.95f else 0.25f
                                )
                            )
                    )
                }
            }
        }
    }
}

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
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
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
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Subtle style name label at bottom
        Text(
            text = currentStyle.label ?: localizedStringResource(currentStyle.nameRes ?: R.string.gallery_style_custom, language),
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
        if (state.showWeather) add(DashboardPanel.Weather)
        if (state.showPomodoro) add(DashboardPanel.Pomodoro)
        add(DashboardPanel.Media)
    }
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Swipe hint + dot indicator
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = localizedStringResource(R.string.swipe_vertical_hint, language),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            repeat(pages.size) { i ->
                Box(
                    modifier = Modifier
                        .size(if (i == pagerState.currentPage) 7.dp else 5.dp)
                        .clip(CircleShape)
                        .background(
                            if (i == pagerState.currentPage) accentColor
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            }
        }

        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
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

        if (state.showWeather) {
            WeatherCard(state, language, accentColor, onIntent, onRequestLocationPermission)
        }
        if (state.showPomodoro) {
            PomodoroCard(state, language, accentColor, onIntent)
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
    onRequestLocationPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = localizedStringResource(R.string.setup_label, language),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = localizedStringResource(R.string.swipe_styles_hint, language),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // All settings in one card
        SettingsCard(
            state = state,
            language = language,
            accentColor = accentColor,
            onIntent = onIntent,
            onRequestLocationPermission = onRequestLocationPermission
        )
    }
}

@Composable
private fun SettingsCard(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    onRequestLocationPermission: () -> Unit
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
                        onClick = { onIntent(StandTimeIntent.ChangeLanguage(StandTimeLanguage.ENGLISH)) }
                    )
                    LanguageChip(
                        selected = state.language == StandTimeLanguage.UZBEK,
                        label = "O'zbek",
                        onClick = { onIntent(StandTimeIntent.ChangeLanguage(StandTimeLanguage.UZBEK)) }
                    )
                    LanguageChip(
                        selected = state.language == StandTimeLanguage.RUSSIAN,
                        label = "Русский",
                        onClick = { onIntent(StandTimeIntent.ChangeLanguage(StandTimeLanguage.RUSSIAN)) }
                    )
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
                        label = { Text(localizedStringResource(R.string.dark_theme_label, language)) }
                    )
                    FilterChip(
                        selected = state.themeMode == ThemeMode.LIGHT,
                        onClick = { if (state.themeMode != ThemeMode.LIGHT) onIntent(StandTimeIntent.ToggleTheme) },
                        label = { Text(localizedStringResource(R.string.light_theme_label, language)) }
                    )
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
                        onClick = { onIntent(StandTimeIntent.ChangeAccent(AccentPalette.LIME)) }
                    )
                    AccentChip(
                        label = localizedStringResource(R.string.accent_sky_label, language),
                        color = SkyAccent,
                        selected = state.accentPalette == AccentPalette.SKY,
                        onClick = { onIntent(StandTimeIntent.ChangeAccent(AccentPalette.SKY)) }
                    )
                    AccentChip(
                        label = localizedStringResource(R.string.accent_coral_label, language),
                        color = CoralAccent,
                        selected = state.accentPalette == AccentPalette.CORAL,
                        onClick = { onIntent(StandTimeIntent.ChangeAccent(AccentPalette.CORAL)) }
                    )
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
                        onCheckedChange = { onIntent(StandTimeIntent.ToggleCalendar) }
                    )
                    SettingRow(
                        label = localizedStringResource(R.string.show_weather_label, language),
                        checked = state.showWeather,
                        onCheckedChange = { onIntent(StandTimeIntent.ToggleWeather) }
                    )
                    SettingRow(
                        label = localizedStringResource(R.string.show_battery_label, language),
                        checked = state.showBattery,
                        onCheckedChange = { onIntent(StandTimeIntent.ToggleBattery) }
                    )
                    SettingRow(
                        label = localizedStringResource(R.string.show_pomodoro_label, language),
                        checked = state.showPomodoro,
                        onCheckedChange = { onIntent(StandTimeIntent.TogglePomodoro) }
                    )
                    SettingRow(
                        label = localizedStringResource(R.string.show_seconds_label, language),
                        checked = state.showSeconds,
                        onCheckedChange = { onIntent(StandTimeIntent.ToggleSeconds) }
                    )
                }
            }

            // ── Location / weather refresh ────────────────────────────────
            if (!state.locationPermissionGranted) {
                Button(
                    onClick = onRequestLocationPermission,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(localizedStringResource(R.string.weather_enable_location, language))
                }
            } else {
                FilterChip(
                    selected = false,
                    onClick = { onIntent(StandTimeIntent.RefreshWeather) },
                    label = { Text(localizedStringResource(R.string.weather_refresh, language)) }
                )
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
                        label = { Text(localizedStringResource(R.string.weather_refresh, language)) }
                    )
                }
                else -> {
                    Text(
                        text = state.locationName.ifBlank {
                            localizedStringResource(R.string.weather_location_unknown, language)
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = accentColor
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
                                R.string.weather_wind_value,
                                language,
                                state.weatherWind
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    FilterChip(
                        selected = false,
                        onClick = { onIntent(StandTimeIntent.RefreshWeather) },
                        label = { Text(localizedStringResource(R.string.weather_refresh, language)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarCard(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color
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
    weekDayLabels: List<String>,
    cells: List<CalendarDayCell>,
    accentColor: Color
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
    itemSpacing: androidx.compose.ui.unit.Dp = 6.dp,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        content = content
    )
}

@Composable
private fun RowScope.CalendarTextCell(
    text: String,
    color: Color,
    backgroundColor: Color = Color.Transparent,
    compact: Boolean = false
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(vertical = if (compact) 5.dp else 8.dp),
        contentAlignment = Alignment.Center
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
    PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = localizedStringResource(R.string.pomodoro_title, language),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = state.remainingPomodoroText(),
                style = MaterialTheme.typography.displaySmall,
                color = accentColor
            )
            Text(
                text = if (state.isPomodoroRunning)
                    localizedStringResource(R.string.pomodoro_running, language)
                else
                    localizedStringResource(R.string.pomodoro_paused, language),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            ChipRow {
                state.pomodoroPresets.forEach { preset ->
                    FilterChip(
                        selected = state.selectedPomodoroMinutes == preset.minutes,
                        onClick = { onIntent(StandTimeIntent.SelectPomodoroPreset(preset.minutes)) },
                        label = {
                            Text(
                                localizedStringResource(
                                    R.string.pomodoro_preset_label,
                                    language,
                                    preset.label
                                )
                            )
                        }
                    )
                }
            }
            ChipRow {
                FilterChip(
                    selected = state.isPomodoroRunning,
                    onClick = { onIntent(StandTimeIntent.TogglePomodoroTimer) },
                    label = {
                        Text(
                            if (state.isPomodoroRunning)
                                localizedStringResource(R.string.pomodoro_pause, language)
                            else
                                localizedStringResource(R.string.pomodoro_start, language)
                        )
                    }
                )
                FilterChip(
                    selected = false,
                    onClick = { onIntent(StandTimeIntent.ResetPomodoro) },
                    label = { Text(localizedStringResource(R.string.pomodoro_reset, language)) }
                )
            }
        }
    }
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
                style = MaterialTheme.typography.headlineMedium,
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
                            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    }
                ) {
                    Text(localizedStringResource(R.string.media_enable_access, language))
                }
            } else if (!state.mediaSessionAvailable) {
                Text(
                    text = localizedStringResource(R.string.media_unavailable, language),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = state.mediaTitle.ifBlank {
                        localizedStringResource(R.string.media_title, language)
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = accentColor
                )
                Text(
                    text = state.mediaSubtitle.ifBlank {
                        localizedStringResource(R.string.media_source_value, language, state.mediaAppName)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = localizedStringResource(
                        R.string.media_source_value, language, state.mediaAppName
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ChipRow {
                    FilterChip(
                        selected = state.isMediaPlaying,
                        onClick = { onIntent(StandTimeIntent.ToggleMediaPlayback) },
                        label = {
                            Text(
                                if (state.isMediaPlaying)
                                    localizedStringResource(R.string.media_pause, language)
                                else
                                    localizedStringResource(R.string.media_play, language)
                            )
                        }
                    )
                    FilterChip(
                        selected = false,
                        onClick = { onIntent(StandTimeIntent.SkipToNextTrack) },
                        label = { Text(localizedStringResource(R.string.media_next, language)) }
                    )
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
    accentColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
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
    title: String,
    content: @Composable () -> Unit
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
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun AccentChip(
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
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
        }
    )
}

@Composable
private fun ColorOptionChip(
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
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
        }
    )
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
    Calendar,
    Weather,
    Pomodoro,
    Media
}
