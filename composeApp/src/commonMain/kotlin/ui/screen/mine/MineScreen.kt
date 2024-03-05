package ui.screen.mine

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.WindowInfo
import platform.rememberWindowInfo
import ui.theme.LedgerTheme
import ui.theme.LocalColor
import ui.theme.LocalDrawable
import ui.widget.LoadingDialog

/**
 * 我的 屏幕
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
@Composable
fun MineScreen(
    component: MineVM
) {
    val state by component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact || windowInfo.screenWidthInfo == WindowInfo.WindowType.Medium) {
            MineContent(state, component::onEvent)
        } else {
            MineContentLarge(state, component::onEvent)
        }
    }
    LoadingDialog(state.isLoading)
}

/**
 * 竖屏，窄屏布局
 */
@Composable
private fun MineContent(
    state: MineState,
    onEvent: (MineEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MineHeaderContent(state,onEvent)
        MineAccountInfoContent(onEvent)
        MineMenuContent(onEvent)
    }
}

/**
 * 用户头像、姓名、记账数
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
private fun MineHeaderContent(
    state: MineState,
    onEvent: (MineEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 25.dp, horizontal = 15.dp)
            .clickable { onEvent(MineEvent.ToUserInfo) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(60.dp)
                .clip(CircleShape),
            painter = painterResource(LocalDrawable.current.logo),
            contentDescription = Res.strings.str_app_name,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(start = 15.dp)
                .weight(1f)
        ) {
            Text(
                text = state.nickName,
                color = LocalColor.current.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "已坚持记账25天",
                color = LocalColor.current.textHint,
                fontSize = 12.sp
            )
        }
        Icon(
            modifier = Modifier.size(25.dp),
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = LocalColor.current.textSecondary
        )
    }
}

/**
 * 用户账户信息
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
private fun MineAccountInfoContent(
    onEvent: (MineEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .paint(painter = painterResource(LocalDrawable.current.mineCard), contentScale = ContentScale.FillBounds)
            .padding(vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = Res.strings.str_account_info,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                        .clickable {
                            onEvent(MineEvent.ToAccount)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "8", fontSize = 23.sp, color = Color.White)
                    Text(
                        text = Res.strings.str_mine_account,
                        fontSize = 12.sp,
                        color = Color.White.copy(0.8f)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "2418", fontSize = 23.sp, color = Color.White)
                    Text(
                        text = Res.strings.str_mine_bill_number,
                        fontSize = 12.sp,
                        color = Color.White.copy(0.8f)
                    )
                }
            }
        }
    }
}

/**
 * 用户设置功能
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
private fun MineMenuContent(
    onEvent: (MineEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxSize()
            .background(
                color = LocalColor.current.card,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
    ) {
        item("account"){
            MineMenuItemContent(painterResource(LocalDrawable.current.account), Res.strings.str_mine_account){
                onEvent(MineEvent.ToAccount)
            }
        }
//        item("bill"){
//            MineMenuItemContent(painterResource(LocalDrawable.current.bill), Res.strings.str_mine_bill){
//
//            }
//        }
//        item("borrow"){
//            MineMenuItemContent(painterResource(LocalDrawable.current.borrow), Res.strings.str_borrow)
//        }
//        item("refund"){
//            MineMenuItemContent(painterResource(LocalDrawable.current.refund), Res.strings.str_refund)
//        }
        item("type"){
            MineMenuItemContent(painterResource(LocalDrawable.current.type), Res.strings.str_pay_type_manager){
                // 跳转到收支类型
                onEvent(MineEvent.ToPayType)
            }
        }
        item("setting"){
            MineMenuItemContent(painterResource(LocalDrawable.current.setting), Res.strings.str_setting){
                onEvent(MineEvent.ToSetting)
            }
        }
    }
}

/**
 * 菜单Item
 */
@Composable
private fun MineMenuItemContent(
    painter: Painter,
    menu: String,
    onClick:() -> Unit = {}
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painter,
            contentDescription = menu,
            tint = LocalColor.current.textPrimary
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f),
            text = menu,
            fontSize = 15.sp,
            color = LocalColor.current.textPrimary
        )

        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = menu,
            tint = LocalColor.current.textPrimary
        )
    }
}

/**
 * 横屏，宽屏布局
 */
@Composable
private fun MineContentLarge(
    state: MineState,
    onEvent: (MineEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.weight(1F)
        ) {
            MineHeaderContent(state,onEvent)
            MineAccountInfoContent(onEvent)
        }
        Column(
            modifier = Modifier.weight(1F)
        ) {
            MineMenuContent(onEvent)
        }
    }
}

@Preview
@Composable
fun MineScreenPreview() {
    LedgerTheme {
        Surface {
            MineContent(MineState(), {})
        }
    }
}