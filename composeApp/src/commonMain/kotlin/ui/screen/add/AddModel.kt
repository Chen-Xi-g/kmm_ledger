package ui.screen.add

import core.domain.entity.PayTypeEntity
import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 新增账单 状态
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 *
 * @param isIncome 是否是收入, true: 收入, false: 支出
 * @param currentPayType 当前选中的消费类型
 * @param currentPayTypeChild 当前选中的消费类型子列表
 * @param types 消费类型列表
 * @param typeChildList 消费类型子列表
 * @param typeName 类型名称
 * @param isLoading 是否正在加载
 */
data class AddState(
    val isIncome: Boolean = true,
    val currentPayType: Int = 0,
    val currentPayTypeChild: Int = 0,
    val types: List<PayTypeEntity> = emptyList(),
    val typeChildList: List<PayTypeEntity> = emptyList(),
    val typeName: String = "",
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
     * 获取消费类型
     */
    data object GetPayType: AddEvent

    /**
     * 获取消费类型子列表
     *
     * @param typeId 消费类型Id
     */
    data class GetPayTypeChild(val typeId: String): AddEvent

    /**
     * 新增账单类型
     *
     * @param typeName 类型名称
     */
    data class AddPayType(val typeName: String): AddEvent

    /**
     * 新增账单类型子数据
     *
     * @param typeId 消费类型Id
     * @param parentId 父级id
     * @param typeName 类型名称
     * @param typeTag 类型标签 0-支出 1-收入
     */
    data class AddPayTypeChild(val typeId: String,val parentId: String, val typeName: String, val typeTag: String): AddEvent
}

/**
 * 新增账单 效果
 */
sealed interface AddEffect: UiEffect