package ui.screen.setting

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.Res
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea
import ui.screen.account.login.LoginEvent
import ui.theme.LedgerTheme
import ui.theme.LocalColor
import ui.widget.FillGradationButton
import ui.widget.LedgerTitle

/**
 * 设置页面 屏幕
 *
 * @author 高国峰
 * @date 2024/02/03-23:13
 */
@Composable
fun SettingScreen(
    component: SettingVM
) {
    val state by component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
            SettingContent(state, component::onEvent)
        } else {
            SettingContentLarge(state, component::onEvent)
        }
    }
}

/**
 * 竖屏，窄屏布局
 */
@Composable
private fun SettingContent(
    state: SettingState,
    onEvent: (SettingEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .safeArea()
    ) {
        LedgerTitle(
            title = Res.strings.str_setting,
            onBack = {
                onEvent(SettingEvent.Back)
            }
        )
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item("user_agreement") {
                SettingMenuItem(
                    Res.strings.str_user_agreement
                ) {
                    onEvent(SettingEvent.SystemAgreement(1))
                }
            }
            item("privacy_agreement") {
                SettingMenuItem(
                    Res.strings.str_privacy_policy
                ) {
                    onEvent(SettingEvent.SystemAgreement(2))
                }
            }
            item("about_us") {
                SettingMenuItem(
                    Res.strings.str_about_us,
                    isDivider = false
                ) {
                    onEvent(SettingEvent.AboutUs)
                }
            }
        }
    }
}

@Composable
private fun SettingMenuItem(
    text: String = "",
    isDivider: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 15.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            fontSize = 14.sp
        )
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
private fun SettingContentLarge(
    state: SettingState,
    onEvent: (SettingEvent) -> Unit
) {

}

@Preview
@Composable
fun PreviewSettingScreen() {
    LedgerTheme {
        Surface {
            SettingContent(SettingState(), {})
        }
    }
}