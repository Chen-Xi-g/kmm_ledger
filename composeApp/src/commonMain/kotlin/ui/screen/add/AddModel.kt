package ui.screen.add

import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 新增账单 状态
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
data class AddState(
    val error: String? = null,
    val isLoading: Boolean = false
): UiState

/**
 * 新增账单 事件
 */
sealed interface AddEvent: UiEvent

/**
 * 新增账单 效果
 */
sealed interface AddEffect: UiEffect