package ui.screen.setting.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.KeyRepository
import core.utils.Res
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea
import ui.screen.setting.SettingEvent
import ui.theme.LocalColor
import ui.widget.FillGradationButton
import ui.widget.LedgerTitle

/**
 * 用户信息 屏幕
 *
 * @author 高国峰
 * @date 2024/02/05-15:27
 */
@Composable
fun UserInfoScreen(
    component: UserInfoVM
) {
    val state by component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
            UserInfoContent(component, state, component::onEvent)
        } else {
            UserInfoContentLarge(state, component::onEvent)
        }
    }
}

/**
 * 竖屏，窄屏布局
 */
@Composable
private fun UserInfoContent(
    component: UserInfoVM,
    state: UserInfoState,
    onEvent: (UserInfoEvent) -> Unit
) {
    var value by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize()
            .safeArea()
    ) {
        LedgerTitle(
            title = Res.strings.str_user_info,
            menu = Res.strings.str_save,
            onBack = {
                onEvent(UserInfoEvent.Back)
            },
            onMenu = {
                onEvent(UserInfoEvent.ModifyUserInfo)
            }
        )
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item("name") {
                UserInfoMenuItem(
                    title = Res.strings.str_nickname,
                    text = component.nickName,
                    hint = Res.strings.str_hint_nickname,
                    onValueChange = component::updateNickName
                )
            }
            item("email") {
                UserInfoMenuItem(
                    title = Res.strings.str_email,
                    text = KeyRepository.email,
                    isEnable = false
                )
            }
            item("forget") {
                UserInfoMenuItem(
                    title = Res.strings.str_forget_password,
                    isEdit = false,
                    isDivider = false,
                    onClick = {
                        onEvent(UserInfoEvent.ForgetPassword)
                    }
                )
            }
        }
        FillGradationButton(
            modifier = Modifier.padding(horizontal = 25.dp).padding(top = 15.dp, bottom = 25.dp),
            text = Res.strings.str_logout
        ) {
            onEvent(UserInfoEvent.OnClickLogout)
        }
    }
}

@Composable
private fun UserInfoMenuItem(
    title: String = "",
    text: String = "",
    hint: String = "",
    isEdit: Boolean = true,
    isDivider: Boolean = true,
    isEnable: Boolean = true,
    onValueChange: (String) -> Unit = {},
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = if (isEdit) 0.dp else 15.dp)
            .clickable {
                       if (!isEdit){
                           onClick()
                       }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = LocalColor.current.textSecondary
        )
        if (isEdit){
            BasicTextField(
                value = text,
                onValueChange = onValueChange,
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .padding(vertical = 15.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = hint,
                                fontSize = 14.sp,
                                color = LocalColor.current.textHint
                            )
                        }
                        innerTextField()  //<-- Add this
                    }
                },
                cursorBrush = SolidColor(LocalColor.current.themePrimary),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = if (isEdit) LocalColor.current.textPrimary else LocalColor.current.textSecondary,
                    textAlign = TextAlign.End
                ),
                enabled = isEnable
            )
        }
    }
    if (isDivider) {
        Divider(
            modifier = Modifier.height(1.dp),
            color = LocalColor.current.divider
        )
    }
}

/**
 * 横屏，宽屏布局
 */
@Composable
private fun UserInfoContentLarge(
    state: UserInfoState,
    onEvent: (UserInfoEvent) -> Unit
) {

}