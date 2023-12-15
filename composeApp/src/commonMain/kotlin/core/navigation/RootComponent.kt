package core.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.items
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import core.utils.GlobalNavigator
import core.utils.GlobalNavigatorListener
import core.utils.KeyRepository
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
                    pushNew(Configuration.Login)
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
                SplashVM(componentContext) {
                    if (KeyRepository.isFirstLaunch) {
                        KeyRepository.isFirstLaunch = false
                        navigation.replaceCurrent(Configuration.Guide)
                        return@SplashVM
                    } else if (KeyRepository.token.isNotEmpty()) {
                        navigation.replaceCurrent(Configuration.Main)
                    } else {
                        navigation.replaceCurrent(Configuration.Login)
                    }
                }
            )

            Configuration.Guide -> Child.Guide(
                GuideVM(componentContext) {
                    if (KeyRepository.token.isNotEmpty()) {
                        navigation.replaceCurrent(Configuration.Main)
                    } else {
                        navigation.replaceCurrent(Configuration.Login)
                    }
                }
            )

            Configuration.Main -> Child.Main(
                MainVM(componentContext) {
                    onBackClicked()
                }
            )

            Configuration.Login -> Child.Login(
                LoginVM(
                    componentContext,
                    goBack = {
                        onBackClicked()
                    },
                    onNavigationToScreenMain = {
                        navigation.replaceCurrent(Configuration.Main)
                    },
                    onNavigationToScreenRegister = {
                        pushNew(Configuration.Register(it))
                    },
                    onNavigationToScreenActivateAccount = {
                        pushNew(Configuration.ActivateAccount)
                    },
                    onNavigationToScreenForgetPassword = {
                        pushNew(Configuration.ForgetPwd(it))
                    },
                    onNavigationToScreenUserAgreement = {
                        pushNew(Configuration.Agreement(1))
                    },
                    onNavigationToScreenPrivacyPolicy = {
                        pushNew(Configuration.Agreement(2))
                    }
                )
            )

            is Configuration.Register -> Child.Register(
                RegisterVM(
                    componentContext,
                    accountUsername = config.username,
                    goBack = {
                        onBackClicked()
                    }
                )
            )

            is Configuration.ForgetPwd -> Child.ForgetPwd(
                ForgetPwdVM(
                    componentContext,
                    accountUsername = config.username,
                    goBack = {
                        onBackClicked()
                    }
                )
            )

            Configuration.ActivateAccount -> Child.ActivateAccount(
                ActivateVM(
                    componentContext,
                    goBack = {
                        onBackClicked()
                    }
                )
            )

            is Configuration.Agreement -> Child.Agreement(
                AgreementVM(
                    componentContext,
                    type = config.type,
                    goBack = {
                        onBackClicked()
                    }
                )
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

        /**
         * 引导页
         */
        data class Guide(val component: GuideVM) : Child()

        /**
         * 主页
         */
        data class Main(val component: MainVM) : Child()

        /**
         * 登录
         */
        data class Login(val component: LoginVM) : Child()

        /**
         * 注册
         */
        data class Register(val component: RegisterVM) : Child()

        /**
         * 忘记密码
         */
        data class ForgetPwd(val component: ForgetPwdVM) : Child()

        /**
         * 激活账号
         */
        data class ActivateAccount(val component: ActivateVM) : Child()

        /**
         * 系统协议
         */
        data class Agreement(val component: AgreementVM) : Child()
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

        /**
         * 引导页
         */
        @Serializable
        data object Guide : Configuration()

        /**
         * 主页
         */
        @Serializable
        data object Main : Configuration()

        /**
         * 登录
         */
        @Serializable
        data object Login : Configuration()

        /**
         * 注册
         */
        @Serializable
        data class Register(val username: String) : Configuration()

        /**
         * 忘记密码
         */
        @Serializable
        data class ForgetPwd(val username: String) : Configuration()

        /**
         * 激活账号
         */
        @Serializable
        data object ActivateAccount : Configuration()

        /**
         * 系统协议
         */
        @Serializable
        data class Agreement(val type: Int) : Configuration()
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

}