package com.example.standtime.standtime.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockContent
import com.example.standtime.standtime.feature.components.galleryStyleAt
import com.example.standtime.standtime.feature.components.galleryStyleCount
import com.example.standtime.standtime.feature.components.galleryParts
import com.example.standtime.standtime.feature.utils.StandTimeIntent
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.StandTimeUiState
import com.example.standtime.standtime.feature.utils.localizedStringResource


@Composable
fun ClockStylesPage(
    state: StandTimeUiState,
    language: StandTimeLanguage,
    accentColor: Color,
    onIntent: (StandTimeIntent) -> Unit
) {
    val stylesCount = galleryStyleCount
    val parts = state.galleryParts()
    val galleryPagerState = rememberPagerState(pageCount = { stylesCount })
    val currentIndex = galleryPagerState.currentPage
    val currentStyle = galleryStyleAt(currentIndex)
    val styleName = localizedStringResource(currentStyle.nameRes, language)

    LaunchedEffect(currentIndex) {
        onIntent(StandTimeIntent.ChangeGalleryStyleIndex(currentIndex))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VerticalPager(
            state = galleryPagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                GalleryClockContent(
                    index = page,
                    parts = parts,
                    language = language,
                    accentColor = accentColor,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = localizedStringResource(
                    R.string.gallery_charging_status,
                    language,
                    state.batteryLevel
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.Black.copy(alpha = 0.28f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp,
                color = Color.White
            )
            Text(
                text = localizedStringResource(
                    R.string.gallery_style_counter,
                    language,
                    styleName,
                    currentIndex + 1,
                    stylesCount
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.Black.copy(alpha = 0.28f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp,
                color = Color.White
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(stylesCount) { index ->
                Box(
                    modifier = Modifier
                        .width(if (index == currentIndex) 22.dp else 6.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            Color.White.copy(
                                alpha = if (index == currentIndex) 0.95f else 0.25f
                            )
                        )
                )
            }
        }
    }
}
