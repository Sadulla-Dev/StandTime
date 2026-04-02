package com.example.standtime.standtime.feature.components.style

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.components.LocalGalleryScaleFactor
import com.example.standtime.standtime.feature.utils.CustomClockFont
import com.example.standtime.standtime.feature.utils.CustomClockLayout
import com.example.standtime.standtime.feature.utils.CustomClockStyleSettings
import com.example.standtime.ui.theme.StandTimeFontFamilies

@Composable
fun CustomClockStyle(
    parts: GalleryClockParts,
    custom: CustomClockStyleSettings,
    modifier: Modifier = Modifier
) {
    val scale = LocalGalleryScaleFactor.current
    val textColor = Color(custom.textColor.argb)
    val backgroundColors = buildList {
        add(Color(custom.backgroundStartColor.argb))
        if (custom.showBackgroundCenterColor) {
            add(Color(custom.backgroundCenterColor.argb))
        }
        if (custom.showBackgroundEndColor) {
            add(Color(custom.backgroundEndColor.argb))
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (backgroundColors.size == 1) {
                    Brush.linearGradient(listOf(backgroundColors.first(), backgroundColors.first()))
                } else {
                    Brush.linearGradient(backgroundColors)
                }
            ),
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
            if (custom.layout == CustomClockLayout.VERTICAL) {
                TextBlock(parts.hours, textColor, custom, scale)
                TextBlock(parts.minutes, textColor.copy(alpha = 0.95f), custom, scale)
                if (custom.showSeconds) {
                    MetaLine(parts.seconds, textColor.copy(alpha = 0.8f), custom, scale, compact = true)
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextBlock(parts.hours, textColor, custom, scale)
                    MetaLine(":", textColor.copy(alpha = 0.9f), custom, scale, compact = true)
                    TextBlock(parts.minutes, textColor.copy(alpha = 0.95f), custom, scale)
                    if (custom.showSeconds) {
                        Spacer(modifier = Modifier.width(10.dp))
                        MetaLine(parts.seconds, textColor.copy(alpha = 0.8f), custom, scale, compact = true)
                    }
                }
            }

            if (custom.showDate) {
                MetaLine(parts.dateText, textColor.copy(alpha = 0.7f), custom, scale)
            }
            if (custom.showWeather) {
                MetaLine(
                    listOf(parts.weatherTemperature, parts.weatherSummary)
                        .filter { it.isNotBlank() }
                        .joinToString("  •  ")
                        .ifBlank { "Weather" },
                    textColor.copy(alpha = 0.6f),
                    custom,
                    scale
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
    scale: Float
) {
    androidx.compose.material3.Text(
        text = value,
        color = color,
        style = custom.font.toTextStyle((118f * scale).coerceIn(54f, 136f))
    )
}

@Composable
private fun MetaLine(
    value: String,
    color: Color,
    custom: CustomClockStyleSettings,
    scale: Float,
    compact: Boolean = false
) {
    androidx.compose.material3.Text(
        text = value,
        color = color,
        style = custom.font.toTextStyle(
            if (compact) (84f * scale).coerceIn(40f, 92f) else (22f * scale).coerceIn(12f, 28f)
        )
    )
}

private fun CustomClockFont.toTextStyle(size: Float): TextStyle = when (this) {
    CustomClockFont.MONO -> TextStyle(fontFamily = StandTimeFontFamilies.Inter, fontWeight = FontWeight.Bold, fontSize = size.sp)
    CustomClockFont.MONO_WIDE -> TextStyle(fontFamily = StandTimeFontFamilies.Oswald, fontWeight = FontWeight.Bold, letterSpacing = 3.sp, fontSize = size.sp)
    CustomClockFont.SERIF_CLASSIC -> TextStyle(fontFamily = StandTimeFontFamilies.PlayfairDisplay, fontWeight = FontWeight.Bold, fontSize = size.sp)
    CustomClockFont.SERIF_SOFT -> TextStyle(fontFamily = StandTimeFontFamilies.PlayfairDisplay, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic, fontSize = size.sp)
    CustomClockFont.SANS_CLEAN -> TextStyle(fontFamily = StandTimeFontFamilies.Inter, fontWeight = FontWeight.Medium, fontSize = size.sp)
    CustomClockFont.SANS_BOLD -> TextStyle(fontFamily = StandTimeFontFamilies.Poppins, fontWeight = FontWeight.Bold, fontSize = size.sp)
    CustomClockFont.CONDENSED -> TextStyle(fontFamily = StandTimeFontFamilies.Oswald, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp, fontSize = size.sp)
    CustomClockFont.CURSIVE -> TextStyle(fontFamily = StandTimeFontFamilies.Caveat, fontWeight = FontWeight.Bold, fontSize = size.sp)
    CustomClockFont.TECH -> TextStyle(fontFamily = StandTimeFontFamilies.PressStart2P, fontWeight = FontWeight.Normal, fontSize = (size * 0.58f).sp, letterSpacing = 1.sp)
    CustomClockFont.POSTER -> TextStyle(fontFamily = StandTimeFontFamilies.Oswald, fontWeight = FontWeight.Black, letterSpacing = 1.sp, fontSize = size.sp)
    CustomClockFont.ELEGANT -> TextStyle(fontFamily = StandTimeFontFamilies.PlayfairDisplay, fontWeight = FontWeight.Light, fontStyle = FontStyle.Italic, fontSize = size.sp)
    CustomClockFont.MINIMAL -> TextStyle(fontFamily = StandTimeFontFamilies.Nunito, fontWeight = FontWeight.Light, fontSize = size.sp)
}
