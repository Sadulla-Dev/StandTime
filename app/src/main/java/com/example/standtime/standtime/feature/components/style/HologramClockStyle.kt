package com.standtime.clock.standtime.feature.components.style

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlin.math.min
import kotlin.math.sin

@Composable
fun HologramClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "holo")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "t"
    )
    val flicker = (0.85f + 0.15f * sin(t * 7f)).coerceIn(0f, 1f)

    Box(
        modifier = modifier.background(Color(0xFF000810)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val maxR = min(size.width, size.height) * 0.45f

            // Scan lines
            var y = 0f
            while (y < size.height) {
                val alpha = (0.03f + 0.02f * sin(y * 0.1f + t)).coerceIn(0f, 1f)
                drawLine(
                    color = Color(0xFF00CCFF).copy(alpha = alpha * flicker),
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += 3f
            }

            // Concentric rings
            var r = 20f
            while (r < maxR) {
                drawCircle(
                    color = Color(0xFF00CCFF).copy(alpha = 0.2f * flicker),
                    radius = r,
                    center = androidx.compose.ui.geometry.Offset(cx, cy),
                    style = Stroke(width = 0.8f)
                )
                r += 18f
            }
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFAAEEFF).copy(alpha = flicker),
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                shadow = Shadow(
                    color = Color(0xFF00DDFF),
                    blurRadius = 20f
                )
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000810, widthDp = 800, heightDp = 360)
@Composable
private fun HologramClockStylePreview() = ClockStylePreviewFrame { modifier ->
    HologramClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
