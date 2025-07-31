package com.ityu.studymvvmandroid.base // 替换为你的包名

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ityu.studymvvmandroid.databinding.FragmentBaseListBinding
import com.ityu.studymvvmandroid.utils.PAGE_SIZE
import com.ityu.studymvvmandroid.utils.PAGE_START


abstract class BaseListFragment<T, VH : RecyclerView.ViewHolder, A : ListAdapter<T, VH>> : BaseFragment<FragmentBaseListBinding>(FragmentBaseListBinding::inflate) {




    protected open val pageSize: Int = PAGE_SIZE
    protected var currentPage = PAGE_START
    protected var isLastPage = false
    protected var isLoading = false
    protected lateinit var listAdapter: A

    abstract fun createAdapter(): A
    abstract fun loadData(loadType: LoadType)
    open fun createLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(context)
    open fun configureSpanSize(layoutManager: GridLayoutManager) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefreshLayout()
        // **核心修改：移除了初始加载调用**
        // loadData(LoadType.INITIAL) // THIS IS REMOVED
    }

    private fun setupRecyclerView() {
        listAdapter = createAdapter()
        val layoutManager = createLayoutManager()
        if (layoutManager is GridLayoutManager) {
            configureSpanSize(layoutManager)
        }
        binding.recyclerView.apply {
            this.layoutManager = layoutManager
            adapter = listAdapter
            if (itemDecorationCount == 0 && this@BaseListFragment is GridSpacingProvider) {
                addItemDecoration((this@BaseListFragment as GridSpacingProvider).createItemDecoration())
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    handleLoadMore(recyclerView.layoutManager)
                }
            })
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData(LoadType.REFRESH)
        }
    }

    private fun handleLoadMore(layoutManager: RecyclerView.LayoutManager?) {
        if (layoutManager == null || isLoading || isLastPage) return
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions(null).minOrNull() ?: 0
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> return
        }
        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
            loadData(LoadType.LOAD_MORE)
        }
    }

    protected fun onLoading(loadType: LoadType) {
        isLoading = true
        // 只有当列表当前为空时，才显示全屏的加载动画
        if (listAdapter.currentList.isEmpty()) {
            showLoadingView()
        }
    }

    protected fun onDataLoaded(newData: List<T>, loadType: LoadType) {
        isLoading = false
        binding.swipeRefreshLayout.isRefreshing = false
        hideAllStateViews()
        binding.swipeRefreshLayout.visibility = View.VISIBLE
        isLastPage = newData.size < pageSize

        when (loadType) {
            LoadType.INITIAL, LoadType.REFRESH -> {
                currentPage = PAGE_START + 1
            }
            LoadType.LOAD_MORE -> {
                currentPage++
            }
        }
        submitDataToAdapter(newData, loadType)
        if (listAdapter.itemCount == 0) {
            binding.emptyView.visibility = View.VISIBLE
            binding.swipeRefreshLayout.visibility = View.GONE
        }
    }

    protected fun onError(throwable: Throwable, loadType: LoadType) {
        isLoading = false
        binding.swipeRefreshLayout.isRefreshing = false
        // 只有在列表为空时，加载失败才显示全屏错误页
        if (listAdapter.currentList.isEmpty()) {
            hideAllStateViews()
            binding.errorView.visibility = View.VISIBLE
            binding.errorText.text = throwable.localizedMessage ?: "An error occurred"
            binding.retryButton.setOnClickListener {
                loadData(LoadType.REFRESH) // 重试可以视为一次刷新
            }
        } else {
            Toast.makeText(context, throwable.localizedMessage ?: "Load failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitDataToAdapter(data: List<T>, loadType: LoadType) {
        when (loadType) {
            LoadType.INITIAL, LoadType.REFRESH -> {
                listAdapter.submitList(data)
            }
            LoadType.LOAD_MORE -> {
                val currentList = listAdapter.currentList.toMutableList()
                currentList.addAll(data)
                listAdapter.submitList(currentList)
            }
        }
    }

    private fun showLoadingView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.swipeRefreshLayout.visibility = View.GONE
        binding.emptyView.visibility = View.GONE
        binding.errorView.visibility = View.GONE
    }

    private fun hideAllStateViews() {
        binding.progressBar.visibility = View.GONE
        binding.emptyView.visibility = View.GONE
        binding.errorView.visibility = View.GONE
    }

    interface GridSpacingProvider {
        fun createItemDecoration(): RecyclerView.ItemDecoration
    }
}