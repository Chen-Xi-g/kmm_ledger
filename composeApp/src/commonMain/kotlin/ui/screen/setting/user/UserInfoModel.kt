package ui.screen.setting.user

import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState
import ui.screen.setting.SettingEvent

/**
 * 用户信息 状态
 *
 * @author 高国峰
 * @date 2024/02/05-15:27
 */
data class UserInfoState(
    val error: String? = null,
    val isLoading: Boolean = false
): UiState

/**
 * 用户信息 事件
 */
sealed interface UserInfoEvent: UiEvent{
    /**
     * 返回页面
     */
    data object Back: UserInfoEvent

    /**
     * 忘记密码
     */
    data object ForgetPassword: UserInfoEvent

    /**
     * 退出登录
     */
    data object OnClickLogout: UserInfoEvent

    /**
     * 修改个人信息
     */
    data object ModifyUserInfo: UserInfoEvent
}

/**
 * 用户信息 效果
 */
sealed interface UserInfoEffect: UiEffect