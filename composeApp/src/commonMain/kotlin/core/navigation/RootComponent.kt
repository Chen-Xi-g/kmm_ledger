package core.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.items
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import core.utils.GlobalNavigator
import core.utils.GlobalNavigatorListener
import kotlinx.serialization.Serializable
import platform.goHome
import ui.screen.splash.SplashVM
import ui.widget.ToastState

/**
 * 设置导航
 *
 * @author 高国峰
 * @date 2023/12/6-15:23
 */
@OptIn(ExperimentalDecomposeApi::class)
class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, BackHandlerOwner {

    /**
     * Toast状态
     */
    var toastState: ((String, ToastState.ToastStyle) -> Unit)? = null

    /**
     * 导航堆栈
     */
    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.Splash,
        handleBackButton = true,
        childFactory = ::createChild
    )

    init {
        GlobalNavigator.setListener(
            object : GlobalNavigatorListener {
                override fun login() {
                    // TODO: 跳转到登录页
                }
            }
        )
    }

    private fun createChild(
        config: Configuration,
        componentContext: ComponentContext
    ): Child {
        return when (config) {
            Configuration.Splash -> Child.Splash(
                SplashVM(
                    componentContext = componentContext,
                    onToast = ::showToast
                ) {
                    // TODO: 跳转到引导页, 建议使用pushNew
                }
            )
        }
    }

    /**
     * 子组件
     */
    sealed class Child {
        /**
         * 启动页
         */
        data class Splash(val component: SplashVM) : Child()
    }

    /**
     * 导航屏幕配置
     */
    @Serializable
    sealed class Configuration {

        /**
         * 启动页
         */
        @Serializable
        data object Splash : Configuration()
    }

    private fun pushNew(configuration: Configuration) {
        if (childStack.items.any { it.configuration == configuration }) {
            navigation.bringToFront(configuration)
        } else {
            navigation.pushNew(configuration)
        }
    }

    fun onBackClicked() {
        if (childStack.items.size <= 1) {
            // 退出到桌面
            goHome()
            return
        }
        navigation.pop()
    }

    private fun showToast(msg: String?, style: ToastState.ToastStyle) {
        toastState?.invoke(msg ?: "", style)
    }

}