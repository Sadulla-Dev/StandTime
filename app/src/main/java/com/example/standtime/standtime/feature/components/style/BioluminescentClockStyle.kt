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
import androidx.compose.runtime.mutableStateOf
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

private data class BioOrb(
    var x: Float, var y: Float,
    val vx: Float, val vy: Float,
    val r: Float, val hue: Float, val phase: Float
)

@Composable
fun BioluminescentClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "bio")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(7000, easing = LinearEasing)),
        label = "t"
    )

    val orbs = remember {
        mutableStateOf(
            List(18) {
                BioOrb(
                    x = Random.nextFloat(),
                    y = Random.nextFloat(),
                    vx = (Random.nextFloat() - 0.5f) * 0.0006f,
                    vy = (Random.nextFloat() - 0.5f) * 0.0006f,
                    r = (10f + Random.nextFloat() * 16f),
                    hue = 150f + Random.nextFloat() * 60f,
                    phase = Random.nextFloat() * (2 * Math.PI).toFloat()
                )
            }
        )
    }

    Box(
        modifier = modifier.background(Color(0xFF000A0A)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val updated = orbs.value.map { o ->
                var nx = o.x + o.vx + sin(t + o.phase) * 0.0003f
                var ny = o.y + o.vy + cos(t * 0.7f + o.phase) * 0.0002f
                if (nx < -0.05f) nx = 1.05f; if (nx > 1.05f) nx = -0.05f
                if (ny < -0.05f) ny = 1.05f; if (ny > 1.05f) ny = -0.05f

                val cx = nx * size.width
                val cy = ny * size.height
                val pulse = (0.4f + 0.3f * sin(t * 2f + o.phase)).coerceIn(0f, 1f)

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.hsv(o.hue, 1f, 1f).copy(alpha = pulse * 0.8f),
                            Color.hsv(o.hue, 0.9f, 0.7f).copy(alpha = pulse * 0.3f),
                            Color.Transparent
                        ),
                        center = Offset(cx, cy),
                        radius = o.r * 2.5f
                    ),
                    radius = o.r * 2.5f,
                    center = Offset(cx, cy)
                )
                o.copy(x = nx, y = ny)
            }
            orbs.value = updated
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFCCFFEE),
                fontSize = 122.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(color = Color(0xFF00FFCC), blurRadius = 24f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000A0A, widthDp = 800, heightDp = 360)
@Composable
private fun BioluminescentClockStylePreview() = ClockStylePreviewFrame { modifier ->
    BioluminescentClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
