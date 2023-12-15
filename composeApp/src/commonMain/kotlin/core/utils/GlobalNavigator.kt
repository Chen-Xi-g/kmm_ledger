package core.utils

/**
 * 全局Navigation跳转监听, 如触发登录跳转等全局跳转
 *
 * @author 高国峰
 * @date 2023/12/9-14:06
 */
object GlobalNavigator {

    private val listener = mutableListOf<GlobalNavigatorListener>()

    /**
     * 设置监听
     */
    fun setListener(listener: GlobalNavigatorListener) {
        if (!this.listener.contains(listener)){
            this.listener.add(listener)
        }
    }

    /**
     * 打开登录页
     */
    fun login() {
        listener.forEach {
            it.login()
        }
    }

    /**
     * 移除监听
     */
    fun removeListener() {
        listener.clear()
    }
}

/**
 * 全局Navigation跳转监听
 */
interface GlobalNavigatorListener {
    /**
     * 打开登录页
     */
    fun login()
}