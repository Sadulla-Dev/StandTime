package com.example.standtime

object AppVisibilityTracker {
    @Volatile
    var isAppVisible: Boolean = false
}
