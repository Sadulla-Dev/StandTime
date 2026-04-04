package com.example.standtime.standtime.feature

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.galleryParts
import com.example.standtime.standtime.feature.components.style.CustomClockStyle
import com.example.standtime.standtime.feature.utils.CustomClockFont
import com.example.standtime.standtime.feature.utils.CustomClockLayout
import com.example.standtime.standtime.feature.utils.CustomColorValue
import com.example.standtime.standtime.feature.utils.StandTimeIntent
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.StandTimeUiState
import com.example.standtime.standtime.feature.utils.localizedStringResource
import com.example.standtime.ui.theme.StandTimeFontFamilies
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private const val PreviewReductionFactor = 3f

@Composable
fun CustomClockStudioPage(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val custom = state.customClockStyle
    val isLandscape =
        LocalConfiguration.current.screenWidthDp > LocalConfiguration.current.screenHeightDp
    var activePicker by remember { mutableStateOf<CustomClockColorTarget?>(null) }

    val picker = activePicker
    if (picker != null) {
        ClockColorPickerDialog(
            language = language,
            title = localizedStringResource(picker.titleRes, language),
            initialColor = when (picker) {
                CustomClockColorTarget.TEXT -> custom.textColor
                CustomClockColorTarget.BACKGROUND_START -> custom.backgroundStartColor
                CustomClockColorTarget.BACKGROUND_CENTER -> custom.backgroundCenterColor
                CustomClockColorTarget.BACKGROUND_END -> custom.backgroundEndColor
            },
            recentColors = custom.recentColors,
            onDismiss = { activePicker = null },
            onConfirm = { color ->
                when (picker) {
                    CustomClockColorTarget.TEXT -> onIntent(
                        StandTimeIntent.ChangeCustomClockTextColor(
                            color
                        )
                    )

                    CustomClockColorTarget.BACKGROUND_START -> onIntent(
                        StandTimeIntent.ChangeCustomClockBackgroundStart(
                            color
                        )
                    )

                    CustomClockColorTarget.BACKGROUND_CENTER -> onIntent(
                        StandTimeIntent.ChangeCustomClockBackgroundCenter(
                            color
                        )
                    )

                    CustomClockColorTarget.BACKGROUND_END -> onIntent(
                        StandTimeIntent.ChangeCustomClockBackgroundEnd(
                            color
                        )
                    )
                }
                activePicker = null
            }
        )
    }

    if (isLandscape) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StudioPreviewCard(
                state = state,
                language = language,
                accentColor = accentColor,
                onIntent = onIntent,
                modifier = Modifier
                    .weight(1.15f)
                    .fillMaxHeight()
            )
            StudioControlsCard(
                custom = custom,
                language = language,
                accentColor = accentColor,
                onIntent = onIntent,
                onPickColor = { activePicker = it },
                onClose = onClose,
                onSave = onSave,
                modifier = Modifier
                    .weight(0.95f)
                    .fillMaxHeight()
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StudioPreviewCard(
                state = state,
                language = language,
                accentColor = accentColor,
                onIntent = onIntent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
            StudioControlsCard(
                custom = custom,
                language = language,
                accentColor = accentColor,
                onIntent = onIntent,
                onPickColor = { activePicker = it },
                onClose = onClose,
                onSave = onSave,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StudioPreviewCard(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val custom = state.customClockStyle
    val parts = state.galleryParts()
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        tonalElevation = 8.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                        )
                    )
                )
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = localizedStringResource(R.string.custom_preview_title, language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = localizedStringResource(R.string.custom_drag_hint, language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                PhonePreviewCanvas(
                    parts = parts,
                    custom = custom,
                    onIntent = onIntent
                )
            }
        }
    }
}

@Composable
private fun StudioControlsCard(
    custom: com.example.standtime.standtime.feature.utils.CustomClockStyleSettings,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit,
    onPickColor: (CustomClockColorTarget) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        tonalElevation = 8.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    text = localizedStringResource(R.string.gallery_style_custom, language),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = localizedStringResource(R.string.custom_controls_hint, language),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                StudioSection(
                    title = localizedStringResource(
                        R.string.custom_layout_title,
                        language
                    )
                ) {
                    ChipRow {
                        FilterChip(
                            selected = custom.layout == CustomClockLayout.HORIZONTAL,
                            onClick = {
                                onIntent(
                                    StandTimeIntent.ChangeCustomClockLayout(
                                        CustomClockLayout.HORIZONTAL
                                    )
                                )
                            },
                            label = {
                                Text(
                                    localizedStringResource(
                                        R.string.custom_layout_horizontal,
                                        language
                                    )
                                )
                            }
                        )
                        FilterChip(
                            selected = custom.layout == CustomClockLayout.VERTICAL,
                            onClick = {
                                onIntent(
                                    StandTimeIntent.ChangeCustomClockLayout(
                                        CustomClockLayout.VERTICAL
                                    )
                                )
                            },
                            label = {
                                Text(
                                    localizedStringResource(
                                        R.string.custom_layout_vertical,
                                        language
                                    )
                                )
                            }
                        )
                    }
                }

                StudioSection(
                    title = localizedStringResource(
                        R.string.custom_elements_title,
                        language
                    )
                ) {
                    ChipRow {
                        FilterChip(
                            selected = custom.showSeconds,
                            onClick = {
                                onIntent(
                                    StandTimeIntent.ToggleCustomClockSeconds(!custom.showSeconds)
                                )
                            },
                            label = {
                                Text(
                                    localizedStringResource(
                                        R.string.custom_show_seconds,
                                        language
                                    )
                                )
                            }
                        )
                        FilterChip(
                            selected = custom.showDate,
                            onClick = {
                                onIntent(
                                    StandTimeIntent.ToggleCustomClockDate(!custom.showDate)
                                )
                            },
                            label = {
                                Text(
                                    localizedStringResource(
                                        R.string.custom_show_date,
                                        language
                                    )
                                )
                            }
                        )
                        FilterChip(
                            selected = custom.showWeather,
                            onClick = {
                                onIntent(
                                    StandTimeIntent.ToggleCustomClockWeather(!custom.showWeather)
                                )
                            },
                            label = {
                                Text(
                                    localizedStringResource(
                                        R.string.custom_show_weather,
                                        language
                                    )
                                )
                            }
                        )
                    }
                }

                StudioSection(
                    title = localizedStringResource(
                        R.string.custom_scale_title,
                        language
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                onIntent(
                                    StandTimeIntent.ChangeCustomClockScale(
                                        (custom.scale - 0.12f).coerceIn(
                                            0.45f,
                                            3.5f
                                        )
                                    )
                                )
                            }
                        ) {
                            Text(localizedStringResource(R.string.custom_zoom_out, language))
                        }
                        Text(
                            text = "${(custom.scale * 100).roundToInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        OutlinedButton(
                            onClick = {
                                onIntent(
                                    StandTimeIntent.ChangeCustomClockScale(
                                        (custom.scale + 0.12f).coerceIn(
                                            0.45f,
                                            3.5f
                                        )
                                    )
                                )
                            }
                        ) {
                            Text(localizedStringResource(R.string.custom_zoom_in, language))
                        }
                        OutlinedButton(
                            onClick = {
                                onIntent(StandTimeIntent.ChangeCustomClockOffset(0f, 0f))
                            }
                        ) {
                            Text(localizedStringResource(R.string.custom_center_button, language))
                        }
                    }
                }

                StudioSection(
                    title = localizedStringResource(
                        R.string.custom_color_title,
                        language
                    )
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ColorField(
                            title = localizedStringResource(R.string.custom_text_color, language),
                            color = custom.textColor,
                            onClick = { onPickColor(CustomClockColorTarget.TEXT) }
                        )
                        ColorField(
                            title = localizedStringResource(
                                R.string.custom_background_start,
                                language
                            ),
                            color = custom.backgroundStartColor,
                            onClick = { onPickColor(CustomClockColorTarget.BACKGROUND_START) }
                        )
                        FilterChip(
                            selected = custom.showBackgroundCenterColor,
                            onClick = {
                                onIntent(
                                    StandTimeIntent.ToggleCustomClockBackgroundCenter(
                                        !custom.showBackgroundCenterColor
                                    )
                                )
                            },
                            label = {
                                Text(
                                    localizedStringResource(
                                        R.string.custom_background_center_toggle,
                                        language
                                    )
                                )
                            }
                        )
                        if (custom.showBackgroundCenterColor) {
                            ColorField(
                                title = localizedStringResource(
                                    R.string.custom_background_center,
                                    language
                                ),
                                color = custom.backgroundCenterColor,
                                onClick = { onPickColor(CustomClockColorTarget.BACKGROUND_CENTER) }
                            )
                        }
                        FilterChip(
                            selected = custom.showBackgroundEndColor,
                            onClick = {
                                onIntent(
                                    StandTimeIntent.ToggleCustomClockBackgroundEnd(
                                        !custom.showBackgroundEndColor
                                    )
                                )
                            },
                            label = {
                                Text(
                                    localizedStringResource(
                                        R.string.custom_background_end_toggle,
                                        language
                                    )
                                )
                            }
                        )
                        if (custom.showBackgroundEndColor) {
                            ColorField(
                                title = localizedStringResource(
                                    R.string.custom_background_end,
                                    language
                                ),
                                color = custom.backgroundEndColor,
                                onClick = { onPickColor(CustomClockColorTarget.BACKGROUND_END) }
                            )
                        }
                    }
                }

                StudioSection(
                    title = localizedStringResource(
                        R.string.custom_font_title,
                        language
                    )
                ) {
                    FontCarousel(
                        custom = custom,
                        language = language,
                        onIntent = onIntent
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onClose,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(localizedStringResource(R.string.custom_close_button, language))
                }
                OutlinedButton(
                    onClick = onSave,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(localizedStringResource(R.string.custom_save_button, language))
                }
            }
        }
    }
}

@Composable
private fun FontCarousel(
    custom: com.example.standtime.standtime.feature.utils.CustomClockStyleSettings,
    language: StandTimeLanguage,
    onIntent: (StandTimeIntent) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val pages = remember {
        CustomClockFont.entries.chunked(4)
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp)
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pages.getOrNull(page).orEmpty().chunked(2).forEach { rowFonts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowFonts.forEach { font ->
                            FontOptionCard(
                                font = font,
                                language = language,
                                selected = custom.font == font,
                                modifier = Modifier.weight(1f),
                                onClick = { onIntent(StandTimeIntent.ChangeCustomClockFont(font)) }
                            )
                        }
                        repeat(2 - rowFonts.size) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pages.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            }
        }
    }
}

@Composable
private fun FontOptionCard(
    font: CustomClockFont,
    language: StandTimeLanguage,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val background = if (selected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    }
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp)),
        color = background,
        shape = RoundedCornerShape(22.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = localizedStringResource(R.string.custom_clock_sample_time, language),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = font.previewFamily(),
                fontWeight = font.previewWeight(),
                fontSize = if (font == CustomClockFont.TECH) 14.sp else 28.sp,
                letterSpacing = font.previewLetterSpacing(),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = font.displayName(language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ColorField(
    title: String,
    color: CustomColorValue,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(color.argb))
                )
                Text(
                    text = "#%08X".format(color.argb),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ClockColorPickerDialog(
    language: StandTimeLanguage,
    title: String,
    initialColor: CustomColorValue,
    recentColors: List<CustomColorValue>,
    onDismiss: () -> Unit,
    onConfirm: (CustomColorValue) -> Unit
) {
    val initialHsv = remember(initialColor) { initialColor.toHsv() }
    var hue by remember(initialColor) { mutableFloatStateOf(initialHsv[0]) }
    var saturation by remember(initialColor) { mutableFloatStateOf(initialHsv[1]) }
    var value by remember(initialColor) { mutableFloatStateOf(initialHsv[2]) }
    var paletteWidth by remember { mutableIntStateOf(0) }
    var paletteHeight by remember { mutableIntStateOf(0) }
    var hueWidth by remember { mutableIntStateOf(0) }

    val selectedColor = remember(hue, saturation, value) {
        Color.hsv(hue, saturation.coerceIn(0f, 1f), value.coerceIn(0f, 1f))
    }
    val presetColors = remember {
        listOf(
            Color(0xFFFFFFFF),
            Color(0xFF111827),
            Color(0xFFF97316),
            Color(0xFF38BDF8),
            Color(0xFF22C55E),
            Color(0xFFA855F7),
            Color(0xFFFACC15),
            Color(0xFFF43F5E),
            Color(0xFF14B8A6),
            Color(0xFFFB7185)
        )
    }

    fun updateFromColor(color: Color) {
        val hsv = color.toCustomColorValue().toHsv()
        hue = hsv[0]
        saturation = hsv[1]
        value = hsv[2]
    }

    fun updatePaletteSelection(x: Float, y: Float) {
        if (paletteWidth == 0 || paletteHeight == 0) return
        saturation = (x / paletteWidth.toFloat()).coerceIn(0f, 1f)
        value = 1f - (y / paletteHeight.toFloat()).coerceIn(0f, 1f)
    }

    fun updateHueSelection(x: Float) {
        if (hueWidth == 0) return
        hue = ((x / hueWidth.toFloat()).coerceIn(0f, 1f)) * 360f
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 460.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = localizedStringResource(R.string.custom_recent_colors, language),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ChipRow {
                    (recentColors + presetColors.map { it.toCustomColorValue() })
                        .distinctBy { it.argb }
                        .take(12)
                        .forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(color.argb))
                                    .clickable { updateFromColor(Color(color.argb)) }
                            )
                        }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.White, Color.hsv(hue, 1f, 1f))
                            )
                        )
                        .onSizeChanged {
                            paletteWidth = it.width
                            paletteHeight = it.height
                        }
                        .pointerInput(hue, paletteWidth, paletteHeight) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    updatePaletteSelection(
                                        offset.x,
                                        offset.y
                                    )
                                },
                                onDrag = { change, _ ->
                                    updatePaletteSelection(
                                        change.position.x,
                                        change.position.y
                                    )
                                }
                            )
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Black)
                                )
                            )
                    )
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    (saturation * paletteWidth).roundToInt() - 11,
                                    ((1f - value) * paletteHeight).roundToInt() - 11
                                )
                            }
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.Transparent)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.9f))
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Red,
                                    Color.Yellow,
                                    Color.Green,
                                    Color.Cyan,
                                    Color.Blue,
                                    Color.Magenta,
                                    Color.Red
                                )
                            )
                        )
                        .onSizeChanged { hueWidth = it.width }
                        .pointerInput(hueWidth) {
                            detectDragGestures(
                                onDragStart = { offset -> updateHueSelection(offset.x) },
                                onDrag = { change, _ -> updateHueSelection(change.position.x) }
                            )
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    ((hue / 360f) * hueWidth).roundToInt() - 11,
                                    3
                                )
                            }
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#%08X".format(selectedColor.toArgb().toLong() and 0xFFFFFFFFL),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(selectedColor)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedColor.toCustomColorValue()) }) {
                Text(localizedStringResource(R.string.custom_done, language))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(localizedStringResource(R.string.custom_cancel, language))
            }
        }
    )
}

@Composable
private fun StudioSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.4.sp
        )
        content()
    }
}

@Composable
private fun ChipRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

private enum class CustomClockColorTarget(val titleRes: Int) {
    TEXT(R.string.custom_text_color),
    BACKGROUND_START(R.string.custom_background_start),
    BACKGROUND_CENTER(R.string.custom_background_center),
    BACKGROUND_END(R.string.custom_background_end)
}

@Composable
private fun CustomClockFont.displayName(language: StandTimeLanguage): String = when (this) {
    CustomClockFont.MONO -> localizedStringResource(R.string.custom_font_mono, language)
    CustomClockFont.MONO_WIDE -> localizedStringResource(R.string.custom_font_mono_wide, language)
    CustomClockFont.SERIF_CLASSIC -> localizedStringResource(
        R.string.custom_font_serif_classic,
        language
    )

    CustomClockFont.SERIF_SOFT -> localizedStringResource(R.string.custom_font_serif_soft, language)
    CustomClockFont.SANS_CLEAN -> localizedStringResource(R.string.custom_font_sans_clean, language)
    CustomClockFont.SANS_BOLD -> localizedStringResource(R.string.custom_font_sans_bold, language)
    CustomClockFont.CONDENSED -> localizedStringResource(R.string.custom_font_condensed, language)
    CustomClockFont.CURSIVE -> localizedStringResource(R.string.custom_font_cursive, language)
    CustomClockFont.TECH -> localizedStringResource(R.string.custom_font_tech, language)
    CustomClockFont.POSTER -> localizedStringResource(R.string.custom_font_poster, language)
    CustomClockFont.ELEGANT -> localizedStringResource(R.string.custom_font_elegant, language)
    CustomClockFont.MINIMAL -> localizedStringResource(R.string.custom_font_minimal, language)
}

private fun CustomClockFont.previewFamily(): FontFamily = when (this) {
    CustomClockFont.MONO -> StandTimeFontFamilies.Inter
    CustomClockFont.MONO_WIDE,
    CustomClockFont.CONDENSED,
    CustomClockFont.POSTER -> StandTimeFontFamilies.Oswald

    CustomClockFont.SERIF_CLASSIC,
    CustomClockFont.SERIF_SOFT,
    CustomClockFont.ELEGANT -> StandTimeFontFamilies.PlayfairDisplay

    CustomClockFont.SANS_CLEAN -> StandTimeFontFamilies.Inter
    CustomClockFont.SANS_BOLD -> StandTimeFontFamilies.Poppins
    CustomClockFont.CURSIVE -> StandTimeFontFamilies.Caveat
    CustomClockFont.TECH -> StandTimeFontFamilies.PressStart2P
    CustomClockFont.MINIMAL -> StandTimeFontFamilies.Nunito
}

private fun CustomClockFont.previewWeight(): FontWeight = when (this) {
    CustomClockFont.MINIMAL,
    CustomClockFont.ELEGANT -> FontWeight.Light

    CustomClockFont.SANS_CLEAN,
    CustomClockFont.SERIF_SOFT -> FontWeight.Medium

    CustomClockFont.TECH -> FontWeight.Normal
    CustomClockFont.MONO,
    CustomClockFont.SERIF_CLASSIC,
    CustomClockFont.CURSIVE -> FontWeight.Bold

    CustomClockFont.MONO_WIDE,
    CustomClockFont.SANS_BOLD,
    CustomClockFont.CONDENSED,
    CustomClockFont.POSTER -> FontWeight.Black
}

private fun CustomClockFont.previewLetterSpacing() = when (this) {
    CustomClockFont.MONO_WIDE -> 3.sp
    CustomClockFont.TECH -> 2.sp
    CustomClockFont.CONDENSED -> (-2).sp
    else -> 0.sp
}

private fun CustomColorValue.toHsv(): FloatArray {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(argb.toInt(), hsv)
    return hsv
}

private fun Color.toCustomColorValue(): CustomColorValue {
    return CustomColorValue(toArgb().toLong() and 0xFFFFFFFFL)
}

@Composable
private fun PhonePreviewCanvas(
    parts: com.example.standtime.standtime.feature.components.GalleryClockParts,
    custom: com.example.standtime.standtime.feature.utils.CustomClockStyleSettings,
    onIntent: (StandTimeIntent) -> Unit
) {
    var previewScale by remember(custom.scale) { mutableFloatStateOf(custom.scale) }
    var previewOffsetX by remember(custom.offsetX) { mutableFloatStateOf(custom.offsetX) }
    var previewOffsetY by remember(custom.offsetY) { mutableFloatStateOf(custom.offsetY) }
    val previewLayoutScale = if (custom.layout == CustomClockLayout.VERTICAL) 0.8f else 1f

    LaunchedEffect(previewScale, previewOffsetX, previewOffsetY) {
        delay(90)
        if (kotlin.math.abs(previewScale - custom.scale) > 0.001f) {
            onIntent(StandTimeIntent.ChangeCustomClockScale(previewScale))
        }
        if (
            kotlin.math.abs(previewOffsetX - custom.offsetX) > 0.5f ||
            kotlin.math.abs(previewOffsetY - custom.offsetY) > 0.5f
        ) {
            onIntent(StandTimeIntent.ChangeCustomClockOffset(previewOffsetX, previewOffsetY))
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val previewWidth = preferredPreviewWidth(maxWidth)
        Box(
            modifier = Modifier
                .width(previewWidth)
                .clip(RoundedCornerShape(34.dp))
                .pointerInput(custom.scale, custom.offsetX, custom.offsetY) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        previewScale = (previewScale * zoom).coerceIn(0.45f, 3.5f)
                        previewOffsetX += pan.x * PreviewReductionFactor
                        previewOffsetY += pan.y * PreviewReductionFactor
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            val backgroundColors = buildList {
                add(Color(custom.backgroundStartColor.argb))
                if (custom.showBackgroundCenterColor) {
                    add(Color(custom.backgroundCenterColor.argb))
                }
                if (custom.showBackgroundEndColor) {
                    add(Color(custom.backgroundEndColor.argb))
                }
            }
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (backgroundColors.size == 1) {
                            Brush.linearGradient(
                                listOf(
                                    backgroundColors.first(),
                                    backgroundColors.first()
                                )
                            )
                        } else {
                            Brush.linearGradient(backgroundColors)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                val virtualWidth = maxWidth * PreviewReductionFactor
                val virtualHeight = maxHeight * PreviewReductionFactor
                Box(
                    modifier = Modifier
                        .width(virtualWidth)
                        .height(virtualHeight)
                        .graphicsLayer {
                            scaleX = 1f / PreviewReductionFactor
                            scaleY = 1f / PreviewReductionFactor
                        }
                ) {
                    CustomClockStyle(
                        parts = parts,
                        custom = custom.copy(
                            scale = previewScale * previewLayoutScale,
                            offsetX = previewOffsetX,
                            offsetY = previewOffsetY
                        ),
                        isStudio = true,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

private fun preferredPreviewWidth(maxWidth: Dp): Dp {
    return when {
        maxWidth > 420.dp -> 360.dp
        maxWidth > 360.dp -> 320.dp
        else -> maxWidth * 0.9f
    }
}

private fun preferredPreviewHeight(maxHeight: Dp): Dp {
    return when {
        maxHeight > 260.dp -> 170.dp
        maxHeight > 220.dp -> 150.dp
        else -> maxHeight * 0.72f
    }
}
