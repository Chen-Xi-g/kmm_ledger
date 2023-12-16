package ui.screen.add

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
 * 新增账单 屏幕
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
@Composable
fun AddScreen(
    component: AddVM
) {
    val state by component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
            .safeArea()
    ) {
        if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
            AddContent(state, component::onEvent)
        } else {
            AddContentLarge(state, component::onEvent)
        }
    }
}

/**
 * 竖屏，窄屏布局
 */
@Composable
private fun AddContent(
    state: AddState,
    onEvent: (AddEvent) -> Unit
) {
    Text(text = "新增账单")
}

/**
 * 横屏，宽屏布局
 */
@Composable
private fun AddContentLarge(
    state: AddState,
    onEvent: (AddEvent) -> Unit
) {
    Text(text = "新增账单")
}