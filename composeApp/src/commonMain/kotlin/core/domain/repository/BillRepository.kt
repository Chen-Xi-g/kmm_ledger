package core.domain.repository

import core.data.dto.ChangePayTypeSortDto
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
     * @param accountType 消费类型Id null-全部，00-电子账户，01-储蓄账户
     * @param typeTag 消费类型标签 null-全部 0-支出 1-收入
     */
    suspend fun getBill(
        beginTime: String? = null,
        endTime: String? = null,
        billName: String? = null,
        accountType: String? = null,
        typeTag: String? = null
    ): ResNet<List<BillListEntity>>

    /**
     * 新增账单
     *
     * @param billName 账单名称
     * @param billAmount 账单金额
     * @param typeId 消费类型Id
     * @param accountId 是否是收入
     * @param remark 备注
     */
    suspend fun addBill(
        billName: String = "",
        billAmount: Long = 0,
        typeId: Long = 0,
        accountId: Long? = null,
        remark: String? = null,
        billId: String? = null
    ): ResNet<String>

    /**
     * 获取消费类型和子类型
     *
     * @param typeTag 类型标签（0代表支出 1代表收入）
     * @param typeName 类型名称
     */
    suspend fun getPayTypeAndChild(
        typeTag: String = "0",
        typeName: String? = null
    ): ResNet<List<PayTypeEntity>>

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
     * 获取消费类型子类型
     *
     * @param parentId 父级id
     */
    suspend fun getPayTypeChild(
        parentId: Long
    ): ResNet<List<PayTypeEntity>>

    /**
     * 添加或修改消费类型
     *
     * @param typeName 类型名称
     * @param typeTag 类型标签（0代表支出 1代表收入）
     * @param parentId 父级id
     */
    suspend fun addPayType(
        typeName: String,
        typeTag: String,
        parentId: Long? = null
    ): ResNet<Long>

    /**
     * 修改消费类型排序
     *
     * @param request 请求体
     */
    suspend fun changePayTypeSort(
        request: ChangePayTypeSortDto
    ): ResNet<String>

    /**
     * 删除消费类型
     *
     * @param typeId 消费类型id
     */
    suspend fun removePayType(
        typeId: Long
    ): ResNet<String>
}