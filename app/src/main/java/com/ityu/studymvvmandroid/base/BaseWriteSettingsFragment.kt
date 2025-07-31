package com.ityu.studymvvmandroid.base

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewbinding.ViewBinding

/**
 * 一个专门处理 WRITE_SETTINGS 权限的基类 Fragment。
 * 需要此权限的 Fragment (如亮度、屏保设置) 应继承此类。
 */
abstract class BaseWriteSettingsFragment<VB : ViewBinding>(bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    BaseFragment<VB>(
        bindingInflater
    ) {

    private lateinit var writeSettingsLauncher: ActivityResultLauncher<Intent>

    /**
     * 子类必须实现此方法，以定义在权限被授予后需要执行的初始化操作。
     * 例如：设置监听器、观察 ViewModel。
     */
    abstract fun onPermissionGranted()

    /**
     * (可选) 子类可以重写此方法，以处理权限被拒绝的情况。
     */
    open fun onPermissionDenied() {
        Toast.makeText(
            context,
            "Permission not granted. This feature is disabled.",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 在 onCreate 中注册 ActivityResultLauncher
        writeSettingsLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // 当用户从设置页面返回时，再次检查权限
            if (hasWriteSettingsPermission()) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkAndRequestPermission()
    }

    /**
     * 检查权限，如果已授予则执行 onPermissionGranted()，否则启动权限请求流程。
     */
    private fun checkAndRequestPermission() {
        if (hasWriteSettingsPermission()) {
            onPermissionGranted()
        } else {
            requestWriteSettingsPermission()
        }
    }

    private fun requestWriteSettingsPermission() {
        Toast.makeText(
            context,
            "Please grant permission to modify system settings.",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:${requireActivity().packageName}")
        }
        writeSettingsLauncher.launch(intent)
    }

    /**
     * 辅助函数，用于检查权限。
     */
    protected fun hasWriteSettingsPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(requireContext())
        } else {
            true
        }
    }
}