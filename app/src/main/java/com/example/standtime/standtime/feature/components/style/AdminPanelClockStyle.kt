package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.components.GalleryWidgetCard
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun AdminPanelClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.background(Brush.linearGradient(listOf(Color.Black, Color(0xFF111827)))),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.weight(2.2f).fillMaxHeight().clip(RoundedCornerShape(38.dp)).background(Color(0x14181818)), contentAlignment = Alignment.Center) {
            Text("${parts.hours}:${parts.minutes}", color = Color.White, fontSize = 118.sp, fontWeight = FontWeight.Black)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)) {
            GalleryWidgetCard(
                Color(0xFFDC2626),
                localizedStringResource(R.string.gallery_admin_battery_value, language),
                localizedStringResource(R.string.gallery_admin_battery_label, language)
            )
            GalleryWidgetCard(
                Color.White.copy(alpha = 0.08f),
                localizedStringResource(R.string.gallery_admin_device_value, language),
                localizedStringResource(R.string.gallery_admin_device_label, language)
            )
        }
    }
}
