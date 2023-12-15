package platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.essenty.backhandler.BackHandler
import org.jetbrains.skia.Image
import kotlin.system.exitProcess

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val userAgent: String
        get() {
            val deviceName = System.getProperty("os.name")
            val systemVersion = System.getProperty("os.version")
            return "Mozilla/5.0 ($deviceName; $deviceName $systemVersion) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/604.1"
        }
}

actual fun getPlatform(): Platform = JVMPlatform()

@OptIn(ExperimentalDecomposeApi::class)
actual fun <C : Any, T : Any> backAnimation(
    backHandler: BackHandler,
    onBack: () -> Unit,
): StackAnimation<C, T> =
    predictiveBackAnimation(
        backHandler = backHandler,
        animation = stackAnimation(slide()),
        onBack = onBack,
    )

actual fun ByteArray.base64ToBitmap(): ImageBitmap =
    Image.makeFromEncoded(this).toComposeImageBitmap()

actual fun String.log(){
    if (this.isEmpty()) return
    println(this)
}

actual fun goHome(){
    // 关闭应用
    exitProcess(0)
}