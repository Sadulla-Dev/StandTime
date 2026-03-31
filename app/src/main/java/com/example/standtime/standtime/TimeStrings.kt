package com.example.standtime.standtime

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

typealias StandTimeStrings = StandTimeLanguage

@Composable
@ReadOnlyComposable
fun localizedStringResource(
    @StringRes id: Int,
    language: StandTimeLanguage
): String {
    val context = LocalContext.current
    return context.localizedContext(language).getString(id)
}

private fun Context.localizedContext(language: StandTimeLanguage): Context {
    val locale = when (language) {
        StandTimeLanguage.ENGLISH -> Locale.ENGLISH
        StandTimeLanguage.UZBEK -> Locale.forLanguageTag("uz")
        StandTimeLanguage.RUSSIAN -> Locale.forLanguageTag("ru")
    }
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    return createConfigurationContext(configuration)
}

val StandTimeLanguage.chargingLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.charging_label, this)

val StandTimeLanguage.batteryLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.battery_label, this)

val StandTimeLanguage.batteryIdleLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.battery_idle_label, this)

val StandTimeLanguage.swipeStylesHint: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.swipe_styles_hint, this)

val StandTimeLanguage.swipeDashboardHint: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.swipe_dashboard_hint, this)

val StandTimeLanguage.swipeSetupHint: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.swipe_setup_hint, this)

val StandTimeLanguage.swipeVerticalHint: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.swipe_vertical_hint, this)

val StandTimeLanguage.clockStylesLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.clock_styles_label, this)

val StandTimeLanguage.dashboardLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.dashboard_label, this)

val StandTimeLanguage.setupLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.setup_label, this)

val StandTimeLanguage.nothingStyleLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.nothing_style_label, this)

val StandTimeLanguage.pixelStyleLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.pixel_style_label, this)

val StandTimeLanguage.iphoneStyleLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.iphone_style_label, this)

val StandTimeLanguage.minimalStyleLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.minimal_style_label, this)

val StandTimeLanguage.calendarTitle: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.calendar_title, this)

val StandTimeLanguage.pomodoroTitle: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.pomodoro_title, this)

val StandTimeLanguage.pomodoroRunning: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.pomodoro_running, this)

val StandTimeLanguage.pomodoroPaused: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.pomodoro_paused, this)

val StandTimeLanguage.pomodoroStart: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.pomodoro_start, this)

val StandTimeLanguage.pomodoroPause: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.pomodoro_pause, this)

val StandTimeLanguage.pomodoroReset: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.pomodoro_reset, this)

val StandTimeLanguage.mediaTitle: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.media_title, this)

val StandTimeLanguage.mediaPermissionBody: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.media_permission_body, this)

val StandTimeLanguage.mediaEnableAccess: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.media_enable_access, this)

val StandTimeLanguage.mediaUnavailable: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.media_unavailable, this)

val StandTimeLanguage.mediaPlay: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.media_play, this)

val StandTimeLanguage.mediaPause: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.media_pause, this)

val StandTimeLanguage.mediaNext: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.media_next, this)

val StandTimeLanguage.mediaSource: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.media_source, this)

val StandTimeLanguage.customizeTitle: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.customize_title, this)

val StandTimeLanguage.orientationHint: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.orientation_hint, this)

val StandTimeLanguage.languageTitle: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.language_title, this)

val StandTimeLanguage.themeTitle: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.theme_title, this)

val StandTimeLanguage.accentTitle: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.accent_title, this)

val StandTimeLanguage.clockStyleTitle: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.clock_style_title, this)

val StandTimeLanguage.showCalendarLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.show_calendar_label, this)

val StandTimeLanguage.showBatteryLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.show_battery_label, this)

val StandTimeLanguage.showPomodoroLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.show_pomodoro_label, this)

val StandTimeLanguage.showSecondsLabel: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.show_seconds_label, this)

val StandTimeLanguage.darkTheme: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.dark_theme_label, this)

val StandTimeLanguage.lightTheme: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.light_theme_label, this)

val StandTimeLanguage.accentLime: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.accent_lime_label, this)

val StandTimeLanguage.accentSky: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.accent_sky_label, this)

val StandTimeLanguage.accentCoral: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.accent_coral_label, this)

val StandTimeLanguage.portraitTitle: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.portrait_title, this)

val StandTimeLanguage.portraitBody: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.portrait_body, this)

val StandTimeLanguage.presetsTitle: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.presets_title, this)

val StandTimeLanguage.minutesSuffix: String
    @Composable
    get() = localizedStringResource(com.example.standtime.R.string.minutes_suffix, this)
