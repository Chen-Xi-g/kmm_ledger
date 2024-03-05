package ui.screen.web

import com.arkivanov.decompose.ComponentContext
import core.navigation.BaseComponent
import core.navigation.IRootComponent

/**
 * 网页布局 VM
 *
 * @author 高国峰
 * @date 2024/02/03-23:42
 */
class WebViewVM(
    componentContext: ComponentContext,
    private val url: String,
    private val title: String,
    private val rootComponent: IRootComponent
) : BaseComponent<WebViewState, WebViewEvent, WebViewEffect>(componentContext) {
    override fun initialState(): WebViewState {
        return WebViewState(url = url, title = title)
    }

    override fun onEvent(event: WebViewEvent) {
        when(event){
            WebViewEvent.GoBack -> {
                rootComponent.onBack()
            }
        }
    }
}