package com.standtime.clock.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.standtime.clock.R
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.components.GallerySquareIcon
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import com.standtime.clock.standtime.feature.utils.localizedStringResource

@Composable
fun MacOsClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF0F172A)))),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Bottom
    ) {
        Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(48.dp)).background(Color.Black.copy(alpha = 0.18f)).padding(28.dp).padding(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("${parts.hours}:${parts.minutes}", color = Color.White, fontSize = 88.sp, fontWeight = FontWeight.Bold)
            Column(horizontalAlignment = Alignment.End) {
                Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
                    GallerySquareIcon("\u2601")
                    GallerySquareIcon("\uD83D\uDCC5")
                }
                Text(
                    parts.locationName.ifBlank { localizedStringResource(R.string.gallery_location_tashkent, language) },
                    modifier = Modifier.padding(top = 12.dp),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 24.sp
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun MacOsClockStylePreview() = ClockStylePreviewFrame { modifier ->
    MacOsClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
