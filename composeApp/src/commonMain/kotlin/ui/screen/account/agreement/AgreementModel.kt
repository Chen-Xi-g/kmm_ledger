package ui.screen.account.agreement

import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 系统协议
 *
 * @property content 协议内容
 * @property title 协议标题
 */
data class AgreementState(
    val content: String = "",
    val title: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
): UiState

sealed interface AgreementEvent: UiEvent{

    data object GoBack: AgreementEvent

    data object ClearError: AgreementEvent

    /**
     * 加载协议
     */
    data class LoadAgreement(val type: Int): AgreementEvent
}