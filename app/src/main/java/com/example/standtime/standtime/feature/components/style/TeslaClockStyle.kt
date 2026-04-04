package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.components.GalleryMetricCard
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun TeslaClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFF111111), Color(0xFF050505)))),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(start = 20.dp)) {
            Text(text = "${parts.hours}:${parts.minutes}", style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 96.sp), color = Color(0xFFF5F5F5))
            Text(
                text = "P  R  N  D",
                modifier = Modifier.padding(top = 10.dp),
                style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 24.sp, letterSpacing = 4.sp),
                color = Color(0xFF6C6C6C)
            )
        }
        Box(modifier = Modifier.width(1.dp).height(220.dp).background(Color(0xFF2A2A2A)))
        Column(modifier = Modifier.weight(1f), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
            GalleryMetricCard(
                localizedStringResource(R.string.gallery_tesla_range, language),
                localizedStringResource(R.string.gallery_tesla_range_value, language),
                Color(0xFF1A1A1A),
                Color.White
            )
            GalleryMetricCard(
                localizedStringResource(R.string.gallery_tesla_temp, language),
                parts.weatherTemperature,
                Color(0xFF1A1A1A),
                Color.White
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun TeslaClockStylePreview() = ClockStylePreviewFrame { modifier ->
    TeslaClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
