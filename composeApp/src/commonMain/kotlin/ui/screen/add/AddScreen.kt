package ui.screen.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import core.domain.entity.PayTypeEntity
import core.utils.Res
import core.utils.Res.strings.str_expenditure
import core.utils.Res.strings.str_income
import core.utils.Res.strings.str_next_step
import core.utils.Res.strings.str_pay_type_manager
import core.utils.Res.strings.str_plus
import core.utils.pxToDp
import core.utils.toDateString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.WindowInfo
import platform.log
import platform.rememberWindowInfo
import ui.theme.LedgerTheme
import ui.theme.LocalColor
import ui.theme.LocalDrawable
import ui.widget.FilterDateTimePicker
import ui.widget.LoadingDialog
import ui.widget.RoundedIndicator
import ui.widget.TipDialog

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
        AddContent(state, component.keyboardContent, component::onEvent)
    }
    LoadingDialog(state.isLoading)
    FilterDateTimePicker(
        state.visibleDateTimePickerShow,
        2,
        state.dateTime,
        confirmClicked = { year, month, day ->
            // 选择数据
            component.onEvent(AddEvent.ChangeDate(false, LocalDate(year, month, day)))
        },
        cancelClicked = {
            // 取消选择
            component.onEvent(AddEvent.ChangeDate(false, null))
        }
    )
    if (state.isSaveDialog) {
        TipDialog(
            title = Res.strings.str_tip,
            message = Res.strings.str_add_dialog_tips,
            positiveText = Res.strings.str_setting,
            negativeText = Res.strings.str_save,
            subText = Res.strings.str_cancel,
            onPositiveClick = {
                component.onEvent(AddEvent.ToMoreInfo)
            },
            onNegativeClick = {
                component.onEvent(AddEvent.SaveBill)
            },
            subClick = {
                component.onEvent(AddEvent.CancelSaveBill)
            },
            onDismiss = {}
        )
    }
}

/**
 * 竖屏，窄屏布局
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddContent(
    state: AddState,
    keyboardContent: String,
    onEvent: (AddEvent) -> Unit
) {
    val pagerState = rememberPagerState() {
        state.types.size
    }
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalColor.current.surfaceVariant),
            horizontalAlignment = Alignment.Start
        ) {
            // 标题
            AddTitleContent(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                onEvent = onEvent
            )
            // 消费类型
            AddPayTypeContent(
                state = state,
                pagerState = pagerState,
                onEvent = onEvent
            )
            // 消费类型子列表
            HorizontalPager(
                modifier = Modifier.weight(1f),
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { page ->
                AddPayTypeChildContent(
                    page = page,
                    state = state,
                    onEvent = onEvent
                )
            }
            // 键盘
            AddKeyboardContent(
                modifier = Modifier.fillMaxWidth(),
                keyboardContent = keyboardContent,
                state = state,
                onEvent = onEvent
            )
        }
    }
}

/**
 * 金额键盘
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun AddKeyboardContent(
    state: AddState,
    keyboardContent: String,
    onEvent: (AddEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val rotate = animateFloatAsState(
        targetValue = if (state.isKeyboardShow) 0f else 180f
    )
    var keyboardHeight by remember { mutableStateOf(IntSize.Zero) }
    Column(
        modifier = modifier
            .background(
                LocalColor.current.surface,
                RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 折叠
        IconButton(
            modifier = Modifier
                .rotate(rotate.value)
                .size(45.dp),
            onClick = { onEvent(AddEvent.SwitchKeyboard) }
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowUp,
                contentDescription = null,
                tint = LocalColor.current.textTitle
            )
        }

        AnimatedVisibility(
            visible = state.isKeyboardShow,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                // 输入内容
                Row(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "金额：$keyboardContent",
                        fontSize = 15.sp,
                        color = LocalColor.current.textTitle
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            onEvent(AddEvent.ChangeDate(true, null))
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(15.dp),
                            painter = painterResource(LocalDrawable.current.dateFilter),
                            contentDescription = null,
                            tint = LocalColor.current.textTitle
                        )
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = state.dateTime.toDateString(),
                            fontSize = 13.sp,
                            color = LocalColor.current.textTitle
                        )
                    }
                }
                // 键盘123456789-0.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .onSizeChanged {
                                keyboardHeight = it
                            },
                        columns = GridCells.Fixed(3)
                    ) {
                        items(state.keyboardList) {
                            Box(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(color = LocalColor.current.textHint),
                                    ) { onEvent(AddEvent.ChangeKeyboardContent(it.toCharArray()[0])) }
                                    .padding(vertical = 15.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = it,
                                    fontSize = 15.sp,
                                    color = LocalColor.current.textTitle,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    // 删除 + 下一步
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(keyboardHeight.height.pxToDp()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.25f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(color = LocalColor.current.textHint),
                                ) { onEvent(AddEvent.DeleteKeyboardContent) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                painter = painterResource(LocalDrawable.current.backspace),
                                contentDescription = null,
                                tint = LocalColor.current.textTitle
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.25f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(color = LocalColor.current.textHint),
                                ) { onEvent(AddEvent.ChangeKeyboardContent('+')) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = str_plus,
                                fontSize = 15.sp,
                                color = LocalColor.current.textTitle,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(color = LocalColor.current.textHint),
                                ) { onEvent(AddEvent.KeyboardNext) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = str_next_step,
                                fontSize = 15.sp,
                                color = LocalColor.current.textTitle,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 消费类型
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddPayTypeContent(
    pagerState: PagerState,
    state: AddState,
    onEvent: (AddEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    ScrollableTabRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp),
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            if (tabPositions.isNotEmpty()) {
                RoundedIndicator(
                    list = tabPositions,
                    pagerState = pagerState
                )
            }
        },
        divider = {},
        edgePadding = 0.dp
    ) {
        state.types.forEachIndexed { index, payTypeEntity ->
            val item = state.types[index]
            Tab(
                modifier = Modifier
                    .height(42.dp)
                    .zIndex(2f),
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            ) {
                Text(
                    text = item.typeName,
                    fontSize = 13.sp,
                    color = LocalColor.current.textPrimary,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

/**
 * 账单类型子类型数据
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddPayTypeChildContent(
    page: Int,
    state: AddState,
    onEvent: (AddEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .padding(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        state.types[page].child.forEachIndexed { index, payTypeEntity ->
            AddPayTypeChildItemContent(
                payTypeEntity.typeName,
                isSelected = state.currentPayTypeChild == index
            ) {
                onEvent(AddEvent.SwitchPayTypeChild(index))
            }
        }
    }
}

@Composable
private fun AddPayTypeChildItemContent(
    typeName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tween = tween<Color>(
        durationMillis = AnimationConstants.DefaultDurationMillis * 2,
        delayMillis = 100,
        easing = LinearOutSlowInEasing,
    )
    val transition = updateTransition(isSelected)
    val background by transition.animateColor(transitionSpec = { tween }) {
        if (isSelected) LocalColor.current.themePrimary else LocalColor.current.surfaceVariant
    }
    val shadowColor by transition.animateColor(transitionSpec = { tween }) {
        if (isSelected) LocalColor.current.themePrimary else LocalColor.current.textTitle
    }
    val textColor by transition.animateColor(transitionSpec = { tween }) {
        if (isSelected) Color.White else LocalColor.current.textPrimary
    }
    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 75.dp)
            .shadow(
                3.dp,
                shape = RoundedCornerShape(4.dp),
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .background(
                color = background,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = typeName,
            fontSize = 13.sp,
            color = textColor,
        )
    }
}

/**
 * 新增账单标题
 */
@Composable
private fun AddTitleContent(
    state: AddState,
    onEvent: (AddEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(LocalColor.current.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable {
                        onEvent(AddEvent.ToPayType)
                    }
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                text = str_pay_type_manager,
                fontSize = 13.sp,
                color = LocalColor.current.textHint,
                fontWeight = FontWeight.Bold
            )
            // 收入 and 支出
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .wrapContentSize()
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(8.dp),
                        color = LocalColor.current.themePrimary,
                    )
            ) {
                // 支出
                Box(
                    modifier = Modifier
                        .background(
                            color = if (!state.isIncome) LocalColor.current.themePrimary else Color.Transparent,
                            shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                        )
                        .clickable { onEvent(AddEvent.SwitchIncome(false)) }
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
                // 收入
                Box(
                    modifier = Modifier
                        .background(
                            color = if (state.isIncome) LocalColor.current.themePrimary else Color.Transparent,
                            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                        )
                        .clickable { onEvent(AddEvent.SwitchIncome(true)) }
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
            }

            IconButton(
                onClick = {
                    if (!state.isLoading) {
                        onEvent(AddEvent.GetPayType)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 15.dp)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = null,
                    tint = LocalColor.current.textTitle
                )
            }
        }
        // 分割线
        Divider(
            modifier = Modifier
                .height(1.dp),
            color = LocalColor.current.divider
        )
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
}

@Preview
@Composable
fun AddScreenPreview() {
    LedgerTheme(
        darkTheme = false
    ) {
        AddContent(
            state = AddState(
                types = listOf(
                    PayTypeEntity(
                        typeName = "餐饮"
                    ),
                    PayTypeEntity(
                        typeName = "购物"
                    ),
                    PayTypeEntity(
                        typeName = "交通"
                    ),
                    PayTypeEntity(
                        typeName = "娱乐"
                    ),
                    PayTypeEntity(
                        typeName = "通讯"
                    ),
                    PayTypeEntity(
                        typeName = "居家"
                    ),
                    PayTypeEntity(
                        typeName = "孩子"
                    ),
                    PayTypeEntity(
                        typeName = "长辈"
                    ),
                    PayTypeEntity(
                        typeName = "社交"
                    ),
                    PayTypeEntity(
                        typeName = "旅行"
                    ),
                    PayTypeEntity(
                        typeName = "烟酒"
                    )
                )
            ),
            "123",
            onEvent = {}
        )
    }
}