package com.echochat.cid.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Menyimpan data akun tamu (UID lokal + nama panggilan) di SharedPreferences.
 * Tidak ada server; setiap perangkat membuat identitas sendiri saat pertama kali dibuka.
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    val myUid: String
        get() {
            var uid = prefs.getString(KEY_UID, null)
            if (uid == null) {
                uid = UidGenerator.generate()
                prefs.edit().putString(KEY_UID, uid).apply()
            }
            return uid
        }

    var displayName: String
        get() = prefs.getString(KEY_NAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_NAME, value).apply()

    val hasCompletedSetup: Boolean
        get() = displayName.isNotBlank()

    companion object {
        private const val PREFS_NAME = "echochat_session"
        private const val KEY_UID = "my_uid"
        private const val KEY_NAME = "display_name"
    }
}
