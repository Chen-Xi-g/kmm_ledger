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
 * 打印日志
 */
expect fun String.log()

/**
 * 返回到桌面
 */
expect fun goHome()