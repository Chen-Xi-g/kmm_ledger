package ui.widget

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ui.theme.LocalColor

/**
 * 自定义Tab指示器
 *
 * @author 高国峰
 * @date 2023/12/29-18:40
 *
 * @param list Tab列表
 * @param pagerState PagerState
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoundedIndicator(
    list: List<TabPosition>,
    pagerState: PagerState
) {
    val transition = updateTransition(
        targetState = pagerState.currentPage
    )

    val indicatorStart by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 50f)
            } else {
                spring(dampingRatio = 1f, stiffness = 500f)
            }
        }, label = ""
    ) { page ->
        // 避免索引越界
        if (page >= list.size) {
            return@animateDp 0.dp
        }
        list[page].left
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 500f)
            } else {
                spring(dampingRatio = 1f, stiffness = 50f)
            }
        }, label = ""
    ) { page ->
        // 避免索引越界
        if (page >= list.size) {
            return@animateDp 0.dp
        }
        list[page].right
    }

    Box(
        Modifier
            .offset(x = indicatorStart)
            .wrapContentSize(align = Alignment.BottomStart)
            .width(indicatorEnd - indicatorStart)
            .fillMaxSize()
            .background(color = LocalColor.current.selectedThemePrimary)
            .zIndex(1f)
    ){
        Divider(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.BottomCenter),
            color = LocalColor.current.themePrimary
        )
    }
}