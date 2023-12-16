package ui.screen.account.activate

import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 激活状态
 *
 * @param codeImg 验证码图片
 * @param uuid 验证码图片的uuid
 * @param errorUsername 用户名错误信息
 * @param errorCaptcha 验证码错误信息
 * @param isLoading 是否正在加载
 */
data class ActivateState(
    val codeImg: String = "",
    val uuid: String = "",
    val errorUsername: String? = null,
    val errorCaptcha: String? = null,
    val isLoading: Boolean = false
): UiState

sealed interface ActivateEvent: UiEvent{
    /**
     * 返回
     */
    data object GoBack: ActivateEvent

    /**
     * 刷新验证码
     */
    data object RefreshCode: ActivateEvent

    /**
     * 提交
     */
    data object Submit: ActivateEvent
}