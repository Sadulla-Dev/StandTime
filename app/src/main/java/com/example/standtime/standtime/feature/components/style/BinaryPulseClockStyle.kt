package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

@Composable
fun BinaryPulseClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    val secondSeed = parts.seconds.toIntOrNull() ?: 0
    Column(
        modifier = modifier
            .background(Brush.linearGradient(listOf(Color.Black, Color(0xFF052E16))))
            .padding(horizontal = 24.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(text = "01010111 11001010 01110101 10101011", color = Color(0xFF22C55E).copy(alpha = 0.16f), fontFamily = FontFamily.Monospace, fontSize = 24.sp)
        Text(text = "${parts.hours}:${parts.minutes}", modifier = Modifier.padding(top = 10.dp), color = Color(0xFF22C55E), fontSize = 118.sp, fontWeight = FontWeight.Black)
        Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)) {
            repeat(28) { index ->
                Box(
                    modifier = Modifier.weight(1f).height(14.dp).clip(RoundedCornerShape(50)).background(
                        if ((index + secondSeed) % 3 == 0) Color(0xFF22C55E) else Color(0xFF14532D).copy(alpha = 0.22f)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun BinaryPulseClockStylePreview() = ClockStylePreviewFrame { modifier ->
    BinaryPulseClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
