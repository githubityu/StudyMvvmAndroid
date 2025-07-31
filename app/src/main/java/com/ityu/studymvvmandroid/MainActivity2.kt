package com.ityu.studymvvmandroid

import android.os.Bundle

import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.ityu.studymvvmandroid.databinding.ActivityMain2Binding
import com.ityu.studymvvmandroid.di.AppStatusManager
import com.ityu.studymvvmandroid.domain.model.SplashViewModel
import com.ityu.studymvvmandroid.utils.ApiLoadingState
import com.ityu.studymvvmandroid.utils.LoadingUtils
import com.ityu.studymvvmandroid.utils.Theme2Utils
import com.ityu.studymvvmandroid.utils.ToolbarController
import com.ityu.studymvvmandroid.utils.launchAndRepeatOnLifecycle


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity2 : AppCompatActivity(), ToolbarController {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var navController: NavController


    private lateinit var leftElement: MaterialButton
    private lateinit var centerTitle: TextView
    private lateinit var rightElement: TextView

    // 预先创建两种布局状态的 ConstraintSet
    private val detailPageConstraintSet = ConstraintSet()
    private val mainPageConstraintSet = ConstraintSet()


    @Inject
    lateinit var appStatusManager: AppStatusManager

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashViewModel

        splashScreen.setKeepOnScreenCondition {
            !appStatusManager.isAppReady.value
        }

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        goFullscreen() // Renamed for clarity, assuming modern API
        setupToolbar()
        setupNavigation()
        observeLoadingState()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        leftElement = binding.toolbarLeftElement
        centerTitle = binding.toolbarCenterTitle
        rightElement = binding.toolbarRightElement

        // 初始化 ConstraintSet
        val container = binding.toolbarContainer
        mainPageConstraintSet.clone(container) // 记录当前XML定义的布局为“主页”布局

        detailPageConstraintSet.clone(container)
        // 定义“详情页”布局：让中间标题真正居中，忽略左右元素
        detailPageConstraintSet.connect(
            R.id.toolbar_center_title,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        detailPageConstraintSet.connect(
            R.id.toolbar_center_title,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        detailPageConstraintSet.setHorizontalBias(R.id.toolbar_center_title, 0.5f)
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.MainFragment) {
                // 应用主页样式
                applyMainPageToolbarStyle()
            } else {
                // 应用详情页样式
                applyDetailPageToolbarStyle(destination.label.toString())
            }
        }
    }

    private fun applyMainPageToolbarStyle() {
        // 1. 切换到主页的布局约束
        mainPageConstraintSet.applyTo(binding.toolbarContainer)
        // 2. 重置元素状态
        setLeft(null, null, null) // 清空左侧
        setRight(null)                 // 清空右侧
        setTitle(null)                 // 清空中间标题
    }

    /**
     * 应用详情页的 Toolbar 样式。
     * 职责：切换布局约束，并设置通用的“详情页”元素。
     * @param defaultTitle 从 navigation graph 的 label 中获取的默认标题。
     */
    private fun applyDetailPageToolbarStyle(defaultTitle: String) {
        // 1. 切换到详情页的布局约束
        detailPageConstraintSet.applyTo(binding.toolbarContainer)

        // 2. 设置通用元素
        setTitle(defaultTitle)
        setLeft("Back", R.drawable.ic_arrow_back) { navController.navigateUp() }
        setRight(null) // 详情页通常没有右侧元素
    }


    override fun setLeft(text: String?, iconResId: Int?, onClick: ((View) -> Unit)?) {
        leftElement.text = text
        leftElement.visibility = if (text == null && iconResId == null) View.GONE else View.VISIBLE

        if (iconResId != null) {
            leftElement.setIconResource(iconResId)
            leftElement.setTextColor(getColorStateList(android.R.color.white))
            leftElement.backgroundTintList =
                getColorStateList(android.R.color.holo_blue_dark) // 返回按钮有背景色
        } else {
            leftElement.icon = null
            leftElement.setTextColor(getColorStateList(android.R.color.holo_blue_dark))
            leftElement.backgroundTintList =
                getColorStateList(android.R.color.transparent) // 左侧文本无背景色
        }

        leftElement.setOnClickListener(onClick)
    }

    override fun setRight(text: String?, iconResId: Int?, onClick: ((View) -> Unit)?) {
        rightElement.text = text
        rightElement.setTextColor(getColorStateList(android.R.color.holo_blue_dark))
        rightElement.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
        rightElement.setOnClickListener(onClick)
    }

    override fun setTitle(text: String?) {
        centerTitle.text = text
        centerTitle.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
    }


    private fun goFullscreen() {
        Theme2Utils.hideSystemUI(this.window)
    }

    private fun observeLoadingState() {
        // launchAndRepeatOnLifecycle 是 LifecycleOwner 的扩展函数，Activity同样是LifecycleOwner
        launchAndRepeatOnLifecycle {
            ApiLoadingState.isLoading.collectLatest { isLoading ->
                if (isLoading) {
                    // 在Activity中，上下文直接使用 this
                    LoadingUtils.show(this@MainActivity2)
                } else {
                    LoadingUtils.hide()
                }
            }
        }
    }
}