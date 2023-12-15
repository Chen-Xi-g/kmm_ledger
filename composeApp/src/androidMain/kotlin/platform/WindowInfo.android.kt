package platform

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

/**
 * 窗口信息实现
 */
@Composable
actual fun rememberWindowInfo(): WindowInfo{
    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenWidthInfo = when{
            configuration.screenWidthDp < 600 -> WindowInfo.WindowType.Compact
            configuration.screenWidthDp < 840 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Large
        },
        screenHeightInfo = when{
            configuration.screenHeightDp < 600 -> WindowInfo.WindowType.Compact
            configuration.screenHeightDp < 840 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Large
        },
        screenWidthDp = configuration.screenWidthDp.dp,
        screenHeightDp = configuration.screenHeightDp.dp
    )
}

/**
 * 设置安全区域距离
 */
actual fun Modifier.safeArea(): Modifier {
    return this.statusBarsPadding().navigationBarsPadding()
}