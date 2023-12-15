package ui.widget

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.LocalColor

/**
 * 底部带下划线的输入框
 *
 * @param label 输入框标签
 * @param value 输入框值
 * @param modifier 输入框修饰符
 * @param type 0: 普通输入框 1: 密码输入框 2: 数字输入框
 * @param onValueChange 输入框值变化回调
 */
@Composable
fun BottomOutlineInput(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    type: Int = 0,
    onValueChange: (String) -> Unit
) {
    val usernameInteractionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (type == 1) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (type == 1) KeyboardType.Password else if (type == 2) KeyboardType.Number else KeyboardType.Ascii,
            imeAction = ImeAction.Next
        ),
        interactionSource = usernameInteractionSource,
        cursorBrush = SolidColor(LocalColor.current.themePrimary),
        textStyle = TextStyle.Default.copy(
            color = LocalColor.current.textPrimary,
            fontSize = 15.sp
        )
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()){
                Text(
                    text = label,
                    style = TextStyle.Default.copy(
                        color = LocalColor.current.textHint,
                        fontSize = 15.sp
                    )
                )
            }
            innerTextField()
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.BottomCenter),
                color = LocalColor.current.border
            )
        }
    }
}