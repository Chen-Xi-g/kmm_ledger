package core.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * 获取当前时间的LocalDateTime
 */
fun currentLocalDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.UTC)
}

/**
 * 获取当前时间的LocalDate
 */
fun currentLocalDate(): LocalDate {
    return currentLocalDateTime().date
}

/**
 * LocalDateTime 转换为毫秒
 */
fun LocalDateTime.toEpochMilliseconds(): Long {
    return this.toInstant(TimeZone.UTC).toEpochMilliseconds()
}

/**
 * LocalDate 转换为毫秒
 */
fun LocalDate.toEpochMilliseconds(): Long {
    return this.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
}

/**
 * 将毫秒转换为LocalDateTime
 */
fun Long.toLocalDate(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC)
}

/**
 * 将yyyy-MM-dd HH:mm:ss转换为LocalDateTime
 */
fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this.replace(" ","T"))
}

/**
 * 计算这个月有多少天
 */
fun LocalDate.monthDays(): Int {
    return when (this.month) {
        Month.FEBRUARY -> if (this.year.isLeapYear()) 29 else 28
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        else -> 31
    }
}

/**
 * 判断是否是闰年
 */
fun Int.isLeapYear(): Boolean {
    val yearLong: Long = this.toLong()
    return yearLong and 3 == 0L && (yearLong % 100 != 0L || yearLong % 400 == 0L)
}

/**
 * 将 yyyy-MM-dd HH:mm:ss 转换为友好的时间
 */
fun String.toFriendlyTime(): String {
    val now = Clock.System.now().toEpochMilliseconds()
    val localDate = LocalDate.parse(this)
    val time = localDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    // 转换为今天\昨天\前天
    return when (now - time) {
        in 0..(1000 * 60 * 60 * 24) -> "今天"
        in (1000 * 60 * 60 * 24)..(1000 * 60 * 60 * 24 * 2) -> "昨天"
        in (1000 * 60 * 60 * 24 * 2)..(1000 * 60 * 60 * 24 * 3) -> "前天"
        else -> this
    }
}

/**
 * 将 yyyy-MM-dd HH:mm:ss 转换为 HH:mm
 */
fun String.toHourMinute(): String {
    val localDateTime = this.replace(" ", "T").toLocalDateTime()
    return "${localDateTime.hour.addZero()}:${localDateTime.minute.addZero()}"
}

/**
 * LocalDate转换为 yyyy-MM-dd
 */
fun LocalDate.toDateString(): String {
    return "${this.year}-${this.monthNumber.addZero()}-${this.dayOfMonth.addZero()}"
}

/**
 * 小于10的数字前面加0
 */
fun Int.addZero(): String {
    return if (this < 10) "0$this" else this.toString()
}