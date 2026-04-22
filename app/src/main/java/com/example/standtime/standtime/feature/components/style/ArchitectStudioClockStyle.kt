package com.standtime.clock.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.standtime.clock.R
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import com.standtime.clock.standtime.feature.utils.localizedStringResource

@Composable
fun ArchitectStudioClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Row(modifier = modifier.background(Color(0xFFD4D4D8)), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(1.dp)) {
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFF5F5F5)), contentAlignment = Alignment.Center) {
            Text(parts.hours, color = Color.Black, fontSize = 176.sp, fontWeight = FontWeight.Black)
        }
        Column(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFF5F5F5)).padding(24.dp)) {
            Text(parts.minutes, color = Color(0xFF52525B), fontSize = 120.sp, fontWeight = FontWeight.Light)
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.Bottom, horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween) {
                Text(localizedStringResource(R.string.gallery_architect_grid, language), color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 32.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text("2024", color = Color.Black, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Text(localizedStringResource(R.string.gallery_architect_edition, language), color = Color.Black.copy(alpha = 0.4f), fontSize = 11.sp, letterSpacing = 2.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun ArchitectStudioClockStylePreview() = ClockStylePreviewFrame { modifier ->
    ArchitectStudioClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
