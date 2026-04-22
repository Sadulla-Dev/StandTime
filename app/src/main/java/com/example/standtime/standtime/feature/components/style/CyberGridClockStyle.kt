package com.standtime.clock.standtime.feature.components.style

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import kotlin.math.pow

@Composable
fun CyberGridClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val timeSeconds = rememberContinuousAnimationSeconds()

    Box(
        modifier = modifier.background(Color(0xFF000510)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val vp = size.width / 2f
            val horizon = size.height * 0.5f
            val gridStep = size.width * 0.08f

            // Perspective lines from vanishing point
            var x = -size.width
            while (x < size.width * 2) {
                drawLine(
                    color = Color(0xFF0096FF).copy(alpha = 0.2f),
                    start = Offset(vp, horizon),
                    end = Offset(x, size.height),
                    strokeWidth = 0.5f
                )
                x += gridStep
            }

            // Moving horizontal lines
            for (i in 0..7) {
                val pct = ((i.toFloat() / 8f + timeSeconds * 0.10f) % 1f).pow(2f)
                val y = horizon + (size.height - horizon) * pct
                val alpha = (0.05f + pct * 0.25f).coerceIn(0f, 1f)
                drawLine(
                    color = Color(0xFF0096FF).copy(alpha = alpha),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 0.8f
                )
            }

            // Radial glow at horizon
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0064FF).copy(alpha = 0.35f),
                        Color.Transparent
                    ),
                    center = Offset(vp, horizon),
                    radius = size.width * 0.35f
                ),
                radius = size.width * 0.35f,
                center = Offset(vp, horizon)
            )
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFF88CCFF),
                fontSize = 102.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                shadow = Shadow(color = Color(0xFF0066FF), blurRadius = 22f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000510, widthDp = 800, heightDp = 360)
@Composable
private fun CyberGridClockStylePreview() = ClockStylePreviewFrame { modifier ->
    CyberGridClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
