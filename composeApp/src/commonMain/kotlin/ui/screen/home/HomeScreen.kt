package ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea
import ui.theme.LocalColor
import ui.theme.LocalDrawable
import ui.widget.gradationBrush

/**
 * 首页
 *
 * @author
 * @date 2023/12/14-09:54
 */
@Composable
fun HomeScreen(
    component: HomeVM
) {
    val state = component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
            .safeArea()
    ) {
        if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
            HomeContent()
        } else {
            HomeContentLarge()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
private fun HomeContent() {
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

@Composable
private fun HomeContentLarge() {

}