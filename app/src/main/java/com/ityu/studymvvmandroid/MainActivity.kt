package com.ityu.studymvvmandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.ityu.studymvvmandroid.databinding.ActivityMainBinding
import com.ityu.studymvvmandroid.domain.model.SharedViewModel
import com.ityu.studymvvmandroid.utils.LogUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        sharedViewModel.sharedData.observe(this) { data ->
            // Update UI based on shared data
            LogUtils.e("MainActivity", "Received shared data: $data")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // 在这里添加你的拦截逻辑
        // 检查当前是否在需要拦截的页面
        if (navController.currentDestination?.id == R.id.FirstFragment) { // 假设你的Fragment在nav_graph中的ID是这个
            // 是目标页面，显示弹窗
            showExitConfirmationDialog()

            // 返回 true 表示我们已经处理了这个点击事件，不需要系统再做任何事。
            // NavController 的 navigateUp 将不会被调用，除非我们在弹窗中手动调用它。
            return true
        }

        // 如果不是目标页面，就执行默认的返回逻辑
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("放弃编辑？")
            .setMessage("您所做的更改将不会被保存，确定要离开吗？")
            .setPositiveButton("确定离开") { _, _ ->
                // 用户确认离开，现在我们手动执行真正的返回操作
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                // 这里调用 navigateUp 并传入 appBarConfiguration，行为和默认情况完全一致
                navController.navigateUp(appBarConfiguration)
            }
            .setNegativeButton("取消", null) // 用户点击取消，对话框消失，不做任何事
            .show()
    }

}