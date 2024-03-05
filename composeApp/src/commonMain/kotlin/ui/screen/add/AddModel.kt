package ui.screen.add

import core.domain.entity.PayTypeEntity
import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState
import core.utils.currentLocalDate
import kotlinx.datetime.LocalDate

/**
 * 新增账单 状态
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 *
 * @param isIncome 是否是收入, true: 收入, false: 支出
 * @param isKeyboardShow 键盘是否显示
 * @param isSaveDialog 是否显示保存弹窗
 * @param visibleDateTimePickerShow 日期选择器是否显示
 * @param currentPayType 当前选中的消费类型索引
 * @param currentPayTypeChild 当前选中的消费类型子列表索引
 * @param keyboardList 键盘列表
 * @param types 消费类型列表
 * @param isLoading 是否正在加载
 */
data class AddState(
    val isIncome: Boolean = false,
    val isKeyboardShow: Boolean = true,
    val isSaveDialog: Boolean = false,
    val visibleDateTimePickerShow: Boolean = false,
    val dateTime: LocalDate = currentLocalDate(),
    val currentPayType: Int = 0,
    val currentPayTypeChild: Int = 0,
    val keyboardList: List<String> = listOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "-", "0", "."
    ),
    val types: List<PayTypeEntity> = emptyList(),
    val isLoading: Boolean = false
): UiState

/**
 * 新增账单 事件
 */
sealed interface AddEvent: UiEvent{
    /**
     * 切换收入支出
     *
     * @param isIncome 是否是收入, true: 收入, false: 支出
     */
    data class SwitchIncome(val isIncome: Boolean): AddEvent

    /**
     * 切换消费类型索引
     *
     * @param index 索引
     */
    data class SwitchPayType(val index: Int): AddEvent

    /**
     * 切换消费类型子列表索引
     */
    data class SwitchPayTypeChild(val index: Int): AddEvent

    /**
     * 切换键盘显示状态
     */
    data object SwitchKeyboard: AddEvent

    /**
     * 修改键盘输入内容
     *
     * @param content 键盘内容
     */
    data class ChangeKeyboardContent(val content: Char): AddEvent

    /**
     * 删除键盘输入内容
     */
    data object DeleteKeyboardContent: AddEvent

    /**
     * 修改账单日期
     */
    data class ChangeDate(val visible: Boolean, val dateTime: LocalDate? = currentLocalDate()): AddEvent

    /**
     * 键盘下一步
     */
    data object KeyboardNext: AddEvent

    /**
     * 获取消费类型
     */
    data object GetPayType: AddEvent

    /**
     * 跳转到收支类型
     */
    data object ToPayType: AddEvent

    /**
     * 填写更多账单信息
     */
    data object ToMoreInfo: AddEvent

    /**
     * 保存账单
     */
    data object SaveBill: AddEvent

    /**
     * 取消保存账单
     */
    data object CancelSaveBill: AddEvent
}

sealed interface AddEffect: UiEffect{
}