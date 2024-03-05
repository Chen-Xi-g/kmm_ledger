package core.domain.entity

import core.data.dto.UserAccountDto
import core.data.dto.UserPayTypeDto
import core.utils.currentLocalDate
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 账单列表的页面数据
 *
 * @property createTime 账单创建时间
 * @property expenditure 总支出
 * @property income 总收入
 * @property children 账单详情列表
 * @property isExpanded 是否展开
 */
@Serializable
data class BillListEntity(
    val createTime: String = "",
    val expenditure: String = "",
    val income: String = "",
    val children: List<BillDetailListEntity> = listOf(),
    val isExpanded: Boolean = false
)

/**
 * 账单详情列表的页面数据
 *
 * @property billId 账单id
 * @property cover 封面
 * @property billName 账单名称
 * @property billRemark 账单备注
 * @property billAmount 账单金额
 * @property createTime 创建时间
 * @property isIncome 是否是收入
 */
@Serializable
data class BillDetailListEntity(
    val billId: Long = 0L,
    val cover: String = "",
    val billName: String = "",
    val billRemark: String = "",
    val billAmount: String = "",
    val billTypeName: String = "",
    val createTime: String = "",
    val isIncome: Boolean = false,
    val userAccountDto: UserAccountDto = UserAccountDto(),
    val userPayTypeDto: UserPayTypeDto = UserPayTypeDto()
)

/**
 * 收支类型
 *
 * @property type null：全部，0：支出，1：收入
 * @property typeName 类型名称
 */
@Serializable
data class TypeEntity(
    val type: Long? = null,
    val typeName: String = ""
)

/**
 * 消费类型
 *
 * @property typeId 消费类型id
 * @property parentId 父级id
 * @property typeName 类型名称
 */
@Serializable
data class PayTypeEntity(
    val typeId: Long = 0L,
    val parentId: Long = 0L,
    val typeName: String = "",
    var child: List<PayTypeEntity> = emptyList()
)

/**
 * 收支日期
 *
 * @property date 日期
 * @property type 0：年，1：月，2：日
 */
@Serializable
data class DateEntity(
    val type: Int = 1,
    val date: LocalDate = currentLocalDate()
)

/**
 * 收支账户
 *
 * @property type null：全部，0：电子账户，1：储蓄账户
 * @property name 名称
 */
@Serializable
data class AccountEntity(
    val type: Int? = null,
    val name: String = ""
)

/**
 * 跳转到新增账单页所需参数
 *
 * @property billAmount 账单金额
 * @property createTime 创建时间
 * @property payTypeEntity 消费类型
 * @property isIncome 是否是收入(true:收入, false:支出)
 * @property billId 账单id(修改时传入)
 * @property address 地址
 * @property latitude 纬度
 * @property longitude 经度
 * @property remark 备注
 * @property isAdd 是否是新增账单
 */
@Serializable
data class BillDetailEntity(
    val billAmount: Long = 0,
    val billName: String = "",
    val createTime: LocalDate = currentLocalDate(),
    val payTypeEntity: PayTypeEntity = PayTypeEntity(),
    val accountEntity: UserAccountDto? = null,
    val isIncome: Boolean = false,
    val billId: Long? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val remark: String? = null,
    val isAdd: Boolean = true,
)
