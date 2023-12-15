import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.PredictiveBackGestureOverlay
import com.arkivanov.decompose.lifecycle.ApplicationLifecycle
import com.arkivanov.essenty.backhandler.BackDispatcher
import core.navigation.RootComponent

@OptIn(ExperimentalDecomposeApi::class)
fun MainViewController() = ComposeUIViewController {
    val lifecycle = ApplicationLifecycle()
    val backDispatcher = BackDispatcher()
    val root = remember {
        RootComponent(
            DefaultComponentContext(
                lifecycle = lifecycle,
                backHandler = backDispatcher
            )
        )
    }
    PredictiveBackGestureOverlay(
        backDispatcher = backDispatcher, // Use the same BackDispatcher as above
        backIcon = null,
        modifier = Modifier.fillMaxSize(),
    ) {
        App(root)
    }
}