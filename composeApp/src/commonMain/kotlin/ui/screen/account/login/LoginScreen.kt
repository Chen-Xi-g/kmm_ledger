package ui.screen.account.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.KeyRepository.username
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
import ui.widget.Toast
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * 登录界面
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginScreen(
    component: LoginVM
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
                    component.onEvent(LoginEvent.GoBack)
                }
            ) {
                Icon(
                    painter = painterResource(LocalDrawable.current.backIcon),
                    contentDescription = Res.strings.str_back,
                    tint = LocalColor.current.backIcon
                )
            }
            LoginScreenContent(component, state, component::onEvent)
        }
    }
    AnimatedVisibility(
        visible = state.isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ){
        LoadingDialog()
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalEncodingApi::class)
@Composable
private fun LoginScreenContent(
    component: LoginVM,
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    var forgetSize by remember { mutableStateOf(IntSize.Zero) }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item("title") {
            Text(
                modifier = Modifier.padding(top = 15.dp, start = 25.dp),
                text = Res.strings.str_login_account,
                fontSize = 36.sp,
                color = LocalColor.current.textTitle
            )
        }
        item("input") {
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
            Box(
                modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .fillMaxWidth()
            ) {
                BottomOutlineInput(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .fillMaxWidth()
                        .padding(end = with(LocalDensity.current) { forgetSize.width.toDp() }),
                    label = Res.strings.str_hint_login_password,
                    value = component.password,
                    type = 1
                ) {
                    component.updatePassword(it)
                }
                Text(
                    modifier = Modifier
                        .clickable { onEvent(LoginEvent.ForgetPassword) }
                        .padding(vertical = 12.dp)
                        .align(Alignment.CenterEnd)
                        .onSizeChanged {
                            forgetSize = it
                        },
                    text = Res.strings.str_forget_password,
                    fontSize = 12.sp,
                    color = LocalColor.current.themePrimary
                )
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
                        ).clickable { onEvent(LoginEvent.RefreshCode) }
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
        item("start") {
            Row(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .clickable { onEvent(LoginEvent.ActivateAccount(username)) }
                        .padding(vertical = 15.dp),
                    text = Res.strings.str_activate_account,
                    fontSize = 12.sp,
                    color = LocalColor.current.themePrimary
                )
                Text(
                    modifier = Modifier
                        .clickable { onEvent(LoginEvent.Register(username)) }
                        .padding(vertical = 15.dp),
                    text = Res.strings.str_register_account,
                    fontSize = 12.sp,
                    color = LocalColor.current.themePrimary
                )
            }
            FillGradationButton(
                modifier = Modifier.padding(horizontal = 25.dp),
                text = Res.strings.str_login
            ) {
                onEvent(LoginEvent.Submit)
            }
        }
        item("terms") {
            Spacer(modifier = Modifier.height(50.dp))
            val clickableText = buildAnnotatedString {

                append(Res.strings.str_read_agree)

                pushStringAnnotation(
                    tag = "UserAgreement",
                    annotation = "UserAgreement"
                )
                withStyle(
                    style = SpanStyle(
                        color = LocalColor.current.themePrimary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(Res.strings.str_terms)
                }
                pop()

                append(Res.strings.str_and)

                pushStringAnnotation(
                    tag = "PrivacyAgreement",
                    annotation = "PrivacyAgreement"
                )
                withStyle(
                    style = SpanStyle(
                        color = LocalColor.current.themePrimary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(Res.strings.str_privacy)
                }
                pop()

            }
            /*条款*/
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    modifier = Modifier.size(30.dp).padding(5.dp),
                    onClick = {
                        onEvent(LoginEvent.UpdateTerms(!state.isAcceptedTerms))
                    }
                ){
                    Icon(
                        painter = painterResource(LocalDrawable.current.uncheckIcon),
                        contentDescription = null,
                        tint = LocalColor.current.themePrimary
                    )
                    AnimatedVisibility(
                        visible = state.isAcceptedTerms,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ){
                        Icon(
                            painter = painterResource(LocalDrawable.current.checkIcon),
                            contentDescription = null,
                            tint = LocalColor.current.themePrimary
                        )
                    }
                }
                ClickableText(
                    text = clickableText,
                    onClick = {
                        clickableText.getStringAnnotations(
                            tag = "UserAgreement",
                            start = it,
                            end = it
                        ).firstOrNull()?.let { _ ->
                            // 用户协议
                            onEvent(LoginEvent.UserAgreement)
                        }
                        clickableText.getStringAnnotations(
                            tag = "PrivacyAgreement",
                            start = it,
                            end = it
                        ).firstOrNull()?.let { _ ->
                            // 隐私政策
                            onEvent(LoginEvent.PrivacyPolicy)
                        }
                    },
                    style = TextStyle.Default.copy(
                        color = LocalColor.current.textPrimary,
                        fontSize = 12.sp
                    )
                )
            }
        }
        item("logo"){
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