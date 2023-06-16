package cz.mendelu.pef.fitnesswatchman.utils

import android.content.Context
import java.util.*

object LanguageUtils {
    private const val PREFERENCE_NAME = "app_preferences"
    private const val LAST_LANGUAGE_KEY = "last_language"

    fun getSavedLanguage(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(LAST_LANGUAGE_KEY, "") ?: ""
    }

    fun saveLanguage(context: Context, language: String) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(LAST_LANGUAGE_KEY, language).apply()
    }

    fun isLanguageCzech(): Boolean {
        val language = Locale.getDefault().language
        return language.equals("cs", ignoreCase = true)
    }
}