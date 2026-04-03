package com.example.standtime.standtime.feature.components.style

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AuraPulseClockStyle(
    parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier
) {
    // 1. Animatsiyalar (Pulse effekti uchun)
    val infiniteTransition = rememberInfiniteTransition(label = "AuraPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    val seconds = parts.seconds.toFloatOrNull() ?: 0f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050505)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val baseRadius = size.minDimension * 0.25f
            val auraRadius = baseRadius * pulseScale

            // 2. Nafas oluvchi Aura (Glow effekti)
            // Native canvas orqali Blur effektini beramiz (Premium ko'rinish uchun)
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    isAntiAlias = true
                    color = accentColor.copy(alpha = 0.2f).toArgb()
                    maskFilter = BlurMaskFilter(auraRadius, BlurMaskFilter.Blur.NORMAL)
                }
                drawCircle(centerX, centerY, auraRadius * 1.5f, paint)
            }

            // 3. Sekundlar halqasi (Arc)
            drawArc(
                color = accentColor.copy(alpha = 0.7f),
                startAngle = -90f,
                sweepAngle = (seconds / 60f) * 360f,
                useCenter = false,
                topLeft = Offset(centerX - baseRadius - 20.dp.toPx(), centerY - baseRadius - 20.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(
                    (baseRadius + 20.dp.toPx()) * 2,
                    (baseRadius + 20.dp.toPx()) * 2
                ),
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )

            // 4. Dekorativ 12 ta nuqta
            val dotRadius = baseRadius + 40.dp.toPx()
            for (i in 0 until 12) {
                val angleInDegrees = (i * 30f) - 90f
                val angleInRadians = Math.toRadians(angleInDegrees.toDouble())
                val dx = centerX + cos(angleInRadians).toFloat() * dotRadius
                val dy = centerY + sin(angleInRadians).toFloat() * dotRadius

                val isActive = (i * 5) <= seconds
                drawCircle(
                    color = if (isActive) accentColor else Color.White.copy(alpha = 0.1f),
                    radius = 2.dp.toPx(),
                    center = Offset(dx, dy)
                )
            }
        }

        // 5. Markaziy Vaqt (Minimalistik)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${parts.hours}:${parts.minutes}",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 100.sp,
                    fontWeight = FontWeight.ExtraLight, // Ultra-thin
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = (-2).sp
                )
            )
        }
    }
}