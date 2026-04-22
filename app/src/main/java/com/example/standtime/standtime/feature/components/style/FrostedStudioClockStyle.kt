package com.standtime.clock.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage

@Composable
fun FrostedStudioClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(
            Brush.linearGradient(
                listOf(
                    Color(0xFF3F3F46),
                    Color.Black
                )
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(80.dp))
                .background(Color.White.copy(alpha = 0.06f))
                .padding(horizontal = 42.dp, vertical = 38.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(28.dp)
        ) {
            Text(parts.hours, color = Color.White, fontSize = 186.sp, fontWeight = FontWeight.Black)
            Box(
                modifier = Modifier
                    .height(130.dp)
                    .width(2.dp)
                    .background(Color.White.copy(alpha = 0.2f))
            )
            Text(
                parts.minutes,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 186.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun FrostedStudioClockStylePreview() = ClockStylePreviewFrame { modifier ->
    FrostedStudioClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
