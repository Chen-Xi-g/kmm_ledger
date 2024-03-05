package core.mappers

import core.data.dto.BillListDto
import core.data.dto.UserPayTypeDto
import core.data.net.ResNet
import core.domain.entity.BillDetailListEntity
import core.domain.entity.BillListEntity
import core.domain.entity.PayTypeEntity
import core.domain.entity.TypeEntity
import core.utils.toFriendlyTime
import core.utils.toHourMinute
import platform.format

/**
 * 将账单列表的数据传输对象转换为页面数据
 */
fun ResNet<List<BillListDto>>.toBillListVo(): ResNet<List<BillListEntity>> = mapData(
    data?.mapIndexed { index, billListDto ->
        BillListEntity(
            createTime = billListDto.createTime.toFriendlyTime(),
            expenditure = billListDto.expenditure.toYuan(),
            income = billListDto.income.toYuan(),
            children = billListDto.children.map { child ->
                BillDetailListEntity(
                    billId = child.billId,
                    cover = if (child.imgIds.isNotEmpty()) child.imgIds.split(",")[0] else "",
                    billName = child.billName,
                    billRemark = child.remark,
                    billAmount = child.billAmount.toYuan(),
                    billTypeName = child.userPayTypeDto.typeName.chunked(2).joinToString("\n"),
                    createTime = child.createTime,
                    isIncome = child.userPayTypeDto.typeTag == "1",
                    userAccountDto = child.userAccountDto,
                    userPayTypeDto = child.userPayTypeDto
                )
            },
            isExpanded = index < 3
        )
    }
)

/**
 * 将Long类型单位为分的金额转换为元, 并保留两位小数
 */
fun Long.toYuan(): String {
    return "%.2f".format(this / 100.0)
}

/**
 * 将String类型单位为元的金额转换为Long, 并将小数点去掉
 */
fun String.toFen(): Long {
    return ((this.toDoubleOrNull() ?: 0.0) * 100).toLong()
}

/**
 * 消费类型传输对象转换为页面数据
 */
fun ResNet<List<UserPayTypeDto>>.toPayTypeVo(): ResNet<List<PayTypeEntity>> = mapData(
    data?.map {
        PayTypeEntity(
            typeId = it.typeId,
            parentId = it.parentId,
            typeName = it.typeName,
            child = it.child.map { child ->
                PayTypeEntity(
                    typeId = child.typeId,
                    parentId = child.parentId,
                    typeName = child.typeName
                )
            }
        )
    }
)