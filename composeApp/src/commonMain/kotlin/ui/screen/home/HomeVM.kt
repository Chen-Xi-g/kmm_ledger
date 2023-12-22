package ui.screen.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.statekeeper.consume
import core.data.net.ResNet
import core.data.repository.BillRepositoryImpl
import core.domain.repository.BillRepository
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import core.utils.monthDays
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import ui.widget.ToastState

/**
 * 首页VM
 *
 * @author 高国峰
 * @date 2023/12/14-09:54
 */
class HomeVM(
    componentContext: ComponentContext,
    private val navigationListener: IRootComponent,
    private val billRepository: BillRepository = BillRepositoryImpl()
) : BaseComponent<HomeState, HomeEvent, HomeEffect>(componentContext) {

    var cacheState = stateKeeper.consume(key = HomeState::class.simpleName ?: "HomeState", strategy = HomeState.serializer()) ?: HomeState()

    init {
        stateKeeper.register(key = HomeState::class.simpleName ?: "HomeState", strategy = HomeState.serializer()){
            cacheState
        }
        lifecycle.doOnCreate {
            onEvent(HomeEvent.QueryBillList)
        }
    }


    override fun initialState(): HomeState {
        return cacheState
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {

            // 显示或隐藏底部弹窗
            HomeEvent.ToggleBottomSheet -> {
                updateState {
                    it.copy(
                        visibleFilter = !it.visibleFilter
                    ).apply {
                        cacheState = this
                    }
                }
            }

            // 切换详情展开收起状态
            is HomeEvent.ToggleDetailVisible -> {
                updateState {
                    it.copy(
                        list = it.list.mapIndexed { index, homeLedgerItem ->
                            if (index == event.position) {
                                homeLedgerItem.copy(isExpanded = !homeLedgerItem.isExpanded)
                            } else {
                                homeLedgerItem
                            }
                        }
                    ).apply {
                        cacheState = this
                    }
                }
            }

            // 修改账户类型索引
            is HomeEvent.ChangeFilterAccountTypeIndex -> {
                updateState {
                    it.copy(
                        currentAccountIndex = event.index
                    ).apply {
                        cacheState = this
                    }
                }
            }

            // 修改账单日期数据
            is HomeEvent.ChangeFilterDateIndex -> {
                updateState {
                    it.copy(
                        currentDateIndex = if (event.visible) {
                            event.index
                        } else {
                            it.currentDateIndex
                        },
                        visibleMonthPicker = event.visible
                    ).apply {
                        cacheState = this
                    }
                }
            }

            // 修改收支类型索引
            is HomeEvent.ChangeFilterTypeIndex -> {
                updateState {
                    it.copy(
                        currentTypeIndex = event.index
                    ).apply {
                        cacheState = this
                    }
                }
            }

            // 修改账单日期
            is HomeEvent.SelectDate -> {
                updateState {
                    it.copy(
                        dates = it.dates.map { date ->
                            date.copy(
                                date = if (it.currentDateIndex == date.type) {
                                    when(it.currentDateIndex) {
                                        0 -> LocalDate(event.year, 1, 1)
                                        1 -> LocalDate(event.year, event.month, 1)
                                        else -> LocalDate(event.year, event.month, event.day)
                                    }
                                } else {
                                    date.date
                                }
                            )
                        },
                        visibleMonthPicker = false
                    ).apply {
                        cacheState = this
                    }
                }
            }

            // 查询账单数据
            is HomeEvent.QueryBillList -> {
                // 收支类型
                val typeTag = when (state.value.currentTypeIndex) {
                    0 -> null
                    1 -> "0"
                    else -> "1"
                }
                // 当前所选账单日期
                val accountDate = state.value.dates[state.value.currentDateIndex].date
                // 开始时间
                val beginTime = when (state.value.currentDateIndex) {
                    0 -> "${accountDate.year}-01-01"
                    1 -> "${accountDate.year}-${accountDate.monthNumber}-01"
                    else -> "${accountDate.year}-${accountDate.monthNumber}-${accountDate.dayOfMonth}"
                }
                // 结束时间
                val endTime = when (state.value.currentDateIndex) {
                    0 -> "${accountDate.year}-12-31"
                    1 -> "${accountDate.year}-${accountDate.monthNumber}-${accountDate.monthDays()}"
                    else -> "${accountDate.year}-${accountDate.monthNumber}-${accountDate.dayOfMonth}"
                }
                // 账户类型
                val typeId = when (state.value.currentAccountIndex) {
                    0 -> null
                    1 -> "00"
                    else -> "01"
                }
                getBillList(
                    beginTime = beginTime,
                    endTime = endTime,
                    typeId = typeId,
                    typeTag = typeTag
                )
            }
        }
    }

    /**
     * 获取账单列表
     *
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param endTime 结束时间 yyyy-MM-dd
     * @param billName 账单名称
     * @param typeId 消费类型Id null-全部，00-电子账户，01-储蓄账户
     * @param typeTag 消费类型标签 null-全部 0-支出 1-收入
     */
    private fun getBillList(
        beginTime: String? = null,
        endTime: String? = null,
        billName: String? = null,
        typeId: String? = null,
        typeTag: String? = null
    ) {
        updateState {
            it.copy(
                isLoading = true,
                visibleFilter = false
            ).apply {
                cacheState = this
            }
        }
        scope.launch {
            val result = billRepository.getBill(
                beginTime = beginTime,
                endTime = endTime,
                billName = billName,
                typeId = typeId,
                typeTag = typeTag
            )
            when (result) {
                is ResNet.Success -> {
                    updateState { state ->
                        state.copy(
                            isLoading = false,
                            list = result.data ?: emptyList()
                        ).apply {
                            cacheState = this
                        }
                    }
                }

                is ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        ).apply {
                            cacheState = this
                        }
                    }
                    navigationListener.toastError(result.msg ?: "获取账单失败")
                }
            }
        }
    }
}