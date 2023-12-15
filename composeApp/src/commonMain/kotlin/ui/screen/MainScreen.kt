package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.Res
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.safeArea
import ui.theme.LocalColor
import ui.theme.LocalDrawable
import ui.widget.gradationBrush

/**
 * 首页
 *
 * @author 高国峰
 * @date 2023/12/6-16:55
 */
@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    component: MainVM
) {
    val state by component.state.collectAsState()
    val pageState = rememberPagerState {
        state.menuList.size
    }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                list = state.menuList,
                selectIndex = pageState.currentPage
            ){
                scope.launch {
                    pageState.animateScrollToPage(it)
                }
            }
        }
    ){
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxSize()
        ){
            when(it){
                0 -> MainScreenContent(state, component::onEvent)
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


@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
private fun MainScreenContent(
    state: MainState,
    event: (MainEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .safeArea(),
    ) {
        stickyHeader {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(LocalColor.current.surface)
                    .padding(vertical = 10.dp, horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Res.strings.str_app_name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .background(LocalColor.current.card, shape = CircleShape)
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "7日待还100单",
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    modifier = Modifier
                        .size(25.dp),
                    onClick = {  }
                ) {
                    Icon(
                        painter = painterResource(LocalDrawable.current.mapIcon),
                        contentDescription = Res.strings.str_bill_map,
                        tint = LocalColor.current.textTitle
                    )
                }
            }
        }
        item(key = "billCard"){
            Column(
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp)
                    .fillMaxWidth()
                    .gradationBrush(RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "账单卡片",
                        fontSize = 16.sp,
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(20.dp),
                        onClick = {  }
                    ) {
                        Icon(
                            painter = painterResource(LocalDrawable.current.visibleIcon),
                            contentDescription = Res.strings.str_bill_map,
                            tint = LocalColor.current.textTitle
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "10000000000000.00${Res.strings.str_money}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier.weight(0.5F),
                        text = "本月支出：1000000000000.00${Res.strings.str_money}",
                        fontSize = 12.sp,
                    )
                    Text(
                        modifier = Modifier.weight(0.5F),
                        text = "本月收入：1000000000000.00${Res.strings.str_money}",
                        fontSize = 12.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
        stickyHeader {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalColor.current.surface)
                    .padding(vertical = 10.dp, horizontal = 15.dp),
                text = Res.strings.str_bill_detail,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        items(100){
            Text(
                modifier = Modifier.padding(15.dp),
                text = "账单卡片",
                fontSize = 16.sp,
            )
        }
    }
}