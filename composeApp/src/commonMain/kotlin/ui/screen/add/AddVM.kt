package ui.screen.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import core.data.net.ResNet
import core.data.repository.BillRepositoryImpl
import core.domain.entity.BillDetailEntity
import core.domain.repository.BillRepository
import core.mappers.toFen
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.format

/**
 * 新增账单 VM
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
class AddVM(
    componentContext: ComponentContext,
    private val rootComponent: IRootComponent,
    private val billRepository: BillRepository = BillRepositoryImpl()
) : BaseComponent<AddState, AddEvent, AddEffect>(componentContext) {

    var keyboardContent by mutableStateOf("0")
        private set

    init {
        lifecycle.doOnResume {
            onEvent(AddEvent.GetPayType)
        }
    }

    override fun initialState(): AddState {
        return AddState()
    }

    override fun onEvent(event: AddEvent) {
        when (event) {
            AddEvent.GetPayType -> {
                // 获取消费类型
                getPayType()
            }

            AddEvent.SwitchKeyboard -> {
                // 切换键盘
                updateState {
                    it.copy(
                        isKeyboardShow = !it.isKeyboardShow
                    )
                }
            }

            AddEvent.DeleteKeyboardContent -> {
                // 删除键盘内容
                deleteKeyboardContent()
            }

            AddEvent.KeyboardNext -> {
                // 键盘下一步
                next()
            }

            is AddEvent.SwitchIncome -> {
                // 切换收入支出
                updateState {
                    it.copy(
                        isIncome = event.isIncome
                    )
                }
                getPayType()
            }

            is AddEvent.SwitchPayType -> {
                // 切换消费类型
                updateState {
                    it.copy(
                        currentPayType = event.index,
                        currentPayTypeChild = 0
                    )
                }
            }

            is AddEvent.SwitchPayTypeChild -> {
                // 切换消费类型子列表
                updateState {
                    it.copy(
                        currentPayTypeChild = event.index
                    )
                }
            }

            is AddEvent.ChangeKeyboardContent -> {
                // 修改键盘输入内容
                changeKeyboardContent(event.content)
            }

            is AddEvent.ChangeDate -> {
                // 修改账单日期
                updateState {
                    it.copy(
                        visibleDateTimePickerShow = event.visible,
                        dateTime = event.dateTime ?: state.value.dateTime
                    )
                }
            }

            AddEvent.ToPayType -> {
                // 跳转到收支类型
                rootComponent.onNavigationToScreenPayTypeManager()
            }

            AddEvent.SaveBill -> {
                saveBill()
            }

            AddEvent.ToMoreInfo -> {
                updateState {
                    it.copy(
                        isSaveDialog = false
                    )
                }
                rootComponent.onNavigationToScreenAddBill(
                    billDetail = BillDetailEntity(
                        billAmount = (keyboardContent.toDouble() * 100).toLong(),
                        createTime = state.value.dateTime,
                        payTypeEntity = if (state.value.types[state.value.currentPayType].child.isEmpty())
                            state.value.types[state.value.currentPayType]
                        else
                            state.value.types[state.value.currentPayType].child[state.value.currentPayTypeChild]
                    )
                )
            }

            AddEvent.CancelSaveBill -> {
                updateState {
                    it.copy(
                        isSaveDialog = false
                    )
                }
            }
        }
    }

    /**
     * 下一步
     */
    private fun next() {
        // 判断输入内容最后一位是否是+-.，如果是，去掉
        if ((keyboardContent.lastOrNull() ?: '0') in "+-.") {
            keyboardContent = keyboardContent.dropLast(1)
        }
        var number = 0.0
        keyboardContent.split("+").forEach {
            if ("-" in it) {
                var minusNumberResult = 0.0
                // 如果是-号开头则为负数
                it.split("-").forEachIndexed { index, minusNumber ->
                    if (index == 0) {
                        minusNumberResult = minusNumber.toDoubleOrNull() ?: 0.0
                    } else {
                        minusNumberResult -= minusNumber.toDouble()
                    }
                }
                number += minusNumberResult
            } else {
                number += it.toDouble()
            }
        }
        keyboardContent = "%.2f".format(number)
        updateState {
            it.copy(
                isSaveDialog = true
            )
        }
    }

    /**
     * 修改键盘输入内容
     */
    private fun changeKeyboardContent(content: Char) {
        keyboardContent = when (content) {
            in '0'..'9' -> handleDigit(content)
            '+', '-' -> handleOperator(content)
            '.' -> handleDecimalPoint()
            else -> keyboardContent // 不处理其他字符
        }
    }

    /**
     * 处理数字
     */
    private fun handleDigit(digit: Char): String {
        keyboardContent.split("+", "-").lastOrNull()?.let {
            if ("." in it) {
                // 判断小数点后面的位数，最多两位
                if (it.split(".").last().length >= 2) {
                    return keyboardContent
                }
            }
        }
        // 如果当前输入是 "0"，直接替换为新的数字
        if (keyboardContent == "0") {
            return digit.toString()
        } else {
            // 否则将新的数字追加到当前输入后面
            return keyboardContent + digit
        }
    }

    /**
     * 处理运算符
     */
    private fun handleOperator(operator: Char): String {
        // 如果最后一位是小数点，去掉小数点
        if (keyboardContent.lastOrNull() == '.') {
            return keyboardContent.dropLast(1) + operator
        }
        // 如果当前输入的最后一个字符是运算符，替换为新的运算符
        return if ((keyboardContent.lastOrNull() ?: '0') in "+-") {
            keyboardContent.dropLast(1) + operator
        } else {
            // 否则，将新的运算符追加到当前输入后面
            keyboardContent + operator
        }
    }

    /**
     * 处理小数点
     */
    private fun handleDecimalPoint(): String {
        // 如果当前输入中已经有小数点，则不处理
        keyboardContent.split("+", "-").lastOrNull()?.let {
            if ('.' in it) {
                return keyboardContent
            }
        }
        // 如果当前输入的最后一个字符是运算符，则不处理
        if ((keyboardContent.lastOrNull() ?: '0') in "+-") {
            return keyboardContent
        }
        // 否则，在当前输入的末尾追加小数点
        return "$keyboardContent."
    }

    /**
     * 删除键盘输入内容
     */
    private fun deleteKeyboardContent() {
        if (keyboardContent.length == 1) {
            keyboardContent = "0"
            return
        }
        keyboardContent = keyboardContent.substring(0, keyboardContent.length - 1)
    }

    /**
     * 获取消费类型
     */
    private fun getPayType() {
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when (val resp =
                billRepository.getPayTypeAndChild(if (state.value.isIncome) "1" else "0")) {
                is core.data.net.ResNet.Success -> {
                    updateState {
                        it.copy(
                            types = resp.data ?: emptyList(),
                            currentPayType = 0,
                            currentPayTypeChild = 0
                        )
                    }
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                }

                is core.data.net.ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastError(resp.msg)
                }
            }
        }
    }

    private fun saveBill() {
        val state = state.value
        val payTypes = if (state.types[state.currentPayType].child.isEmpty())
            state.types[state.currentPayType]
        else
            state.types[state.currentPayType].child[state.currentPayTypeChild]

        val errorMsg = if (payTypes.typeId == 0L || payTypes.typeName.isBlank()){
            "请选择分类"
        }else if (keyboardContent.isBlank()){
            "请输入金额"
        }else if (keyboardContent.toDoubleOrNull() == 0.0){
            "金额不能为0"
        }else{
            ""
        }
        if (errorMsg.isNotBlank()){
            updateState {
                it.copy(
                    isSaveDialog = false
                )
            }
            rootComponent.toastError(errorMsg)
            return
        }
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when(val resp = billRepository.addBill(
                billName = "新增账单",
                billAmount = keyboardContent.toFen(),
                typeId = payTypes.typeId
            )){
                is ResNet.Error -> {
                    updateState {
                        it.copy(
                            isSaveDialog = false,
                            isLoading = false
                        )
                    }
                    rootComponent.toastError(resp.msg)
                }
                is ResNet.Success -> {
                    updateState {
                        it.copy(
                            isSaveDialog = false,
                            isKeyboardShow = false,
                            isLoading = false
                        )
                    }
                    rootComponent.toastSuccess("保存成功")
                }
            }
        }
    }


}