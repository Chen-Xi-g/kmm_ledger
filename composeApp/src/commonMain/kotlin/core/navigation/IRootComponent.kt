package core.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import core.data.dto.UserAccountDto
import core.domain.entity.BillDetailEntity
import core.domain.entity.PayTypeEntity
import ui.screen.MainVM
import ui.screen.account.activate.ActivateVM
import ui.screen.account.agreement.AgreementVM
import ui.screen.account.forget.ForgetPwdVM
import ui.screen.account.login.LoginVM
import ui.screen.account.register.RegisterVM
import ui.screen.add.bill.AddBillVM
import ui.screen.guide.GuideVM
import ui.screen.guide.splash.SplashVM
import ui.screen.payType.PayTypeManagerVM
import ui.screen.setting.SettingVM
import ui.screen.setting.account.AccountVM
import ui.screen.setting.user.UserInfoVM
import ui.screen.web.WebViewVM

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
     *
     * @param isFinishAll 是否关闭所有页面
     */
    fun onNavigationToScreenLogin(isFinishAll: Boolean = false)

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
     * 导航到收支类型管理页
     */
    fun onNavigationToScreenPayTypeManager(isSelect: Boolean = false)

    /**
     * 跳转到收支类型子类管理页
     *
     * @param name 收支类型名称
     * @param id 收支类型id
     * @param isSelect 是否是选择
     */
    fun onNavigationToScreenPayTypeSubManager(name: String, id: Long, isSelect: Boolean = false)

    /**
     * 跳转到账户管理页面
     */
    fun onNavigationToScreenAccountManager(isSelect: Boolean = false)

    /**
     * 跳转到设置页面
     */
    fun onNavigationToScreenSetting()

    /**
     * 跳转到WebView页面
     *
     * @param url 网页地址
     * @param title 网页标题
     */
    fun onNavigationToScreenWebView(url: String, title: String = "")

    /**
     * 跳转到用户资料页面
     */
    fun onNavigationToScreenUserInfo()

    /**
     * 跳转到账单新增页面
     *
     * @param billDetail 账单详情
     */
    fun onNavigationToScreenAddBill(billDetail: BillDetailEntity? = null)

    /**
     * 选择支付类型回调
     *
     * @param item 支付类型
     */
    fun onSelectedPayType(item: PayTypeEntity, isIncome: Boolean)

    /**
     * 选择账户回调
     *
     * @param item 账户
     */
    fun onSelectedAccount(item: UserAccountDto)

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

        /**
         * 收支类型管理
         */
        data class PayTypeManager(val component: PayTypeManagerVM) : Child()

        /**
         * 账户管理
         */
        data class AccountManager(val component: AccountVM) : Child()

        /**
         * 设置
         */
        data class Setting(val component: SettingVM): Child()

        /**
         * WebView
         */
        data class WebView(val component: WebViewVM): Child()

        /**
         * 修改资料
         */
        data class UserInfo(val component: UserInfoVM): Child()

        /**
         * 新增账单
         */
        data class AddBill(val component: AddBillVM): Child()
    }
}