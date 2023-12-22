package core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 账单列表的传输对象
 *
 * @property createTime 创建时间
 * @property expenditure 总支出
 * @property income 总收入
 */
@Serializable
data class BillListDto(
    @SerialName("createTime")
    val createTime: String = "",
    @SerialName("expenditure")
    val expenditure: Long = 0,
    @SerialName("income")
    val income: Long = 0,
    @SerialName("children")
    val children: List<BillDetailDto> = listOf(),
)

/**
 * 账单详情的传输对象
 *
 * @property accountId 账户id
 * @property address 地址
 * @property billAmount 账单金额
 * @property billId 账单id
 * @property billName 账单名称
 * @property createTime 创建时间
 * @property imgIds 图片id
 * @property latitude 纬度
 * @property longitude 经度
 * @property remark 备注
 * @property typeId 消费类型id
 * @property userAccountDto 账户类型
 * @property userPayTypeDto 消费类型
 */
@Serializable
data class BillDetailDto(
    @SerialName("accountId")
    val accountId: Long = 0,
    @SerialName("address")
    val address: String = "",
    @SerialName("billAmount")
    val billAmount: Long = 0,
    @SerialName("billId")
    val billId: Long = 0,
    @SerialName("billName")
    val billName: String = "",
    @SerialName("createTime")
    val createTime: String = "",
    @SerialName("imgIds")
    val imgIds: String = "",
    @SerialName("latitude")
    val latitude: Double = 0.0,
    @SerialName("longitude")
    val longitude: Double = 0.0,
    @SerialName("remark")
    val remark: String = "",
    @SerialName("typeId")
    val typeId: Long = 0,
    @SerialName("userAccount")
    val userAccountDto: UserAccountDto = UserAccountDto(),
    @SerialName("userPayType")
    val userPayTypeDto: UserPayTypeDto = UserPayTypeDto()
)

/**
 * 账户类型
 *
 * @property balance 余额
 * @property cardCode 卡号
 * @property cardName 卡名称
 * @property id 账户id
 * @property remark 备注
 * @property type 账户类型（00 电子账户 01储蓄账户）
 */
@Serializable
data class UserAccountDto(
    @SerialName("balance")
    val balance: Long = 0,
    @SerialName("cardCode")
    val cardCode: String = "",
    @SerialName("cardName")
    val cardName: String = "",
    @SerialName("id")
    val id: Long = 0,
    @SerialName("remark")
    val remark: String = "",
    @SerialName("type")
    val type: String = ""
)

/**
 * 消费类型
 *
 * @property typeId 消费类型id
 * @property parentId 父类型id
 * @property typeName 消费类型名称
 * @property typeTag 消费类型标签
 */
@Serializable
data class UserPayTypeDto(
    @SerialName("typeId")
    val typeId: Long = 0,
    @SerialName("parentId")
    val parentId: Long = 0,
    @SerialName("typeName")
    val typeName: String = "",
    @SerialName("typeTag")
    val typeTag: String = ""
)