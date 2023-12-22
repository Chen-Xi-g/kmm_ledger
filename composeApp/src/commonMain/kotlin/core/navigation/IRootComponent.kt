package core.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ui.screen.MainVM
import ui.screen.account.activate.ActivateVM
import ui.screen.account.agreement.AgreementVM
import ui.screen.account.forget.ForgetPwdVM
import ui.screen.account.login.LoginVM
import ui.screen.account.register.RegisterVM
import ui.screen.guide.GuideVM
import ui.screen.guide.splash.SplashVM
import ui.widget.ToastState

/**
 * 全局导航组件接口
 *
 * @author 高国峰
 * @date 2023/12/22-23:42
 */
interface IRootComponent {

    companion object{

        /**
         * 用户协议
         */
        const val TYPE_AGREEMENT_USER = 0

        /**
         * 隐私协议
         */
        const val TYPE_AGREEMENT_PRIVACY = 1

    }

    val childStack: Value<ChildStack<*, Child>>

    /**
     * toast默认状态
     */
    fun toastDefault(msg: String?)

    /**
     * toast错误状态
     */
    fun toastError(msg: String?)

    /**
     * toast成功状态
     */
    fun toastSuccess(msg: String?)

    /**
     * 后退
     */
    fun onBack()

    /**
     * 导航到引导页
     */
    fun onNavigationToScreenGuide()

    /**
     * 替换到主页
     */
    fun onNavigationReplaceToScreenMain()

    /**
     * 导航到主页
     */
    fun onNavigationToScreenMain()

    /**
     * 导航到注册页
     */
    fun onNavigationToScreenRegister(username: String)

    /**
     * 导航到登录页
     */
    fun onNavigationToScreenLogin()

    /**
     * 导航到忘记密码页
     */
    fun onNavigationToScreenForgetPwd(username: String)

    /**
     * 导航到激活账号页
     */
    fun onNavigationToScreenActivateAccount(username: String)

    /**
     * 导航到系统协议页
     *
     * @param type 0:用户协议 1:隐私协议
     */
    fun onNavigationToScreenAgreement(type: Int)

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
}