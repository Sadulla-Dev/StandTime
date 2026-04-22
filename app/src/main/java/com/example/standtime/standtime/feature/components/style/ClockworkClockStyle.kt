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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClockworkClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val timeSeconds = rememberContinuousAnimationSeconds()

    Box(
        modifier = modifier.background(Color(0xFF080400)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f

            fun drawGear(gx: Float, gy: Float, r: Float, teeth: Int, rot: Float, col: Color) {
                val path = androidx.compose.ui.graphics.Path()
                for (i in 0 until teeth * 2) {
                    val a = rot + i * (Math.PI / teeth).toFloat()
                    val rv = if (i % 2 == 0) r else r * 0.78f
                    val px = gx + cos(a) * rv
                    val py = gy + sin(a) * rv
                    if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
                }
                path.close()
                drawPath(path, col, style = Stroke(width = 0.8f))
                drawCircle(color = col, radius = r * 0.25f, center = Offset(gx, gy), style = Stroke(width = 0.8f))
            }

            drawGear(
                cx - size.width * 0.22f,
                cy,
                size.height * 0.28f,
                12,
                timeSeconds * 0.72f,
                Color(0xFFC88228).copy(alpha = 0.4f)
            )
            drawGear(
                cx + size.width * 0.22f,
                cy,
                size.height * 0.22f,
                9,
                -timeSeconds * 0.96f,
                Color(0xFFA06E1E).copy(alpha = 0.35f)
            )
            drawGear(
                cx,
                cy - size.height * 0.22f,
                size.height * 0.16f,
                7,
                timeSeconds * 1.28f,
                Color(0xFFD09632).copy(alpha = 0.30f)
            )

            // Tick ring
            for (i in 0 until 60) {
                val a = i / 60f * (2 * Math.PI).toFloat() - (Math.PI / 2).toFloat()
                val isMajor = i % 5 == 0
                val r1 = size.height * 0.35f
                val r2 = if (isMajor) size.height * 0.28f else size.height * 0.32f
                drawLine(
                    color = Color(0xFFCC9622).copy(alpha = if (isMajor) 0.5f else 0.2f),
                    start = Offset(cx + cos(a) * r1, cy + sin(a) * r1),
                    end = Offset(cx + cos(a) * r2, cy + sin(a) * r2),
                    strokeWidth = if (isMajor) 1.2f else 0.5f
                )
            }
        }

        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFF0C860),
                fontSize = 122.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                shadow = Shadow(color = Color(0xFFCC9922), blurRadius = 16f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF080400, widthDp = 800, heightDp = 360)
@Composable
private fun ClockworkClockStylePreview() = ClockStylePreviewFrame { modifier ->
    ClockworkClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
