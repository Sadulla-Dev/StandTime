package com.example.standtime.standtime.feature.components.style

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Ember(
    var x: Float,
    var y: Float,
    val vx: Float,
    val vy: Float,
    var alpha: Float,
    val size: Float
)

@Composable
fun DarkSoulClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "darkSoul")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing)),
        label = "t"
    )

    val embers = remember {
        mutableStateOf(
            List(35) {
                Ember(
                    x = Random.nextFloat() * 800f,
                    y = Random.nextFloat() * 360f + 360f,
                    vx = (Random.nextFloat() - 0.5f) * 0.8f,
                    vy = -Random.nextFloat() * 1.2f - 0.3f,
                    alpha = Random.nextFloat() * 0.8f + 0.2f,
                    size = Random.nextFloat() * 2.5f + 0.5f
                )
            }
        )
    }

    Box(
        modifier = modifier.background(Color(0xFF040404)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f

            // Embers
            val updatedEmbers = embers.value.map { e ->
                var nx = e.x + e.vx
                var ny = e.y + e.vy
                var na = e.alpha - 0.003f
                if (na <= 0f || ny < -10f) {
                    nx = Random.nextFloat() * size.width
                    ny = size.height + 10f
                    na = Random.nextFloat() * 0.8f + 0.2f
                }
                val r = (180 + Random.nextInt(75)).coerceIn(0, 255)
                val g = Random.nextInt(60)
                drawCircle(
                    color = Color(r / 255f, g / 255f, 0f, na),
                    radius = e.size,
                    center = Offset(nx, ny)
                )
                e.copy(x = nx, y = ny, alpha = na)
            }
            embers.value = updatedEmbers

            // Bonfire glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF6400).copy(alpha = 0.2f),
                        Color(0xFFB41E00).copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(cx, size.height * 0.85f),
                    radius = size.width * 0.4f
                ),
                radius = size.width * 0.4f,
                center = Offset(cx, size.height * 0.85f)
            )

            // Rune circle
            drawCircle(
                color = Color(0xFFC85000).copy(alpha = 0.4f),
                radius = size.width * 0.3f,
                center = Offset(cx, cy),
                style = Stroke(width = 1.2f)
            )

            // Rune spokes
            for (i in 0..5) {
                val angle = t + i * (Math.PI / 3.0).toFloat()
                val r = size.width * 0.3f
                drawLine(
                    color = Color(0xFFC83C00).copy(alpha = 0.3f),
                    start = Offset(cx + cos(angle) * r * 0.2f, cy + sin(angle) * r * 0.2f),
                    end = Offset(cx + cos(angle) * r, cy + sin(angle) * r),
                    strokeWidth = 1f
                )
            }
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFFF8844),
                fontSize = 102.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                shadow = Shadow(color = Color(0xFFFF4400), blurRadius = 26f)
            )
        )

    }
}

@Preview(showBackground = true, backgroundColor = 0xFF040404, widthDp = 800, heightDp = 360)
@Composable
private fun DarkSoulClockStylePreview() = ClockStylePreviewFrame { modifier ->
    DarkSoulClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
