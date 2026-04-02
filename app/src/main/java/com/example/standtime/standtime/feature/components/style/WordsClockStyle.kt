package com.example.standtime.standtime.feature.components.style
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun WordsClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ClockPulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AlphaAnimation"
    )

    val fullText = localizedStringResource(
        R.string.gallery_words_clock,
        language,
        parts.hours,
        parts.minutes
    )

    val annotatedString = buildAnnotatedString {
        val hourStr = parts.hours
        val minuteStr = parts.minutes

        val hourIndex = fullText.indexOf(hourStr)
        val minuteIndex = fullText.indexOf(minuteStr)

        if (hourIndex != -1 && minuteIndex != -1) {
            // Soatgacha bo'lgan qism
            append(fullText.substring(0, hourIndex))

            // Soat qismi (Accent rang va miltillash)
            withStyle(style = SpanStyle(color = accentColor.copy(alpha = alpha), fontWeight = FontWeight.ExtraBold)) {
                append(hourStr)
            }

            // Soat va daqiqa orasi
            append(fullText.substring(hourIndex + hourStr.length, minuteIndex))

            // Daqiqa qismi (Accent rang va miltillash)
            withStyle(style = SpanStyle(color = accentColor.copy(alpha = alpha), fontWeight = FontWeight.ExtraBold)) {
                append(minuteStr)
            }

            // Qolgan qism
            append(fullText.substring(minuteIndex + minuteStr.length))
        } else {
            // Agar format mos kelmasa oddiy matn
            append(fullText)
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier
            .background(Brush.linearGradient(listOf(Color(0xFF09090B), Color(0xFF18181B))))
            .fillMaxSize()
            .padding(horizontal = 36.dp, vertical = 70.dp),
        color = Color.White,
        fontSize = 52.sp,
        fontWeight = FontWeight.Black,
        lineHeight = 50.sp
    )
}
