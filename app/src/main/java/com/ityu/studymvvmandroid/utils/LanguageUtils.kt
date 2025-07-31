package com.ityu.studymvvmandroid.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.fragment.app.FragmentActivity
import java.util.Locale

object LanguageUtils {
    private fun toggleLanguage(context: Context): Context {
        // 获取当前语言
        val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
        // 根据当前语言切换到另一种语言
        val newLanguage = if (currentLocale.language == "en") "zh" else "en"
        return setLocale(context, newLanguage)
    }



    private fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)

        configuration.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.createConfigurationContext(configuration)
        } else {
            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context
        }
    }


    fun changeLanguage(context: FragmentActivity) {
        val newContext = toggleLanguage(context)
        context.resources.updateConfiguration(
            newContext.resources.configuration,
            newContext.resources.displayMetrics
        )
    }

    fun getCurrentLanguage(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].language
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale.language
        }
    }

}