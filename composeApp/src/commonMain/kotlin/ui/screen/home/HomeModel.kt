package ui.screen.home

import core.domain.entity.AccountEntity
import core.domain.entity.BillListEntity
import core.domain.entity.DateEntity
import core.domain.entity.TypeEntity
import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState
import kotlinx.serialization.Serializable

/**
 * 首页状态
 *
 * @author
 * @date 2023/12/14-09:54
 *
 * @param visibleFilter 是否展示底部弹窗
 * @param visibleMonthPicker 月份选择器是否展示
 * @param list 账单列表
 * @param types 类型列表
 * @param dates 日期列表
 * @param accounts 账户列表
 * @param currentTypeIndex 当前选中的类型索引
 * @param currentDateIndex 当前选中的日期索引
 * @param currentAccountIndex 当前选中的账户索引
 * @param isLoading 是否正在加载
 */
@Serializable
data class HomeState(
    val visibleFilter: Boolean = false,
    val visibleMonthPicker: Boolean = false,
    val list: List<BillListEntity> = emptyList(),
    val types: List<TypeEntity> = listOf(
        TypeEntity(null, "全部"),
        TypeEntity(0, "支出"),
        TypeEntity(1, "收入")
    ),
    val dates: List<DateEntity> = listOf(
        DateEntity(0),
        DateEntity(1),
        DateEntity(2)
    ),
    val accounts: List<AccountEntity> = listOf(
        AccountEntity(null, "全部账户"),
        AccountEntity(0, "电子账户"),
        AccountEntity(1, "储蓄账户"),
    ),
    val currentTypeIndex: Int = 0,
    val currentDateIndex: Int = 1,
    val currentAccountIndex: Int = 0,
    val isLoading: Boolean = false,
) : UiState

/**
 * 首页事件
 */
sealed interface HomeEvent : UiEvent {

    /**
     * 显示或隐藏底部弹窗
     */
    data object ToggleBottomSheet : HomeEvent

    /**
     * 选择日期
     *
     * @param year 年
     * @param month 月
     * @param day 日
     */
    data class SelectDate(
        val year: Int,
        val month: Int,
        val day: Int
    ) : HomeEvent

    /**
     * 切换选中类型的索引
     *
     * @param index 索引
     */
    data class ChangeFilterTypeIndex(
        val index: Int
    ) : HomeEvent

    /**
     * 切换选中日期的索引
     *
     * @param index 索引
     * @param visible 是否显示
     */
    data class ChangeFilterDateIndex(
        val index: Int,
        val visible: Boolean
    ) : HomeEvent

    /**
     * 切换选中账户的索引
     *
     * @param index 索引
     */
    data class ChangeFilterAccountTypeIndex(
        val index: Int
    ) : HomeEvent

    /**
     * 展开或收起指定位置的账单
     */
    data class ToggleDetailVisible(
        val position: Int
    ) : HomeEvent

    /**
     * 查询账单列表
     */
    data object QueryBillList : HomeEvent
}

/**
 * 首页效果
 */
sealed interface HomeEffect : UiEffect {
    /**
     * 列表滚动到顶部
     */
    data object ScrollToTop : HomeEffect
}