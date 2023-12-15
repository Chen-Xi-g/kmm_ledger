package ui.screen.home

import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 首页状态
 *
 * @author
 * @date 2023/12/14-09:54
 */
data class HomeState(
    val error: String? = null,
    val isLoading: Boolean = false
): UiState

/**
 * 首页事件
 */
sealed interface HomeEvent: UiEvent

/**
 * 首页效果
 */
sealed interface HomeEffect: UiEffect