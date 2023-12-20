
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import core.navigation.RootComponent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.backAnimation
import platform.log
import ui.screen.MainScreen
import ui.screen.account.activate.ActivateScreen
import ui.screen.account.agreement.AgreementScreen
import ui.screen.account.forget.ForgetPwdScreen
import ui.screen.account.login.LoginScreen
import ui.screen.account.register.RegisterScreen
import ui.screen.guide.GuideScreen
import ui.screen.guide.splash.SplashScreen
import ui.theme.LedgerTheme
import ui.theme.LocalColor
import ui.widget.Toast
import ui.widget.ToastState
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
                    onBack = root::onBackClicked
                )
            ){ child ->
                when(val instance = child.instance){
                    is RootComponent.Child.Guide -> GuideScreen(instance.component)
                    is RootComponent.Child.Main -> MainScreen(instance.component)
                    is RootComponent.Child.Splash -> SplashScreen(instance.component)
                    is RootComponent.Child.Login -> LoginScreen(instance.component)
                    is RootComponent.Child.Register -> RegisterScreen(instance.component)
                    is RootComponent.Child.ForgetPwd -> ForgetPwdScreen(instance.component)
                    is RootComponent.Child.ActivateAccount -> ActivateScreen(instance.component)
                    is RootComponent.Child.Agreement -> AgreementScreen(instance.component)
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