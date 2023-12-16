package ui.screen.mine

import com.arkivanov.decompose.ComponentContext
import core.navigation.BaseComponent
import ui.widget.ToastState

/**
 * 我的 VM
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
class MineVM(
    componentContext: ComponentContext,
    private val onToast: (String?, ToastState.ToastStyle) -> Unit
) : BaseComponent<MineState, MineEvent, MineEffect>(componentContext) {
    override fun initialState(): MineState {
        return MineState()
    }

    override fun onEvent(event: MineEvent) {
    }
}