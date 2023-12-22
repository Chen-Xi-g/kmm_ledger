package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.InternalDecomposeApi
import com.arkivanov.decompose.Ref
import com.arkivanov.decompose.extensions.compose.jetbrains.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.pages.defaultHorizontalPager
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.hashString
import com.arkivanov.decompose.router.pages.ChildPages
import core.navigation.RootComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.backAnimation
import platform.log
import ui.screen.account.activate.ActivateScreen
import ui.screen.account.agreement.AgreementScreen
import ui.screen.account.forget.ForgetPwdScreen
import ui.screen.account.login.LoginScreen
import ui.screen.account.register.RegisterScreen
import ui.screen.add.AddScreen
import ui.screen.guide.GuideScreen
import ui.screen.guide.splash.SplashScreen
import ui.screen.home.HomeScreen
import ui.screen.mine.MineScreen
import ui.theme.LocalColor

/**
 * 首页
 *
 * @author 高国峰
 * @date 2023/12/6-16:55
 */
@Composable
fun MainScreen(
    component: MainVM
) {
    val state by component.state.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                list = state.menuList,
                selectIndex = state.selectIndex,
                onClick = component::selectPage
            )
        },
        containerColor = LocalColor.current.surface,
    ){
        Children(component, Modifier.padding(it))
    }
}

@Composable
private fun Children(component: MainVM, modifier: Modifier = Modifier) {
    com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) { child ->
        when (val instance = child.instance) {
            is MainVM.Child.Add -> AddScreen(instance.component)
            is MainVM.Child.Home -> HomeScreen(instance.component)
            is MainVM.Child.Mine -> MineScreen(instance.component)
        }
    }
}

@Composable
private fun NavigationBar(
    list: List<Menu>,
    selectIndex: Int,
    onClick: (Int) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(LocalColor.current.card)
            .navigationBarsPadding()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        list.forEachIndexed { index, menu ->
            NavigationBarItem(menu, index == selectIndex){
                onClick(index)
            }
        }
    }
}

@Composable
private fun NavigationBarItem(menu: Menu, isSelect: Boolean, onClick: () -> Unit){
    Column(
        modifier = Modifier
            .padding(5.dp)
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = if (isSelect) menu.selectIcon else menu.unSelectIcon,
            contentDescription = menu.title,
            tint = if (isSelect) LocalColor.current.themePrimary else LocalColor.current.textTitle
        )
        Text(
            text = menu.title,
            fontSize = 13.sp,
            color = if (isSelect) LocalColor.current.themePrimary else LocalColor.current.textTitle
        )
    }
}