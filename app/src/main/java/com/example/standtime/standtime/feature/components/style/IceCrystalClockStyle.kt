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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun IceCrystalClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "ice")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing)),
        label = "t"
    )

    Box(
        modifier = modifier.background(Color(0xFF000D1A)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val maxLen = min(size.width, size.height) * 0.38f

            for (arm in 0..5) {
                val angle = arm * (Math.PI / 3.0).toFloat() + t * 0.08f
                for (seg in 0..4) {
                    val r1 = maxLen * (seg / 5f)
                    val r2 = maxLen * ((seg + 1) / 5f)
                    val x1 = cx + cos(angle) * r1
                    val y1 = cy + sin(angle) * r1
                    val x2 = cx + cos(angle) * r2
                    val y2 = cy + sin(angle) * r2

                    drawLine(
                        color = Color(0xFF8CDCFF).copy(alpha = 0.2f),
                        start = Offset(x1, y1),
                        end = Offset(x2, y2),
                        strokeWidth = 0.8f
                    )

                    if (seg % 2 == 0) {
                        val branchLen = r2 * 0.28f
                        val ba1 = angle + (Math.PI / 4.0).toFloat()
                        val ba2 = angle - (Math.PI / 4.0).toFloat()
                        drawLine(
                            color = Color(0xFF8CDCFF).copy(alpha = 0.15f),
                            start = Offset(x2, y2),
                            end = Offset(x2 + cos(ba1) * branchLen, y2 + sin(ba1) * branchLen),
                            strokeWidth = 0.5f
                        )
                        drawLine(
                            color = Color(0xFF8CDCFF).copy(alpha = 0.15f),
                            start = Offset(x2, y2),
                            end = Offset(x2 + cos(ba2) * branchLen, y2 + sin(ba2) * branchLen),
                            strokeWidth = 0.5f
                        )
                    }
                }
            }

            // Sparkles
            for (i in 0 until 18) {
                val sx = (i * 137.5f + t * 30f) % size.width
                val sy = (i * 71.3f + t * 20f) % size.height
                val alpha = (0.3f + 0.5f * sin(t * 3f + i * 0.7f)).coerceIn(0f, 1f)
                drawCircle(
                    color = Color(0xFFB4E6FF).copy(alpha = alpha),
                    radius = 1.2f + (i % 2) * 0.6f,
                    center = Offset(sx, sy)
                )
            }
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFE8F8FF),
                fontSize = 122.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(color = Color(0xFF88DDFF), blurRadius = 20f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000D1A, widthDp = 800, heightDp = 360)
@Composable
private fun IceCrystalClockStylePreview() = ClockStylePreviewFrame { modifier ->
    IceCrystalClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
