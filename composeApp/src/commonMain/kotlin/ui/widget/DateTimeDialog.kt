package ui.widget

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.utils.Res.strings.str_cancel
import core.utils.Res.strings.str_confirm
import core.utils.Res.strings.str_last_year
import core.utils.Res.strings.str_this_year
import core.utils.currentLocalDateTime
import core.utils.toEpochMilliseconds
import core.utils.toLocalDate
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import platform.log
import ui.theme.LocalColor

/**
 * 筛选年月日的选择器
 *
 * @param visible 是否展示
 * @param type 0：年，1：年月，2：年月日
 * @param date 当前年份
 * @param confirmClicked 确认按钮点击事件
 * @param cancelClicked 取消按钮点击事件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDateTimePicker(
    visible: Boolean,
    type: Int = 2,
    date: LocalDate,
    confirmClicked: (year: Int, month: Int, day: Int) -> Unit,
    cancelClicked: () -> Unit
) {
    when (type) {
        0 -> YearPickerDialog(
            visible = visible,
            dateTime = date,
            confirmClicked = confirmClicked,
            cancelClicked = cancelClicked
        )

        1 -> MonthPickerDialog(
            visible = visible,
            dateTime = date,
            confirmClicked = confirmClicked,
            cancelClicked = cancelClicked
        )

        2 -> {
            DayPickerDialog(
                visible = visible,
                dateTime = date,
                confirmClicked = confirmClicked,
                cancelClicked = cancelClicked
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun YearPickerDialog(
    visible: Boolean,
    dateTime: LocalDate,
    confirmClicked: (year: Int, month: Int, day: Int) -> Unit,
    cancelClicked: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val currentDateTime = currentLocalDateTime()
    // key = page, value = years
    val years = mutableListOf<List<Int>>()
    val pages = 5
    val count = 12
    // 2023 - 2012 = 11
    // 5 * 12 / 11 = 5
    for (page in pages downTo 1) {
        val list = mutableListOf<Int>()
        if (page == 1) {
            for (year in currentDateTime.year - (page * count) + 1..currentDateTime.year) {
                list.add(year)
            }
        }else {
            for (year in currentDateTime.year - (page * count) + 1 until (currentDateTime.year - page * count + count) + 1) {
                list.add(year)
            }
        }
        years.add(list)
    }

    var year by remember { mutableStateOf(dateTime.year) }
    val pageState = rememberPagerState(
        initialPage = (year - (currentDateTime.year - (pages * count)) / count).coerceIn(
            0,
            pages - 1
        ) + 1,
    ) {
        pages
    }

    if (visible) {
        AlertDialog(
            containerColor = LocalColor.current.surface,
            onDismissRequest = {},
            confirmButton = {
                Box(
                    modifier = Modifier
                        .clickable { confirmClicked(year, 1, 1) }
                        .gradationBrush(CircleShape)
                        .padding(horizontal = 27.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = str_confirm,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp, bottom = 10.dp)
                        .clickable { cancelClicked() }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    text = str_cancel,
                    color = LocalColor.current.textSecondary
                )
            },
            shape = RoundedCornerShape(16.dp),
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .clickable {
                                    scope.launch {
                                        if (pageState.currentPage == 0) {
                                            return@launch
                                        }
                                        pageState.animateScrollToPage(pageState.currentPage - 1)
                                    }
                                }.padding(10.dp),
                            text = str_last_year,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pageState.currentPage == 0) LocalColor.current.textPrimary.copy(alpha = 0.5f) else LocalColor.current.textPrimary
                        )
                        Text(
                            modifier = Modifier
                                .clickable {
                                    scope.launch {
                                        if (pageState.currentPage == pages - 1) {
                                            return@launch
                                        }
                                        pageState.animateScrollToPage(pageState.currentPage + 1)
                                    }
                                }.padding(10.dp),
                            text = str_this_year,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pageState.currentPage == pages - 1) LocalColor.current.textPrimary.copy(
                                alpha = 0.5f
                            ) else LocalColor.current.textPrimary
                        )
                    }
                    HorizontalPager(pageState) { page ->
                        Card(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    10.dp,
                                    Alignment.CenterHorizontally
                                ),
                                verticalArrangement = Arrangement.spacedBy(
                                    10.dp,
                                    Alignment.CenterVertically
                                ),
                                maxItemsInEachRow = 4
                            ) {
                                years[page].forEach { selectYear ->
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clickable {
                                                year = selectYear
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {

                                        val animatedSize by animateDpAsState(
                                            targetValue = if (year == selectYear) 50.dp else 20.dp,
                                            animationSpec = tween(
                                                durationMillis = 600,
                                                easing = LinearOutSlowInEasing
                                            )
                                        )

                                        Box(
                                            modifier = Modifier
                                                .size(animatedSize)
                                                .background(
                                                    color = if (year == selectYear) LocalColor.current.themePrimary else Color.Transparent,
                                                    shape = CircleShape
                                                )
                                        )

                                        Text(
                                            text = selectYear.toString(),
                                            color = if (year == selectYear) Color.White else LocalColor.current.textPrimary,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MonthPickerDialog(
    visible: Boolean,
    dateTime: LocalDate,
    confirmClicked: (year: Int, month: Int, day: Int) -> Unit,
    cancelClicked: () -> Unit
) {
    val currentDateTime = currentLocalDateTime()

    val monthsLock = listOf(
        "一月",
        "二月",
        "三月",
        "四月",
        "五月",
        "六月",
        "七月",
        "八月",
        "九月",
        "十月",
        "十一月",
        "十二月"
    )

    val months = mutableListOf<String>()
    for (i in 0 until currentDateTime.monthNumber) {
        months.add(monthsLock[i])
    }

    var month by remember { mutableStateOf(months[dateTime.monthNumber - 1]) }

    var year by remember { mutableStateOf(dateTime.year) }

    if (visible) {
        AlertDialog(
            containerColor = LocalColor.current.surface,
            onDismissRequest = {},
            confirmButton = {
                Box(
                    modifier = Modifier
                        .clickable { confirmClicked(year, months.indexOf(month) + 1, 1) }
                        .gradationBrush(CircleShape)
                        .padding(horizontal = 27.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = str_confirm,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                Text(
                    modifier = Modifier
                        .clickable { cancelClicked() }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    text = str_cancel,
                    color = LocalColor.current.textSecondary
                )
            },
            shape = RoundedCornerShape(16.dp),
            text = {
                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            modifier = Modifier.size(35.dp),
                            onClick = {
                                year--
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowLeft,
                                contentDescription = str_last_year,
                                tint = LocalColor.current.textPrimary
                            )
                        }

                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = "$year 年",
                            fontSize = 20.sp,
                            color = LocalColor.current.textTitle,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            modifier = Modifier.size(35.dp),
                            onClick = {
                                if (currentDateTime.year == year) {
                                    return@IconButton
                                }
                                year++
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowRight,
                                contentDescription = str_this_year,
                                tint = if (currentDateTime.year == year) LocalColor.current.textPrimary.copy(
                                    alpha = 0.5f
                                ) else LocalColor.current.textPrimary
                            )
                        }

                    }

                    Card(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                16.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalArrangement = Arrangement.spacedBy(
                                16.dp,
                                Alignment.CenterVertically
                            ),
                            maxItemsInEachRow = 3
                        ) {
                            months.forEach {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            month = it
                                        },
                                    contentAlignment = Alignment.Center
                                ) {

                                    val animatedSize by animateDpAsState(
                                        targetValue = if (it == month) 50.dp else 20.dp,
                                        animationSpec = tween(
                                            durationMillis = 600,
                                            easing = LinearOutSlowInEasing
                                        )
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(animatedSize)
                                            .background(
                                                color = if (it == month) LocalColor.current.themePrimary else Color.Transparent,
                                                shape = CircleShape
                                            )
                                    )

                                    Text(
                                        text = it,
                                        color = if (it == month) Color.White else LocalColor.current.textPrimary,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                    }
                }

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayPickerDialog(
    visible: Boolean,
    dateTime: LocalDate,
    confirmClicked: (year: Int, month: Int, day: Int) -> Unit,
    cancelClicked: () -> Unit
){
    if (visible){
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dateTime.toEpochMilliseconds()
        )
        DatePickerDialog(
            onDismissRequest = {},
            shape = RoundedCornerShape(16.dp),
            confirmButton = {
                Box(
                    modifier = Modifier
                        .padding(end = 15.dp, bottom = 15.dp)
                        .clickable {
                            datePickerState.selectedDateMillis?.let {
                                val localDate = it.toLocalDate()
                                confirmClicked(
                                    localDate.year,
                                    localDate.monthNumber,
                                    localDate.dayOfMonth
                                )
                            }
                        }
                        .gradationBrush(CircleShape)
                        .padding(horizontal = 27.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = str_confirm,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                Text(
                    modifier = Modifier
                        .clickable { cancelClicked() }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    text = str_cancel,
                    color = LocalColor.current.textSecondary
                )
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = LocalColor.current.surface,
                    selectedYearContainerColor = LocalColor.current.themePrimary,
                    selectedDayContainerColor = LocalColor.current.themePrimary
                ),
                showModeToggle = false,
                dateValidator = { timestamp ->
                    timestamp <= Clock.System.now().toEpochMilliseconds()
                }
            )
        }
    }
}