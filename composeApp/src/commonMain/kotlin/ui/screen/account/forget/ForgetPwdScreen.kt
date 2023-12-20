package ui.screen.account.forget

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.base64ToBitmap
import platform.safeArea
import ui.theme.LocalColor
import ui.theme.LocalDrawable
import ui.widget.BottomOutlineInput
import ui.widget.FillGradationButton
import ui.widget.LoadingDialog
import ui.widget.LogoTextWidget
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * 忘记密码
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun ForgetPwdScreen(
    component: ForgetPwdVM
) {
    val state by component.state.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.safeArea()
        ){
            IconButton(
                modifier = Modifier.padding(5.dp).size(50.dp).padding(15.dp),
                onClick = {
                    component.onEvent(ForgetPwdEvent.GoBack)
                }
            ) {
                Icon(
                    painter = painterResource(LocalDrawable.current.back),
                    contentDescription = Res.strings.str_back,
                    tint = LocalColor.current.backIcon
                )
            }
            ForgetPwdContent(component, state, component::onEvent)
        }
    }
    LoadingDialog(state.isLoading)
}

@OptIn(ExperimentalEncodingApi::class)
@Composable
private fun ForgetPwdContent(
    component: ForgetPwdVM,
    state: ForgetPwdState,
    onEvent: (ForgetPwdEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item(key = "title") {
            Text(
                modifier = Modifier.padding(top = 15.dp, start = 25.dp),
                text = Res.strings.str_forget_password,
                fontSize = 36.sp,
                color = LocalColor.current.textTitle
            )
        }
        item(key = "input") {
            // 用户名
            Spacer(modifier = Modifier.height(30.dp))
            BottomOutlineInput(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .fillMaxWidth(),
                label = Res.strings.str_hint_login_username,
                value = component.username
            ) {
                component.updateUsername(it)
            }
            if (state.errorUsername.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(15.dp))
            } else {
                Text(
                    modifier = Modifier.padding(start = 25.dp),
                    text = state.errorUsername,
                    fontSize = 12.sp,
                    color = LocalColor.current.textError
                )
            }
            // 密码
            BottomOutlineInput(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .fillMaxWidth(),
                label = Res.strings.str_hint_login_password,
                value = component.password,
                type = 1
            ) {
                component.updatePassword(it)
            }
            if (state.errorPassword.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(15.dp))
            } else {
                Text(
                    modifier = Modifier.padding(start = 25.dp),
                    text = state.errorPassword,
                    fontSize = 12.sp,
                    color = LocalColor.current.textError
                )
            }
            // 密码
            BottomOutlineInput(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .fillMaxWidth(),
                label = Res.strings.str_hint_login_confirm_password,
                value = component.confirmPassword,
                type = 1
            ) {
                component.updateConfirmPassword(it)
            }
            if (state.errorConfirmPassword.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(15.dp))
            } else {
                Text(
                    modifier = Modifier.padding(start = 25.dp),
                    text = state.errorConfirmPassword,
                    fontSize = 12.sp,
                    color = LocalColor.current.textError
                )
            }
            // 验证码
            Row(
                modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomOutlineInput(
                    modifier = Modifier
                        .weight(1F),
                    label = Res.strings.str_hint_login_code,
                    value = component.code,
                    type = 2
                ) {
                    component.updateCode(it)
                }
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(width = 120.dp, height = 45.dp)
                        .border(
                            width = 2.dp,
                            color = LocalColor.current.themePrimary,
                            shape = RoundedCornerShape(4.dp)
                        ).clickable { onEvent(ForgetPwdEvent.RefreshCode) }
                ) {
                    if (state.codeImg.isNotEmpty()) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(4.dp)),
                            bitmap = Base64.decode(state.codeImg).base64ToBitmap(),
                            contentDescription = Res.strings.str_code,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
            if (!state.errorCaptcha.isNullOrBlank()) {
                Text(
                    modifier = Modifier.padding(start = 25.dp),
                    text = state.errorCaptcha,
                    fontSize = 12.sp,
                    color = LocalColor.current.textError
                )
            }
        }
        item(key = "submit") {
            Spacer(modifier = Modifier.height(47.dp))
            FillGradationButton(
                modifier = Modifier.padding(horizontal = 25.dp),
                text = Res.strings.str_submit
            ) {
                onEvent(ForgetPwdEvent.Submit)
            }
        }
        item(key = "logo") {
            Spacer(modifier = Modifier.height(120.dp))
            LogoTextWidget(
                modifier = Modifier.fillMaxWidth(),
                color = LocalColor.current.textTitle,
                fontSize = 24.sp,
                logoSize = 45.dp
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}