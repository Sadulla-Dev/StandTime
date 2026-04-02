package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun NightOwlClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF1E1B4B)))),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${parts.hours}:${parts.minutes}", color = Color(0xFF818CF8), fontSize = 116.sp, fontWeight = FontWeight.Black)
        Column(modifier = Modifier.padding(start = 18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("\uD83C\uDF19", fontSize = 62.sp)
            Text(localizedStringResource(R.string.gallery_sleep_mode, language), modifier = Modifier.padding(top = 8.dp), color = Color(0xFF818CF8).copy(alpha = 0.55f), fontSize = 18.sp)
        }
    }
}
