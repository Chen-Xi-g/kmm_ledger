package platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * 获取屏幕信息
 */
@Composable
expect fun rememberWindowInfo(): WindowInfo

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidthDp: Dp,
    val screenHeightDp: Dp,
){
    sealed class WindowType{
        data object Compact : WindowType()
        data object Medium : WindowType()
        data object Large : WindowType()
    }
}

/**
 * 设置安全区域距离
 */
expect fun Modifier.safeArea(): Modifier