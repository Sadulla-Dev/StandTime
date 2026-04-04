package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun LuxuryClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Brush.linearGradient(listOf(Color(0xFF14110D), Color(0xFF0C0A09))))
            .padding(24.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(localizedStringResource(R.string.gallery_luxury_est, language), color = Color(0xFFD4AF37).copy(alpha = 0.4f), fontSize = 20.sp, letterSpacing = 8.sp)
        Text(
            text = "${parts.hours}:${parts.minutes}",
            modifier = Modifier.padding(top = 24.dp).clip(RoundedCornerShape(8.dp)).background(Color.Transparent).padding(horizontal = 24.dp),
            color = Color(0xFFD4AF37),
            fontSize = 188.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun LuxuryClockStylePreview() = ClockStylePreviewFrame { modifier ->
    LuxuryClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
