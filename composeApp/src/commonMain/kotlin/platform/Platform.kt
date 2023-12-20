package platform

import androidx.compose.ui.graphics.ImageBitmap
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.essenty.backhandler.BackHandler

interface Platform {
    val name: String

    /**
     * UserAgent
     */
    val userAgent: String
}

expect fun getPlatform(): Platform

/**
 * 设置返回动画
 */
expect fun <C : Any, T : Any> backAnimation(
    backHandler: BackHandler,
    onBack: () -> Unit,
): StackAnimation<C, T>

/**
 * Base64转换为Bitmap
 */
expect fun ByteArray.base64ToBitmap(): ImageBitmap

/**
 * 打印日志
 */
expect fun String.log()

/**
 * 返回到桌面
 */
expect fun goHome()

/**
 * 格式化字符串
 */
expect fun String.format(vararg args: Any?): String