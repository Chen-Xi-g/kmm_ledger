package ui.screen.setting.account

import core.data.dto.UserAccountDto
import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 账户管理 状态
 *
 * @author 高国峰
 * @date 2024/01/29-11:36
 *
 * @property savingsList 储蓄账户列表
 * @property electronicList 电子账户列表
 */
data class AccountState(
    val savingsList: List<UserAccountDto> = emptyList(),
    val electronicList: List<UserAccountDto> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
): UiState

/**
 * 账户管理 事件
 */
sealed interface AccountEvent: UiEvent{

    /**
     * 返回
     */
    data object Back: AccountEvent

    /**
     * 选择账户
     */
    data class SelectAccount(val account: UserAccountDto): AccountEvent

}

/**
 * 账户管理 效果
 */
sealed interface AccountEffect: UiEffect