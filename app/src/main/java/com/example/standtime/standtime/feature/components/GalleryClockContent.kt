package com.example.standtime.standtime.feature.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.standtime.standtime.feature.components.style.AbstractGeometricClockStyle
import com.example.standtime.standtime.feature.components.style.AdminPanelClockStyle
import com.example.standtime.standtime.feature.components.style.AnalogZenClockStyle
import com.example.standtime.standtime.feature.components.style.ArchitectStudioClockStyle
import com.example.standtime.standtime.feature.components.style.BauhausClockStyle
import com.example.standtime.standtime.feature.components.style.BinaryPulseClockStyle
import com.example.standtime.standtime.feature.components.style.BraunClockStyle
import com.example.standtime.standtime.feature.components.style.CoffeeClockStyle
import com.example.standtime.standtime.feature.components.style.CustomClockStyle
import com.example.standtime.standtime.feature.components.style.CyberGlitchClockStyle
import com.example.standtime.standtime.feature.components.style.CyberpunkClockStyle
import com.example.standtime.standtime.feature.components.style.FrostedStudioClockStyle
import com.example.standtime.standtime.feature.components.style.GlassClockStyle
import com.example.standtime.standtime.feature.components.style.IndustrialClockStyle
import com.example.standtime.standtime.feature.components.style.LiquidGradientClockStyle
import com.example.standtime.standtime.feature.components.style.LofiClockStyle
import com.example.standtime.standtime.feature.components.style.LuxuryClockStyle
import com.example.standtime.standtime.feature.components.style.MacOsClockStyle
import com.example.standtime.standtime.feature.components.style.NasaClockStyle
import com.example.standtime.standtime.feature.components.style.NightOwlClockStyle
import com.example.standtime.standtime.feature.components.style.NordicClockStyle
import com.example.standtime.standtime.feature.components.style.NothingDotClockStyle
import com.example.standtime.standtime.feature.components.style.NothingOfficialClockStyle
import com.example.standtime.standtime.feature.components.style.OledStealthClockStyle
import com.example.standtime.standtime.feature.components.style.PaperMinimalismClockStyle
import com.example.standtime.standtime.feature.components.style.PixelPetClockStyle
import com.example.standtime.standtime.feature.components.style.PixelStackClockStyle
import com.example.standtime.standtime.feature.components.style.Ps5ClockStyle
import com.example.standtime.standtime.feature.components.style.RetroFlipClockStyle
import com.example.standtime.standtime.feature.components.style.RolexClockStyle
import com.example.standtime.standtime.feature.components.style.SolarOrbitClockStyle
import com.example.standtime.standtime.feature.components.style.SwissClockStyle
import com.example.standtime.standtime.feature.components.style.SynthwaveClockStyle
import com.example.standtime.standtime.feature.components.style.TerminalClockStyle
import com.example.standtime.standtime.feature.components.style.TeslaClockStyle
import com.example.standtime.standtime.feature.components.style.TokyoClockStyle
import com.example.standtime.standtime.feature.components.style.TokyoNeonClockStyle
import com.example.standtime.standtime.feature.components.style.TypewriterClockStyle
import com.example.standtime.standtime.feature.components.style.TypographyFocusClockStyle
import com.example.standtime.standtime.feature.components.style.WordsClockStyle
import com.example.standtime.standtime.feature.components.style.ZenArchitectureClockStyle
import com.example.standtime.standtime.feature.utils.SavedCustomClockStyle
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

private val builtinClockStyles =
    listOf<@Composable (GalleryClockParts, StandTimeLanguage, Color, Modifier) -> Unit>(
        ::NothingOfficialClockStyle,
        ::Ps5ClockStyle,
        ::TeslaClockStyle,
        ::NasaClockStyle,
        ::PixelStackClockStyle,
        ::TokyoClockStyle,
        ::BraunClockStyle,
        ::TerminalClockStyle,
        ::CyberpunkClockStyle,
        ::PixelPetClockStyle,
        ::LofiClockStyle,
        ::RolexClockStyle,
        ::GlassClockStyle,
        ::LuxuryClockStyle,
        ::BauhausClockStyle,
        ::MacOsClockStyle,
        ::WordsClockStyle,
        ::CoffeeClockStyle,
        ::NightOwlClockStyle,
        ::AnalogZenClockStyle,
        ::RetroFlipClockStyle,
        ::BinaryPulseClockStyle,
        ::SolarOrbitClockStyle,
        ::TypewriterClockStyle,
        ::LiquidGradientClockStyle,
        ::AdminPanelClockStyle,
        ::SynthwaveClockStyle,
        ::ZenArchitectureClockStyle,
        ::ArchitectStudioClockStyle,
        ::OledStealthClockStyle,
        ::NordicClockStyle,
        ::SwissClockStyle,
        ::IndustrialClockStyle,
        ::FrostedStudioClockStyle,
        ::TokyoNeonClockStyle,
        ::PaperMinimalismClockStyle,
        ::CyberGlitchClockStyle,
        ::AbstractGeometricClockStyle,
        ::TypographyFocusClockStyle,
        ::NothingDotClockStyle
    )

@Composable
fun GalleryClockContent(
    index: Int,
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    customStyles: List<SavedCustomClockStyle> = emptyList(),
    modifier: Modifier = Modifier
) {
    ResponsiveGalleryFrame(modifier = modifier) {
        AnimatedGalleryStyle(index = index) {
            if (index < builtinClockStyles.size) {
                builtinClockStyles[index].invoke(
                    parts,
                    language,
                    accentColor,
                    Modifier.fillMaxSize()
                )
            } else {
                customStyles.getOrNull(index - builtinClockStyles.size)?.let { savedStyle ->
                    CustomClockStyle(
                        parts = parts,
                        custom = savedStyle.settings,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: FrostedStudioClockStyle(
                    parts,
                    language,
                    accentColor,
                    Modifier.fillMaxSize()
                )
            }
        }
    }
}
