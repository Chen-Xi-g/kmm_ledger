package ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.domain.entity.BillDetailListEntity
import core.domain.entity.BillListEntity
import core.navigation.collectEffect
import core.utils.Res
import core.utils.Res.strings.str_account_type
import core.utils.Res.strings.str_add_bill
import core.utils.Res.strings.str_bill_date
import core.utils.Res.strings.str_bill_detail
import core.utils.Res.strings.str_expand
import core.utils.Res.strings.str_format_expenditure
import core.utils.Res.strings.str_format_income
import core.utils.Res.strings.str_income_expenditure_type
import core.utils.Res.strings.str_no_bill
import core.utils.Res.strings.str_no_more
import core.utils.Res.strings.str_pack_up
import core.utils.Res.strings.str_query
import core.utils.toHourMinute
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.WindowInfo
import platform.rememberWindowInfo
import ui.theme.LedgerTheme
import ui.theme.LocalColor
import ui.theme.LocalDrawable
import ui.widget.FillGradationButton
import ui.widget.FilterDateTimePicker
import ui.widget.gradationBrush
import ui.widget.refresh.PullRefreshLayout
import ui.widget.refresh.rememberPullRefreshState
import kotlin.random.Random

/**
 * 首页
 *
 * @author
 * @date 2023/12/14-09:54
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    component: HomeVM
) {
    val state by component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    val listState = rememberLazyListState()
    val bottomSheetState = rememberModalBottomSheetState()
    component.collectEffect {
        when (it) {
            HomeEffect.ScrollToTop -> {
                listState.animateScrollToItem(0)
            }
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val refreshState = rememberPullRefreshState(
            refreshing = state.isLoading,
            onRefresh = { component.onEvent(HomeEvent.QueryBillList) }
        )
        PullRefreshLayout(
            modifier = Modifier.fillMaxSize(),
            state = refreshState
        ){
            HomeContent(listState, state.list, component::onEvent, component::onEffect)
        }
    }
    // 筛选
    if (state.visibleFilter) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            windowInsets = WindowInsets(0.dp),
            containerColor = LocalColor.current.surface,
            onDismissRequest = { component.onEvent(HomeEvent.ToggleBottomSheet) }
        ) {
            HomeBottomSheet(state, component::onEvent)
        }
    }
    // 选择日期
    FilterDateTimePicker(
        state.visibleMonthPicker,
        state.currentDateIndex,
        state.dates[state.currentDateIndex].date,
        confirmClicked = { year, month, day ->
            // 选择数据
            component.onEvent(HomeEvent.SelectDate(year, month, day))
        },
        cancelClicked = {
            // 取消选择
            component.onEvent(HomeEvent.ChangeFilterDateIndex(-1, false))
        }
    )
}

/**
 * 窄屏首页布局
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
private fun HomeContent(
    listState: LazyListState,
    list: List<BillListEntity>,
    onEvent: (HomeEvent) -> Unit,
    onEffect: (HomeEffect) -> Unit
) {
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState
        ) {
            // 忞鹿记账
            stickyHeader(key = "topHeader") {
                HomeHeaderContent()
            }
            // 账单卡片
            item(key = "billCard") {
                HomeCardContent()
                Spacer(modifier = Modifier.height(25.dp))
            }
            // 账单详情
            stickyHeader(key = "detailHeader") {
                HomeDetailHeaderContent(onEvent)
            }
            // 列表数据
            if (list.isEmpty()) {
                item("empty") {
                    HomeItemEmptyContent()
                }
            } else {
                items(list.size) { index ->
                    HomeDetailItem(index, list[index], onEvent)
                }
                item(key = "noMore"){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = str_no_more,
                            fontSize = 12.sp,
                            color = LocalColor.current.textHint
                        )
                    }
                }
            }
        }

        // 悬浮置顶按钮
        AnimatedVisibility(
            visible = listState.isScrollingUp(),
            modifier = Modifier
                .padding(15.dp)
                .size(35.dp)
                .align(Alignment.BottomEnd),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(
                onClick = {
                    onEffect(HomeEffect.ScrollToTop)
                }
            ) {
                Image(
                    painter = painterResource(LocalDrawable.current.scrollTop),
                    contentDescription = Res.strings.str_scroll_to_top,
                )
            }
        }
    }
}

/**
 * 首页列表为空时的布局
 */
@Composable
private fun HomeItemEmptyContent() {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = LocalColor.current.card,
                shape = RoundedCornerShape(14.dp)
            ).padding(vertical = 10.dp, horizontal = 15.dp)
    ) {
        Text(
            modifier = Modifier,
            text = str_no_bill,
            fontSize = 16.sp,
            color = LocalColor.current.textPrimary
        )
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .wrapContentSize()
                .background(LocalColor.current.surface, shape = CircleShape)
                .align(Alignment.End),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                text = str_add_bill,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }
}

/**
 * 首页明细列表头部
 */
@Composable
@OptIn(ExperimentalResourceApi::class)
private fun HomeDetailHeaderContent(onEvent: (HomeEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(LocalColor.current.surface),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 15.dp),
            text = str_bill_detail,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            modifier = Modifier
                .padding(end = 10.dp)
                .size(35.dp)
                .padding(5.dp),
            onClick = { onEvent(HomeEvent.ToggleBottomSheet) }
        ) {
            Icon(
                painter = painterResource(LocalDrawable.current.dateFilter),
                contentDescription = Res.strings.str_bill_date_filter,
                tint = LocalColor.current.textPrimary
            )
        }
    }
}

/**
 * 首页账单卡片
 */
@Composable
@OptIn(ExperimentalResourceApi::class)
private fun HomeCardContent() {
    Column(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp)
            .fillMaxWidth()
            .gradationBrush(RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        // 账单卡片标题
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "账单卡片",
                fontSize = 16.sp,
                color = Color.White
            )
            IconButton(
                modifier = Modifier
                    .padding(5.dp)
                    .size(20.dp),
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(LocalDrawable.current.visible),
                    contentDescription = Res.strings.str_bill_map,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // 金额
        Text(
            text = "10000000000000.00${Res.strings.str_money}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(5.dp))

        // 支出收入数据
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(0.5F),
                text = "本月支出：1000000000000.00${Res.strings.str_money}",
                fontSize = 12.sp,
                color = Color.White
            )
            Text(
                modifier = Modifier.weight(0.5F),
                text = "本月收入：1000000000000.00${Res.strings.str_money}",
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

/**
 * 首页头部
 */
@Composable
@OptIn(ExperimentalResourceApi::class)
private fun HomeHeaderContent() {
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
                .wrapContentSize()
                .background(LocalColor.current.card, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                text = "7日待还100单",
                fontSize = 11.sp,
                lineHeight = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            modifier = Modifier
                .size(25.dp),
            onClick = { }
        ) {
            Icon(
                painter = painterResource(LocalDrawable.current.map),
                contentDescription = Res.strings.str_bill_map,
                tint = LocalColor.current.textTitle
            )
        }
    }
}

/**
 * 首页底部弹窗
 */
@Composable
private fun HomeBottomSheet(
    uiState: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
            .fillMaxWidth()
    ) {
        // 收支类型筛选
        Text(
            text = str_income_expenditure_type,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColor.current.textTitle
        )
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            uiState.types.forEachIndexed { index, typeEntity ->
                Box(
                    modifier = Modifier.weight(1F)
                        .clickable { onEvent(HomeEvent.ChangeFilterTypeIndex(index)) }
                        .background(
                            color = if (uiState.currentTypeIndex == index) LocalColor.current.themePrimary else LocalColor.current.card,
                            shape = CircleShape
                        ).padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = typeEntity.typeName,
                        fontSize = 13.sp,
                        color = if (uiState.currentTypeIndex == index) Color.White else LocalColor.current.textHint
                    )
                }
            }
        }

        // 账单日期筛选
        Text(
            modifier = Modifier.padding(top = 15.dp),
            text = str_bill_date,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColor.current.textTitle
        )
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            uiState.dates.forEachIndexed { index, typeEntity ->
                Box(
                    modifier = Modifier.weight(1F)
                        .clickable { onEvent(HomeEvent.ChangeFilterDateIndex(index, true)) }
                        .background(
                            color = if (uiState.currentDateIndex == index) LocalColor.current.themePrimary else LocalColor.current.card,
                            shape = CircleShape
                        ).padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (typeEntity.type) {
                            0 -> "${typeEntity.date.year}年"
                            1 -> "${typeEntity.date.year}年${typeEntity.date.monthNumber}月"
                            else -> "${typeEntity.date.year}年${typeEntity.date.monthNumber}月${typeEntity.date.dayOfMonth}日"
                        },
                        fontSize = 13.sp,
                        color = if (uiState.currentDateIndex == index) Color.White else LocalColor.current.textHint
                    )
                }
            }
        }

        // 账户类型
        Text(
            modifier = Modifier.padding(top = 15.dp),
            text = str_account_type,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColor.current.textTitle
        )
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            uiState.accounts.forEachIndexed { index, typeEntity ->
                Box(
                    modifier = Modifier.weight(1F)
                        .clickable { onEvent(HomeEvent.ChangeFilterAccountTypeIndex(index)) }
                        .background(
                            color = if (uiState.currentAccountIndex == index) LocalColor.current.themePrimary else LocalColor.current.card,
                            shape = CircleShape
                        ).padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = typeEntity.name,
                        fontSize = 13.sp,
                        color = if (uiState.currentAccountIndex == index) Color.White else LocalColor.current.textHint
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(45.dp))

        FillGradationButton(
            modifier = Modifier.padding(horizontal = 25.dp),
            text = str_query,
            onClick = {
                onEvent(HomeEvent.QueryBillList)
            }
        )

        Spacer(modifier = Modifier.height(15.dp))
    }
}

/**
 * 首页明细列表Item
 */
@Composable
private fun HomeDetailItem(
    index: Int,
    item: BillListEntity,
    onEvent: (HomeEvent) -> Unit
) {

    Row(
        modifier = Modifier
            .padding(vertical = 7.dp)
            .clickable { onEvent(HomeEvent.ToggleDetailVisible(index)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 日期
        Text(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            text = item.createTime,
            color = LocalColor.current.textSecondary,
            fontSize = 13.sp
        )

        // 占位符
        Spacer(modifier = Modifier.weight(1F))

        // 收支
        Column {
            Text(
                text = item.income.str_format_income,
                color = LocalColor.current.textSecondary,
                fontSize = 13.sp
            )
            Text(
                text = item.expenditure.str_format_expenditure,
                color = LocalColor.current.textSecondary,
                fontSize = 13.sp
            )
        }

        // 展开或收起
        Icon(
            modifier = Modifier
                .padding(start = 5.dp, end = 10.dp),
            imageVector = if (item.isExpanded) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
            contentDescription = if (item.isExpanded) str_pack_up else str_expand,
            tint = LocalColor.current.textPrimary.copy(
                alpha = 0.3F
            )
        )
    }

    AnimatedVisibility(
        visible = item.isExpanded,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        HomeDetailChild(item.children, onEvent)
    }
}

/**
 * 首页明细列表子列表
 */
@Composable
private fun HomeDetailChild(
    child: List<BillDetailListEntity>,
    onEvent: (HomeEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = LocalColor.current.card,
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        child.forEachIndexed { index, item ->
            // 子列表Item
            HomeDetailChildItem(item, onEvent)
            if (index != child.lastIndex) {
                Divider(
                    modifier = Modifier.height(1.dp),
                    color = LocalColor.current.divider
                )
            }
        }
    }
}

/**
 * 首页明细列表子列表Item
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
private fun HomeDetailChildItem(
    item: BillDetailListEntity,
    onEvent: (HomeEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .height(73.dp)
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .clickable { onEvent(HomeEvent.BillItemClick(item)) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 封面
        Box(
            modifier = Modifier.size(43.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(LocalDrawable.current.homeListType),
                modifier = Modifier
                    .size(43.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentDescription = null
            )
            Text(
                text = item.billTypeName,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2,
                lineHeight = 18.sp
            )
        }

        // 内容
        Column {

            // 标题 + 金额
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.7F),
                    text = item.billName,
                    color = LocalColor.current.textPrimary,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
                Text(
                    modifier = Modifier.weight(1F),
                    text = "${if (item.isIncome) "+" else "-"}${item.billAmount}￥",
                    color = LocalColor.current.textPrimary,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End
                )
            }

            // 备注 + 时间
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.7F),
                    text = item.billRemark,
                    fontSize = 13.sp,
                    color = LocalColor.current.textHint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = item.createTime.toHourMinute(),
                    fontSize = 13.sp,
                    color = LocalColor.current.textHint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun HomeContentLarge() {

}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Preview
@Composable
fun PreviewHomeContent() {
    LedgerTheme(
        darkTheme = false
    ) {
        HomeContent(
            listState = rememberLazyListState(),
            list = listOf(
                BillListEntity(
                    createTime = "2021-12-12",
                    expenditure = "1000",
                    income = "1000",
                    children = listOf(
                        BillDetailListEntity(
                            billId = Random.nextLong(),
                            cover = "",
                            billName = "测试账单",
                            billRemark = "测试账单备注",
                            billAmount = "1000",
                            createTime = "2021-12-12 12:12:12",
                            isIncome = true
                        ),
                        BillDetailListEntity(
                            billId = Random.nextLong(),
                            cover = "",
                            billName = "测试账单",
                            billRemark = "测试账单备注",
                            billAmount = "1000",
                            createTime = "2021-12-12 12:12:12",
                            isIncome = true
                        )
                    ),
                    isExpanded = true
                )
            ),
            onEvent = {},
            onEffect = {}
        )
    }
}