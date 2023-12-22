package core.data.repository

import core.data.api.NetApi
import core.data.dto.BillListDto
import core.data.dto.UserPayTypeDto
import core.data.net.ResNet
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
        typeId: String?,
        typeTag: String?
    ): ResNet<List<BillListEntity>> {
        val req = NetApi.BillApi.GetBill(
            beginTime = beginTime,
            endTime = endTime,
            billName = billName,
            typeId = typeId,
            typeTag = typeTag
        )
        return get<List<BillListDto>>(req.url){
            putAll(req.toMap())
        }.toBillListVo()
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

    override suspend fun getPayTypeChild(typeId: String): ResNet<List<PayTypeEntity>> {
        val req = NetApi.BillApi.GetPayTypeChild(
            typeId = typeId
        )
        return get<List<UserPayTypeDto>>(req.url).toPayTypeVo()
    }

    override suspend fun addPayType(
        typeName: String,
        typeTag: String,
        parentId: Long?,
        typeId: Long?
    ): ResNet<Long> {
        val req = NetApi.BillApi.AddOrEditPayType(
            typeName = typeName,
            typeTag = typeTag,
            parentId = parentId,
            typeId = typeId
        )
        return post(req.url, req.toMap())
    }
}