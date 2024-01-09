package ua.com.honchar.arstudy.data.sharedPref

import android.content.Context

class SharedPref(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    fun saveLangId(langId: Int) {
        prefs.edit().putInt(LANG_ID, langId).apply()
    }

    fun getLangId(): Int {
        return prefs.getInt(LANG_ID, -1)
    }

    fun saveSpeakInfo(speak: Boolean) {
        prefs.edit().putBoolean(SPEAK, speak).apply()
    }

    fun getSpeakInfo(): Boolean {
        return prefs.getBoolean(SPEAK, true)
    }

    companion object {
        const val SHARED_PREFS = "arStudy"
        private const val LANG_ID = "lang_id"
        private const val SPEAK = "speak"
    }
}