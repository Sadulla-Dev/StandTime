package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.standtime.standtime.StandTimeLanguage

@Composable
fun GalleryClockContent(
    index: Int,
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(20.dp)) {
        when (index) {
            0 -> NothingOfficialClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            1 -> Ps5ClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            2 -> TeslaClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            3 -> MinecraftClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            4 -> SpotifyClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            5 -> NasaClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            6 -> PixelStackClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            7 -> TokyoClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            8 -> IosStackClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            9 -> BraunClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            10 -> TerminalClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            11 -> CyberpunkClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            12 -> PixelPetClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            13 -> LofiClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            14 -> RolexClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            15 -> GlassClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            16 -> LuxuryClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            17 -> BauhausClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            18 -> MacOsClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            19 -> WordsClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            20 -> CoffeeClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            21 -> NightOwlClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            22 -> ArcadeClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            23 -> AnalogZenClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            24 -> RetroFlipClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            25 -> BinaryPulseClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            26 -> SolarOrbitClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            27 -> TypewriterClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            28 -> LiquidGradientClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            29 -> AdminPanelClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            30 -> PhotoFrameClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            31 -> SynthwaveClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            32 -> ZenArchitectureClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            33 -> ArchitectStudioClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            34 -> OledStealthClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            35 -> NordicClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            36 -> SwissClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            37 -> IndustrialClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
            38 -> TokyoNeonClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
//            39 -> PaperMinimalismClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
//            40 -> CyberGlitchClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
//            41 -> AbstractGeometricClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
//            42 -> TypographyFocusClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
//            43 -> NothingDotClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
//            else -> FrostedStudioClockStyle(parts, language, accentColor, Modifier.fillMaxSize())
        }
    }
}
