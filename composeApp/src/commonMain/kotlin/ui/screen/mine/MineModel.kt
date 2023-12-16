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
    val error: String? = null,
    val isLoading: Boolean = false
): UiState

/**
 * 我的 事件
 */
sealed interface MineEvent: UiEvent

/**
 * 我的 效果
 */
sealed interface MineEffect: UiEffect