package ui.screen.home

import com.arkivanov.decompose.ComponentContext
import core.navigation.BaseComponent
import ui.widget.ToastState

/**
 * 首页VM
 *
 * @author 高国峰
 * @date 2023/12/14-09:54
 */
class HomeVM(
    componentContext: ComponentContext,
    private val onToast: (String?, ToastState.ToastStyle) -> Unit
) : BaseComponent<HomeState, HomeEvent, HomeEffect>(componentContext){
    override fun initialState(): HomeState {
        return HomeState()
    }

    override fun onEvent(event: HomeEvent) {
    }
}