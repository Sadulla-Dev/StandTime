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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import kotlin.math.pow
import kotlin.math.sin

@Composable
fun DesertStormClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "desert")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing)),
        label = "t"
    )

    Box(
        modifier = modifier.background(Color(0xFF0A0500)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Sun glow top right
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFC850).copy(alpha = 0.55f),
                        Color(0xFFFF9628).copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.78f, size.height * 0.25f),
                    radius = size.width * 0.25f
                ),
                radius = size.width * 0.25f,
                center = Offset(size.width * 0.78f, size.height * 0.25f)
            )

            // Dunes
            val duneConfigs = listOf(
                Triple(0.12f, 0.7f, 0.08f),
                Triple(0.08f, 1.3f, 0.06f),
                Triple(0.05f, 2.1f, 0.04f)
            )
            duneConfigs.forEachIndexed { d, (amp, freq, alpha) ->
                val path = Path()
                path.moveTo(0f, size.height)
                val baseY = size.height * (0.5f + d * 0.12f)
                path.lineTo(0f, baseY)
                for (x in 0..size.width.toInt() step 4) {
                    val y = baseY + sin(x / size.width * Math.PI.toFloat() * freq + t + d) * size.height * amp
                    path.lineTo(x.toFloat(), y)
                }
                path.lineTo(size.width, size.height)
                path.close()
                drawPath(path, Color(0xFFB46432).copy(alpha = alpha), style = Fill)
            }

            // Drifting sand particles
            for (i in 0 until 30) {
                val px = (i * 97.3f + t * 25f) % size.width
                val py = size.height * 0.3f + (i * 57.7f + t * 15f) % (size.height * 0.5f)
                val a = (0.1f + 0.2f * sin(t * 3f + i)).coerceIn(0f, 1f)
                drawCircle(color = Color(0xFFDCA03C).copy(alpha = a), radius = 1f, center = Offset(px, py))
            }
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFFFDDAA),
                fontSize = 122.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(color = Color(0xFFFF9922), blurRadius = 18f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0500, widthDp = 800, heightDp = 360)
@Composable
private fun DesertStormClockStylePreview() = ClockStylePreviewFrame { modifier ->
    DesertStormClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
