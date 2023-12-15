package ui.screen.guide

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea
import ui.theme.COLOR_DDD
import ui.theme.LocalColor
import ui.theme.LocalDrawable
import ui.widget.FillGradationButton

/**
 * 引导页
 *
 * @author 高国峰
 * @date 2023/12/6-16:55
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuideScreen(
    component: GuideVM
) {
    val state = rememberPagerState {
        3
    }
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Box(
            modifier = Modifier.safeArea()
        ) {
            HorizontalPager(
                state = state
            ) {
                if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
                    GuideItem(it)
                } else {
                    GuideItemLarge(it)
                }
            }

            // 指示器
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(state.pageCount) { iteration ->
                    val color =
                        if (state.currentPage == iteration) LocalColor.current.themePrimary else COLOR_DDD
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(10.dp)
                    )
                }
            }
            // 跳过
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp),
                visible = state.currentPage != 2,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    modifier = Modifier.clickable { component.onEvent(GuideEvent.ClickButton) },
                    text = Res.strings.str_skip,
                    color = LocalColor.current.textSecondary,
                    fontSize = 14.sp
                )
            }

            // 进入App
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 54.dp),
                visible = state.currentPage == 2,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FillGradationButton(
                    modifier = Modifier.padding(horizontal = 40.dp),
                    text = Res.strings.str_enter
                ) {
                    component.onEvent(GuideEvent.ClickButton)
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun GuideItem(
    index: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 引导图
        Image(
            modifier = Modifier.height(310.dp),
            painter = painterResource(
                when (index) {
                    1 -> LocalDrawable.current.guide2
                    2 -> LocalDrawable.current.guide3
                    else -> LocalDrawable.current.guide1
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = when (index) {
                1 -> Res.strings.str_guide2
                2 -> Res.strings.str_guide3
                else -> Res.strings.str_guide1
            },
            color = LocalColor.current.themePrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(35.dp))
        Text(
            text = when (index) {
                1 -> Res.strings.str_guide2_content
                2 -> Res.strings.str_guide3_content
                else -> Res.strings.str_guide1_content
            },
            color = LocalColor.current.themePrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun GuideItemLarge(
    index: Int
) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 引导图
        Image(
            modifier = Modifier.height(310.dp),
            painter = painterResource(
                when (index) {
                    1 -> LocalDrawable.current.guide2
                    2 -> LocalDrawable.current.guide3
                    else -> LocalDrawable.current.guide1
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )
        Spacer(modifier = Modifier.width(60.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = when (index) {
                    1 -> Res.strings.str_guide2
                    2 -> Res.strings.str_guide3
                    else -> Res.strings.str_guide1
                },
                color = LocalColor.current.themePrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(35.dp))
            Text(
                text = when (index) {
                    1 -> Res.strings.str_guide2_content
                    2 -> Res.strings.str_guide3_content
                    else -> Res.strings.str_guide1_content
                },
                color = LocalColor.current.themePrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}