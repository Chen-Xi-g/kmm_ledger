package platform

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.layout
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.isFront
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveback.predictiveBackAnimatable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimator
import com.arkivanov.essenty.backhandler.BackHandler
import org.jetbrains.skia.Image
import platform.UIKit.UIDevice
import platform.posix.exit

class IOSPlatform: Platform {
    override val name: String
        get() {
            val deviceName = UIDevice.currentDevice.name
            val deviceModel = UIDevice.currentDevice.model
            val systemName = UIDevice.currentDevice.systemName()
            val systemVersion = UIDevice.currentDevice.systemVersion
            val deviceType:String = if (UIDevice.currentDevice.userInterfaceIdiom == 0L) "iPhone" else "iPad"
            return "deviceName:${deviceName},deviceModel:${deviceModel},systemName:${systemName},systemVersion:${systemVersion},deviceType:${deviceType}"
        }
    override val userAgent: String
        get() {
            val deviceName = UIDevice.currentDevice.name
            val systemVersion = UIDevice.currentDevice.systemVersion
            return "Mozilla/5.0 ($deviceName; $deviceName $systemVersion) AppleWebKit/537.36 (KHTML, like Gecko) Safari/604.1"
        }
}

actual fun getPlatform(): Platform = IOSPlatform()

/**
 * 设置返回动画
 */
@OptIn(ExperimentalDecomposeApi::class)
actual fun <C : Any, T : Any> backAnimation(
    backHandler: BackHandler,
    onBack: () -> Unit,
): StackAnimation<C, T> =
    predictiveBackAnimation(
        backHandler = backHandler,
        animation = stackAnimation(iosLikeSlide()),
        selector = { initialBackEvent, _, _ ->
            predictiveBackAnimatable(
                initialBackEvent = initialBackEvent,
                exitModifier = { progress, _ -> Modifier.slideExitModifier(progress = progress) },
                enterModifier = { progress, _ -> Modifier.slideEnterModifier(progress = progress) },
            )
        },
        onBack = onBack,
    )

private fun iosLikeSlide(animationSpec: FiniteAnimationSpec<Float> = tween()): StackAnimator =
    stackAnimator(animationSpec = animationSpec) { factor, direction, content ->
        content(
            Modifier
                .then(if (direction.isFront) Modifier else Modifier.fade(factor + 1F))
                .offsetXFactor(factor = if (direction.isFront) factor else factor * 0.5F)
        )
    }

private fun Modifier.slideExitModifier(progress: Float): Modifier =
    offsetXFactor(progress)

private fun Modifier.slideEnterModifier(progress: Float): Modifier =
    fade(progress).offsetXFactor((progress - 1f) * 0.5f)

private fun Modifier.fade(factor: Float) =
    drawWithContent {
        drawContent()
        drawRect(color = Color(red = 0F, green = 0F, blue = 0F, alpha = (1F - factor) / 4F))
    }

private fun Modifier.offsetXFactor(factor: Float): Modifier =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(x = (placeable.width.toFloat() * factor).toInt(), y = 0)
        }
    }

actual fun ByteArray.base64ToBitmap(): ImageBitmap =
    Image.makeFromEncoded(this).toComposeImageBitmap()

actual fun String.log(){
    if (this.isEmpty()) return
    println(this)
}

actual fun goHome(){
    // 关闭应用
    exit(0)
}