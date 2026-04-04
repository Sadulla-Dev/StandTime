package com.example.standtime.standtime.feature.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.CustomClockFont
import com.example.standtime.standtime.feature.utils.CustomClockLayout
import com.example.standtime.standtime.feature.utils.CustomClockStyleSettings
import com.example.standtime.standtime.feature.utils.CustomColorValue
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.ui.theme.StandTimeTheme

@Preview(
    showBackground = true,
    backgroundColor = 0xFF101418,
    widthDp = 800,
    heightDp = 360
)
annotation class ClockLandscapePreview

private val previewParts = GalleryClockParts(
    hours = "18",
    minutes = "24",
    seconds = "36",
    dayText = "Friday",
    dateText = "Apr 4",
    kanjiHours = "一八",
    kanjiMinutes = "二四",
    weatherTemperature = "24°",
    weatherSummary = "Sunny",
    locationName = "Tashkent",
    batteryInfo = "Charging 82%"
)

private val previewAccent = Color(0xFFE67E22)

private val previewCustomStyle = CustomClockStyleSettings(
    font = CustomClockFont.CONDENSED,
    textColor = CustomColorValue(0xFFF8FAFC),
    backgroundStartColor = CustomColorValue(0xFF0F172A),
    showBackgroundCenterColor = true,
    backgroundCenterColor = CustomColorValue(0xFF1D4ED8),
    showBackgroundEndColor = true,
    backgroundEndColor = CustomColorValue(0xFF7C3AED),
    scale = 1.12f,
    layout = CustomClockLayout.HORIZONTAL,
    showSeconds = true,
    showDate = true,
    showWeather = true
)

@Composable
private fun ClockPreview(content: @Composable (Modifier) -> Unit) {
    StandTimeTheme(darkTheme = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            content(Modifier.fillMaxSize())
        }
    }
}

@ClockLandscapePreview
@Composable
private fun NothingOfficialClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.NothingOfficialClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun NothingDotClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.NothingDotClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun NasaClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.NasaClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun PixelStackClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.PixelStackClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun TokyoClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.TokyoClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun BraunClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.BraunClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun TerminalClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.TerminalClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun CyberpunkClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.CyberpunkClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun PixelPetClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.PixelPetClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun LofiClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.LofiClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun RolexClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.RolexClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun AnalogZenClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.AnalogZenClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun TeslaClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.TeslaClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun GlassClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.GlassClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun LuxuryClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.LuxuryClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun BauhausClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.BauhausClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun MacOsClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.MacOsClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun WordsClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.WordsClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun CoffeeClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.CoffeeClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun RetroFlipClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.RetroFlipClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun BinaryPulseClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.BinaryPulseClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun SolarOrbitClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.SolarOrbitClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun TypewriterClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.TypewriterClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun AdminPanelClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.AdminPanelClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun SynthwaveClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.SynthwaveClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun ZenArchitectureClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.ZenArchitectureClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun ArchitectStudioClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.ArchitectStudioClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun OledStealthClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.OledStealthClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun SwissClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.SwissClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun IndustrialClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.IndustrialClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun FrostedStudioClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.FrostedStudioClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun TokyoNeonClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.TokyoNeonClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun PaperMinimalismClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.PaperMinimalismClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun CyberGlitchClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.CyberGlitchClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun AbstractGeometricClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.AbstractGeometricClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun TypographyFocusClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.TypographyFocusClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun HorizonStudioClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.HorizonStudioClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun ContrastSplitClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.ContrastSplitClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun Ps5ClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.Ps5ClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun AuraPulseClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.AuraPulseClockStyle(
        previewParts,
        StandTimeLanguage.ENGLISH,
        previewAccent,
        it
    )
}

@ClockLandscapePreview
@Composable
private fun CustomClockStylePreview() = ClockPreview {
    _root_ide_package_.com.example.standtime.standtime.feature.components.style.CustomClockStyle(
        parts = previewParts,
        custom = previewCustomStyle,
        modifier = it
    )
}
