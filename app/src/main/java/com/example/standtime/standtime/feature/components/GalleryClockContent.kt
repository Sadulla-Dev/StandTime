package com.example.standtime.standtime.feature.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.standtime.standtime.feature.components.style.AbstractGeometricClockStyle
import com.example.standtime.standtime.feature.components.style.AdminPanelClockStyle
import com.example.standtime.standtime.feature.components.style.AnalogZenClockStyle
import com.example.standtime.standtime.feature.components.style.ArabicMajlisClockStyle
import com.example.standtime.standtime.feature.components.style.ArchitectStudioClockStyle
import com.example.standtime.standtime.feature.components.style.AuraPulseClockStyle
import com.example.standtime.standtime.feature.components.style.AuroraClockStyle
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
import com.example.standtime.standtime.feature.components.style.LofiClockStyle
import com.example.standtime.standtime.feature.components.style.LuxuryClockStyle
import com.example.standtime.standtime.feature.components.style.MacOsClockStyle
import com.example.standtime.standtime.feature.components.style.NasaClockStyle
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
import com.example.standtime.standtime.feature.components.style.ContrastSplitClockStyle
import com.example.standtime.standtime.feature.components.style.CyberGridClockStyle
import com.example.standtime.standtime.feature.components.style.DarkSoulClockStyle
import com.example.standtime.standtime.feature.components.style.GlitchClockStyle
import com.example.standtime.standtime.feature.components.style.HologramClockStyle
import com.example.standtime.standtime.feature.components.style.QuantumClockStyle
import com.example.standtime.standtime.feature.components.style.RetroTerminalClockStyle
import com.example.standtime.standtime.feature.components.style.TokyoClockStyle
import com.example.standtime.standtime.feature.components.style.TokyoNeonClockStyle
import com.example.standtime.standtime.feature.components.style.TypewriterClockStyle
import com.example.standtime.standtime.feature.components.style.TypographyFocusClockStyle
import com.example.standtime.standtime.feature.components.style.VaporWaveClockStyle
import com.example.standtime.standtime.feature.components.style.WordsClockStyle
import com.example.standtime.standtime.feature.components.style.ZenArchitectureClockStyle
import com.example.standtime.standtime.feature.utils.SavedCustomClockStyle
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

private val builtinClockStyles =
    listOf<@Composable (GalleryClockParts, StandTimeLanguage, Color, Modifier) -> Unit>(
        ::NothingOfficialClockStyle,     // 01
        ::ContrastSplitClockStyle,       // 02
        ::AuraPulseClockStyle,           // 03
        ::QuantumClockStyle,             // 04
        ::NothingDotClockStyle,          // 05
        ::NasaClockStyle,                // 06
        ::BauhausClockStyle,             // 07
        ::PixelStackClockStyle,          // 08
        ::IndustrialClockStyle,          // 09
        ::GlitchClockStyle,              // 10
        ::WordsClockStyle,               // 11
        ::TokyoClockStyle,               // 12
        ::ArabicMajlisClockStyle,        // 13
        ::RetroFlipClockStyle,           // 14
        ::VaporWaveClockStyle,           // 15
        ::SwissClockStyle,               // 16
        ::BraunClockStyle,               // 17
        ::TerminalClockStyle,            // 18
        ::CyberpunkClockStyle,           // 19
        ::LofiClockStyle,                // 20
        ::RolexClockStyle,               // 21
        ::AuroraClockStyle,              // 22
        ::AnalogZenClockStyle,           // 23
        ::TeslaClockStyle,               // 24
        ::GlassClockStyle,               // 25
        ::PaperMinimalismClockStyle,     // 26
        ::LuxuryClockStyle,              // 27
        ::MacOsClockStyle,               // 28
        ::CyberGridClockStyle,           // 29
        ::CoffeeClockStyle,              // 30
        ::BinaryPulseClockStyle,         // 31
        ::SolarOrbitClockStyle,          // 32
        ::TypewriterClockStyle,          // 33
        ::AdminPanelClockStyle,          // 34
        ::SynthwaveClockStyle,           // 35
        ::ZenArchitectureClockStyle,     // 36
        ::RetroTerminalClockStyle,       // 37
        ::ArchitectStudioClockStyle,     // 38
        ::OledStealthClockStyle,         // 39
        ::FrostedStudioClockStyle,       // 40
        ::TokyoNeonClockStyle,           // 41
        ::PixelPetClockStyle,            // 42
        ::CyberGlitchClockStyle,         // 43
        ::HologramClockStyle,            // 44
        ::AbstractGeometricClockStyle,   // 45
        ::TypographyFocusClockStyle,     // 46
        ::Ps5ClockStyle,                 // 47
        ::DarkSoulClockStyle,            // 48
    )

@Composable
fun GalleryClockContent(
    index: Int,
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    customStyles: List<SavedCustomClockStyle> = emptyList(),
    burnInProtectionEnabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    ResponsiveGalleryFrame(modifier = modifier) {
        AnimatedGalleryStyle(index = index, burnInProtectionEnabled = burnInProtectionEnabled) {
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
