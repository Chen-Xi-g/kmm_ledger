package core.data.repository

import core.data.api.NetApi
import core.data.dto.BillListDto
import core.data.dto.ChangePayTypeSortDto
import core.data.dto.UserPayTypeDto
import core.data.net.ResNet
import core.data.net.delete
import core.data.net.get
import core.data.net.post
import core.domain.entity.BillListEntity
import core.domain.entity.PayTypeEntity
import core.domain.repository.BillRepository
import core.mappers.toBillListVo
import core.mappers.toPayTypeVo

/**
 * 账单存储库具体实现类
 *
 * @author 高国峰
 * @date 2023/12/16-22:51
 */
class BillRepositoryImpl : BillRepository {
    override suspend fun getBill(
        beginTime: String?,
        endTime: String?,
        billName: String?,
        accountType: String?,
        typeTag: String?
    ): ResNet<List<BillListEntity>> {
        val req = NetApi.BillApi.GetBill(
            beginTime = beginTime,
            endTime = endTime,
            billName = billName,
            accountType = accountType,
            typeTag = typeTag
        )
        return get<List<BillListDto>>(req.url){
            putAll(req.toMap())
        }.toBillListVo()
    }

    override suspend fun addBill(
        billName: String,
        billAmount: Long,
        typeId: Long,
        accountId: Long?,
        remark: String?,
        billId: String?
    ): ResNet<String> {
        val req = NetApi.BillApi.AddBill(
            billName = billName,
            billAmount = billAmount.toString(),
            typeId = typeId.toString(),
            accountId = accountId.toString(),
            remark = remark,
            billId = billId
        )
        return post(req.url, req.toMap())
    }

    override suspend fun getPayTypeAndChild(
        typeTag: String,
        typeName: String?
    ): ResNet<List<PayTypeEntity>> {
        val req = NetApi.BillApi.GetPayTypeAndChild(
            typeTag = typeTag,
            typeName = typeName
        )
        return get<List<UserPayTypeDto>>(req.url){
            putAll(req.toMap())
        }.toPayTypeVo()
    }

    override suspend fun getPayType(
        typeTag: String,
        typeName: String?
    ): ResNet<List<PayTypeEntity>> {
        val req = NetApi.BillApi.GetPayType(
            typeTag = typeTag,
            typeName = typeName
        )
        return get<List<UserPayTypeDto>>(req.url){
            putAll(req.toMap())
        }.toPayTypeVo()
    }

    override suspend fun getPayTypeChild(parentId: Long): ResNet<List<PayTypeEntity>> {
        val req = NetApi.BillApi.GetPayTypeChild(
            parentId = parentId
        )
        return get<List<UserPayTypeDto>>(req.url).toPayTypeVo()
    }

    override suspend fun addPayType(
        typeName: String,
        typeTag: String,
        parentId: Long?
    ): ResNet<Long> {
        val req = NetApi.BillApi.AddOrEditPayType(
            typeName = typeName,
            typeTag = typeTag,
            parentId = parentId,
        )
        return post(req.url, req.toMap())
    }

    override suspend fun changePayTypeSort(
        request: ChangePayTypeSortDto
    ): ResNet<String> {
        val req = NetApi.BillApi.ChangePayTypeSort(request)
        return post(req.url, req.request)
    }

    override suspend fun removePayType(typeId: Long): ResNet<String> {
        val req = NetApi.BillApi.DeletePayType(typeId)
        return delete(req.url)
    }
}