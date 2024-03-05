package ui.screen.setting

import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 设置页面 状态
 *
 * @author 高国峰
 * @date 2024/02/03-23:13
 */
data class SettingState(
    val error: String? = null,
    val isLoading: Boolean = false
): UiState

/**
 * 设置页面 事件
 */
sealed interface SettingEvent: UiEvent{

    /**
     * 返回
     */
    data object Back: SettingEvent

    /**
     * 系统协议
     *
     * @param type 协议类型 1:用户协议 2:隐私政策
     */
    data class SystemAgreement(val type: Int): SettingEvent

    /**
     * 关于我们
     */
    data object AboutUs: SettingEvent
}

/**
 * 设置页面 效果
 */
sealed interface SettingEffect: UiEffect