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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import kotlin.math.sin

@Composable
fun AuroraClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val timeSeconds = rememberContinuousAnimationSeconds()

    Box(
        modifier = modifier.background(Color(0xFF000508)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Aurora wave layers
            for (layer in 0..4) {
                val hue = 120f + layer * 25f + sin(timeSeconds * 0.55f + layer).toFloat() * 20f
                val baseY = size.height * 0.2f + layer * size.height * 0.05f
                val waveColor = Color.hsv(hue, 0.8f, 0.6f).copy(alpha = 0.08f)
                val strokeColor = Color.hsv(hue, 0.9f, 0.85f).copy(alpha = 0.3f)

                // Build wave path
                val path = Path()
                path.moveTo(0f, 0f)
                path.lineTo(0f, baseY)
                val steps = 80
                for (s in 0..steps) {
                    val x = s * size.width / steps
                    val y = baseY +
                            sin(s.toFloat() / steps * Math.PI.toFloat() * 2f + timeSeconds * 0.75f + layer * 0.7f) * (size.height * 0.1f) +
                            sin(s.toFloat() / steps * Math.PI.toFloat() * 3f - timeSeconds * 0.95f + layer) * (size.height * 0.06f)
                    if (s == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.lineTo(size.width, 0f)
                path.close()
                drawPath(path, waveColor, style = Fill)

                // Wave line
                val linePath = Path()
                for (s in 0..steps) {
                    val x = s * size.width / steps
                    val y = baseY +
                            sin(s.toFloat() / steps * Math.PI.toFloat() * 2f + timeSeconds * 0.75f + layer * 0.7f) * (size.height * 0.1f) +
                            sin(s.toFloat() / steps * Math.PI.toFloat() * 3f - timeSeconds * 0.95f + layer) * (size.height * 0.06f)
                    if (s == 0) linePath.moveTo(x, y) else linePath.lineTo(x, y)
                }
                drawPath(linePath, strokeColor, style = Stroke(width = 1.5f))
            }

            // Stars
            val starCount = 80
            for (i in 0 until starCount) {
                val sx = (i * 137.5f) % size.width
                val sy = (i * 73.1f) % (size.height * 0.6f)
                val alpha = (0.3f + 0.4f * sin(timeSeconds * 1.4f + i * 0.2f)).coerceIn(0f, 1f)
                drawCircle(
                    color = Color.White.copy(alpha = alpha),
                    radius = 0.8f + (i % 3) * 0.3f,
                    center = Offset(sx, sy)
                )
            }
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFCCFFEE),
                fontSize = 102.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(color = Color(0xFF00FFAA), blurRadius = 22f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000508, widthDp = 800, heightDp = 360)
@Composable
private fun AuroraClockStylePreview() = ClockStylePreviewFrame { modifier ->
    AuroraClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
