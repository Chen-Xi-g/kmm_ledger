package ui.screen.setting.account

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.data.dto.UserAccountDto
import core.mappers.toYuan
import core.utils.Res
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea
import ui.theme.LocalColor
import ui.widget.LedgerTitle

/**
 * 账户管理 屏幕
 *
 * @author 高国峰
 * @date 2024/01/29-11:36
 */
@Composable
fun AccountScreen(
    component: AccountVM
) {
    val state by component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        AccountContent(state, component::onEvent)
    }
}

/**
 * 竖屏，窄屏布局
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AccountContent(
    state: AccountState,
    onEvent: (AccountEvent) -> Unit
) {
    Column(
        modifier = Modifier.safeArea()
    ) {
        LedgerTitle(title = Res.strings.str_mine_account, onBack = {
            onEvent(AccountEvent.Back)
        })
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 15.dp)
        ) {
            stickyHeader {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = Res.strings.str_electronic_account,
                    fontSize = 15.sp,
                    color = LocalColor.current.textPrimary
                )
            }
            itemsIndexed(state.electronicList) { index, account ->
                AccountItem(account,onEvent)
                if (index != state.electronicList.size - 1) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            item("divider") {
                Spacer(modifier = Modifier.height(10.dp))
            }
            stickyHeader {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = Res.strings.str_savings_account,
                    fontSize = 15.sp,
                    color = LocalColor.current.textPrimary
                )
            }
            itemsIndexed(state.savingsList) { index, account ->
                AccountItem(account, onEvent)
                if (index != state.savingsList.size - 1) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun AccountItem(
    account: UserAccountDto,
    onEvent: (AccountEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
            .background(accountBgColor(account), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 15.dp)
            .clickable {
                onEvent(AccountEvent.SelectAccount(account))
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = account.cardName,
            fontSize = 15.sp,
            color = accountTextColor(account)
        )
        Text(
            text = "${account.balance.toYuan()}￥",
            fontSize = 23.sp,
            color = accountTextColor(account)
        )
    }
}

@Composable
private fun accountBgColor(account: UserAccountDto): Color{
    return if (account.type == "00"){
        if (account.cardName == "微信支付"){
            Color.Green
        }else {
            Color.Blue
        }
    }else{
        LocalColor.current.card
    }
}

@Composable
private fun accountTextColor(account: UserAccountDto): Color{
    return if (account.type == "00"){
        Color.White
    }else{
        LocalColor.current.textPrimary
    }
}

/**
 * 横屏，宽屏布局
 */
@Composable
private fun AccountContentLarge(
    state: AccountState,
    onEvent: (AccountEvent) -> Unit
) {

}