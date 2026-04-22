package com.standtime.clock.standtime.feature.components.style

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Star(val x: Float, val y: Float, val r: Float, val phase: Float, val speed: Float)
private data class NebulaCloud(
    val x: Float,
    val y: Float,
    val radiusFactor: Float,
    val driftFactor: Float,
    val phase: Float,
    val color: Color
)

@Composable
fun DeepSpaceClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val timeSeconds = rememberContinuousAnimationSeconds()

    val stars = remember {
        List(120) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                r = Random.nextFloat() * 1.4f,
                phase = Random.nextFloat() * FullTurnRadians,
                speed = Random.nextFloat() * 1.1f + 0.35f
            )
        }
    }

    val nebulaClouds = remember {
        listOf(
            NebulaCloud(0.18f, 0.42f, 0.32f, 0.014f, 0.2f, Color(0xFF3296FF)),
            NebulaCloud(0.42f, 0.36f, 0.30f, 0.012f, 1.1f, Color(0xFF6A63FF)),
            NebulaCloud(0.68f, 0.48f, 0.34f, 0.016f, 2.2f, Color(0xFF8844FF)),
            NebulaCloud(0.84f, 0.34f, 0.28f, 0.010f, 3.1f, Color(0xFF22CC88))
        )
    }

    Box(
        modifier = modifier.background(Color(0xFF02020F)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Nebula clouds
            for (cloud in nebulaClouds) {
                val driftX = sin(timeSeconds * 0.05f + cloud.phase) * size.width * cloud.driftFactor
                val driftY = cos(timeSeconds * 0.04f + cloud.phase) * size.height * cloud.driftFactor * 0.8f
                val center = Offset(
                    x = size.width * cloud.x + driftX,
                    y = size.height * cloud.y + driftY
                )
                val radius = size.width * cloud.radiusFactor
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            cloud.color.copy(alpha = 0.14f),
                            cloud.color.copy(alpha = 0.07f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = radius
                    ),
                    radius = radius,
                    center = center
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            cloud.color.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = center + Offset(radius * 0.12f, radius * 0.04f),
                        radius = radius * 1.28f
                    ),
                    radius = radius * 1.28f,
                    center = center + Offset(radius * 0.12f, radius * 0.04f)
                )
            }

            // Stars
            for (star in stars) {
                val alpha = (0.24f + 0.56f * sin(timeSeconds * star.speed + star.phase)).coerceIn(0f, 1f)
                drawCircle(
                    color = Color.White.copy(alpha = alpha),
                    radius = star.r,
                    center = Offset(star.x * size.width, star.y * size.height)
                )
            }
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFDDDDFF),
                fontSize = 162.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(color = Color(0xFF8888FF), blurRadius = 24f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF02020F, widthDp = 800, heightDp = 360)
@Composable
private fun DeepSpaceClockStylePreview() = ClockStylePreviewFrame { modifier ->
    DeepSpaceClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
