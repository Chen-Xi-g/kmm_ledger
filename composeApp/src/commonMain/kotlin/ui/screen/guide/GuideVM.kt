package ui.screen.guide

import com.arkivanov.decompose.ComponentContext
import core.navigation.BaseComponent
import core.navigation.UiEffect
import core.navigation.UiState

/**
 * 引导页
 *
 * @author 高国峰
 * @date 2023/12/6-16:30
 */
class GuideVM(
    componentContext: ComponentContext,
    private val onNavigationToScreenMain: () -> Unit,
) : BaseComponent<UiState, GuideEvent, UiEffect>(componentContext) {
    override fun initialState(): UiState {
        return object : UiState {}
    }

    override fun onEvent(event: GuideEvent) {
        when (event) {
            GuideEvent.ClickButton -> {
                onNavigationToScreenMain()
            }
        }
    }

}