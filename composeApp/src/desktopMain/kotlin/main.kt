
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import core.navigation.RootComponent
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import utils.runOnUiThread
import java.io.File

@OptIn(ExperimentalDecomposeApi::class)
fun main() = application {
    val windowState = rememberWindowState(
        size = DpSize(375.dp, 812.dp),
        position = WindowPosition(300.dp, 300.dp)
    )
    val lifecycle = LifecycleRegistry()
    val backDispatcher = BackDispatcher()
    val root = runOnUiThread {
        RootComponent(
            DefaultComponentContext(
                lifecycle = lifecycle,
                backHandler = backDispatcher
            )
        )
    }
    LifecycleController(lifecycle, windowState = windowState)
    Window(
        title = "忞鹿记账",
        onCloseRequest = ::exitApplication,
        state = windowState
    ) {
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                KCEF.init(builder = {
                    installDir(File("kcef-bundle"))
                    progress {
                        onDownloading {
                            println("Downloading: $it")
                        }
                        onInitialized {
                            println("Initialized")
                        }
                    }
                    settings {
                        cachePath = File("cache").absolutePath
                    }
                }, onError = {
                    println(it)
                })
            }
        }
        App(root)

        DisposableEffect(Unit){
            onDispose {
                KCEF.disposeBlocking()
            }
        }
    }
}