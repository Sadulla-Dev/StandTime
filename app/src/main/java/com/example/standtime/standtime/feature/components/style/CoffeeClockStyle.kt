package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
fun CoffeeClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFF2C1810), Color(0xFF120B08)))),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("\u2615", fontSize = 84.sp, color = Color(0xFFE6CCB2).copy(alpha = 0.5f))
        Text("${parts.hours}:${parts.minutes}", modifier = Modifier.padding(top = 10.dp), color = Color(0xFFE6CCB2), fontSize = 170.sp, fontWeight = FontWeight.Bold)
        Text(localizedStringResource(R.string.gallery_coffee_brewing, language), modifier = Modifier.padding(top = 12.dp), color = Color(0xFFE6CCB2).copy(alpha = 0.45f), fontSize = 24.sp, letterSpacing = 4.sp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun CoffeeClockStylePreview() = ClockStylePreviewFrame { modifier ->
    CoffeeClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
