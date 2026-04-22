package com.standtime.clock.standtime.feature.components.style

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

private data class NetNode(
    var x: Float, var y: Float,
    val vx: Float, val vy: Float,
    val phase: Float
)

@Composable
fun NeuralNetClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val timeSeconds = rememberContinuousAnimationSeconds()

    val nodes = remember {
        mutableStateOf(
            List(20) {
                NetNode(
                    x = Random.nextFloat(),
                    y = Random.nextFloat(),
                    vx = (Random.nextFloat() - 0.5f) * 0.0008f,
                    vy = (Random.nextFloat() - 0.5f) * 0.0008f,
                    phase = Random.nextFloat() * (2 * Math.PI).toFloat()
                )
            }
        )
    }

    Box(
        modifier = modifier.background(Color(0xFF020810)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val updated = nodes.value.map { n ->
                var nx = n.x + n.vx
                var ny = n.y + n.vy
                val nvx = if (nx < 0f || nx > 1f) -n.vx else n.vx
                val nvy = if (ny < 0f || ny > 1f) -n.vy else n.vy
                nx = nx.coerceIn(0f, 1f)
                ny = ny.coerceIn(0f, 1f)
                n.copy(x = nx, y = ny, vx = nvx, vy = nvy)
            }
            nodes.value = updated

            val maxDist = size.width * 0.32f

            // Connections
            for (i in nodes.value.indices) {
                for (j in i + 1 until nodes.value.size) {
                    val a = nodes.value[i]
                    val b = nodes.value[j]
                    val ax = a.x * size.width; val ay = a.y * size.height
                    val bx = b.x * size.width; val by = b.y * size.height
                    val dx = ax - bx; val dy = ay - by
                    val dist = sqrt(dx * dx + dy * dy)
                    if (dist < maxDist) {
                        val pulse = (0.3f + 0.3f * sin(timeSeconds * 0.42f + i.toFloat() + j.toFloat())).coerceIn(0f, 1f)
                        val alpha = pulse * (1f - dist / maxDist) * 0.4f
                        drawLine(
                            color = Color(0xFF00B4FF).copy(alpha = alpha),
                            start = Offset(ax, ay),
                            end = Offset(bx, by),
                            strokeWidth = 0.7f
                        )
                    }
                }
            }

            // Nodes
            for (n in nodes.value) {
                val glow = (0.4f + 0.3f * sin(timeSeconds * 0.30f + n.phase)).coerceIn(0f, 1f)
                drawCircle(
                    color = Color(0xFF00C8FF).copy(alpha = glow),
                    radius = 2.5f,
                    center = Offset(n.x * size.width, n.y * size.height)
                )
            }
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFAAEEFF),
                fontSize = 122.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                shadow = Shadow(color = Color(0xFF00CCFF), blurRadius = 20f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF020810, widthDp = 800, heightDp = 360)
@Composable
private fun NeuralNetClockStylePreview() = ClockStylePreviewFrame { modifier ->
    NeuralNetClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
