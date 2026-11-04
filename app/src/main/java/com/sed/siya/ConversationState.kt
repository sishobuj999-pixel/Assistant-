package com.sed.siya

class ConversationState {
    enum class PendingAction { NONE, DPI_VALUE }

    var pendingAction: PendingAction = PendingAction.NONE

    fun consume(input: String): String? {
        return when (pendingAction) {
            PendingAction.DPI_VALUE -> {
                pendingAction = PendingAction.NONE
                val value = input.filter { it.isDigit() }
                if (value.isBlank()) {
                    "আমি কোনো DPI value বুঝতে পারিনি। আবার সংখ্যা বলুন।"
                } else {
                    "আপনি $value বলেছেন। Android-এর নিরাপত্তা ও ফোনভেদে সীমাবদ্ধতার কারণে SIYA এখন value-টি Developer Options-এ আপনার জন্য প্রস্তুত workflow হিসেবে ব্যবহার করবে।"
                }
            }
            PendingAction.NONE -> null
        }
    }
}
