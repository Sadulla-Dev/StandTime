package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.components.LocalGalleryScaleFactor
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

@Composable
fun ContrastSplitClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val scale = LocalGalleryScaleFactor.current
    val accent = if (accentColor.alpha > 0f) accentColor.copy(alpha = 0.95f) else Color(0xFFE67E22)
    val lightBg = Color(0xFFF8F9FA)
    val darkBg = Color(0xFF1A1A1A)
    val darkText = Color(0xFF2D3436)
    val seconds = (parts.seconds.toIntOrNull() ?: 0).coerceIn(0, 59)
    val progress = seconds / 60f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBg)
    ) {
        val numberSize = (138f * scale * 1.7f).coerceIn(78f, 300f).sp
        val horizontalPadding = (28f * scale).coerceIn(16f, 36f).dp

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = lightBg,
                size = androidx.compose.ui.geometry.Size(size.width / 2f, size.height)
            )
            drawRect(
                color = darkBg,
                topLeft = androidx.compose.ui.geometry.Offset(size.width / 2f, 0f),
                size = androidx.compose.ui.geometry.Size(size.width / 2f, size.height)
            )

            val centerX = size.width / 2f
            val centerY = size.height / 2f
            drawRect(
                color = accent,
                topLeft = androidx.compose.ui.geometry.Offset(centerX - density, centerY - 100f * density),
                size = androidx.compose.ui.geometry.Size(2f * density, 200f * density)
            )

            val barHeight = 2f * density
            drawRect(
                color = Color.Black.copy(alpha = 0.05f),
                topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - barHeight),
                size = androidx.compose.ui.geometry.Size(size.width, barHeight)
            )
            drawRect(
                color = accent,
                topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - barHeight),
                size = androidx.compose.ui.geometry.Size(size.width * progress, barHeight)
            )
        }

        Text(
            text = parts.hours,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(0.5f)
                .padding(start = horizontalPadding),
            color = darkText,
            fontSize = numberSize,
            fontWeight = FontWeight.ExtraLight,
            textAlign = TextAlign.Center,
            letterSpacing = (-2).sp
        )

        Text(
            text = parts.minutes,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxWidth(0.5f)
                .padding(end = horizontalPadding),
            color = Color.White,
            fontSize = numberSize,
            fontWeight = FontWeight.Thin,
            textAlign = TextAlign.Center,
            letterSpacing = (-2).sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun ContrastSplitClockStylePreview() = ClockStylePreviewFrame { modifier ->
    ContrastSplitClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
