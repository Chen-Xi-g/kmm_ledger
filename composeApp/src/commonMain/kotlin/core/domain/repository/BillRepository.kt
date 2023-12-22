package core.domain.repository

import core.data.net.ResNet
import core.domain.entity.BillListEntity
import core.domain.entity.PayTypeEntity

/**
 * 账单相关存储库
 *
 * @author 高国峰
 * @date 2023/12/16-22:50
 */
interface BillRepository {
    /**
     * 获取账单列表
     *
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param endTime 结束时间 yyyy-MM-dd
     * @param billName 账单名称
     * @param typeId 消费类型Id null-全部，00-电子账户，01-储蓄账户
     * @param typeTag 消费类型标签 null-全部 0-支出 1-收入
     */
    suspend fun getBill(
        beginTime: String? = null,
        endTime: String? = null,
        billName: String? = null,
        typeId: String? = null,
        typeTag: String? = null
    ): ResNet<List<BillListEntity>>

    /**
     * 获取消费类型
     *
     * @param typeTag 类型标签（0代表支出 1代表收入）
     * @param typeName 类型名称
     */
    suspend fun getPayType(
        typeTag: String = "0",
        typeName: String? = null
    ): ResNet<List<PayTypeEntity>>

    /**
     * 获取消费类型子列表
     *
     * @param typeId 消费类型Id
     */
    suspend fun getPayTypeChild(typeId: String): ResNet<List<PayTypeEntity>>

    /**
     * 添加或修改消费类型
     *
     * @param typeName 类型名称
     * @param typeTag 类型标签（0代表支出 1代表收入）
     * @param parentId 父级id
     * @param typeId 消费类型Id
     */
    suspend fun addPayType(
        typeName: String,
        typeTag: String,
        parentId: Long? = null,
        typeId: Long? = null
    ): ResNet<Long>
}