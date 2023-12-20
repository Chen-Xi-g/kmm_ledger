package core.mappers

import core.data.dto.BillListDto
import core.data.net.ResNet
import core.domain.entity.BillDetailListEntity
import core.domain.entity.BillListEntity
import core.utils.toFriendlyTime
import core.utils.toHourMinute
import platform.format

/**
 * 将账单列表的数据传输对象转换为页面数据
 */
fun ResNet<List<BillListDto>>.toBillListVo(): ResNet<List<BillListEntity>> = mapData(
    data?.map {
        BillListEntity(
            createTime = it.createTime.toFriendlyTime(),
            expenditure = it.expenditure.toYuan(),
            income = it.income.toYuan(),
            children = it.children.map { child ->
                BillDetailListEntity(
                    billId = child.billId,
                    cover = if (child.imgIds.isNotEmpty()) child.imgIds.split(",")[0] else "",
                    billName = child.billName,
                    billRemark = child.remark,
                    billAmount = child.billAmount.toYuan(),
                    createTime = child.createTime.toHourMinute(),
                    isIncome = child.userPayTypeDto.typeTag == "1"
                )
            },
            isExpanded = false
        )
    }
)

/**
 * 将Long类型单位为分的金额转换为元, 并保留两位小数
 */
fun Long.toYuan(): String {
    return "%.2f".format(this / 100.0)
}