package ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ui.theme.LocalColor

/**
 * 自定义提示Dialog
 *
 * @param title 标题
 * @param message 内容
 * @param positiveText 确定按钮文本
 * @param negativeText 取消按钮文本
 * @param subText 附加按钮文本
 * @param containerColor 对话框背景色
 * @param onPositiveClick 确定按钮点击事件
 * @param onNegativeClick 取消按钮点击事件
 * @param subClick 附加按钮点击事件
 * @param onDismiss 对话框消失事件
 */
@Composable
fun TipDialog(
    title: String? = "提示",
    message: String? = null,
    positiveText: String? = "确定",
    negativeText: String? = "取消",
    subText: String? = null,
    containerColor: Color = LocalColor.current.surface,
    contentColor: Color = LocalColor.current.onSurface,
    shape: Shape = RoundedCornerShape(8.dp),
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {},
    subClick: () -> Unit = {},
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = shape,
            color = containerColor,
            contentColor = contentColor
        ) {
            Column(modifier = Modifier.padding(top = 15.dp)) {
                if (!title.isNullOrEmpty()) {
                    DialogTitle(text = title)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (!message.isNullOrEmpty()) {
                    DialogContent(text = message)
                    Spacer(modifier = Modifier.height(5.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (!subText.isNullOrEmpty()){
                        DialogButton(text = subText, onClick = subClick)
                    }
                    if (!negativeText.isNullOrEmpty()) {
                        DialogButton(text = negativeText, onClick = onNegativeClick)
                    }
                    if (!positiveText.isNullOrEmpty()) {
                        DialogButton(text = positiveText, onClick = onPositiveClick)
                    }
                }
            }
        }
    }
}

/**
 * 自定义输入Dialog
 *
 * @param title 标题
 * @param message 内容
 * @param messageHint 内容提示
 * @param isError 是否是错误状态
 * @param validate 输入内容验证
 * @param validateError 输入内容验证错误提示
 * @param positiveText 确定按钮文本
 * @param negativeText 取消按钮文本
 * @param containerColor 对话框背景色
 * @param contentColor 对话框内容颜色
 * @param shape 对话框圆角
 * @param onPositiveClick 确定按钮点击事件
 * @param onNegativeClick 取消按钮点击事件
 * @param onDismiss 对话框消失事件
 */
@Composable
fun InputDialog(
    title: String? = "提示",
    message: String = "",
    messageHint: String = "",
    isError: Boolean = false,
    validate: (String) -> Unit = { },
    validateError: String? = null,
    positiveText: String? = "确定",
    negativeText: String? = "取消",
    containerColor: Color = LocalColor.current.surface,
    contentColor: Color = LocalColor.current.onSurface,
    shape: Shape = RoundedCornerShape(8.dp),
    onPositiveClick: (String) -> Unit = {},
    onNegativeClick: () -> Unit = {},
    onDismiss: () -> Unit
) {
    var text by rememberSaveable { mutableStateOf(message) }
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = shape,
            color = containerColor,
            contentColor = contentColor
        ) {
            Column(
                modifier = Modifier.padding(top = 15.dp)
            ) {
                if (!title.isNullOrEmpty()) {
                    DialogTitle(text = title)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                DialogInput(
                    text = text,
                    messageHint = messageHint,
                    isError = isError,
                    validate = validate,
                    validateError = validateError,
                    onValueChange = {
                        text = it
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (!negativeText.isNullOrEmpty()) {
                        DialogButton(text = negativeText, onClick = onNegativeClick)
                    }
                    if (!positiveText.isNullOrEmpty()) {
                        DialogButton(text = positiveText, onClick = {
                            onPositiveClick(text)
                        })
                    }
                }
            }
        }
    }
}

/**
 * Dialog 标题
 */
@Composable
private fun DialogTitle(text: String) {
    Text(
        text = text,
        color = LocalColor.current.textTitle,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth()
    )
}

/**
 * Dialog 内容
 */
@Composable
private fun DialogContent(text: String) {
    Text(
        text = text,
        color = LocalColor.current.textPrimary,
        fontSize = 14.sp,
        modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth()
    )
}

/**
 * Dialog 输入框
 *
 * @param text 输入框内容
 * @param messageHint 输入框提示
 * @param isError 是否是错误状态
 * @param validate 输入内容验证
 * @param validateError 输入内容验证错误提示
 * @param onValueChange 输入框内容改变事件
 */
@Composable
private fun DialogInput(
    text: String,
    messageHint: String,
    isError: Boolean,
    validate: (String) -> Unit,
    validateError: String? = null,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        value = text,
        onValueChange = {
            onValueChange(it)
            validate(it)
        },
        singleLine = true,
        isError = isError,
        supportingText = {
            if (isError && !validateError.isNullOrEmpty()) {
                Text(
                    text = validateError,
                    color = LocalColor.current.toastError,
                    fontSize = 10.sp
                )
            }
        },
        placeholder = {
            Text(
                text = messageHint,
                color = LocalColor.current.textHint,
                fontSize = 14.sp
            )
        },
        keyboardActions = KeyboardActions { validate(text) }
    )
}

/**
 * Dialog 按钮
 */
@Composable
private fun DialogButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick
    ) {
        Text(
            text = text,
            color = LocalColor.current.textPrimary,
            fontSize = 12.sp
        )
    }
}