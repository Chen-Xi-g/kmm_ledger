package ui.screen.web

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea
import ui.screen.account.agreement.AgreementEvent
import ui.widget.LedgerTitle
import ui.widget.LoadingDialog

/**
 * 网页布局 屏幕
 *
 * @author 高国峰
 * @date 2024/02/03-23:42
 */
@Composable
fun WebViewScreen(
    component: WebViewVM
) {
    val state by component.state.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        WebViewContent(state, component::onEvent)
    }
}

/**
 * 竖屏，窄屏布局
 */
@Composable
private fun WebViewContent(
    state: WebViewState,
    onEvent: (WebViewEvent) -> Unit
) {
    val webViewState = rememberWebViewState(url = state.url)
    LoadingDialog(webViewState.isLoading)
    Column(
        modifier = Modifier.fillMaxSize()
            .safeArea()
    ) {
        LedgerTitle(title = state.title, onBack = {
            onEvent(WebViewEvent.GoBack)
        })
        WebView(
            modifier = Modifier.fillMaxSize(),
            state = webViewState
        )
    }
}