package core.navigation

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import core.data.dto.UserAccountDto
import core.domain.entity.BillDetailEntity
import core.domain.entity.PayTypeEntity
import ui.screen.guide.splash.SplashVM

/**
 * 预览根组件
 *
 * @author 高国峰
 * @date 2023/12/23-00:17
 */
class PreviewRootComponent : IRootComponent {
    override val childStack: Value<ChildStack<*, IRootComponent.Child>>
        get() = MutableValue(
            ChildStack(
                configuration = Unit,
                instance = IRootComponent.Child.Splash(
                    component = SplashVM(
                        DefaultComponentContext(
                            LifecycleRegistry()
                        ),
                        this,
                    )
                ),
            )
        )

    override fun toastDefault(msg: String?) {
    }

    override fun toastError(msg: String?) {
    }

    override fun toastSuccess(msg: String?) {
    }

    override fun onBack() {
    }

    override fun onNavigationToScreenGuide() {
    }

    override fun onNavigationReplaceToScreenMain() {
    }

    override fun onNavigationToScreenMain() {
    }

    override fun onNavigationToScreenRegister(username: String) {
    }

    override fun onNavigationToScreenLogin(isFinishAll: Boolean) {
    }

    override fun onNavigationToScreenForgetPwd(username: String) {
    }

    override fun onNavigationToScreenActivateAccount(username: String) {
    }

    override fun onNavigationToScreenAgreement(type: Int) {
    }

    override fun onNavigationToScreenPayTypeManager(isSelect: Boolean) {
    }

    override fun onNavigationToScreenPayTypeSubManager(name: String, id: Long, isSelect: Boolean) {
    }

    override fun onNavigationToScreenAccountManager(isSelect: Boolean) {
    }

    override fun onNavigationToScreenSetting() {
    }

    override fun onNavigationToScreenWebView(url: String, title: String) {
    }

    override fun onNavigationToScreenUserInfo() {
    }

    override fun onNavigationToScreenAddBill(billDetail: BillDetailEntity?) {
    }

    override fun onSelectedPayType(item: PayTypeEntity, isIncome: Boolean) {
    }

    override fun onSelectedAccount(item: UserAccountDto) {
    }

}

fun previewComponentContext() = DefaultComponentContext(
    lifecycle = LifecycleRegistry()
)