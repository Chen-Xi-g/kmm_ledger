package ui.screen.add.bill

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import core.data.dto.UserAccountDto
import core.data.net.ResNet
import core.data.repository.BillRepositoryImpl
import core.domain.entity.BillDetailEntity
import core.domain.entity.PayTypeEntity
import core.domain.repository.BillRepository
import core.mappers.toFen
import core.mappers.toYuan
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import core.utils.Res
import kotlinx.coroutines.launch

/**
 * 新增账单详情页 VM
 *
 * @author 高国峰
 * @date 2024/02/06-19:07
 */
class AddBillVM(
    componentContext: ComponentContext,
    private val billDetail: BillDetailEntity,
    private val rootComponent: IRootComponent,
    private val billRepository: BillRepository = BillRepositoryImpl()
) : BaseComponent<AddBillState, AddBillEvent, AddBillEffect>(componentContext) {

    var amount by mutableStateOf("")
        private set

    var remark by mutableStateOf(billDetail.remark ?: "")
        private set

    var billName by mutableStateOf("")
        private set

    override fun initialState(): AddBillState {
        amount = if (billDetail.billAmount == 0L) {
            ""
        } else {
            billDetail.billAmount.toYuan()
        }
        return AddBillState(
            title = if (billDetail.isAdd) Res.strings.str_new_bill else Res.strings.str_bill_detail,
            billAmount = billDetail.billAmount,
            createTime = billDetail.createTime,
            payTypeEntity = billDetail.payTypeEntity,
            isIncome = billDetail.isIncome,
            billId = billDetail.billId,
            address = billDetail.address,
            latitude = billDetail.latitude,
            longitude = billDetail.longitude
        )
    }

    override fun onEvent(event: AddBillEvent) {
        when (event) {
            AddBillEvent.Back -> {
                rootComponent.onBack()
            }

            AddBillEvent.SelectedPayType -> {
                rootComponent.onNavigationToScreenPayTypeManager(true)
            }

            AddBillEvent.SelectedAccount -> {
                rootComponent.onNavigationToScreenAccountManager(true)
            }

            AddBillEvent.Save -> {
                saveBill()
            }
        }
    }

    private fun saveBill() {
        val state = state.value
        val errorMsg = if(billName.isBlank()){
            "请输入账单名称"
        } else if (billName.length > 15){
            "账单名称不能超过15个字符"
        }else if (state.payTypeEntity.typeId == 0L || state.payTypeEntity.typeName.isBlank()){
            "请选择分类"
        }else if (amount.isBlank()){
            "请输入金额"
        }else if (amount.toDoubleOrNull() == 0.0){
            "金额不能为0"
        }else{
            ""
        }
        if (errorMsg.isNotBlank()){
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
                billName = billName,
                billAmount = amount.toFen(),
                typeId = state.payTypeEntity.typeId,
                accountId = state.account?.id,
                remark = remark
            )){
                is ResNet.Error -> {
                    updateState {
                        it.copy(
                            error = resp.msg,
                            isLoading = false
                        )
                    }
                    rootComponent.toastError(resp.msg)
                }
                is ResNet.Success -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastSuccess("保存成功")
                    rootComponent.onBack()
                }
            }
        }
    }

    /**
     * 修改金额
     */
    fun changeAmount(amount: String) {
        this.amount = amount
    }

    /**
     * 修改备注
     */
    fun changeRemark(remark: String) {
        this.remark = remark
    }

    /**
     * 修改账单名称
     */
    fun changeBillName(billName: String) {
        this.billName = billName
    }

    /**
     * 选择支付类型的回调
     */
    fun onSelectedPayType(item: PayTypeEntity, isIncome: Boolean) {
        updateState {
            it.copy(
                isIncome = isIncome,
                payTypeEntity = item
            )
        }
    }

    /**
     * 选择账户的回调
     */
    fun onSelectAccount(item: UserAccountDto) {
        updateState {
            it.copy(
                account = item
            )
        }
    }
}