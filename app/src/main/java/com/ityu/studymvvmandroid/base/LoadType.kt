package com.ityu.studymvvmandroid.base


sealed class LoadType {
    /**
     * 初始加载：通常在页面第一次创建时，或者数据为空时触发。
     */
    object INITIAL : LoadType()

    /**
     * 下拉刷新：用户通过下拉手势触发，总是会请求网络并清空旧数据。
     */
    object REFRESH : LoadType()

    /**
     * 加载更多：用户滚动到列表底部时触发。
     */
    object LOAD_MORE : LoadType()
}