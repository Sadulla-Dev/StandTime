package com.standtime.clock.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.standtime.clock.R
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import com.standtime.clock.standtime.feature.utils.localizedStringResource

@Composable
fun PixelPetClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    // 1. Animatsiya sozlamalari (Dinozavr qimirlashi uchun)
    val infiniteTransition = rememberInfiniteTransition(label = "petBounce")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            // 'knots' o'rniga 'tween' (duration va easing bilan)
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    // Mitti nafas olish (miqyos o'zgarishi) effekti
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Row(
        modifier = modifier
            .fillMaxSize()
            // 2. Umumiy fon: Accent rangdan foydalanib yengil gradient yoki rang beramiz
            .background(Color(0xFF0F172A))
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Soat va Salomatlik qismi
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(40.dp))
                .background(Color.Black.copy(alpha = 0.08f))
                .padding(24.dp)
        ) {
            Text(
                text = "${parts.hours}:${parts.minutes}",
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    fontSize = 72.sp
                ),
                color = Color.White
            )
            Text(
                text = "BATTERY: ${parts.batteryInfo}%",
                modifier = Modifier.padding(top = 8.dp),
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    letterSpacing = 2.sp
                ),
                color = Color.White.copy(alpha = 0.55f)
            )
        }

        // Dinozavr (Pet) qutisi
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(RoundedCornerShape(36.dp))
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\uD83E\uDD96",
                fontSize = 108.sp,
                modifier = Modifier
                    // Animatsiyalarni qo'llash
                    .graphicsLayer {
                        translationY = offsetY
                        scaleX = scale
                        scaleY = scale
                    }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun PixelPetClockStylePreview() = ClockStylePreviewFrame { modifier ->
    PixelPetClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
