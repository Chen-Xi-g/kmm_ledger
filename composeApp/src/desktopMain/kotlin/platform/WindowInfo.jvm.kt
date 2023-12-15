package platform

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

/**
 * 窗口信息实现
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun rememberWindowInfo(): WindowInfo {
    val configuration = LocalWindowInfo.current.containerSize
    return WindowInfo(
        screenWidthInfo = when{
            configuration.width < 600 -> WindowInfo.WindowType.Compact
            configuration.width < 840 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Large
        },
        screenHeightInfo = when{
            configuration.height < 600 -> WindowInfo.WindowType.Compact
            configuration.height < 840 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Large
        },
        screenWidthDp = configuration.height.dp,
        screenHeightDp = configuration.height.dp
    )
}

/**
 * 设置安全区域距离
 */
actual fun Modifier.safeArea(): Modifier {
    return this.statusBarsPadding().navigationBarsPadding()
}