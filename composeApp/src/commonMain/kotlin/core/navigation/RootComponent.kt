package core.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.items
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import core.utils.GlobalNavigator
import core.utils.GlobalNavigatorListener
import core.utils.KeyRepository
import core.utils.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import platform.goHome
import ui.screen.MainVM
import ui.screen.account.activate.ActivateVM
import ui.screen.account.agreement.AgreementVM
import ui.screen.account.forget.ForgetPwdVM
import ui.screen.account.login.LoginVM
import ui.screen.account.register.RegisterVM
import ui.screen.guide.GuideVM
import ui.screen.guide.splash.SplashVM
import ui.widget.ToastState
import core.navigation.IRootComponent.Child

/**
 * 全局导航组件
 *
 * @author 高国峰
 * @date 2023/12/6-15:23
 */
@OptIn(ExperimentalDecomposeApi::class)
class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, IRootComponent {

    val scope = coroutineScope(Dispatchers.Main)

    /**
     * Toast状态
     */
    var toastState: ((String, ToastState.ToastStyle) -> Unit)? = null

    /**
     * 导航堆栈
     */
    private val navigation = StackNavigation<Configuration>()

    private val stack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.Splash,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val childStack: Value<ChildStack<*, Child>> = stack

    /**
     * 记录当前点击返回按钮的时间
     */
    private var lastBackTime = 0L

    init {
        GlobalNavigator.setListener(
            object : GlobalNavigatorListener {
                override fun login() {
                    pushNew(Configuration.Login)
                }
            }
        )
    }

    private fun createChild(
        config: Configuration,
        component: ComponentContext
    ): Child {
        return when (config) {
            Configuration.Splash -> Child.Splash(SplashVM(component, this))
            Configuration.Guide -> Child.Guide(GuideVM(component, this))
            Configuration.Main -> Child.Main(MainVM(component, this))
            Configuration.Login -> Child.Login(LoginVM(component, this))
            Configuration.ActivateAccount -> Child.ActivateAccount(ActivateVM(component, this))
            is Configuration.Register -> Child.Register(
                RegisterVM(
                    component,
                    config.username,
                    this
                )
            )

            is Configuration.ForgetPwd -> Child.ForgetPwd(
                ForgetPwdVM(
                    component,
                    config.username,
                    this
                )
            )
            is Configuration.Agreement -> Child.Agreement(
                AgreementVM(
                    component,
                    config.type,
                    this
                )
            )
        }
    }

    override fun toastDefault(msg: String?) {
        toastState?.invoke(msg ?: "", ToastState.ToastStyle.Default)
    }

    override fun toastError(msg: String?) {
        toastState?.invoke(msg ?: "", ToastState.ToastStyle.Error)
    }

    override fun toastSuccess(msg: String?) {
        toastState?.invoke(msg ?: "", ToastState.ToastStyle.Success)
    }

    override fun onBack() {
        if (childStack.items.size <= 1) {
            // 退出到桌面
            goHome()
            return
        }
        navigation.pop()
    }

    override fun onNavigationToScreenGuide() {
        if (KeyRepository.isFirstLaunch) {
            KeyRepository.isFirstLaunch = false
            navigation.replaceCurrent(Configuration.Guide)
            return
        } else if (KeyRepository.token.isNotEmpty()) {
            navigation.replaceCurrent(Configuration.Main)
        } else {
            navigation.replaceCurrent(Configuration.Login)
        }
    }

    override fun onNavigationReplaceToScreenMain() {
        if (KeyRepository.token.isNotEmpty()) {
            navigation.replaceCurrent(Configuration.Main)
        } else {
            navigation.replaceCurrent(Configuration.Login)
        }
    }

    override fun onNavigationToScreenMain() {
        pushNew(Configuration.Main, true)
    }

    override fun onNavigationToScreenRegister(username: String) {
        pushNew(Configuration.Register(username))
    }

    override fun onNavigationToScreenLogin() {
        pushNew(Configuration.Login)
    }

    override fun onNavigationToScreenForgetPwd(username: String) {
        pushNew(Configuration.ForgetPwd(username))
    }

    override fun onNavigationToScreenActivateAccount(username: String) {
        pushNew(Configuration.ActivateAccount)
    }

    override fun onNavigationToScreenAgreement(type: Int) {
        pushNew(Configuration.Agreement(type))
    }

    /**
     * 导航屏幕配置
     */
    @Serializable
    private sealed interface Configuration {

        /**
         * 启动页
         */
        @Serializable
        data object Splash : Configuration

        /**
         * 引导页
         */
        @Serializable
        data object Guide : Configuration

        /**
         * 主页
         */
        @Serializable
        data object Main : Configuration

        /**
         * 登录
         */
        @Serializable
        data object Login : Configuration

        /**
         * 注册
         */
        @Serializable
        data class Register(val username: String) : Configuration

        /**
         * 忘记密码
         */
        @Serializable
        data class ForgetPwd(val username: String) : Configuration

        /**
         * 激活账号
         */
        @Serializable
        data object ActivateAccount : Configuration

        /**
         * 系统协议
         */
        @Serializable
        data class Agreement(val type: Int) : Configuration
    }

    /**
     * 导航到新的屏幕
     *
     * @param configuration 屏幕配置
     * @param isReplace 是否替换当前屏幕
     */
    private fun pushNew(configuration: Configuration, isReplace: Boolean = false) {
        scope.launch {
            if (childStack.items.any { it.configuration == configuration }) {
                // 已存在，带到最前面
                navigation.bringToFront(configuration)
            } else {
                if (isReplace) {
                    navigation.replaceCurrent(configuration)
                } else {
                    navigation.pushNew(configuration)
                }
            }
        }
    }

    private fun showToast(msg: String?, style: ToastState.ToastStyle) {
        toastState?.invoke(msg ?: "", style)
    }

}