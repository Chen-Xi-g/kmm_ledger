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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.pages.Pages
import com.arkivanov.decompose.extensions.compose.jetbrains.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.log
import ui.screen.add.AddScreen
import ui.screen.home.HomeScreen
import ui.screen.mine.MineScreen
import ui.theme.LocalColor

/**
 * 首页
 *
 * @author 高国峰
 * @date 2023/12/6-16:55
 */
@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class,
    ExperimentalDecomposeApi::class
)
@Composable
fun MainScreen(
    component: MainVM
) {
    val state by component.state.collectAsState()
    val pages by component.pages.subscribeAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                list = state.menuList,
                selectIndex = pages.selectedIndex,
                onClick = component::selectPage
            )
        }
    ){
        Pages(
            pages = component.pages,
            onPageSelected = component::selectPage,
            scrollAnimation = PagesScrollAnimation.Default,
        ){ index: Int, page: MainVM.Page ->
            when(page){
                is MainVM.Page.Home -> HomeScreen(page.component)
                is MainVM.Page.Add -> AddScreen(page.component)
                is MainVM.Page.Mine -> MineScreen(page.component)
            }
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