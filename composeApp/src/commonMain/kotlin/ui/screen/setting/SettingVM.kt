package ui.screen.setting

import com.arkivanov.decompose.ComponentContext
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import core.utils.Res

/**
 * 设置页面 VM
 *
 * @author 高国峰
 * @date 2024/02/03-23:13
 */
class SettingVM(
    componentContext: ComponentContext,
    private val rootComponent: IRootComponent
) : BaseComponent<SettingState, SettingEvent, SettingEffect>(componentContext) {
    override fun initialState(): SettingState {
        return SettingState()
    }

    override fun onEvent(event: SettingEvent) {
        when(event){
            SettingEvent.Back -> {
                rootComponent.onBack()
            }

            is SettingEvent.SystemAgreement -> {
                rootComponent.onNavigationToScreenAgreement(event.type)
            }

            SettingEvent.AboutUs -> {
                rootComponent.onNavigationToScreenWebView(Res.strings.str_about_us_url, Res.strings.str_about_us)
            }
        }
    }
}