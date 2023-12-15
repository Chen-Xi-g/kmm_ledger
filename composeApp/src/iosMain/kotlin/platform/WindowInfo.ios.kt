package platform

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication
import platform.UIKit.UIScreen
import platform.UIKit.UIWindow

/**
 * 窗口信息实现
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberWindowInfo(): WindowInfo {
    // 获取设备的屏幕宽高
    val logicSize = UIScreen.mainScreen.bounds.useContents { size.width to size.height }
    return WindowInfo(
        screenWidthInfo = when {
            logicSize.first < 600 -> WindowInfo.WindowType.Compact
            logicSize.first < 840 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Large
        },
        screenHeightInfo = when {
            logicSize.second < 600 -> WindowInfo.WindowType.Compact
            logicSize.second < 840 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Large
        },
        screenWidthDp = logicSize.second.dp,
        screenHeightDp = logicSize.second.dp
    )
}

/**
 * 设置安全区域距离
 */
@OptIn(ExperimentalForeignApi::class)
actual fun Modifier.safeArea(): Modifier {
    val safe =
        (UIApplication.sharedApplication.windows.first() as? UIWindow)?.safeAreaInsets?.useContents { left to right }
    val safeTopBottom = (UIApplication.sharedApplication.windows.first() as? UIWindow)?.safeAreaInsets?.useContents { top to bottom }
    return this.padding(
        start = safe?.first?.dp ?: 0.dp,
        top = safeTopBottom?.first?.dp ?: 0.dp,
        bottom = safeTopBottom?.second?.dp ?: 0.dp,
        end = safe?.first?.dp ?: 0.dp,
    )
}