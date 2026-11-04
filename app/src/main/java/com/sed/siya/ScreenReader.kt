package com.sed.siya

object ScreenReader {
    fun currentVisibleText(): String {
        return SIYAAccessibilityService.instance?.visibleScreenText().orEmpty()
    }
}
