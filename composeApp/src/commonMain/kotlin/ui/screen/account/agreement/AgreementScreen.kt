package ui.screen.account.agreement

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import platform.safeArea
import ui.widget.LedgerTitle
import ui.widget.LoadingDialog
import ui.widget.Toast

/**
 * 系统协议
 *
 * @author 高国峰
 * @date 2023/12/10-14:45
 */
@Composable
fun AgreementScreen(
    component: AgreementVM,
) {
    val state by component.state.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        AgreementContent(state, component::onEvent)
    }
    Toast(text = state.error) {
        component.onEvent(AgreementEvent.ClearError)
    }
    AnimatedVisibility(visible = state.isLoading) {
        LoadingDialog()
    }
}

@Composable
private fun AgreementContent(
    state: AgreementState,
    onEvent: (AgreementEvent) -> Unit
) {
    val webViewState = rememberWebViewStateWithHTMLData(
        data = state.content
    )
    Column(
        modifier = Modifier.safeArea()
    ) {
        LedgerTitle(title = state.title, onBack = {
            onEvent(AgreementEvent.GoBack)
        })
        WebView(
            modifier = Modifier.fillMaxSize(),
            state = webViewState
        )
    }
}