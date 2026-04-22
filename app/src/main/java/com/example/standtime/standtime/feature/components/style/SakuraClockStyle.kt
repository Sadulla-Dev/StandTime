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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import kotlin.math.sin
import kotlin.random.Random

private data class Petal(
    var x: Float, var y: Float,
    val vx: Float, val vy: Float,
    var rot: Float, val vrot: Float,
    val size: Float, val phase: Float
)

@Composable
fun SakuraClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "sakura")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing)),
        label = "t"
    )

    val petals = remember {
        mutableStateOf(
            List(25) {
                Petal(
                    x = Random.nextFloat(),
                    y = Random.nextFloat() - 1f,
                    vx = (Random.nextFloat() - 0.5f) * 0.001f,
                    vy = 0.001f + Random.nextFloat() * 0.0015f,
                    rot = Random.nextFloat() * 360f,
                    vrot = (0.02f + Random.nextFloat() * 0.03f),
                    size = 3f + Random.nextFloat() * 4f,
                    phase = Random.nextFloat() * (2 * Math.PI).toFloat()
                )
            }
        )
    }

    Box(
        modifier = modifier.background(Color(0xFF0F0008)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Soft pink radial bg
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFF6496).copy(alpha = 0.05f), Color.Transparent),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = size.width * 0.6f
                ),
                radius = size.width * 0.6f,
                center = Offset(size.width / 2f, size.height / 2f)
            )

            val updated = petals.value.map { p ->
                val nx = p.x + p.vx + sin(t * 0.3f + p.phase) * 0.0008f
                val ny = p.y + p.vy
                val nr = p.rot + p.vrot
                val finalX = if (ny > 1.05f) Random.nextFloat() else nx
                val finalY = if (ny > 1.05f) -0.05f else ny

                val px = finalX * size.width
                val py = finalY * size.height
                val alpha = (0.3f + 0.3f * sin(t * 0.5f + p.phase)).coerceIn(0f, 1f)

                rotate(nr, Offset(px, py)) {
                    drawOval(
                        color = Color(0xFFFFB4C8).copy(alpha = alpha),
                        topLeft = Offset(px - p.size, py - p.size * 0.6f),
                        size = androidx.compose.ui.geometry.Size(p.size * 2f, p.size * 1.2f)
                    )
                }
                p.copy(x = finalX, y = finalY, rot = nr)
            }
            petals.value = updated
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFFFDDEE),
                fontSize = 162.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(color = Color(0xFFFF88AA), blurRadius = 20f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0008, widthDp = 800, heightDp = 360)
@Composable
private fun SakuraClockStylePreview() = ClockStylePreviewFrame { modifier ->
    SakuraClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
