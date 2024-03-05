package ui.screen.add.bill

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.mappers.toFen
import core.mappers.toYuan
import core.utils.Res
import platform.WindowInfo
import platform.rememberWindowInfo
import platform.safeArea
import ui.theme.LocalColor
import ui.widget.LedgerTitle

/**
 * 新增账单详情页 屏幕
 *
 * @author 高国峰
 * @date 2024/02/06-19:07
 */
@Composable
fun AddBillScreen(
    component: AddBillVM
) {
    val state by component.state.collectAsState()
    val windowInfo = rememberWindowInfo()
    Surface(
        modifier = Modifier.fillMaxSize()
            .safeArea()
    ) {
        AddBillContent(component, state, component::onEvent)
    }
}

/**
 * 竖屏，窄屏布局
 */
@Composable
private fun AddBillContent(
    vm: AddBillVM,
    state: AddBillState,
    onEvent: (AddBillEvent) -> Unit
) {
    Column {
        LedgerTitle(
            title = state.title,
            menu = Res.strings.str_save,
            onBack = {
                onEvent(AddBillEvent.Back)
            },
            onMenu = {
                onEvent(AddBillEvent.Save)
            }
        )
        LazyColumn {
            item("bill_name"){
                AddBillItem(
                    title = Res.strings.str_bill_name,
                    hint = Res.strings.str_bill_name_hint,
                    text = vm.billName,
                    onValueChange = {
                        vm.changeBillName(it)
                    }
                )
            }

            item("pay_type"){
                AddBillItem(
                    title = Res.strings.str_income_expenditure_type,
                    text = if (state.isIncome) Res.strings.str_income else Res.strings.str_expenditure,
                    isEdit = false,
                    isEnable = false,
                    isDivider = true
                )
            }

            item("bill_type"){
                AddBillItem(
                    title = Res.strings.str_type,
                    hint = Res.strings.str_type_hint,
                    text = state.payTypeEntity.typeName,
                    isEdit = false,
                    isSelect = true
                ){
                    onEvent(AddBillEvent.SelectedPayType)
                }
            }

            item("amount"){
                AddBillItem(
                    title = Res.strings.str_amount,
                    hint = Res.strings.str_amount_hint,
                    text = vm.amount,
                    isNumber = true,
                    onValueChange = {
                        // 只允许输入数字和小数点
                        val regex = Regex("[^\\d.]")
                        // 如果当前已经有小数点了，则不再允许输入小数点
                        if (it.endsWith(".")) {
                            if ('.' in vm.amount) {
                                // 判断是增加还是删除
                                if (it.length > vm.amount.length) {
                                    return@AddBillItem
                                }
                            }
                        }
                        // 如果小数点后面的位数超过2位，则不再允许输入
                        if (it.contains(".")) {
                            val split = it.split(".")
                            if (split.size == 2 && split[1].length > 2) {
                                return@AddBillItem
                            }
                            if (it.length == 1){
                                return@AddBillItem
                            }
                            // 小数点前最多输入10位
                            if (split[0].length > 10){
                                return@AddBillItem
                            }
                        }
                        // 判断输入是否合法
                        if (regex.containsMatchIn(it)) {
                            return@AddBillItem
                        }
                        vm.changeAmount(it)
                    }
                )
            }

            item("account"){
                AddBillItem(
                    title = Res.strings.str_account,
                    hint = Res.strings.str_account_hint,
                    text = state.account?.cardName ?: "",
                    isEdit = false,
                    isSelect = true
                ){
                    onEvent(AddBillEvent.SelectedAccount)
                }
            }

            item("remark"){
                AddBillItem(
                    title = Res.strings.str_remark,
                    hint = Res.strings.str_remark_hint,
                    text = vm.remark,
                    onValueChange = {
                        vm.changeRemark(it)
                    }
                )
            }
        }
    }
}

/**
 * 新增账单Item
 *
 * @param title 标题
 * @param text 文本
 * @param hint 提示
 * @param isEdit 是否可编辑
 * @param isDivider 是否显示分割线
 * @param isEnable 输入框是否可用
 * @param isSelect 是否可选择
 * @param isNumber 是否是数字
 * @param onValueChange 输入框内容变化
 * @param onClick 点击事件
 */
@Composable
private fun AddBillItem(
    title: String = "",
    text: String = "",
    hint: String = "",
    isEdit: Boolean = true,
    isDivider: Boolean = true,
    isEnable: Boolean = true,
    isSelect: Boolean = false,
    isNumber: Boolean = false,
    onValueChange: (String) -> Unit = {},
    onClick: () -> Unit = {}
){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = if (isEdit) 0.dp else 15.dp)
            .clickable {
                if (!isEdit){
                    onClick()
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = LocalColor.current.textSecondary
        )
        if (isEdit){
            BasicTextField(
                modifier = Modifier.weight(1f),
                value = text,
                onValueChange = onValueChange,
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .padding(vertical = 15.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = hint,
                                fontSize = 14.sp,
                                color = LocalColor.current.textHint
                            )
                        }
                        innerTextField()  //<-- Add this
                    }
                },
                cursorBrush = SolidColor(LocalColor.current.themePrimary),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = LocalColor.current.textPrimary,
                    textAlign = TextAlign.End
                ),
                enabled = isEnable,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (!isNumber) {
                        KeyboardType.Text
                    } else {
                        KeyboardType.Number
                    }
                )
            )
            Spacer(modifier = Modifier.width(20.dp))
        }else{
            Box(
                Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = text.ifBlank { hint },
                    fontSize = 14.sp,
                    color = if (isSelect) {
                        if (text.isEmpty()) LocalColor.current.textHint else LocalColor.current.textPrimary
                    } else {
                        if (isEnable) LocalColor.current.textPrimary else LocalColor.current.textHint
                    }
                )
            }
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = LocalColor.current.textHint
            )
        }
    }
    if (isDivider) {
        Divider(
            modifier = Modifier.height(1.dp),
            color = LocalColor.current.divider
        )
    }
}

/**
 * 横屏，宽屏布局
 */
@Composable
private fun AddBillContentLarge(
    state: AddBillState,
    onEvent: (AddBillEvent) -> Unit
) {

}