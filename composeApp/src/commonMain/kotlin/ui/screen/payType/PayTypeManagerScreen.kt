package ui.screen.payType

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.navigation.collectEffect
import core.utils.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea
import ui.screen.account.agreement.AgreementEvent
import ui.theme.LocalColor
import ui.theme.LocalDrawable
import ui.widget.InputDialog
import ui.widget.LedgerTitle
import ui.widget.LoadingDialog
import ui.widget.TipDialog
import ui.widget.dragList.DragDropColumn

/**
 * 收支类型管理 屏幕
 *
 * @author 高国峰
 * @date 2024/01/13-16:57
 */
@Composable
fun PayTypeManagerScreen(
    component: PayTypeManagerVM
) {
    val state by component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
            PayTypeManagerContent(state, component::onEvent)
        } else {
            PayTypeManagerContentLarge(state, component::onEvent)
        }
    }
    LoadingDialog(state.isLoading)
    RemoveDialog(state, component::onEvent)
    AddDialog(component, state, component::onEvent)
}

/**
 * 竖屏，窄屏布局
 */
@Composable
private fun PayTypeManagerContent(
    state: PayTypeManagerState,
    onEvent: (PayTypeManagerEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .safeArea()
    ) {
        PayTypeManagerTitle(state, onEvent, Modifier.fillMaxWidth())
        Text(
            text = Res.strings.str_change_list_tips,
            fontSize = 12.sp,
            color = LocalColor.current.textHint,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            textAlign = TextAlign.Center
        )
        PayTypeManagerList(state, onEvent)
    }
}

/**
 * 收支类型管理 标题
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
private fun PayTypeManagerTitle(
    state: PayTypeManagerState,
    onEvent: (PayTypeManagerEvent) -> Unit,
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
            // 收入 and 支出
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(5.dp)
                    .size(50.dp)
                    .padding(15.dp),
                onClick = {
                    onEvent(PayTypeManagerEvent.GoBack)
                }
            ) {
                Icon(
                    painter = painterResource(LocalDrawable.current.back),
                    contentDescription = Res.strings.str_back,
                    tint = LocalColor.current.backIcon
                )
            }
            if (state.type == 0){
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
                            .clickable { onEvent(PayTypeManagerEvent.SwitchIncome(false)) }
                            .padding(horizontal = 15.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = Res.strings.str_expenditure,
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
                            .clickable { onEvent(PayTypeManagerEvent.SwitchIncome(true)) }
                            .padding(horizontal = 15.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = Res.strings.str_income,
                            fontSize = 13.sp,
                            color = if (state.isIncome) Color.White else LocalColor.current.textPrimary,
                            fontWeight = if (state.isIncome) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }else{
                // 标题
                Text(
                    modifier = Modifier
                        .align(alignment = Alignment.Center),
                    text = state.typeName,
                    fontSize = 16.sp,
                    color = LocalColor.current.textTitle
                )
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(5.dp)
                    .size(50.dp)
                    .padding(15.dp),
                onClick = {
                    onEvent(PayTypeManagerEvent.ShowAddDialog)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = Res.strings.str_back,
                    tint = LocalColor.current.backIcon
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
 * 收支类型管理 列表
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PayTypeManagerList(
    state: PayTypeManagerState,
    onEvent: (PayTypeManagerEvent) -> Unit
) {
    DragDropColumn(
        items = state.list,
        onSwap = { from, to ->
            onEvent(PayTypeManagerEvent.Move(from, to))
        },
        onDragEnd = {
            onEvent(PayTypeManagerEvent.MoveChange)
        }
    ) { item, isDrag ->
        val dismissState = rememberDismissState()
        LaunchedEffect(state.removeIndex, dismissState.currentValue){
            if (state.removeIndex == -1 && dismissState.currentValue != DismissValue.Default){
                dismissState.reset()
                onEvent(PayTypeManagerEvent.HideRemoveDialog(false))
            }
        }
        if (dismissState.isDismissed(direction = DismissDirection.EndToStart) && !state.removeDialog) {
            onEvent(PayTypeManagerEvent.ShowRemoveDialog(state.list.indexOf(item)))
        }
        // 侧滑删除
        SwipeToDismiss(
            state = dismissState,
            directions = setOf(DismissDirection.EndToStart),
            background = {
                // background color
                val backgroundColor by animateColorAsState(
                    when (dismissState.targetValue) {
                        DismissValue.DismissedToStart -> Color.Red.copy(alpha = 0.8f)
                        else -> Color.Transparent
                    }
                )

                // icon size
                val scale by animateFloatAsState(
                    targetValue = if (dismissState.targetValue == DismissValue.Default) 0.5f else 1.3f
                )

                Row(
                    Modifier
                        .fillMaxSize()
                        .background(color = backgroundColor)
                        .padding(start = 16.dp, end = 16.dp), // inner padding
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.scale(scale),
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        text = Res.strings.str_delete,
                        color = Color.White,
                        modifier = Modifier.padding(start = 10.dp)
                            .scale(scale),
                        fontSize = 10.sp
                    )
                }
            },
            dismissContent = {
                Card(
                    modifier = Modifier
                        .clickable { onEvent(PayTypeManagerEvent.ItemClick(item )) },
                ) {
                    Text(
                        text = item.typeName,
                        color = LocalColor.current.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isDrag) LocalColor.current.selectedThemePrimary else LocalColor.current.card)
                            .padding(16.dp),
                    )
                }
            }
        )
    }
}

/**
 * 删除条目对话框
 */
@Composable
private fun RemoveDialog(
    state: PayTypeManagerState,
    onEvent: (PayTypeManagerEvent) -> Unit
) {
    if (state.removeDialog) {
        TipDialog(
            title = Res.strings.str_tip,
            message = Res.strings.str_delete_type_tip,
            onPositiveClick = {
                onEvent(PayTypeManagerEvent.Remove)
            },
            onNegativeClick = {
                onEvent(PayTypeManagerEvent.HideRemoveDialog(true))
            },
            onDismiss = {
                onEvent(PayTypeManagerEvent.HideRemoveDialog(true))
            }
        )
    }
}

/**
 * 新增类型对话框
 */
@Composable
private fun AddDialog(
    component: PayTypeManagerVM,
    state: PayTypeManagerState,
    onEvent: (PayTypeManagerEvent) -> Unit
) {
    if (state.isAddDialog) {
        InputDialog(
            title = Res.strings.str_tip,
            messageHint = "请输入${if (state.isIncome) "收入" else "支出"}类型名称，5个字以",
            isError = component.isError,
            validate = {
                component.check(it)
            },
            validateError = component.validateError,
            onPositiveClick = {
                onEvent(PayTypeManagerEvent.Add(it))
            },
            onNegativeClick = {
                onEvent(PayTypeManagerEvent.HideAddDialog)
            },
            onDismiss = {
                onEvent(PayTypeManagerEvent.HideAddDialog)
            }
        )
    }
}

/**
 * 横屏，宽屏布局
 */
@Composable
private fun PayTypeManagerContentLarge(
    state: PayTypeManagerState,
    onEvent: (PayTypeManagerEvent) -> Unit
) {

}