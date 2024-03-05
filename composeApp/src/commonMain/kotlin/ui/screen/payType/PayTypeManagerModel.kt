package ui.screen.payType

import core.domain.entity.PayTypeEntity
import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState
import ui.screen.add.AddEvent

/**
 * 收支类型管理 状态
 *
 * @author 高国峰
 * @date 2024/01/13-16:57
 *
 * @property type 收支类型，0父类型，1子类型
 * @property list 收支类型列表
 * @property error 错误信息
 * @property isLoading 是否正在加载
 * @property isIncome 是否是收入
 * @property toIndex 移动到的位置
 * @property removeIndex 需要删除的位置
 * @property removeDialog 是否显示删除对话框
 * @property isAddDialog 是否显示添加对话框
 */
data class PayTypeManagerState(
    val type: Int = 0,
    val typeName: String = "",
    val list: List<PayTypeEntity> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val isIncome: Boolean = false,
    val toIndex: Int = -1,
    val removeIndex: Int = -1,
    val removeDialog: Boolean = false,
    val isAddDialog: Boolean = false
): UiState

/**
 * 收支类型管理 事件
 */
sealed interface PayTypeManagerEvent: UiEvent{

    /**
     * 返回
     */
    data object GoBack: PayTypeManagerEvent

    /**
     * 切换收入支出
     *
     * @param isIncome 是否是收入, true: 收入, false: 支出
     */
    data class SwitchIncome(val isIncome: Boolean): PayTypeManagerEvent

    /**
     * 移动
     *
     * @param from 移动的位置
     * @param to 移动到的位置
     */
    data class Move(val from: Int, val to: Int): PayTypeManagerEvent

    /**
     * 请求服务器修改移动后的数据
     */
    data object MoveChange: PayTypeManagerEvent

    /**
     * 显示删除对话框
     *
     * @param index 删除的位置
     */
    data class ShowRemoveDialog(val index: Int): PayTypeManagerEvent

    /**
     * 隐藏删除对话框
     *
     * @param isDialog 是否是对话框
     */
    data class HideRemoveDialog(val isDialog: Boolean): PayTypeManagerEvent

    /**
     * 显示添加对话框
     */
    data object ShowAddDialog: PayTypeManagerEvent

    /**
     * 隐藏添加对话框
     */
    data object HideAddDialog: PayTypeManagerEvent

    /**
     * 添加
     *
     * @param name 收支类型名称
     */
    data class Add(val name: String): PayTypeManagerEvent

    /**
     * 删除
     */
    data object Remove: PayTypeManagerEvent

    /**
     * 类型列表Item点击事件
     *
     * @param item 点击的消费类型
     */
    data class ItemClick(val item: PayTypeEntity): PayTypeManagerEvent

}

/**
 * 收支类型管理 效果
 */
sealed interface PayTypeManagerEffect: UiEffect