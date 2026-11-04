package com.sed.siya

import android.content.Context
import android.content.Intent
import android.provider.Settings

class CommandRouter(
    private val context: Context,
    private val state: ConversationState
) {
    data class Result(val reply: String, val action: (() -> Unit)? = null)

    fun handle(command: String): Result {
        state.consume(command)?.let { return Result(it) }

        val c = command.lowercase()
        return when {
            c.contains("read screen") || c.contains("স্ক্রিন পড়") || c.contains("স্ক্রিনে কি আছে") -> {
                val text = ScreenReader.currentVisibleText()
                Result(if (text.isBlank()) "আমি এখনো screen text access পাচ্ছি না। SIYA Screen Control enable করুন।" else text.take(1200))
            }
            c.contains("whatsapp") && (c.contains("message") || c.contains("মেসেজ")) -> {
                Result("আমি WhatsApp খুলছি। এরপর SIYA screen control দিয়ে দৃশ্যমান chat তথ্য পড়তে পারবে, যদি Accessibility চালু থাকে।") {
                    SIYAAccessibilityService.instance?.openApp("com.whatsapp")
                }
            }
            c.contains("developer") || c.contains("dpi") || c.contains("ডিপি") -> {
                state.pendingAction = ConversationState.PendingAction.DPI_VALUE
                Result("Developer settings খুলছি। কত DPI বা Smallest width value রাখতে চান?") {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }
            c.contains("accessibility") || c.contains("অ্যাক্সেসিবিলিটি") -> {
                Result("SIYA Accessibility permission settings খুলছি।") {
                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }
            else -> Result("এই command-এর জন্য tool এখনো যোগ করা হয়নি: $command")
        }
    }
}
