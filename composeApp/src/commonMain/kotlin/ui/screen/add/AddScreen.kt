package ui.screen.add

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.Res.strings.str_expenditure
import core.utils.Res.strings.str_income
import core.utils.Res.strings.str_next_step
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea
import ui.theme.LedgerTheme
import ui.theme.LocalColor
import ui.widget.FillGradationMenuButton

/**
 * 新增账单 屏幕
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
@Composable
fun AddScreen(
    component: AddVM
) {
    val state by component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact || windowInfo.screenWidthInfo == WindowInfo.WindowType.Medium) {
            AddContent(state, component::onEvent)
        } else {
            AddContentLarge(state, component::onEvent)
        }
    }
}

/**
 * 竖屏，窄屏布局
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddContent(
    state: AddState,
    onEvent: (AddEvent) -> Unit
) {
    val payTypeState = rememberPagerState{
        0
    }
    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item(key = "title") {
                Row(
                    modifier = Modifier
                        .background(LocalColor.current.surface)
                        .padding(horizontal = 15.dp, vertical = 7.dp)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 收入 and 支出
                    Row(
                        modifier = Modifier
                            .wrapContentSize()
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(8.dp),
                                color = LocalColor.current.themePrimary,
                            ),
                    ) {
                        // 收入
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (state.isIncome) LocalColor.current.themePrimary else Color.Transparent,
                                    shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                                )
                                .padding(horizontal = 15.dp, vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = str_income,
                                fontSize = 13.sp,
                                color = if (state.isIncome) Color.White else LocalColor.current.textPrimary,
                                fontWeight = if (state.isIncome) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        // 支出
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (!state.isIncome) LocalColor.current.themePrimary else Color.Transparent,
                                    shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                                )
                                .padding(horizontal = 15.dp, vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = str_expenditure,
                                fontSize = 13.sp,
                                color = if (!state.isIncome) Color.White else LocalColor.current.textPrimary,
                                fontWeight = if (!state.isIncome) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                    // 下一步
                    FillGradationMenuButton(
                        text = str_next_step,
                        onClick = {

                        }
                    )
                }
            }
            item(key = "payType"){
                // 消费类型
//                HorizontalPager()
            }
        }
    }
}

/**
 * 横屏，宽屏布局
 */
@Composable
private fun AddContentLarge(
    state: AddState,
    onEvent: (AddEvent) -> Unit
) {
    Text(text = "新增账单")
}

@Preview
@Composable
fun AddScreenPreview() {
    LedgerTheme(
        darkTheme = false
    ) {
        AddContent(
            state = AddState(),
            onEvent = {}
        )
    }
}