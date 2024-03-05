package ui.screen.add.bill

import core.data.dto.UserAccountDto
import core.domain.entity.BillDetailEntity
import core.domain.entity.PayTypeEntity
import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState
import core.utils.Res
import core.utils.currentLocalDate
import kotlinx.datetime.LocalDate

/**
 * 新增账单详情页 状态
 *
 * @author 高国峰
 * @date 2024/02/06-19:07
 */
data class AddBillState(
    val title: String = Res.strings.str_new_bill,
    val billAmount: Long = 0,
    val createTime: LocalDate = currentLocalDate(),
    val payTypeEntity: PayTypeEntity = PayTypeEntity(),
    val isIncome: Boolean = false,
    val billId: Long? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val account: UserAccountDto? = null,
    val longitude: Double? = null,
    val error: String? = null,
    val isLoading: Boolean = false
): UiState

/**
 * 新增账单详情页 事件
 */
sealed interface AddBillEvent: UiEvent{
    /**
     * 返回
     */
    data object Back: AddBillEvent

    /**
     * 选择支付类型
     */
    data object SelectedPayType: AddBillEvent

    /**
     * 选择账户
     */
    data object SelectedAccount: AddBillEvent

    /**
     * 保存
     */
    data object Save: AddBillEvent
}

/**
 * 新增账单详情页 效果
 */
sealed interface AddBillEffect: UiEffect