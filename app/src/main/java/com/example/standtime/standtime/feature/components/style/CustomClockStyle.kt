package com.standtime.clock.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.components.LocalGalleryScaleFactor
import com.standtime.clock.standtime.feature.utils.CustomClockFont
import com.standtime.clock.standtime.feature.utils.CustomClockLayout
import com.standtime.clock.standtime.feature.utils.CustomClockStyleSettings
import com.standtime.clock.ui.theme.StandTimeFontFamilies

@Composable
fun CustomClockStyle(
    parts: GalleryClockParts,
    custom: CustomClockStyleSettings,
    isStudio: Boolean = false,
    modifier: Modifier = Modifier
) {
    val scale = LocalGalleryScaleFactor.current
    val textColor = Color(custom.textColor.argb)
    val isVertical = custom.layout == CustomClockLayout.VERTICAL
    val mainSize = if (isVertical) {
        (76f * scale).coerceIn(34f, 86f)
    } else {
        (118f * scale).coerceIn(54f, 136f)
    }
    val compactMetaSize = if (isVertical) {
        (22f * scale).coerceIn(12f, 30f)
    } else {
        (84f * scale).coerceIn(40f, 92f)
    }
    val regularMetaSize = if (isVertical) {
        (14f * scale).coerceIn(10f, 18f)
    } else {
        (22f * scale).coerceIn(12f, 28f)
    }
    val backgroundColors = buildList {
        add(Color(custom.backgroundStartColor.argb))
        if (custom.showBackgroundCenterColor) {
            add(Color(custom.backgroundCenterColor.argb))
        }
        if (custom.showBackgroundEndColor) {
            add(Color(custom.backgroundEndColor.argb))
        }
    }
    val backgroundBrush = if (isStudio) {
        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
    } else {
        if (backgroundColors.size == 1) {
            Brush.linearGradient(
                listOf(backgroundColors.first(), backgroundColors.first())
            )
        } else {
            Brush.linearGradient(backgroundColors)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        val contentModifier = Modifier.graphicsLayer {
            scaleX = custom.scale
            scaleY = custom.scale
            translationX = custom.offsetX
            translationY = custom.offsetY
        }

        Column(
            modifier = contentModifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isVertical) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy((2f * scale).coerceIn(1f, 4f).dp)
                ) {
                    TextBlock(parts.hours, textColor, custom, mainSize)
                    TextBlock(
                        value = parts.minutes,
                        color = textColor.copy(alpha = 0.95f),
                        custom = custom,
                        size = mainSize
                    )
                }
                if (custom.showSeconds) {
                    MetaLine(
                        parts.seconds,
                        textColor.copy(alpha = 0.8f),
                        custom,
                        compactMetaSize
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextBlock(parts.hours, textColor, custom, mainSize)
                    MetaLine(":", textColor.copy(alpha = 0.9f), custom, compactMetaSize)
                    TextBlock(parts.minutes, textColor.copy(alpha = 0.95f), custom, mainSize)
                    if (custom.showSeconds) {
                        Spacer(modifier = Modifier.width(10.dp))
                        MetaLine(parts.seconds, textColor.copy(alpha = 0.8f), custom, compactMetaSize)
                    }
                }
            }

            if (custom.showDate) {
                MetaLine(parts.dateText, textColor.copy(alpha = 0.7f), custom, regularMetaSize)
            }
            if (custom.showWeather) {
                MetaLine(
                    listOf(parts.weatherTemperature, parts.weatherSummary)
                        .filter { it.isNotBlank() }
                        .joinToString("  •  ")
                        .ifBlank { "Weather" },
                    textColor.copy(alpha = 0.6f),
                    custom,
                    regularMetaSize
                )
            }
        }
    }
}

@Composable
private fun TextBlock(
    value: String,
    color: Color,
    custom: CustomClockStyleSettings,
    size: Float,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Text(
        text = value,
        modifier = modifier,
        color = color,
        textAlign = TextAlign.Center,
        style = custom.font.toTextStyle(size),
        softWrap = false,
        maxLines = 1
    )
}

@Composable
private fun MetaLine(
    value: String,
    color: Color,
    custom: CustomClockStyleSettings,
    size: Float,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Text(
        text = value,
        modifier = modifier,
        color = color,
        textAlign = TextAlign.Center,
        style = custom.font.toTextStyle(size),
        softWrap = false,
        maxLines = 1
    )
}

private fun CustomClockFont.toTextStyle(size: Float): TextStyle {
    val base = TextStyle(
        lineHeight = (size * 0.88f).sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )
    return when (this) {
        CustomClockFont.MONO -> base.copy(fontFamily = StandTimeFontFamilies.Inter, fontWeight = FontWeight.Bold, fontSize = size.sp)
        CustomClockFont.MONO_WIDE -> base.copy(fontFamily = StandTimeFontFamilies.Oswald, fontWeight = FontWeight.Bold, letterSpacing = 3.sp, fontSize = size.sp)
        CustomClockFont.SERIF_CLASSIC -> base.copy(fontFamily = StandTimeFontFamilies.PlayfairDisplay, fontWeight = FontWeight.Bold, fontSize = size.sp)
        CustomClockFont.SERIF_SOFT -> base.copy(fontFamily = StandTimeFontFamilies.PlayfairDisplay, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic, fontSize = size.sp)
        CustomClockFont.SANS_CLEAN -> base.copy(fontFamily = StandTimeFontFamilies.Inter, fontWeight = FontWeight.Medium, fontSize = size.sp)
        CustomClockFont.SANS_BOLD -> base.copy(fontFamily = StandTimeFontFamilies.Poppins, fontWeight = FontWeight.Bold, fontSize = size.sp)
        CustomClockFont.CONDENSED -> base.copy(fontFamily = StandTimeFontFamilies.Oswald, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp, fontSize = size.sp)
        CustomClockFont.CURSIVE -> base.copy(fontFamily = StandTimeFontFamilies.Caveat, fontWeight = FontWeight.Bold, fontSize = size.sp)
        CustomClockFont.TECH -> base.copy(fontFamily = StandTimeFontFamilies.PressStart2P, fontWeight = FontWeight.Normal, fontSize = (size * 0.58f).sp, letterSpacing = 1.sp)
        CustomClockFont.POSTER -> base.copy(fontFamily = StandTimeFontFamilies.Oswald, fontWeight = FontWeight.Black, letterSpacing = 1.sp, fontSize = size.sp)
        CustomClockFont.ELEGANT -> base.copy(fontFamily = StandTimeFontFamilies.PlayfairDisplay, fontWeight = FontWeight.Light, fontStyle = FontStyle.Italic, fontSize = size.sp)
        CustomClockFont.MINIMAL -> base.copy(fontFamily = StandTimeFontFamilies.Nunito, fontWeight = FontWeight.Light, fontSize = size.sp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun CustomClockStylePreview() = ClockStylePreviewFrame { modifier ->
    CustomClockStyle(
        parts = ClockStylePreviewParts,
        custom = ClockStylePreviewCustomStyle,
        modifier = modifier
    )
}
