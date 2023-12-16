package ui.screen.account.login

import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 登录状态
 *
 * @param isAcceptedTerms 条款是否同意
 * @param codeImg 验证码图片
 * @param uuid 验证码图片的uuid
 * @param errorUsername 用户名错误信息
 * @param errorPassword 密码错误信息
 * @param errorCaptcha 验证码错误信息
 * @param isLoading 是否正在加载
 */
data class LoginState(
    val isAcceptedTerms: Boolean = false,
    val codeImg: String = "",
    val uuid: String = "",
    val errorUsername: String? = null,
    val errorPassword: String? = null,
    val errorCaptcha: String? = null,
    val isLoading: Boolean = false
): UiState

sealed interface LoginEvent : UiEvent{

    /**
     * 返回
     */
    data object GoBack : LoginEvent

    /**
     * 更新条款是否同意
     */
    data class UpdateTerms(val isAcceptedTerms: Boolean) : LoginEvent

    /**
     * 刷新验证码
     */
    data object RefreshCode : LoginEvent

    /**
     * 提交
     */
    data object Submit : LoginEvent

    /**
     * 跳转忘记密码
     */
    data object ForgetPassword : LoginEvent

    /**
     * 跳转注册
     */
    data class Register(val username: String) : LoginEvent

    /**
     * 跳转激活账号
     */
    data class ActivateAccount(val username: String) : LoginEvent

    /**
     * 跳转用户协议
     */
    data object UserAgreement : LoginEvent

    /**
     * 跳转隐私政策
     */
    data object PrivacyPolicy : LoginEvent
}