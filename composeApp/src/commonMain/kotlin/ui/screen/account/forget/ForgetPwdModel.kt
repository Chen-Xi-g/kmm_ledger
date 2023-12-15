package ui.screen.account.forget

import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 忘记密码的状态
 *
 * @param codeImg 验证码图片
 * @param uuid 验证码图片的uuid
 * @param errorUsername 用户名错误信息
 * @param errorPassword 密码错误信息
 * @param errorConfirmPassword 确认密码错误信息
 * @param errorCaptcha 验证码错误信息
 * @param isLoading 是否正在加载
 * @param error 错误信息
 */
data class ForgetPwdState(
    val codeImg: String = "",
    val uuid: String = "",
    val errorUsername: String? = null,
    val errorPassword: String? = null,
    val errorConfirmPassword: String? = null,
    val errorCaptcha: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

/**
 * 忘记密码的事件
 */
sealed interface ForgetPwdEvent : UiEvent {

    /**
     * 返回
     */
    data object GoBack : ForgetPwdEvent

    /**
     * 刷新验证码
     */
    data object RefreshCode : ForgetPwdEvent

    /**
     * 清空错误
     */
    data object ClearError : ForgetPwdEvent

    /**
     * 提交
     */
    data object Submit: ForgetPwdEvent

}