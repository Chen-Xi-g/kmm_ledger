package ui.screen.mine

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea

/**
 * 我的 屏幕
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
@Composable
fun MineScreen(
    component: MineVM
) {
    val state by component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
            .safeArea()
    ) {
        if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact || windowInfo.screenWidthInfo == WindowInfo.WindowType.Medium) {
            MineContent(state, component::onEvent)
        } else {
            MineContentLarge(state, component::onEvent)
        }
    }
}

/**
 * 竖屏，窄屏布局
 */
@Composable
private fun MineContent(
    state: MineState,
    onEvent: (MineEvent) -> Unit
) {
    Text(text = "我的")
}

/**
 * 横屏，宽屏布局
 */
@Composable
private fun MineContentLarge(
    state: MineState,
    onEvent: (MineEvent) -> Unit
) {
    Text(text = "我的")
}