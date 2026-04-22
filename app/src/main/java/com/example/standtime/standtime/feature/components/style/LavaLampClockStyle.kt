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

private data class LavaBlob(
    val cx: Float, val cy: Float, val r: Float,
    val sx: Float, val sy: Float,
    val speedX: Float, val speedY: Float
)

@Composable
fun LavaLampClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val timeSeconds = rememberContinuousAnimationSeconds()

    val blobs = remember {
        List(6) {
            LavaBlob(
                cx = 0.35f + Random.nextFloat() * 0.3f,
                cy = 0.35f + Random.nextFloat() * 0.3f,
                r = 0.08f + Random.nextFloat() * 0.06f,
                sx = Random.nextFloat() * FullTurnRadians,
                sy = Random.nextFloat() * FullTurnRadians,
                speedX = 0.4f + Random.nextFloat() * 0.3f,
                speedY = 0.3f + Random.nextFloat() * 0.4f
            )
        }
    }

    Box(
        modifier = modifier.background(Color(0xFF0D0005)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (blob in blobs) {
                val bx = size.width * (blob.cx + sin(timeSeconds * blob.speedX + blob.sx) * 0.18f)
                val by = size.height * (blob.cy + cos(timeSeconds * blob.speedY + blob.sy) * 0.22f)
                val br = size.width * blob.r * 1.4f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF1E78).copy(alpha = 0.38f),
                            Color(0xFFC80064).copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        center = Offset(bx, by),
                        radius = br
                    ),
                    radius = br,
                    center = Offset(bx, by)
                )
            }
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFFFAADD),
                fontSize = 122.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(color = Color(0xFFFF2288), blurRadius = 22f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0005, widthDp = 800, heightDp = 360)
@Composable
private fun LavaLampClockStylePreview() = ClockStylePreviewFrame { modifier ->
    LavaLampClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
