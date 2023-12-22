package ui.screen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import core.navigation.RootComponent
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
    private val navigationListener: IRootComponent
) : BaseComponent<MainState, MainEvent, MainEffect>(componentContext) {

    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.Home,
        key = "MainComponentPage",
        childFactory = ::createChild
    )

    override fun initialState(): MainState {
        return MainState()
    }

    override fun onEvent(event: MainEvent) {
    }

    private fun createChild(
        config: Configuration,
        componentContext: ComponentContext
    ): Child {
        return when (config) {
            is Configuration.Home -> Child.Home(HomeVM(componentContext, navigationListener))
            is Configuration.Add -> Child.Add(AddVM(componentContext, navigationListener))
            is Configuration.Mine -> Child.Mine(MineVM(componentContext, navigationListener))
        }
    }

    fun selectPage(index: Int) {
        updateState {
            it.copy(
                selectIndex = index
            )
        }
        navigation.bringToFront(
            when(index) {
                0 -> Configuration.Home
                1 -> Configuration.Add
                2 -> Configuration.Mine
                else -> Configuration.Home
            }
        )
    }

    sealed class Child {
        data class Home(val component: HomeVM) : Child()

        data class Add(val component: AddVM) : Child()

        data class Mine(val component: MineVM) : Child()
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