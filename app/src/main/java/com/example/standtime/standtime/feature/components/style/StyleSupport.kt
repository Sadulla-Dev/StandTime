package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.example.standtime.standtime.StandTimeUiState

data class GalleryClockParts(
    val hours: String,
    val minutes: String,
    val seconds: String,
    val dayText: String,
    val dateText: String,
    val kanjiHours: String,
    val kanjiMinutes: String
)

fun StandTimeUiState.galleryParts(): GalleryClockParts {
    val segments = timeText.split(":", ".")
    val hourPart = segments.getOrNull(0).orEmpty().padStart(2, '0').takeLast(2)
    val minutePart = segments.getOrNull(1).orEmpty().padStart(2, '0').takeLast(2)
    val secondPart = segments.getOrNull(2).orEmpty().padStart(2, '0').takeLast(2)
    return GalleryClockParts(
        hours = hourPart,
        minutes = minutePart,
        seconds = secondPart,
        dayText = dayText,
        dateText = dateText,
        kanjiHours = hourPart.toKanji(),
        kanjiMinutes = minutePart.toKanji()
    )
}

private fun String.toKanji(): String {
    val digits = listOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
    return map { digits.getOrElse(it.digitToIntOrNull() ?: 0) { "零" } }.joinToString("")
}

data class GalleryStyleSpec(
    val name: String,
    val background: Brush,
    val darkOverlay: Boolean = true
)

fun galleryOverlayColor(index: Int): Color = if (index in setOf(9, 12, 17, 23, 24, 27)) {
    Color(0xFF18181B).copy(alpha = 0.7f)
} else {
    Color.White.copy(alpha = 0.6f)
}

@Composable
fun GalleryMetricCard(
    title: String,
    value: String,
    background: Color,
    contentColor: Color
) {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(background)
            .padding(18.dp)
    ) {
        Text(title.uppercase(), color = contentColor.copy(alpha = 0.5f), fontSize = 12.sp)
        Text(value, color = contentColor, fontSize = 30.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun GalleryWidgetCard(
    background: Color,
    primary: String,
    secondary: String
) {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(background)
            .padding(18.dp)
    ) {
        Text(secondary, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
        Text(primary, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun GallerySquareIcon(value: String) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Text(value, fontSize = 28.sp)
    }
}

@Composable
fun ClockHand(length: Dp, width: Dp, angle: Float, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { rotationZ = angle },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = length / 2)
                .width(width)
                .height(length)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}

@Composable
fun RowScope.GlassTimeBlock(value: String) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(220.dp)
            .clip(RoundedCornerShape(44.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = value, color = Color.White, fontSize = 92.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun BauhausColumn(top: Char, bottom: Char, topColor: Color, bottomColor: Color, topRound: Boolean) {
    androidx.compose.foundation.layout.Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(if (topRound) CircleShape else RoundedCornerShape(0.dp))
                .background(topColor),
            contentAlignment = Alignment.Center
        ) {
            Text(top.toString(), color = Color.White, fontSize = 72.sp, fontWeight = FontWeight.Black)
        }
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(bottomStart = if (topRound) 0.dp else 80.dp))
                .background(bottomColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                bottom.toString(),
                color = if (bottomColor == Color(0xFFF9C32E)) Color.Black else Color.White,
                fontSize = 72.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun FlipBlock(value: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFF27272A))
            .padding(horizontal = 34.dp, vertical = 26.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 104.sp,
            fontWeight = FontWeight.Black
        )
    }
}
