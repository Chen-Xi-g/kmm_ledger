package ui.screen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import core.navigation.BaseComponent
import kotlinx.serialization.Serializable
import platform.log
import ui.screen.add.AddVM
import ui.screen.home.HomeVM
import ui.screen.mine.MineVM
import ui.widget.ToastState

/**
 * 首页
 *
 * @author 高国峰
 * @date 2023/12/6-16:32
 */
@OptIn(ExperimentalDecomposeApi::class)
class MainVM(
    componentContext: ComponentContext,
    private val onToast: (String?, ToastState.ToastStyle) -> Unit
) : BaseComponent<MainState, MainEvent, MainEffect>(componentContext) {

    private val navigation = PagesNavigation<Configuration>()
    val pages = childPages(
        source = navigation,
        serializer = Configuration.serializer(),
        initialPages = {
            Pages(
                items = listOf(
                    Configuration.Home,
                    Configuration.Add,
                    Configuration.Mine
                ),
                selectedIndex = 0
            )
        },
        key = "MainComponentPage",
        childFactory = ::createChild
    )

    override fun initialState(): MainState {
        return MainState()
    }

    override fun onEvent(event: MainEvent) {
    }

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): Page {
        return when (configuration) {
            is Configuration.Home -> Page.Home(HomeVM(componentContext, onToast))
            is Configuration.Add -> Page.Add(AddVM(componentContext, onToast))
            is Configuration.Mine -> Page.Mine(MineVM(componentContext, onToast))
        }
    }

    fun selectPage(index: Int) {
        "当前index： $index".log()
        navigation.select(index)
    }

    sealed class Page {
        data class Home(val component: HomeVM) : Page()

        data class Add(val component: AddVM) : Page()

        data class Mine(val component: MineVM) : Page()
    }


    @Serializable
    sealed class Configuration {
        @Serializable
        data object Home : Configuration()

        @Serializable
        data object Add : Configuration()

        @Serializable
        data object Mine : Configuration()
    }
}