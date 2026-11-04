package com.sed.siya

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class SIYAAccessibilityService : AccessibilityService() {
    companion object {
        @Volatile var instance: SIYAAccessibilityService? = null
    }

    override fun onServiceConnected() {
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    fun visibleScreenText(): String {
        val root = rootInActiveWindow ?: return ""
        val out = StringBuilder()
        collect(root, out)
        return out.toString().trim()
    }

    private fun collect(node: AccessibilityNodeInfo, out: StringBuilder) {
        node.text?.toString()?.takeIf { it.isNotBlank() }?.let {
            out.append(it).append("\n")
        }
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { collect(it, out) }
        }
    }

    fun openApp(packageName: String): Boolean {
        val intent = packageManager.getLaunchIntentForPackage(packageName) ?: return false
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        return true
    }
}
