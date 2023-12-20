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
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.hashString
import com.arkivanov.decompose.router.pages.ChildPages
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
        },
        containerColor = LocalColor.current.surface,
    ){
        // Pages 是 Decompose 提供的一个组件，用于管理多个页面。
        // 内部使用HorizontalPager实现
        // 需要注意的是compose-multiplatform-core在1.5.4版本存在缓慢滑动onPageSelected不回调的问题。
        Pages<MainVM.Configuration, MainVM.Page>(
            modifier = Modifier.padding(it),
            pages = component.pages.subscribeAsState(),
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

@OptIn(InternalDecomposeApi::class)
@ExperimentalFoundationApi
@ExperimentalDecomposeApi
@Composable
fun <C : Any, T : Any> Pages(
    pages: State<ChildPages<C, T>>,
    onPageSelected: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollAnimation: PagesScrollAnimation = PagesScrollAnimation.Disabled,
    pager: Pager = defaultHorizontalPager(),
    key: (Child<C, T>) -> Any = { it.configuration.hashString() },
    pageContent: @Composable PagerScope.(index: Int, page: T) -> Unit,
) {
    val childPages by pages
    val selectedIndex = childPages.selectedIndex
    val state = rememberPagerState(
        initialPage = selectedIndex,
        pageCount = { childPages.items.size },
    )

    LaunchedEffect(selectedIndex) {
        if (state.currentPage != selectedIndex) {
            when (scrollAnimation) {
                is PagesScrollAnimation.Disabled -> state.scrollToPage(selectedIndex)
                is PagesScrollAnimation.Default -> state.animateScrollToPage(page = selectedIndex)
                is PagesScrollAnimation.Custom -> state.animateScrollToPage(page = selectedIndex, animationSpec = scrollAnimation.spec)
            }
        }
    }

    DisposableEffect(state.currentPage, state.targetPage) {
        if (state.currentPage == state.targetPage) {
            onPageSelected(state.currentPage)
        }

        onDispose {}
    }

    pager(
        modifier,
        state,
        { key(childPages.items[it]) },
    ) { pageIndex ->
        val item = childPages.items[pageIndex]

        val pageRef = remember(item.configuration) { Ref(item.instance) }
        if (item.instance != null) {
            pageRef.value = item.instance
        }

        val page = pageRef.value
        if (page != null) {
            pageContent(pageIndex, page)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
internal typealias Pager =
        @Composable (
            Modifier,
            PagerState,
            key: (index: Int) -> Any,
            pageContent: @Composable PagerScope.(index: Int) -> Unit,
        ) -> Unit