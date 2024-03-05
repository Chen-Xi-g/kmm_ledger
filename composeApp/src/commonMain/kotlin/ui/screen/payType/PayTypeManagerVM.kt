package ui.screen.payType

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import core.data.dto.ChangePayTypeSortDto
import core.data.repository.BillRepositoryImpl
import core.domain.entity.PayTypeEntity
import core.domain.repository.BillRepository
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 收支类型管理 VM
 *
 * @author 高国峰
 * @date 2024/01/13-16:57
 */
class PayTypeManagerVM(
    componentContext: ComponentContext,
    private val parentName: String? = null,
    private val parentId: Long? = null,
    private val isSelect: Boolean = false,
    private val rootComponent: IRootComponent,
    private val billRepository: BillRepository = BillRepositoryImpl()
) : BaseComponent<PayTypeManagerState, PayTypeManagerEvent, PayTypeManagerEffect>(componentContext) {

    var isError by mutableStateOf(false)
        private set
    var validateError by mutableStateOf("")
        private set

    init {
        lifecycle.doOnCreate {
            getPayType()
        }
    }

    override fun initialState(): PayTypeManagerState {
        return PayTypeManagerState(type = if (parentId != null) 1 else 0, typeName = parentName ?: "")
    }

    override fun onEvent(event: PayTypeManagerEvent) {
        when (event) {
            PayTypeManagerEvent.GoBack -> rootComponent.onBack()
            PayTypeManagerEvent.MoveChange -> changePayTypeSort()
            is PayTypeManagerEvent.SwitchIncome -> {
                updateState {
                    it.copy(
                        isIncome = event.isIncome
                    )
                }
                getPayType()
            }

            is PayTypeManagerEvent.Move -> {
                move(event.from, event.to)
            }

            is PayTypeManagerEvent.HideRemoveDialog -> {
                updateState {
                    it.copy(
                        removeDialog = event.isDialog,
                        removeIndex = -1
                    )
                }
            }
            PayTypeManagerEvent.Remove -> {
                remove()
            }
            is PayTypeManagerEvent.ShowRemoveDialog -> {
                updateState {
                    it.copy(
                        removeDialog = true,
                        removeIndex = event.index
                    )
                }
            }

            is PayTypeManagerEvent.Add -> addPayType(event.name)
            PayTypeManagerEvent.HideAddDialog -> {
                updateState {
                    it.copy(
                        isAddDialog = false
                    )
                }
            }
            PayTypeManagerEvent.ShowAddDialog -> {
                updateState {
                    it.copy(
                        isAddDialog = true
                    )
                }
            }

            is PayTypeManagerEvent.ItemClick -> {
                if (parentId == null){
                    rootComponent.onNavigationToScreenPayTypeSubManager(event.item.typeName, event.item.typeId, isSelect)
                }else if (isSelect){
                    rootComponent.onSelectedPayType(event.item, state.value.isIncome)
                }
            }
        }
    }

    /**
     * 移动
     *
     * @param from 移动的位置
     * @param to 移动到的位置
     */
    private fun move(from: Int, to: Int) {
        if (from == to) return
        val toMutableList = state.value.list.toMutableList()
        val fromItem = toMutableList[from]
        val toItem = toMutableList[to]
        toMutableList[from] = toItem
        toMutableList[to] = fromItem
        updateState {
            it.copy(
                list = toMutableList,
                toIndex = to
            )
        }
    }

    /**
     * 删除
     */
    private fun remove() {
        val index = state.value.removeIndex
        if (index == -1) return
        val toMutableList = state.value.list.toMutableList()
        scope.launch {
            when(val resp = billRepository.removePayType(toMutableList[index].typeId)){
                is core.data.net.ResNet.Success -> {
                    toMutableList.removeAt(index)
                    // TODO 需请求接口进行删除逻辑
                    updateState {
                        it.copy(
                            list = toMutableList,
                            removeIndex = -1,
                            removeDialog = false
                        )
                    }
                }

                is core.data.net.ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastError(resp.msg)
                }
            }
        }
    }

    /**
     * 获取消费类型
     */
    private fun getPayType() {
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            if (parentId == null) {
                when (val resp = billRepository.getPayType(if (state.value.isIncome) "1" else "0")) {
                    is core.data.net.ResNet.Success -> {
                        updateState {
                            it.copy(
                                list = resp.data ?: emptyList(),
                            )
                        }
                        scope.launch {
                            updateState {
                                it.copy(
                                    isLoading = false
                                )
                            }
                        }
                    }

                    is core.data.net.ResNet.Error -> {
                        updateState {
                            it.copy(
                                isLoading = false
                            )
                        }
                        rootComponent.toastError(resp.msg)
                    }
                }
            }else{
                when (val resp = billRepository.getPayTypeChild(parentId)) {
                    is core.data.net.ResNet.Success -> {
                        updateState {
                            it.copy(
                                list = resp.data ?: emptyList(),
                            )
                        }
                        scope.launch {
                            updateState {
                                it.copy(
                                    isLoading = false
                                )
                            }
                        }
                    }

                    is core.data.net.ResNet.Error -> {
                        updateState {
                            it.copy(
                                isLoading = false
                            )
                        }
                        rootComponent.toastError(resp.msg)
                    }
                }
            }
        }
    }

    /**
     * 校验是否合法
     *
     * @param text 文本
     * @return true 合法
     */
    fun check(text: String) {
        if (text.isEmpty()){
            isError = true
            validateError = "类型名称不能为空"
            return
        }
        if (text.length > 5){
            isError = true
            validateError = "请输入5个字以内的名称"
            return
        }
        isError = false
        validateError = ""
    }

    /**
     * 新增消费类型
     */
    private fun addPayType(name: String){
        if (isError) return
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when(val resp = billRepository.addPayType(typeName = name, typeTag = if (state.value.isIncome) "1" else "0", parentId = parentId)){
                is core.data.net.ResNet.Success -> {
                    updateState {
                        it.copy(
                            isLoading = false,
                            isAddDialog = false
                        )
                    }
                    getPayType()
                }

                is core.data.net.ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastError(resp.msg)
                }
            }
        }
    }

    /**
     * 修改消费类型排序
     */
    private fun changePayTypeSort() {
        if (state.value.toIndex == -1 || state.value.list.size == 1) return
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when (val resp = billRepository.changePayTypeSort(
                request = ChangePayTypeSortDto(
                    typeTag = if (state.value.isIncome) "1" else "0",
                    typeIds = state.value.list.map { it.typeId }
                )
            )) {
                is core.data.net.ResNet.Success -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                }

                is core.data.net.ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastError(resp.msg)
                }
            }
        }
    }

}