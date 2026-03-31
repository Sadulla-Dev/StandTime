package com.example.standtime.standtime

import android.content.Intent
import android.content.res.Configuration
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.style.GalleryClockParts
import com.example.standtime.standtime.feature.components.style.galleryParts
import com.example.standtime.ui.theme.CoralAccent
import com.example.standtime.ui.theme.LimeAccent
import com.example.standtime.ui.theme.SkyAccent

@Composable
fun StandTimeRoute(
    state: StandTimeUiState,
    onIntent: (StandTimeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = state.language
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val accentColor = state.accentColor()
    val background = Brush.radialGradient(
        colors = listOf(
            accentColor.copy(alpha = if (state.themeMode == ThemeMode.DARK) 0.32f else 0.20f),
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surface
        )
    )
    val rootPagerState = rememberPagerState(pageCount = { 3 })

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
        ) {
            HorizontalPager(
                state = rootPagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Box(
                    modifier = if (page == 0) {
                        Modifier.fillMaxSize()
                    } else {
                        Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    }
                ) {
                    when (page) {
                        0 -> ClockStylesPage(
                            state = state,
                            language = language,
                            accentColor = accentColor,
                            onIntent = onIntent
                        )
                        1 -> DashboardPage(
                            state = state,
                            language = language,
                            accentColor = accentColor,
                            onIntent = onIntent,
                            isLandscape = isLandscape
                        )
                        else -> SetupPage(
                            state = state,
                            language = language,
                            accentColor = accentColor,
                            onIntent = onIntent
                        )
                    }
                }
            }

            if (rootPagerState.currentPage != 0) {
                PagerHeader(
                    currentPage = rootPagerState.currentPage,
                    strings = language,
                    accentColor = accentColor,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ClockStylesPage(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit
) {
    val stylesCount = GALLERY_STYLE_COUNT
    val parts = state.galleryParts()
    val galleryPagerState = rememberPagerState(pageCount = { stylesCount })
    val currentStyle = galleryPagerState.currentPage
    val styleName = galleryStyleName(currentStyle)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(0.dp))
            .background(galleryBackground(currentStyle))
    ) {
        VerticalPager(
            state = galleryPagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(galleryBackground(page))
            ) {
                GalleryClockContent(
                    index = page,
                    parts = parts,
                    accentColor = accentColor,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 24.dp, vertical = 18.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CHARGING ${state.batteryLevel}%",
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                ),
                color = galleryOverlayColor(currentStyle)
            )
            Text(
                text = "$styleName  ${currentStyle + 1}/$stylesCount",
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                ),
                color = galleryOverlayColor(currentStyle)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(stylesCount) { index ->
                Box(
                    modifier = Modifier
                        .width(if (index == currentStyle) 24.dp else 8.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(galleryOverlayColor(currentStyle).copy(alpha = if (index == currentStyle) 1f else 0.3f))
                )
            }
        }
    }
}

@Composable
private fun DashboardPage(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    isLandscape: Boolean
) {
    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 44.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            ClockPanel(
                state = state,
                strings = language,
                accentColor = accentColor,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            SwipePanel(
                state = state,
                strings = language,
                accentColor = accentColor,
                onIntent = onIntent,
                modifier = Modifier
                    .weight(1.08f)
                    .fillMaxHeight()
            )
        }
    } else {
        PortraitFallback(
            state = state,
            strings = language,
            accentColor = accentColor,
            onIntent = onIntent,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 44.dp)
        )
    }
}

@Composable
private fun SetupPage(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 44.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = localizedStringResource(R.string.setup_label, language),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = localizedStringResource(R.string.swipe_styles_hint, language),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        CustomizeCard(
            state = state,
            strings = language,
            accentColor = accentColor,
            onIntent = onIntent
        )
    }
}

@Composable
private fun SwipePanel(
    state: StandTimeUiState,
    strings: StandTimeStrings,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = buildList<DashboardPanel> {
        if (state.showCalendar) add(DashboardPanel.Calendar)
        if (state.showPomodoro) add(DashboardPanel.Pomodoro)
        add(DashboardPanel.Media)
    }
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = strings.swipeVerticalHint,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (pages[page]) {
                DashboardPanel.Calendar -> CalendarCard(
                    state = state,
                    strings = strings,
                    accentColor = accentColor
                )
                DashboardPanel.Pomodoro -> PomodoroCard(
                    state = state,
                    strings = strings,
                    accentColor = accentColor,
                    onIntent = onIntent
                )
                DashboardPanel.Media -> MediaCard(
                    state = state,
                    strings = strings,
                    accentColor = accentColor,
                    onIntent = onIntent
                )
            }
        }
    }
}

@Composable
private fun ClockPanel(
    state: StandTimeUiState,
    strings: StandTimeStrings,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val statusText = if (state.isCharging) strings.chargingLabel else strings.batteryIdleLabel
    val batteryProgress = state.batteryLevel / 100f

    PanelCard(modifier = modifier, accentColor = accentColor) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AccentDot(accentColor = accentColor)
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ClockFace(
                    style = state.clockStyle,
                    state = state,
                    accentColor = accentColor,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = strings.swipeSetupHint,
                    style = MaterialTheme.typography.bodyLarge,
                    color = accentColor
                )
            }

            if (state.showBattery) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "${strings.batteryLabel} ${state.batteryLevel}%",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    LinearProgressIndicator(
                        progress = { batteryProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(50)),
                        color = accentColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = strings.orientationHint,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ClockFace(
    style: ClockStyle,
    state: StandTimeUiState,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    when (style) {
        ClockStyle.NOTHING -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = state.timeText,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 68.sp,
                    letterSpacing = (-2).sp,
                    lineHeight = 70.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AccentDot(accentColor = accentColor)
                Text(
                    text = state.dayText,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = state.dateText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        ClockStyle.PIXEL -> Column(
            modifier = modifier
                .clip(RoundedCornerShape(20.dp))
                .background(accentColor.copy(alpha = 0.10f))
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = state.timeText,
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 56.sp,
                    letterSpacing = 1.sp
                ),
                color = accentColor
            )
            Text(
                text = state.dayText.uppercase(),
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = state.dateText,
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        ClockStyle.IPHONE -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = state.timeText,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light,
                    fontSize = 82.sp,
                    lineHeight = 84.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${state.dayText}, ${state.dateText}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        ClockStyle.MINIMAL -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = state.timeText,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 60.sp,
                    lineHeight = 62.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.dayText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "•",
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor
                )
                Text(
                    text = state.dateText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PortraitFallback(
    state: StandTimeUiState,
    strings: StandTimeStrings,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = strings.portraitTitle,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = strings.portraitBody,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ClockFace(
                    style = state.clockStyle,
                    state = state,
                    accentColor = accentColor,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        CalendarCard(state = state, strings = strings, accentColor = accentColor)
        if (state.showPomodoro) {
            PomodoroCard(
                state = state,
                strings = strings,
                accentColor = accentColor,
                onIntent = onIntent
            )
        }
        MediaCard(
            state = state,
            strings = strings,
            accentColor = accentColor,
            onIntent = onIntent
        )
    }
}

@Composable
private fun CalendarCard(
    state: StandTimeUiState,
    strings: StandTimeStrings,
    accentColor: Color
) {
    PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = strings.calendarTitle,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = state.monthTitle,
                style = MaterialTheme.typography.titleLarge,
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
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CalendarRow {
            weekDayLabels.forEach { label ->
                CalendarTextCell(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        cells.chunked(7).forEach { week ->
            CalendarRow {
                week.forEach { cell ->
                    CalendarTextCell(
                        text = cell.label,
                        color = if (cell.isCurrentMonth) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
                        },
                        backgroundColor = if (cell.isToday) accentColor.copy(alpha = 0.20f) else Color.Transparent
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@Composable
private fun RowScope.CalendarTextCell(
    text: String,
    color: Color,
    backgroundColor: Color = Color.Transparent
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PomodoroCard(
    state: StandTimeUiState,
    strings: StandTimeStrings,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit
) {
    PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = strings.pomodoroTitle,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = state.remainingPomodoroText(),
                style = MaterialTheme.typography.displayMedium,
                color = accentColor
            )
            Text(
                text = if (state.isPomodoroRunning) strings.pomodoroRunning else strings.pomodoroPaused,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = strings.presetsTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            ChipRow {
                state.pomodoroPresets.forEach { preset ->
                    FilterChip(
                        selected = state.selectedPomodoroMinutes == preset.minutes,
                        onClick = { onIntent(StandTimeIntent.SelectPomodoroPreset(preset.minutes)) },
                        label = { Text("${preset.label} ${strings.minutesSuffix}") }
                    )
                }
            }
            ChipRow {
                FilterChip(
                    selected = state.isPomodoroRunning,
                    onClick = { onIntent(StandTimeIntent.TogglePomodoroTimer) },
                    label = { Text(if (state.isPomodoroRunning) strings.pomodoroPause else strings.pomodoroStart) }
                )
                FilterChip(
                    selected = false,
                    onClick = { onIntent(StandTimeIntent.ResetPomodoro) },
                    label = { Text(strings.pomodoroReset) }
                )
            }
        }
    }
}

@Composable
private fun MediaCard(
    state: StandTimeUiState,
    strings: StandTimeStrings,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit
) {
    val context = LocalContext.current

    PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = strings.mediaTitle,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (!state.mediaPermissionGranted) {
                Text(
                    text = strings.mediaPermissionBody,
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
                    Text(strings.mediaEnableAccess)
                }
            } else if (!state.mediaSessionAvailable) {
                Text(
                    text = strings.mediaUnavailable,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = state.mediaTitle.ifBlank { strings.mediaTitle },
                    style = MaterialTheme.typography.displayMedium,
                    color = accentColor
                )
                Text(
                    text = state.mediaSubtitle.ifBlank { "${strings.mediaSource}: ${state.mediaAppName}" },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${strings.mediaSource}: ${state.mediaAppName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ChipRow {
                    FilterChip(
                        selected = state.isMediaPlaying,
                        onClick = { onIntent(StandTimeIntent.ToggleMediaPlayback) },
                        label = { Text(if (state.isMediaPlaying) strings.mediaPause else strings.mediaPlay) }
                    )
                    FilterChip(
                        selected = false,
                        onClick = { onIntent(StandTimeIntent.SkipToNextTrack) },
                        label = { Text(strings.mediaNext) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GalleryClockContent(
    index: Int,
    parts: GalleryClockParts,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(20.dp)) {
        when (index) {
            0 -> NothingOfficialClock(parts)
            1 -> Ps5Clock(parts)
            2 -> TeslaClock(parts)
            3 -> MinecraftClock(parts)
            4 -> SpotifyClock(parts)
            5 -> NasaClock(parts)
            6 -> PixelStackClock(parts)
            7 -> TokyoClock(parts)
            8 -> IosStackClock(parts)
            9 -> BraunClock(parts)
            10 -> TerminalClock(parts)
            11 -> CyberpunkClock(parts, accentColor)
            12 -> PixelPetClock(parts)
            13 -> LofiClock(parts)
            14 -> RolexClock(parts)
            15 -> GlassClock(parts)
            16 -> LuxuryClock(parts)
            17 -> BauhausClock(parts)
            18 -> MacOsClock(parts)
            19 -> WordsClock(parts)
            20 -> CoffeeClock(parts)
            21 -> NightOwlClock(parts)
            22 -> ArcadeClock(parts)
            23 -> AnalogZenClock(parts)
            24 -> RetroFlipClock(parts)
            25 -> BinaryPulseClock(parts)
            26 -> SolarOrbitClock(parts)
            27 -> TypewriterClock(parts)
            28 -> LiquidGradientClock(parts)
            29 -> AdminPanelClock(parts)
            30 -> PhotoFrameClock(parts)
            else -> SynthwaveClock(parts)
        }
    }
}

@Composable
private fun NothingOfficialClock(parts: GalleryClockParts) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${parts.hours}·${parts.minutes}",
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                fontSize = 116.sp,
                letterSpacing = 2.sp
            ),
            color = Color.White
        )
        Text(
            text = "Nothing OS 3.0",
            modifier = Modifier
                .padding(top = 24.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.08f))
                .padding(horizontal = 18.dp, vertical = 8.dp),
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                letterSpacing = 2.sp
            ),
            color = Color.White.copy(alpha = 0.65f)
        )
    }
}

@Composable
private fun Ps5Clock(parts: GalleryClockParts) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(520.dp)
                .align(Alignment.TopCenter)
                .clip(CircleShape)
                .background(Color(0x332567FF))
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${parts.hours} | ${parts.minutes}",
                style = TextStyle(
                    fontWeight = FontWeight.Light,
                    fontSize = 88.sp,
                    letterSpacing = 6.sp
                ),
                color = Color.White
            )
            Text(
                text = "○   △   □",
                modifier = Modifier.padding(top = 20.dp),
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 24.sp,
                    letterSpacing = 6.sp
                ),
                color = Color.White.copy(alpha = 0.45f)
            )
        }
    }
}

@Composable
private fun TeslaClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${parts.hours}:${parts.minutes}",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 76.sp),
                color = Color(0xFFF5F5F5)
            )
            Text(
                text = "P  R  N  D",
                modifier = Modifier.padding(top = 10.dp),
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 24.sp,
                    letterSpacing = 4.sp
                ),
                color = Color(0xFF6C6C6C)
            )
        }
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(220.dp)
                .background(Color(0xFF2A2A2A))
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GalleryMetricCard("Range", "420 mi", Color(0xFF1A1A1A), Color.White)
            GalleryMetricCard("Temp", "21°C", Color(0xFF1A1A1A), Color.White)
        }
    }
}

@Composable
private fun MinecraftClock(parts: GalleryClockParts) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF1E1E1E))
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${parts.hours}:${parts.minutes}",
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    fontSize = 74.sp,
                    letterSpacing = 4.sp
                ),
                color = Color(0xFF00FF00)
            )
            Text(
                text = "LEVEL 100 CHARGING",
                modifier = Modifier.padding(top = 12.dp),
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    letterSpacing = 2.sp
                ),
                color = Color.White.copy(alpha = 0.45f)
            )
        }
    }
}

@Composable
private fun SpotifyClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF22C55E), Color(0xFF14532D)))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${parts.hours}:${parts.minutes}",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 52.sp),
                color = Color.White
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Time is Running Out",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 38.sp),
                color = Color.White
            )
            Text(
                text = "The Clock - StandBy Album",
                modifier = Modifier.padding(top = 8.dp),
                style = TextStyle(fontSize = 22.sp),
                color = Color(0xFFA1A1AA)
            )
            Box(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF27272A))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(parts.seconds.toFloat() / 60f)
                        .background(Color(0xFF22C55E))
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "00:${parts.seconds}", color = Color(0xFF71717A))
                Text(text = "01:00", color = Color(0xFF71717A))
            }
        }
    }
}

@Composable
private fun NasaClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x14000000))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Latitude", color = Color(0xFFFF8A3D), fontSize = 12.sp)
            Text("41.2995° N", color = Color(0xFFFF8A3D), fontSize = 30.sp, fontFamily = FontFamily.Monospace)
            Text("Longitude", color = Color(0xFFFF8A3D), fontSize = 12.sp)
            Text("69.2401° E", color = Color(0xFFFF8A3D), fontSize = 30.sp, fontFamily = FontFamily.Monospace)
        }
        Column(
            modifier = Modifier.weight(1.4f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 62.sp
                ),
                color = Color(0xFFFF8A3D)
            )
            Text(
                text = "T-MINUS STATUS",
                modifier = Modifier
                    .padding(top = 12.dp)
                    .background(Color(0xFFFF8A3D))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Oxygen", color = Color(0xFFFF8A3D), fontSize = 12.sp)
            Text("98.2%", color = Color(0xFFFF8A3D), fontSize = 30.sp, fontFamily = FontFamily.Monospace)
            Text("Pressure", color = Color(0xFFFF8A3D), fontSize = 12.sp)
            Text("1.0 ATM", color = Color(0xFFFF8A3D), fontSize = 30.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun PixelStackClock(parts: GalleryClockParts) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = parts.hours,
            style = TextStyle(fontWeight = FontWeight.Black, fontSize = 132.sp),
            color = Color(0xFFBFDBFE)
        )
        Text(
            text = parts.minutes,
            modifier = Modifier.offset(y = (-28).dp),
            style = TextStyle(fontWeight = FontWeight.Black, fontSize = 132.sp),
            color = Color.White
        )
    }
}

@Composable
private fun TokyoClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(parts.kanjiHours, color = Color(0xFFEF4444), fontSize = 92.sp, fontWeight = FontWeight.Black)
        Text("TOKYO", modifier = Modifier.padding(horizontal = 20.dp), color = Color.White.copy(alpha = 0.2f), fontSize = 26.sp)
        Text(parts.kanjiMinutes, color = Color(0xFFEF4444), fontSize = 92.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun IosStackClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF27272A)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${parts.hours}:${parts.minutes}", color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.Bold)
                Text("${parts.dayText}, ${parts.dateText}", color = Color(0xFFA1A1AA), fontSize = 18.sp)
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GalleryWidgetCard(Color(0x332565FF), "24°C", "Weather")
            GalleryWidgetCard(Color(0x33EF4444), "Design Task", "Calendar")
            GalleryWidgetCard(Color(0x3322C55E), "12,650.00", "USD/UZS")
        }
    }
}

@Composable
private fun BraunClock(parts: GalleryClockParts) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${parts.hours}:${parts.minutes}",
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF111111))
                .padding(horizontal = 24.dp, vertical = 14.dp),
            color = Color.White,
            fontSize = 88.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.padding(top = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(Color(0xFFFACC15)))
            Box(modifier = Modifier.width(50.dp).height(4.dp).clip(RoundedCornerShape(50)).background(Color(0xFF71717A)))
        }
    }
}

@Composable
private fun TerminalClock(parts: GalleryClockParts) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "> INITIALIZING SYSTEM_CLOCK...",
            color = Color(0xFF22C55E),
            fontFamily = FontFamily.Monospace,
            fontSize = 22.sp
        )
        Text(
            text = "${parts.hours}_${parts.minutes}",
            modifier = Modifier.padding(top = 12.dp),
            color = Color(0xFF22C55E),
            fontFamily = FontFamily.Monospace,
            fontSize = 100.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "// STATUS: STABLE | UPTIME: 100%",
            modifier = Modifier.padding(top = 12.dp),
            color = Color(0xFF22C55E).copy(alpha = 0.7f),
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun CyberpunkClock(parts: GalleryClockParts, accentColor: Color) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${parts.hours}:${parts.minutes}",
            color = Color(0xFFC026D3),
            fontSize = 108.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "${parts.hours}:${parts.minutes}",
            modifier = Modifier.padding(start = 6.dp, top = 6.dp),
            color = accentColor,
            fontSize = 108.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun PixelPetClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(40.dp))
                .background(Color.Black.copy(alpha = 0.08f))
                .padding(24.dp)
        ) {
            Text(
                text = "${parts.hours}:${parts.minutes}",
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    fontSize = 72.sp
                ),
                color = Color(0xFF101010)
            )
            Text(
                text = "HEALTH: 100%",
                modifier = Modifier.padding(top = 8.dp),
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    letterSpacing = 2.sp
                ),
                color = Color(0xFF101010).copy(alpha = 0.55f)
            )
        }
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(RoundedCornerShape(36.dp))
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "\uD83E\uDD96", fontSize = 108.sp)
        }
    }
}

@Composable
private fun LofiClock(parts: GalleryClockParts) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(Color(0xFF1A1C2C), Color(0xFF0F172A))))
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(48.dp))
                .background(Color.Black.copy(alpha = 0.24f))
                .padding(horizontal = 48.dp, vertical = 36.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${parts.hours}:${parts.minutes}",
                    style = TextStyle(fontWeight = FontWeight.ExtraLight, fontSize = 92.sp, letterSpacing = 6.sp),
                    color = Color(0xFFC7D2FE)
                )
                Text(
                    text = "music  lo-fi station",
                    modifier = Modifier.padding(top = 10.dp),
                    style = TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, fontSize = 20.sp),
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
private fun RolexClock(parts: GalleryClockParts) {
    val hour = parts.hours.toIntOrNull() ?: 0
    val minute = parts.minutes.toIntOrNull() ?: 0
    val second = parts.seconds.toIntOrNull() ?: 0
    val hourAngle = (hour % 12) * 30f + minute * 0.5f
    val minuteAngle = minute * 6f
    val secondAngle = second * 6f

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(360.dp)
                .clip(CircleShape)
                .background(Color(0xFF111111))
        ) {
            repeat(12) { index ->
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 18.dp)
                        .size(width = 4.dp, height = 28.dp)
                        .background(Color(0xFF5B5B5B))
                        .graphicsLayer {
                            rotationZ = index * 30f
                            translationY = -140f
                        }
                )
            }
            ClockHand(length = 92.dp, width = 6.dp, angle = hourAngle, color = Color.White)
            ClockHand(length = 126.dp, width = 4.dp, angle = minuteAngle, color = Color(0xFFD4D4D8))
            ClockHand(length = 138.dp, width = 2.dp, angle = secondAngle, color = Color(0xFFEF4444))
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}

@Composable
private fun ClockHand(length: androidx.compose.ui.unit.Dp, width: androidx.compose.ui.unit.Dp, angle: Float, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { rotationZ = angle },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = length / 2)
                .width(width)
                .height(length)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}

@Composable
private fun GlassClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlassTimeBlock(parts.hours)
        GlassTimeBlock(parts.minutes)
    }
}

@Composable
private fun RowScope.GlassTimeBlock(value: String) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(220.dp)
            .clip(RoundedCornerShape(44.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = value, color = Color.White, fontSize = 92.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun LuxuryClock(parts: GalleryClockParts) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("EST. 2024", color = Color(0xFFD4AF37).copy(alpha = 0.4f), fontSize = 20.sp, letterSpacing = 8.sp)
        Text(
            text = "${parts.hours}:${parts.minutes}",
            modifier = Modifier
                .padding(top = 24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Transparent)
                .padding(horizontal = 24.dp),
            color = Color(0xFFD4AF37),
            fontSize = 128.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun BauhausClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BauhausColumn(parts.hours[0], parts.hours[1], Color(0xFF254184), Color(0xFFE84A27), true)
        BauhausColumn(parts.minutes[0], parts.minutes[1], Color(0xFFF9C32E), Color(0xFF231F20), false)
    }
}

@Composable
private fun BauhausColumn(top: Char, bottom: Char, topColor: Color, bottomColor: Color, topRound: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(horizontal = 8.dp)) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(if (topRound) CircleShape else RoundedCornerShape(0.dp))
                .background(topColor),
            contentAlignment = Alignment.Center
        ) {
            Text(top.toString(), color = Color.White, fontSize = 72.sp, fontWeight = FontWeight.Black)
        }
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(bottomStart = if (topRound) 0.dp else 80.dp))
                .background(bottomColor),
            contentAlignment = Alignment.Center
        ) {
            Text(bottom.toString(), color = if (bottomColor == Color(0xFFF9C32E)) Color.Black else Color.White, fontSize = 72.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun MacOsClock(parts: GalleryClockParts) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(48.dp))
                .background(Color.Black.copy(alpha = 0.18f))
                .padding(28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${parts.hours}:${parts.minutes}", color = Color.White, fontSize = 88.sp, fontWeight = FontWeight.Bold)
            Column(horizontalAlignment = Alignment.End) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GallerySquareIcon("\u2601")
                    GallerySquareIcon("\uD83D\uDCC5")
                }
                Text("Tashkent, Uzbekistan", modifier = Modifier.padding(top = 12.dp), color = Color.White.copy(alpha = 0.7f), fontSize = 24.sp)
            }
        }
    }
}

@Composable
private fun GallerySquareIcon(value: String) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Text(value, fontSize = 28.sp)
    }
}

@Composable
private fun WordsClock(parts: GalleryClockParts) {
    Text(
        text = "IT IS CURRENTLY THE HOUR OF ${parts.hours} AND THE MINUTE IS ${parts.minutes} EXACTLY",
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 36.dp),
        color = Color.White,
        fontSize = 42.sp,
        fontWeight = FontWeight.Black,
        lineHeight = 50.sp
    )
}

@Composable
private fun CoffeeClock(parts: GalleryClockParts) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("\u2615", fontSize = 84.sp, color = Color(0xFFE6CCB2).copy(alpha = 0.5f))
        Text("${parts.hours}:${parts.minutes}", modifier = Modifier.padding(top = 14.dp), color = Color(0xFFE6CCB2), fontSize = 110.sp, fontWeight = FontWeight.Bold)
        Text("Brewing Time...", modifier = Modifier.padding(top = 12.dp), color = Color(0xFFE6CCB2).copy(alpha = 0.45f), fontSize = 24.sp, letterSpacing = 4.sp)
    }
}

@Composable
private fun NightOwlClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${parts.hours}:${parts.minutes}", color = Color(0xFF818CF8), fontSize = 116.sp, fontWeight = FontWeight.Black)
        Column(
            modifier = Modifier.padding(start = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("\uD83C\uDF19", fontSize = 62.sp)
            Text("SLEEP MODE", modifier = Modifier.padding(top = 8.dp), color = Color(0xFF818CF8).copy(alpha = 0.55f), fontSize = 18.sp)
        }
    }
}

@Composable
private fun ArcadeClock(parts: GalleryClockParts) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${parts.hours}:${parts.minutes}",
            color = Color(0xFFFACC15),
            fontSize = 112.sp,
            fontWeight = FontWeight.Black
        )
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 130.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("\uD83C\uDF52", fontSize = 56.sp)
            Text("\uD83D\uDC7B", fontSize = 72.sp)
            Text("\uD83D\uDFE1", fontSize = 62.sp)
        }
    }
}

@Composable
private fun AnalogZenClock(parts: GalleryClockParts) {
    val hour = parts.hours.toIntOrNull() ?: 0
    val minute = parts.minutes.toIntOrNull() ?: 0
    val hourAngle = (hour % 12) * 30f + minute * 0.5f
    val minuteAngle = minute * 6f

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(340.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.96f))
        ) {
            ClockHand(length = 90.dp, width = 6.dp, angle = hourAngle, color = Color.Black)
            ClockHand(length = 126.dp, width = 3.dp, angle = minuteAngle, color = Color(0xFF9CA3AF))
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEF4444))
            )
        }
    }
}

@Composable
private fun RetroFlipClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FlipBlock(parts.hours)
        FlipBlock(parts.minutes)
    }
}

@Composable
private fun FlipBlock(value: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFF27272A))
            .padding(horizontal = 34.dp, vertical = 26.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 104.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun BinaryPulseClock(parts: GalleryClockParts) {
    val secondSeed = parts.seconds.toIntOrNull() ?: 0
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "01010111 11001010 01110101 10101011",
            color = Color(0xFF22C55E).copy(alpha = 0.16f),
            fontFamily = FontFamily.Monospace,
            fontSize = 24.sp
        )
        Text(
            text = "${parts.hours}:${parts.minutes}",
            modifier = Modifier.padding(top = 10.dp),
            color = Color(0xFF22C55E),
            fontSize = 118.sp,
            fontWeight = FontWeight.Black
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(28) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if ((index + secondSeed) % 3 == 0) Color(0xFF22C55E)
                            else Color(0xFF14532D).copy(alpha = 0.22f)
                        )
                )
            }
        }
    }
}

@Composable
private fun SolarOrbitClock(parts: GalleryClockParts) {
    val second = parts.seconds.toFloatOrNull() ?: 0f
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(420.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .graphicsLayer { rotationZ = second * 6f }
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF97316))
            )
        }
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .graphicsLayer { rotationZ = second * 12f }
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF38BDF8))
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${parts.hours}:${parts.minutes}", color = Color.White, fontSize = 92.sp, fontWeight = FontWeight.Light, letterSpacing = 10.sp)
            Text("ORBIT CYCLE ${parts.seconds}s", modifier = Modifier.padding(top = 12.dp), color = Color.White.copy(alpha = 0.4f), fontSize = 18.sp, letterSpacing = 4.sp)
        }
    }
}

@Composable
private fun TypewriterClock(parts: GalleryClockParts) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Current Moment", color = Color(0xFF78716C).copy(alpha = 0.5f), fontSize = 28.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        Text(
            text = "${parts.hours}:${parts.minutes}",
            modifier = Modifier.padding(top = 16.dp),
            color = Color(0xFF44403C),
            fontSize = 118.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun LiquidGradientClock(parts: GalleryClockParts) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${parts.hours}:${parts.minutes}",
            color = Color.White,
            fontSize = 124.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun AdminPanelClock(parts: GalleryClockParts) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(2.2f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(38.dp))
                .background(Color(0x14181818)),
            contentAlignment = Alignment.Center
        ) {
            Text("${parts.hours}:${parts.minutes}", color = Color.White, fontSize = 118.sp, fontWeight = FontWeight.Black)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GalleryWidgetCard(Color(0xFFDC2626), "98%", "Battery")
            GalleryWidgetCard(Color.White.copy(alpha = 0.08f), "READY", "Device")
        }
    }
}

@Composable
private fun PhotoFrameClock(parts: GalleryClockParts) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF1E3A8A),
                            Color(0xFF0F766E),
                            Color(0xFF14532D)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(36.dp)
        ) {
            Text("${parts.hours}:${parts.minutes}", color = Color.White, fontSize = 92.sp, fontWeight = FontWeight.Light)
            Text("Tashkent, Uzbekistan", modifier = Modifier.padding(top = 8.dp), color = Color.White.copy(alpha = 0.82f), fontSize = 28.sp)
        }
    }
}

@Composable
private fun SynthwaveClock(parts: GalleryClockParts) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${parts.hours}:${parts.minutes}",
            color = Color(0xFF67E8F9),
            fontSize = 128.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "${parts.hours}:${parts.minutes}",
            modifier = Modifier.offset(y = 8.dp),
            color = Color(0xFFF472B6).copy(alpha = 0.55f),
            fontSize = 128.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun GalleryMetricCard(
    title: String,
    value: String,
    background: Color,
    contentColor: Color
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(background)
            .padding(18.dp)
    ) {
        Text(title.uppercase(), color = contentColor.copy(alpha = 0.5f), fontSize = 12.sp)
        Text(value, color = contentColor, fontSize = 30.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun GalleryWidgetCard(
    background: Color,
    primary: String,
    secondary: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(background)
            .padding(18.dp)
    ) {
        Text(secondary, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
        Text(primary, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CustomizeCard(
    state: StandTimeUiState,
    strings: StandTimeStrings,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit
) {
    PanelCard(accentColor = accentColor, modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = strings.customizeTitle,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = strings.languageTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
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

            Text(
                text = strings.themeTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            ChipRow {
                FilterChip(
                    selected = state.themeMode == ThemeMode.DARK,
                    onClick = { if (state.themeMode != ThemeMode.DARK) onIntent(StandTimeIntent.ToggleTheme) },
                    label = { Text(strings.darkTheme) }
                )
                FilterChip(
                    selected = state.themeMode == ThemeMode.LIGHT,
                    onClick = { if (state.themeMode != ThemeMode.LIGHT) onIntent(StandTimeIntent.ToggleTheme) },
                    label = { Text(strings.lightTheme) }
                )
            }

            Text(
                text = strings.accentTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            ChipRow {
                AccentChip(
                    label = strings.accentLime,
                    color = LimeAccent,
                    selected = state.accentPalette == AccentPalette.LIME,
                    onClick = { onIntent(StandTimeIntent.ChangeAccent(AccentPalette.LIME)) }
                )
                AccentChip(
                    label = strings.accentSky,
                    color = SkyAccent,
                    selected = state.accentPalette == AccentPalette.SKY,
                    onClick = { onIntent(StandTimeIntent.ChangeAccent(AccentPalette.SKY)) }
                )
                AccentChip(
                    label = strings.accentCoral,
                    color = CoralAccent,
                    selected = state.accentPalette == AccentPalette.CORAL,
                    onClick = { onIntent(StandTimeIntent.ChangeAccent(AccentPalette.CORAL)) }
                )
            }

            Text(
                text = strings.clockStyleTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            ChipRow {
                FilterChip(
                    selected = state.clockStyle == ClockStyle.NOTHING,
                    onClick = { onIntent(StandTimeIntent.ChangeClockStyle(ClockStyle.NOTHING)) },
                    label = { Text(strings.nothingStyleLabel) }
                )
                FilterChip(
                    selected = state.clockStyle == ClockStyle.PIXEL,
                    onClick = { onIntent(StandTimeIntent.ChangeClockStyle(ClockStyle.PIXEL)) },
                    label = { Text(strings.pixelStyleLabel) }
                )
                FilterChip(
                    selected = state.clockStyle == ClockStyle.IPHONE,
                    onClick = { onIntent(StandTimeIntent.ChangeClockStyle(ClockStyle.IPHONE)) },
                    label = { Text(strings.iphoneStyleLabel) }
                )
                FilterChip(
                    selected = state.clockStyle == ClockStyle.MINIMAL,
                    onClick = { onIntent(StandTimeIntent.ChangeClockStyle(ClockStyle.MINIMAL)) },
                    label = { Text(strings.minimalStyleLabel) }
                )
            }

            SettingRow(
                label = strings.showCalendarLabel,
                checked = state.showCalendar,
                onCheckedChange = { onIntent(StandTimeIntent.ToggleCalendar) }
            )
            SettingRow(
                label = strings.showBatteryLabel,
                checked = state.showBattery,
                onCheckedChange = { onIntent(StandTimeIntent.ToggleBattery) }
            )
            SettingRow(
                label = strings.showPomodoroLabel,
                checked = state.showPomodoro,
                onCheckedChange = { onIntent(StandTimeIntent.TogglePomodoro) }
            )
            SettingRow(
                label = strings.showSecondsLabel,
                checked = state.showSeconds,
                onCheckedChange = { onIntent(StandTimeIntent.ToggleSeconds) }
            )
        }
    }
}

@Composable
private fun PagerHeader(
    currentPage: Int,
    strings: StandTimeStrings,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val label = when (currentPage) {
        0 -> strings.clockStylesLabel
        1 -> strings.dashboardLabel
        else -> strings.setupLabel
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (index == currentPage) accentColor else MaterialTheme.colorScheme.surfaceVariant)
            )
        }
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
private fun PanelCard(
    accentColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
        tonalElevation = 10.dp,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.10f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                        )
                    )
                )
                .padding(22.dp)
        ) {
            content()
        }
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
private fun LanguageChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(label) })
}

@Composable
private fun AccentDot(accentColor: Color) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(accentColor)
    )
}

@Composable
private fun ChipRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}

private fun galleryBackground(index: Int): Brush = when (index) {
    0 -> Brush.linearGradient(listOf(Color.Black, Color(0xFF090909)))
    1 -> Brush.verticalGradient(listOf(Color.Black, Color(0xFF071A3C)))
    2 -> Brush.linearGradient(listOf(Color(0xFF111111), Color(0xFF050505)))
    3 -> Brush.linearGradient(listOf(Color(0xFF313131), Color(0xFF222222)))
    4 -> Brush.linearGradient(listOf(Color(0xFF121212), Color(0xFF050505)))
    5 -> Brush.linearGradient(listOf(Color(0xFF0A0A0A), Color.Black))
    6 -> Brush.linearGradient(listOf(Color.Black, Color(0xFF0B1220)))
    7 -> Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF111827)))
    8 -> Brush.linearGradient(listOf(Color(0xFF18181B), Color(0xFF09090B)))
    9 -> Brush.linearGradient(listOf(Color(0xFFE5E5E5), Color(0xFFCFCFCF)))
    10 -> Brush.linearGradient(listOf(Color.Black, Color(0xFF031806)))
    11 -> Brush.linearGradient(listOf(Color.Black, Color(0xFF2E1065)))
    12 -> Brush.linearGradient(listOf(Color(0xFF98FB98), Color(0xFFB7F7B7)))
    13 -> Brush.linearGradient(listOf(Color(0xFF1A1C2C), Color(0xFF0F172A)))
    14 -> Brush.linearGradient(listOf(Color(0xFF1A1A1A), Color(0xFF050505)))
    15 -> Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFFF97316)))
    16 -> Brush.linearGradient(listOf(Color(0xFF14110D), Color(0xFF0C0A09)))
    17 -> Brush.linearGradient(listOf(Color(0xFFF2EBDC), Color(0xFFE8DFC7)))
    18 -> Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF0F172A)))
    19 -> Brush.linearGradient(listOf(Color(0xFF09090B), Color(0xFF18181B)))
    20 -> Brush.linearGradient(listOf(Color(0xFF2C1810), Color(0xFF120B08)))
    21 -> Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF1E1B4B)))
    22 -> Brush.linearGradient(listOf(Color.Black, Color(0xFF1F2937)))
    23 -> Brush.linearGradient(listOf(Color(0xFFF8FAFC), Color(0xFFE5E7EB)))
    24 -> Brush.linearGradient(listOf(Color(0xFFE5E7EB), Color(0xFFD4D4D8)))
    25 -> Brush.linearGradient(listOf(Color.Black, Color(0xFF052E16)))
    26 -> Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF111827)))
    27 -> Brush.linearGradient(listOf(Color(0xFFF5F1E8), Color(0xFFE7E0D1)))
    28 -> Brush.linearGradient(listOf(Color(0xFF4F46E5), Color(0xFFEC4899), Color(0xFFF59E0B)))
    29 -> Brush.linearGradient(listOf(Color.Black, Color(0xFF111827)))
    30 -> Brush.linearGradient(listOf(Color(0xFF0F172A), Color(0xFF134E4A), Color(0xFF365314)))
    else -> Brush.linearGradient(listOf(Color(0xFF120422), Color(0xFF312E81)))
}

private fun galleryOverlayColor(index: Int): Color = if (index == 9 || index == 12 || index == 17 || index == 23 || index == 24 || index == 27) {
    Color(0xFF18181B).copy(alpha = 0.7f)
} else {
    Color.White.copy(alpha = 0.6f)
}

private fun galleryStyleName(index: Int): String = when (index) {
    0 -> "Nothing Official"
    1 -> "PlayStation 5"
    2 -> "Tesla Dashboard"
    3 -> "Minecraft Pixel"
    4 -> "Spotify Now Playing"
    5 -> "NASA Mission Control"
    6 -> "Google Pixel"
    7 -> "Tokyo Night"
    8 -> "iPhone Stack"
    9 -> "Braun Classic"
    10 -> "Terminal Matrix"
    11 -> "Cyberpunk Neon"
    12 -> "Pixel Pet"
    13 -> "Lofi Chill"
    14 -> "Rolex Minimal"
    15 -> "Glassmorphism"
    16 -> "Luxury Gold"
    17 -> "Bauhaus"
    18 -> "macOS Big Sur"
    19 -> "Words Clock"
    20 -> "Coffee Shop"
    21 -> "Night Owl"
    22 -> "Retro Arcade"
    23 -> "Analog Zen"
    24 -> "Retro Flip"
    25 -> "Binary Pulse"
    26 -> "Solar Orbit"
    27 -> "Typewriter"
    28 -> "Liquid Gradient"
    29 -> "Nothing Admin"
    30 -> "Digital Frame"
    else -> "Neon Synthwave"
}

private const val GALLERY_STYLE_COUNT = 32

private enum class DashboardPanel {
    Calendar,
    Pomodoro,
    Media
}
