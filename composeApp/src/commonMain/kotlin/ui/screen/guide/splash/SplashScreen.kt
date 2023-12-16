package ui.screen.guide.splash

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea
import ui.theme.LedgerTheme
import ui.theme.LocalColor
import ui.theme.LocalDrawable
import ui.widget.LogoTextWidget

/**
 * 启动页
 *
 * @author 高国峰
 * @date 2023/12/6-16:55
 */
@Composable
fun SplashScreen(
    component: SplashVM
) {
    // 倒计时
    val countDown = component.countDown.collectAsState()
    val rememberWindowInfo = rememberWindowInfo()
    when (rememberWindowInfo.screenWidthInfo) {
        WindowInfo.WindowType.Compact -> {
            // 小屏
            SplashScreenAuto(true, countDown.value, component::onEvent)
        }

        else -> {
            // 大屏
            SplashScreenAuto(false, countDown.value, component::onEvent)
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreenAuto(
    isCompact: Boolean,
    countDown: Int,
    onEvent: (SplashEvent) -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(if (isCompact) LocalDrawable.current.splashBg else LocalDrawable.current.splashBgLarge),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        LogoTextWidget(
            modifier = Modifier
                .padding(bottom = 100.dp)
                .align(
                    Alignment.BottomCenter
                )
        )
        Box(
            modifier = Modifier
                .safeArea()
                .padding(15.dp)
                .size(35.dp)
                .background(
                    color = LocalColor.current.surface.copy(alpha = 0.5f),
                    shape = CircleShape
                ).align(
                    Alignment.TopEnd
                ).clickable {
                    onEvent(SplashEvent.Skip)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = countDown.toString(),
                fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewSplashScreen(){
    LedgerTheme {
        SplashScreen(
            component = SplashVM(
                componentContext = DefaultComponentContext(LifecycleRegistry()),
                onNavigationToScreenGuide = {}
            )
        )
    }
}