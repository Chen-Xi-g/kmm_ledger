package ui.screen.account.register

import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 注册状态
 *
 * @property code 验证码
 * @property codeImg 验证码图片
 * @property uuid 验证码uuid
 * @property errorUsername 用户名错误
 * @property errorEmail 邮箱错误
 * @property errorPassword 密码错误
 * @property errorConfirmPassword 确认密码错误
 * @property errorCaptcha 验证码错误
 * @property isLoading 是否正在加载
 * @property error 错误信息
 */
data class RegisterState(
    val codeImg: String = "",
    val uuid: String = "",
    val errorUsername: String? = null,
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorConfirmPassword: String? = null,
    val errorCaptcha: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
): UiState

/**
 * 注册事件
 */
sealed interface RegisterEvent: UiEvent{

    /**
     * 返回
     */
    data object GoBack: RegisterEvent

    /**
     * 清空错误
     */
    data object ClearError: RegisterEvent

    /**
     * 获取验证码
     */
    data object RefreshCode: RegisterEvent

    /**
     * 提交
     */
    data object Submit: RegisterEvent

}