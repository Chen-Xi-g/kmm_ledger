package core.data.repository

import core.data.api.NetApi
import core.data.dto.BillListDto
import core.data.net.ResNet
import core.data.net.get
import core.domain.entity.BillListEntity
import core.domain.repository.BillRepository
import core.mappers.toBillListVo

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
}