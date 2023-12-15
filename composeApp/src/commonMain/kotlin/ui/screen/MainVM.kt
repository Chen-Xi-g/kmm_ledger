package ui.screen

import com.arkivanov.decompose.ComponentContext
import core.navigation.BaseComponent

/**
 * 首页
 *
 * @author 高国峰
 * @date 2023/12/6-16:32
 */
class MainVM(
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit
): BaseComponent<MainState, MainEvent, MainEffect>(componentContext) {

    override fun initialState(): MainState {
        return MainState()
    }

    override fun onEvent(event: MainEvent) {
    }
}