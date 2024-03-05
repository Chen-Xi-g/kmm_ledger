
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import core.navigation.IRootComponent
import core.navigation.RootComponent
import platform.backAnimation
import ui.screen.MainScreen
import ui.screen.account.activate.ActivateScreen
import ui.screen.account.agreement.AgreementScreen
import ui.screen.account.forget.ForgetPwdScreen
import ui.screen.account.login.LoginScreen
import ui.screen.account.register.RegisterScreen
import ui.screen.add.bill.AddBillScreen
import ui.screen.guide.GuideScreen
import ui.screen.guide.splash.SplashScreen
import ui.screen.payType.PayTypeManagerScreen
import ui.screen.setting.SettingScreen
import ui.screen.setting.account.AccountScreen
import ui.screen.setting.user.UserInfoScreen
import ui.screen.web.WebViewScreen
import ui.theme.LedgerTheme
import ui.widget.Toast
import ui.widget.rememberToastState

@Composable
fun App(root: RootComponent) {
    val toastState = rememberToastState()
    root.toastState = { text, style ->
        toastState.text.value = text
        toastState.style.value = style
    }
    LedgerTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .autoCloseKeyboard()
        ) {
            val childStack by root.childStack.subscribeAsState()
            Children(
                stack = childStack,
                animation = backAnimation(
                    backHandler = root.backHandler,
                    onBack = root::onBack
                )
            ){ child ->
                when(val instance = child.instance){
                    is IRootComponent.Child.Guide -> GuideScreen(instance.component)
                    is IRootComponent.Child.Main -> MainScreen(instance.component)
                    is IRootComponent.Child.Splash -> SplashScreen(instance.component)
                    is IRootComponent.Child.Login -> LoginScreen(instance.component)
                    is IRootComponent.Child.Register -> RegisterScreen(instance.component)
                    is IRootComponent.Child.ForgetPwd -> ForgetPwdScreen(instance.component)
                    is IRootComponent.Child.ActivateAccount -> ActivateScreen(instance.component)
                    is IRootComponent.Child.Agreement -> AgreementScreen(instance.component)
                    is IRootComponent.Child.PayTypeManager -> PayTypeManagerScreen(instance.component)
                    is IRootComponent.Child.AccountManager -> AccountScreen(instance.component)
                    is IRootComponent.Child.Setting -> SettingScreen(instance.component)
                    is IRootComponent.Child.WebView -> WebViewScreen(instance.component)
                    is IRootComponent.Child.UserInfo -> UserInfoScreen(instance.component)
                    is IRootComponent.Child.AddBill -> AddBillScreen(instance.component)
                }
            }
        }
        Toast(toastState)
    }
}

/**
 * 点击屏幕外关闭键盘
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun Modifier.autoCloseKeyboard(): Modifier = composed {
    val keyboardController = LocalSoftwareKeyboardController.current
    pointerInput(this@autoCloseKeyboard) {
        detectTapGestures(
            onPress = {
                keyboardController?.hide()
            }
        )
    }
}