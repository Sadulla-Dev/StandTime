package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
fun HorizonStudioClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val scale = LocalGalleryScaleFactor.current
    val accent = if (accentColor.alpha > 0f) accentColor.copy(alpha = 0.95f) else Color(0xFFE67E22)
    val primary = Color(0xFF2D3436)
    val secondary = Color(0xFF2D3436).copy(alpha = 0.36f)
    val lineColor = Color.Black.copy(alpha = 0.06f)
    val progress = ((parts.seconds.toIntOrNull() ?: 0).coerceIn(0, 59)) / 60f

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        val lineWidth = maxWidth * 0.54f
        val lineY = maxHeight * 0.86f
        val numberSize = (128f * scale).coerceIn(72f, 150f).sp

        Canvas(modifier = Modifier.fillMaxSize().align(Alignment.BottomCenter)) {
            val strokeY = lineY.toPx()
            val startX = (size.width - lineWidth.toPx()) / 2f
            val endX = startX + lineWidth.toPx()
            drawLine(
                color = lineColor,
                start = androidx.compose.ui.geometry.Offset(startX, strokeY),
                end = androidx.compose.ui.geometry.Offset(endX, strokeY),
                strokeWidth = 1.5f * density
            )
            drawCircle(
                color = accent,
                radius = 3.6f * density,
                center = androidx.compose.ui.geometry.Offset(startX + lineWidth.toPx() * progress, strokeY)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .offset(y = (-16f * scale).coerceIn(-28f, -8f).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = parts.hours,
                color = primary,
                fontSize = numberSize,
                fontWeight = FontWeight.ExtraLight,
                letterSpacing = (8f * scale).coerceIn(4f, 10f).sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = parts.minutes,
                color = secondary,
                fontSize = numberSize,
                fontWeight = FontWeight.Thin,
                letterSpacing = (8f * scale).coerceIn(4f, 10f).sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
