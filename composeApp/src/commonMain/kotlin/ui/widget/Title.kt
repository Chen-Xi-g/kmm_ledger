package ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.theme.LocalColor
import ui.theme.LocalDrawable

/**
 * 自定义标题栏
 *
 * @param modifier 样式
 * @param title 标题
 * @param menu 菜单
 * @param onBack 返回
 * @param onMenu 菜单点击
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun LedgerTitle(
    modifier: Modifier = Modifier,
    title: String = "",
    menu: String? = null,
    onBack: () -> Unit = {},
    onMenu: () -> Unit = {}
){
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // 返回按钮
        IconButton(
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .padding(5.dp)
                .size(50.dp)
                .padding(15.dp),
            onClick = {
                onBack()
            }
        ) {
            Icon(
                painter = painterResource(LocalDrawable.current.backIcon),
                contentDescription = Res.strings.str_back,
                tint = LocalColor.current.backIcon
            )
        }
        // 标题
        Text(
            modifier = Modifier
                .align(alignment = Alignment.Center),
            text = title,
            fontSize = 16.sp,
            color = LocalColor.current.textTitle
        )
        // 菜单
        if (!menu.isNullOrBlank()){
            FillGradationMenuButton(
                modifier = Modifier
                    .align(alignment = Alignment.CenterEnd)
                    .padding(10.dp),
                text = menu,
                onClick = onMenu
            )
        }
        Divider(
            modifier = Modifier.fillMaxWidth()
                .height(0.5.dp)
                .align(alignment = Alignment.BottomCenter),
        )
    }
}