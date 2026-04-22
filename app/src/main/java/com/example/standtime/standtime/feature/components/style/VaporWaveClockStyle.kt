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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import kotlin.math.pow

@Composable
fun VaporWaveClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Color(0xFF0A0015)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val horizonY = size.height * 0.52f

            // Background gradient
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF780FFF).copy(alpha = 0.3f),
                        Color(0xFFFF00B4).copy(alpha = 0.15f),
                        Color(0xFFFF6400).copy(alpha = 0.08f)
                    )
                )
            )

            // Sun
            val sunR = size.width * 0.14f
            val sunY = horizonY - sunR * 0.2f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFFF88), Color(0xFFFF88CC), Color.Transparent),
                    center = Offset(cx, sunY),
                    radius = sunR
                ),
                radius = sunR,
                center = Offset(cx, sunY)
            )
            // Sun stripes
            for (i in 0..4) {
                val sy = sunY - sunR * 0.5f + i * sunR * 0.25f
                drawRect(
                    color = Color(0xFF0A0015).copy(alpha = 0.55f),
                    topLeft = Offset(cx - sunR, sy),
                    size = androidx.compose.ui.geometry.Size(sunR * 2, 3f)
                )
            }

            // Grid floor
            val gridStep = size.width * 0.07f
            var gx = -size.width
            while (gx < size.width * 2) {
                drawLine(
                    color = Color(0xFFC800FF).copy(alpha = 0.4f),
                    start = Offset(cx, horizonY),
                    end = Offset(gx, size.height),
                    strokeWidth = 0.8f
                )
                gx += gridStep
            }
            for (i in 0..5) {
                val pct = (i.toFloat() / 5f).pow(1.5f)
                val gy = horizonY + (size.height - horizonY) * pct
                drawLine(
                    color = Color(0xFFC800FF).copy(alpha = 0.1f + pct * 0.3f),
                    start = Offset(0f, gy),
                    end = Offset(size.width, gy),
                    strokeWidth = 0.8f
                )
            }
        }

        // Time text in upper area
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
                style = TextStyle(
                    color = Color(0xFFFFCCFF),
                    fontSize = 100.sp,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(color = Color(0xFFFF44FF), blurRadius = 18f)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0015, widthDp = 800, heightDp = 360)
@Composable
private fun VaporWaveClockStylePreview() = ClockStylePreviewFrame { modifier ->
    VaporWaveClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
