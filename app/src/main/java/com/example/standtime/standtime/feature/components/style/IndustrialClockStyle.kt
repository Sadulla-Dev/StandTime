package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

@Composable
fun IndustrialClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFF1C1C1C), Color(0xFF090909)))).padding(horizontal = 20.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(32.dp)).background(Color.White.copy(alpha = 0.06f)).padding(24.dp), contentAlignment = Alignment.Center) {
            Text(parts.hours, color = Color(0xFFF4F4F5), fontSize = 136.sp, fontFamily = FontFamily.Monospace)
        }
        Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(Color(0xFFF97316)))
            Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(Color(0xFF3F3F46)))
        }
        Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(32.dp)).background(Color.White.copy(alpha = 0.06f)).padding(24.dp), contentAlignment = Alignment.Center) {
            Text(parts.minutes, color = Color(0xFFF4F4F5), fontSize = 136.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun IndustrialClockStylePreview() = ClockStylePreviewFrame { modifier ->
    IndustrialClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
