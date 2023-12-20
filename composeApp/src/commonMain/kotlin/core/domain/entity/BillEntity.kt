package core.domain.entity

import core.utils.currentLocalDate
import kotlinx.datetime.LocalDate

/**
 * 账单列表的页面数据
 *
 * @property createTime 账单创建时间
 * @property expenditure 总支出
 * @property income 总收入
 * @property children 账单详情列表
 * @property isExpanded 是否展开
 */
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
data class BillDetailListEntity(
    val billId: Long = 0L,
    val cover: String = "",
    val billName: String = "",
    val billRemark: String = "",
    val billAmount: String = "",
    val createTime: String = "",
    val isIncome: Boolean = false
)

/**
 * 收支类型
 *
 * @property type null：全部，0：支出，1：收入
 * @property typeName 类型名称
 */
data class TypeEntity(
    val type: Long? = null,
    val typeName: String = ""
)

/**
 * 收支日期
 *
 * @property date 日期
 * @property type 0：年，1：月，2：日
 */
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
data class AccountEntity(
    val type: Int? = null,
    val name: String = ""
)