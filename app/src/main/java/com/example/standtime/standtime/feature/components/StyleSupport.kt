package com.example.standtime.standtime.feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.example.standtime.standtime.feature.utils.StandTimeUiState
import kotlin.math.min

val LocalGalleryScaleFactor = staticCompositionLocalOf { 1f }

data class GalleryClockParts(
    val hours: String,
    val minutes: String,
    val seconds: String,
    val dayText: String,
    val dateText: String,
    val kanjiHours: String,
    val kanjiMinutes: String,
    val weatherTemperature: String,
    val weatherSummary: String,
    val locationName: String
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
        kanjiMinutes = minutePart.toKanji(),
        weatherTemperature = weatherTemperature,
        weatherSummary = weatherSummary,
        locationName = locationName
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
    val scale = LocalGalleryScaleFactor.current
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(background)
            .padding(18.dp)
    ) {
        Text(
            title.uppercase(),
            color = contentColor.copy(alpha = 0.5f),
            fontSize = (12f * scale).coerceIn(10f, 16f).sp
        )
        Text(
            value,
            color = contentColor,
            fontSize = (30f * scale).coerceIn(20f, 40f).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GalleryWidgetCard(
    background: Color,
    primary: String,
    secondary: String
) {
    val scale = LocalGalleryScaleFactor.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(background)
            .padding(18.dp)
    ) {
        Text(
            secondary,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = (14f * scale).coerceIn(11f, 18f).sp
        )
        Text(
            primary,
            color = Color.White,
            fontSize = (24f * scale).coerceIn(17f, 34f).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GallerySquareIcon(value: String) {
    val scale = LocalGalleryScaleFactor.current
    Box(
        modifier = Modifier
            .size((64f * scale).coerceIn(46f, 90f).dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Text(value, fontSize = (28f * scale).coerceIn(18f, 38f).sp)
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
    val scale = LocalGalleryScaleFactor.current
    Box(
        modifier = Modifier
            .weight(1f)
            .height((220f * scale).coerceIn(148f, 260f).dp)
            .clip(RoundedCornerShape(44.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            color = Color.White,
            fontSize = (92f * scale).coerceIn(58f, 110f).sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun BauhausColumn(top: Char, bottom: Char, topColor: Color, bottomColor: Color, topRound: Boolean) {
    val scale = LocalGalleryScaleFactor.current
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size((150f * scale).coerceIn(96f, 186f).dp)
                .clip(if (topRound) CircleShape else RoundedCornerShape(0.dp))
                .background(topColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                top.toString(),
                color = Color.White,
                fontSize = (72f * scale).coerceIn(44f, 84f).sp,
                fontWeight = FontWeight.Black
            )
        }
        Box(
            modifier = Modifier
                .size((150f * scale).coerceIn(96f, 186f).dp)
                .clip(RoundedCornerShape(bottomStart = if (topRound) 0.dp else 80.dp))
                .background(bottomColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                bottom.toString(),
                color = if (bottomColor == Color(0xFFF9C32E)) Color.Black else Color.White,
                fontSize = (72f * scale).coerceIn(44f, 84f).sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun FlipBlock(value: String) {
    val scale = LocalGalleryScaleFactor.current
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFF27272A))
            .padding(
                horizontal = (34f * scale).coerceIn(20f, 42f).dp,
                vertical = (26f * scale).coerceIn(14f, 34f).dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            color = Color.White,
            fontSize = (104f * scale).coerceIn(60f, 122f).sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun ResponsiveGalleryFrame(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val ratio = maxWidth.value / maxHeight.value
        val smallestSide = min(maxWidth.value, maxHeight.value)

        val (baseWidth, baseHeight) = when {
            ratio < 0.9f -> 390f to 780f
            smallestSide < 330f -> 700f to 560f
            ratio < 1.5f -> 760f to 520f
            else -> 920f to 360f
        }
        val safeWidth = maxWidth.value * 0.9f
        val safeHeight = maxHeight.value * 0.9f
        val scale = min(safeWidth / baseWidth, safeHeight / baseHeight).coerceIn(0.58f, 1.25f)
        val density = LocalDensity.current
        val scaledDensity = Density(
            density = density.density * scale,
            fontScale = density.fontScale * scale
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalDensity provides scaledDensity) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Keep style-level scale at 1f to avoid double-scaling in
                    // helpers that already multiply by LocalGalleryScaleFactor.
                    CompositionLocalProvider(LocalGalleryScaleFactor provides 1f) {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedGalleryStyle(
    index: Int,
    content: @Composable BoxScope.() -> Unit
) {
    var visible by remember(index) { mutableStateOf(false) }
    LaunchedEffect(index) {
        visible = false
        visible = true
    }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0.84f,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing),
        label = "galleryAlpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.965f,
        animationSpec = tween(durationMillis = 520, easing = FastOutSlowInEasing),
        label = "galleryScale"
    )
    val settleY by animateFloatAsState(
        targetValue = if (visible) 0f else 26f,
        animationSpec = tween(durationMillis = 520, easing = FastOutSlowInEasing),
        label = "galleryTranslateY"
    )
    val drift by rememberInfiniteTransition(label = "galleryDrift").animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "galleryFloat"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                this.alpha = alpha
                scaleX = scale
                scaleY = scale
                translationY = settleY + drift
            },
        content = content
    )
}
