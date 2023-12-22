package ui.screen.add

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import core.data.repository.BillRepositoryImpl
import core.domain.entity.PayTypeEntity
import core.domain.repository.BillRepository
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import kotlinx.coroutines.launch
import ui.widget.ToastState

/**
 * 新增账单 VM
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
class AddVM(
    componentContext: ComponentContext,
    private val navigationListener: IRootComponent,
    private val billRepository: BillRepository = BillRepositoryImpl()
) : BaseComponent<AddState, AddEvent, AddEffect>(componentContext) {

    init {
        lifecycle.doOnCreate {
            onEvent(AddEvent.GetPayType)
        }
    }

    override fun initialState(): AddState {
        return AddState()
    }

    override fun onEvent(event: AddEvent) {
        when (event) {
            AddEvent.GetPayType -> getPayType()
            is AddEvent.GetPayTypeChild -> getPayTypeChild(event.typeId)
            is AddEvent.SwitchIncome -> {
                updateState {
                    it.copy(
                        isIncome = event.isIncome
                    )
                }
            }

            is AddEvent.AddPayType -> addPayType()
            is AddEvent.AddPayTypeChild -> addPayTypeChild()
        }
    }

    private fun getPayType() {
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when (val resp = billRepository.getPayType(if (state.value.isIncome) "1" else "0")) {
                is core.data.net.ResNet.Success -> {
                    val list = mutableListOf<PayTypeEntity>()
                    list.addAll(resp.data ?: emptyList())
                    list.add(
                        PayTypeEntity(
                            isAdd = true
                        )
                    )
                    updateState {
                        it.copy(
                            isLoading = false,
                            types = resp.data ?: emptyList(),
                        )
                    }
                }

                is core.data.net.ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    navigationListener.toastError(resp.msg)
                }
            }
        }
    }

    private fun getPayTypeChild(typeId: String) {
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when (val resp = billRepository.getPayTypeChild(typeId)) {
                is core.data.net.ResNet.Success -> {
                    updateState {
                        it.copy(
                            isLoading = false,
                            typeChildList = resp.data ?: emptyList(),
                        )
                    }
                }

                is core.data.net.ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    navigationListener.toastError(resp.msg)
                }
            }
        }
    }

    private fun addPayType() {
        val typeName = state.value.typeName
        val typeTag = if (state.value.isIncome) "1" else "0"
        if (typeName.isEmpty()) {
            navigationListener.toastError("请输入类型名称")
            return
        }
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when (val resp = billRepository.addPayType(
                typeName = typeName,
                typeTag = typeTag
            )) {
                is core.data.net.ResNet.Success -> {

                }

                is core.data.net.ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    navigationListener.toastError(resp.msg)
                }
            }
        }
    }

    private fun addPayTypeChild() {
        val typeName = state.value.typeName
        if (typeName.isEmpty()) {
            navigationListener.toastError("请输入类型名称")
            return
        }
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when (val resp = billRepository.addPayType(
                typeName = typeName,
                typeTag = if (state.value.isIncome) "1" else "0",
                parentId = state.value.types[state.value.currentPayType].typeId
            )) {
                is core.data.net.ResNet.Success -> {
                    getPayType()
                }

                is core.data.net.ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    navigationListener.toastError(resp.msg)
                }
            }
        }
    }
}