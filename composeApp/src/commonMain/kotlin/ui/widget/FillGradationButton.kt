package ui.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.LocalColor

/**
 * 宽度填充的渐变按钮, 携带点击缩放效果
 */
@Composable
fun FillGradationButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var selected by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (selected) 0.95f else 1f
    )
    Button(
        modifier = modifier
            .scale(scale.value)
            .pointerInput("scale") {
                // 持续监听指针事件
                while (true) {
                    awaitPointerEventScope {
                        awaitFirstDown(false)
                        selected = true
                        waitForUpOrCancellation()
                        selected = false
                    }
                }
            },
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        elevation = null,
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .gradationBrush(CircleShape)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 宽度填充的渐变菜单按钮, 携带点击缩放效果
 */
@Composable
fun FillGradationMenuButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var selected by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (selected) 0.95f else 1f
    )
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 60.dp)
            .scale(scale.value)
            .gradationBrush(RoundedCornerShape(6.dp))
            .padding(vertical = 5.dp)
            .pointerInput(text) {
                // 持续监听指针事件
                while (true) {
                    awaitPointerEventScope {
                        awaitFirstDown(false)
                        selected = true
                        waitForUpOrCancellation()
                        selected = false
                    }
                }
            }.clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Modifier.gradationBrush(shape: Shape) = this.background(
    brush = Brush.horizontalGradient(
        colors = listOf(
            LocalColor.current.themeSecondary,
            LocalColor.current.themePrimary
        )
    ), shape = shape
)