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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage

@Composable
fun GoldLuxuryClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Color(0xFF050400)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Filigree circle rings (top-left and bottom-right)
            for (i in 0..7) {
                val r = size.height * (0.25f + i * 0.03f)
                drawCircle(
                    color = Color(0xFFC8A028).copy(alpha = 0.15f),
                    radius = r,
                    center = Offset(size.width * 0.15f, size.height * 0.2f),
                    style = Stroke(width = 0.5f)
                )
                drawCircle(
                    color = Color(0xFFC8A028).copy(alpha = 0.12f),
                    radius = r * 1.1f,
                    center = Offset(size.width * 0.85f, size.height * 0.8f),
                    style = Stroke(width = 0.5f)
                )
            }

            // Diagonal gold hatching
            for (i in -5..15) {
                drawLine(
                    color = Color(0xFFC8A028).copy(alpha = 0.06f),
                    start = Offset(i * size.width * 0.12f, 0f),
                    end = Offset(i * size.width * 0.12f + size.width * 0.4f, size.height),
                    strokeWidth = 0.5f
                )
            }

            // Centre radial glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFC89618).copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = size.width * 0.4f
                ),
                radius = size.width * 0.4f,
                center = Offset(size.width / 2f, size.height / 2f)
            )

            // Corner ornaments
            listOf(
                Offset(14f, 14f), Offset(size.width - 14f, 14f),
                Offset(14f, size.height - 14f), Offset(size.width - 14f, size.height - 14f)
            ).forEach { o ->
                drawCircle(color = Color(0xFFC8A028).copy(alpha = 0.3f), radius = 8f, center = o, style = Stroke(0.8f))
                drawCircle(color = Color(0xFFC8A028).copy(alpha = 0.2f), radius = 14f, center = o, style = Stroke(0.5f))
            }

            // Thin border frame
            drawRect(color = Color(0xFFC8A028).copy(alpha = 0.15f), style = Stroke(width = 0.8f))
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFFE080), Color(0xFFFFD040), Color(0xFFCC9900))
                ),
                fontSize = 122.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                shadow = Shadow(color = Color(0xFFCC9900), blurRadius = 22f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050400, widthDp = 800, heightDp = 360)
@Composable
private fun GoldLuxuryClockStylePreview() = ClockStylePreviewFrame { modifier ->
    GoldLuxuryClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
