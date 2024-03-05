package core.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.items
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import core.data.dto.UserAccountDto
import core.domain.entity.BillDetailEntity
import core.domain.entity.PayTypeEntity
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
import ui.screen.add.bill.AddBillVM
import ui.screen.payType.PayTypeManagerVM
import ui.screen.setting.SettingVM
import ui.screen.setting.account.AccountVM
import ui.screen.setting.user.UserInfoVM
import ui.screen.web.WebViewVM

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
            is Configuration.AccountManager -> Child.AccountManager(AccountVM(component, config.isSelect, this))
            Configuration.Setting -> Child.Setting(SettingVM(component, this))
            is Configuration.PayTypeManager -> Child.PayTypeManager(
                PayTypeManagerVM(
                    component,
                    config.parentName,
                    config.parentId,
                    config.isSelect,
                    this
                )
            )

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

            is Configuration.WebView -> Child.WebView(
                WebViewVM(
                    component,
                    config.url,
                    config.title,
                    this
                )
            )

            Configuration.UserInfo -> Child.UserInfo(
                UserInfoVM(
                    component, this
                )
            )

            is Configuration.AddBill -> {
                Child.AddBill(
                    AddBillVM(
                        component,
                        config.billDetail ?: BillDetailEntity(),
                        this
                    )
                )
            }
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
        scope.launch(Dispatchers.Main) {
            if (childStack.items.size <= 1) {
                // 退出到桌面
                goHome()
                return@launch
            }
            navigation.pop()
        }
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

    override fun onNavigationToScreenLogin(isFinishAll: Boolean) {
        if (isFinishAll) {
            scope.launch {
                navigation.replaceAll(Configuration.Login)
            }
        } else {
            pushNew(Configuration.Login)
        }
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

    override fun onNavigationToScreenPayTypeManager(isSelect: Boolean) {
        pushNew(Configuration.PayTypeManager(isSelect = isSelect))
    }

    override fun onNavigationToScreenPayTypeSubManager(name: String, id: Long, isSelect: Boolean) {
        pushNew(Configuration.PayTypeManager(name, id, isSelect))
    }

    override fun onNavigationToScreenAccountManager(isSelect: Boolean) {
        pushNew(Configuration.AccountManager(isSelect))
    }

    override fun onNavigationToScreenSetting() {
        pushNew(Configuration.Setting)
    }

    override fun onNavigationToScreenWebView(url: String, title: String) {
        pushNew(Configuration.WebView(url, title))
    }

    override fun onNavigationToScreenUserInfo() {
        pushNew(Configuration.UserInfo)
    }

    override fun onNavigationToScreenAddBill(billDetail: BillDetailEntity?) {
        pushNew(Configuration.AddBill(billDetail))
    }

    override fun onSelectedPayType(item: PayTypeEntity, isIncome: Boolean) {
        navigation.popWhile(
            {
                it !is Configuration.AddBill
            }
        ){
            (stack.active.instance as? Child.AddBill)?.component?.onSelectedPayType(item, isIncome)
        }
    }

    override fun onSelectedAccount(item: UserAccountDto) {
        navigation.popWhile(
            {
                it !is Configuration.AddBill
            }
        ){
            (stack.active.instance as? Child.AddBill)?.component?.onSelectAccount(item)
        }
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

        /**
         * 收支类型管理
         *
         * @param parentName 父级名称
         * @param parentId 父级id
         */
        @Serializable
        data class PayTypeManager(val parentName: String? = null, val parentId: Long? = null, val isSelect: Boolean = false) :
            Configuration

        /**
         * 账户管理
         *
         * @param isSelect 是否选择
         */
        @Serializable
        data class AccountManager(val isSelect: Boolean) : Configuration

        /**
         * 设置页面
         */
        @Serializable
        data object Setting : Configuration

        /**
         * WebView
         *
         * @param url 链接
         * @param title 标题
         */
        @Serializable
        data class WebView(val url: String, val title: String) : Configuration

        /**
         * 用户资料
         */
        @Serializable
        data object UserInfo : Configuration

        /**
         * 新增账单
         *
         * @param billDetail 账单详情
         */
        @Serializable
        data class AddBill(val billDetail: BillDetailEntity? = null) : Configuration
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

}