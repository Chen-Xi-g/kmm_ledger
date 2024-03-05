package ui.screen.web

import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState
import ui.screen.account.agreement.AgreementEvent

/**
 * 网页布局 状态
 *
 * @author 高国峰
 * @date 2024/02/03-23:42
 *
 * @param url 网页地址
 */
data class WebViewState(
    val url: String = "",
    val title: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
): UiState

/**
 * 网页布局 事件
 */
sealed interface WebViewEvent: UiEvent{
    data object GoBack: WebViewEvent
}

/**
 * 网页布局 效果
 */
sealed interface WebViewEffect: UiEffect