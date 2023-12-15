package ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea

/**
 * 首页
 *
 * @author
 * @date 2023/12/14-09:54
 */
@Composable
fun HomeScreen(
    component: HomeVM
) {
    val state = component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
            .safeArea()
    ) {
        if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
            HomeContent()
        } else {
            HomeContentLarge()
        }
    }
}

@Composable
private fun HomeContent() {

}

@Composable
private fun HomeContentLarge() {

}