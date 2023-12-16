package ui.screen.add

import com.arkivanov.decompose.ComponentContext
import core.navigation.BaseComponent
import ui.widget.ToastState

/**
 * 新增账单 VM
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
class AddVM(
    componentContext: ComponentContext,
    private val onToast: (String?, ToastState.ToastStyle) -> Unit
) : BaseComponent<AddState, AddEvent, AddEffect>(componentContext) {
    override fun initialState(): AddState {
        return AddState()
    }

    override fun onEvent(event: AddEvent) {
    }
}