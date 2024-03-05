package ui.screen.mine

import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 我的 状态
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
data class MineState(
    val nickName: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
): UiState

/**
 * 我的 事件
 */
sealed interface MineEvent: UiEvent{

    /**
     * 跳转到账户管理
     */
    data object ToAccount: MineEvent

    /**
     * 跳转到收支类型
     */
    data object ToPayType: MineEvent

    /**
     * 跳转到设置
     */
    data object ToSetting: MineEvent

    /**
     * 跳转到用户资料
     */
    data object ToUserInfo: MineEvent
}

/**
 * 我的 效果
 */
sealed interface MineEffect: UiEffect