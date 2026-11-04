package com.sed.siya

import android.content.Context
import android.content.Intent
import android.provider.Settings

class CommandRouter(private val context: Context) {
    data class Result(val reply: String, val action: (() -> Unit)? = null)

    fun handle(command: String): Result {
        val c = command.lowercase()
        return when {
            c.contains("whatsapp") && (c.contains("message") || c.contains("মেসেজ")) -> {
                Result("আমি WhatsApp খুলে screen-এ দৃশ্যমান chat ও message check করার চেষ্টা করছি।") {
                    SIYAAccessibilityService.instance?.openApp("com.whatsapp")
                }
            }
            c.contains("developer") || c.contains("dpi") || c.contains("ডিপি") -> {
                Result("Developer settings খুলছি। আপনি যে value দেবেন, SIYA পরের ধাপের জন্য সেটি ব্যবহার করবে।") {
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
