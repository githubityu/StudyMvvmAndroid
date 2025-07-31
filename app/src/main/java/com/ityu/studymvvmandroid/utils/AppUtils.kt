package com.ityu.studymvvmandroid.utils


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.math.BigInteger
import java.security.MessageDigest

/**
 * 一个包含各种常用工具方法的单例对象。
 * 类似于 Flutter 中的静态 Utils 类。
 */
object AppUtils {
    // 使用 lateinit 来持有 ApplicationContext
    private lateinit var appContext: Context
    /**
     * 启动一个 Intent 来打开 URL。
     * @param context 上下文，用于启动 Activity。
     * @param url 要打开的网页链接。
     */
    fun launchURL(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // 如果设备上没有浏览器或能处理此 URL 的应用
            toast(context, "Could not launch $url")
        }
    }

    /**
     * 隐藏软键盘。
     * 推荐从 Activity 中调用。
     * @param activity 当前的 Activity。
     */
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        // 如果没有当前焦点，创建一个新的 view 来获取 token
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 显示一个标准的 Toast 消息。
     * 注意：从 Android 11 (R) 开始，自定义 Toast 视图受到严格限制。
     * 这里的实现是标准的系统 Toast。
     * @param context 上下文。
     * @param message 要显示的消息。
     */
    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * 生成输入字符串的 MD5 哈希值。
     * @param input 原始字符串。
     * @return 32位的 MD5 哈希字符串。
     */
    fun generateMd5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(input.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

    /**
     * 获取完整的设备型号和系统版本信息。
     * @return 格式如 "Google: Pixel 7 Pro Android 14"。
     */
    fun getDeviceFullModelName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val version = Build.VERSION.RELEASE
        return "$manufacturer: $model Android $version"
    }

    /**
     * 获取设备的唯一标识符 ANDROID_ID。
     * 注意：此 ID 在应用卸载重装或设备恢复出厂设置后可能会改变。
     * @param context 上下文，用于获取 ContentResolver。
     * @return 设备的 ANDROID_ID，可能为 null。
     */
    fun getDeviceId(): String? {
        val context = AppContextProvider.getContext()
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}