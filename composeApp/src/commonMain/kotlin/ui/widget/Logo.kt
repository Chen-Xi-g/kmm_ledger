package ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.theme.LocalDrawable

/**
 * 自定义LOGO文字组件
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun LogoTextWidget(
    modifier: Modifier = Modifier,
    text: String = "忞鹿记账",
    fontSize: TextUnit = 24.sp,
    color: Color = Color.White,
    logoSize: Dp = 40.dp
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Image(
            modifier = Modifier.size(logoSize),
            painter = painterResource(LocalDrawable.current.logo),
            contentDescription = Res.strings.str_logo
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = fontSize,
            color = color
        )
    }
}