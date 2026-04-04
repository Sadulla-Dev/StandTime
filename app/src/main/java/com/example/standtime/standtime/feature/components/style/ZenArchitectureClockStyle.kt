package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun ZenArchitectureClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFF0F0F0F), Color(0xFF171717)))),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.size(500.dp).clip(CircleShape).background(Color.Transparent))
        Box(modifier = Modifier.size(700.dp).clip(CircleShape).background(Color.Transparent))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${parts.hours}:${parts.minutes}", color = Color(0xFFF4F4F5), fontSize = 168.sp, fontWeight = FontWeight.ExtraLight)
            Row(modifier = Modifier.padding(top = 18.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.size(width = 52.dp, height = 2.dp).background(Color(0xFF27272A)))
                Text(localizedStringResource(R.string.gallery_zen_sanctuary, language), color = Color(0xFFA1A1AA), fontSize = 16.sp, letterSpacing = 6.sp)
                Box(modifier = Modifier.size(width = 52.dp, height = 2.dp).background(Color(0xFF27272A)))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun ZenArchitectureClockStylePreview() = ClockStylePreviewFrame { modifier ->
    ZenArchitectureClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
