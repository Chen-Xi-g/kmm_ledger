
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
import core.navigation.RootComponent
import platform.backAnimation
import ui.screen.splash.SplashScreen
import ui.theme.LedgerTheme

@Composable
fun App(root: RootComponent) {
    LedgerTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .autoCloseKeyboard(),
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
                    is RootComponent.Child.Splash -> SplashScreen(instance.component)
                }
            }
        }
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