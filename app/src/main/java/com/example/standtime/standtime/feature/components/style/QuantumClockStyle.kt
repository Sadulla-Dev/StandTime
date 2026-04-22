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

private data class QuantumParticle(
    val x: Float,
    val y: Float,
    val swayX: Float,
    val swayY: Float,
    val radius: Float,
    val hue: Float,
    val phase: Float,
    val speed: Float
)

@Composable
fun QuantumClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val timeSeconds = rememberContinuousAnimationSeconds()
    val particles = remember {
        List(28) {
            QuantumParticle(
                x = 0.12f + Random.nextFloat() * 0.76f,
                y = 0.16f + Random.nextFloat() * 0.68f,
                swayX = 0.018f + Random.nextFloat() * 0.026f,
                swayY = 0.014f + Random.nextFloat() * 0.030f,
                radius = 1.3f + Random.nextFloat() * 1.6f,
                hue = 250f + Random.nextFloat() * 70f,
                phase = Random.nextFloat() * FullTurnRadians,
                speed = 0.45f + Random.nextFloat() * 0.65f
            )
        }
    }

    Box(
        modifier = modifier.background(Color(0xFF05000F)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f

            // Orbital rings (3 elliptical rings)
            for (i in 0..2) {
                val ringRadius = size.width * 0.22f + i * 20f
                val scaleY = 0.35f + i * 0.15f
                val rotSpeed = timeSeconds * (0.28f + i * 0.12f) + i * (FullTurnRadians / 6f)
                val ringHue = 260f + i * 20f
                val ringColor = Color.hsv(ringHue, 0.8f, 0.9f).copy(alpha = 0.35f)

                // Draw ellipse as multiple points
                val steps = 60
                for (s in 0 until steps) {
                    val angle = s * (FullTurnRadians / steps)
                    val nextAngle = (s + 1) * (FullTurnRadians / steps)
                    val cosR = cos(rotSpeed)
                    val sinR = sin(rotSpeed)

                    val x1 = cos(angle) * ringRadius
                    val y1 = sin(angle) * ringRadius * scaleY
                    val rx1 = cx + x1 * cosR - y1 * sinR
                    val ry1 = cy + x1 * sinR + y1 * cosR

                    val x2 = cos(nextAngle) * ringRadius
                    val y2 = sin(nextAngle) * ringRadius * scaleY
                    val rx2 = cx + x2 * cosR - y2 * sinR
                    val ry2 = cy + x2 * sinR + y2 * cosR

                    drawLine(ringColor, Offset(rx1, ry1), Offset(rx2, ry2), strokeWidth = 1.2f)
                }

                // Orbiting dot
                val dotAngle = timeSeconds * (0.82f + i * 0.26f) + i * 0.9f
                val dotX = cos(dotAngle) * ringRadius
                val dotY = sin(dotAngle) * ringRadius * scaleY
                val cosR = cos(rotSpeed)
                val sinR = sin(rotSpeed)
                val rdx = cx + dotX * cosR - dotY * sinR
                val rdy = cy + dotX * sinR + dotY * cosR
                drawCircle(
                    color = Color.hsv((260f + i * 30f), 1f, 1f),
                    radius = 5f,
                    center = Offset(rdx, rdy)
                )
            }

            // Floating particles
            for (particle in particles) {
                val px = size.width * particle.x +
                    sin(timeSeconds * particle.speed + particle.phase) * size.width * particle.swayX
                val py = size.height * particle.y +
                    cos(timeSeconds * (particle.speed * 0.82f) + particle.phase) * size.height * particle.swayY
                drawCircle(
                    color = Color.hsv(particle.hue, 0.8f, 0.9f, alpha = 0.45f),
                    radius = particle.radius,
                    center = Offset(px, py)
                )
            }
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color.White,
                fontSize = 142.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(color = Color(0xFFCC88FF), blurRadius = 24f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF05000F, widthDp = 800, heightDp = 360)
@Composable
private fun QuantumClockStylePreview() = ClockStylePreviewFrame { modifier ->
    QuantumClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
