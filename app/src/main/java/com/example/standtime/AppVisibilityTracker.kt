package com.standtime.clock

object AppVisibilityTracker {
    @Volatile
    var isAppVisible: Boolean = false
}
