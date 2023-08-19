package kr.sjh.myschedule.components

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.ui.theme.TextColor
import kr.sjh.myschedule.utill.displayText
import kr.sjh.myschedule.utill.rememberFirstVisibleMonthAfterScroll
import kr.sjh.myschedule.utill.rememberFirstVisibleWeekAfterScroll
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun MyWeekCalendar(
    scheduleMap: Map<LocalDate, List<ScheduleEntity>>,
    selectedDate: LocalDate,
    onSelectedDate: (LocalDate) -> Unit
) {

    val currentDate = remember { LocalDate.now() }

    val startDate = remember { currentDate.minusDays(500) }

    val endDate = remember { currentDate.plusDays(500) }

    var isWeekMode by rememberSaveable { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    val weekState = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = DayOfWeek.MONDAY
    )

    val monthState = rememberCalendarState(
        startMonth = startDate.yearMonth,
        endMonth = endDate.yearMonth,
        firstVisibleMonth = currentDate.yearMonth,
        firstDayOfWeek = DayOfWeek.MONDAY,
    )

    CalendarTitle(
        selectedDate.yearMonth,
        modifier = Modifier.fillMaxWidth(),
        goToPrevious = {
            coroutineScope.launch {
                if (isWeekMode) {
                    weekState.animateScrollToWeek(selectedDate.minusWeeks(1))
                } else {
                    monthState.animateScrollToMonth(selectedDate.yearMonth.minusMonths(1))
                }
            }
        }, goToNext = {
            coroutineScope.launch {
                if (isWeekMode) {
                    weekState.animateScrollToWeek(selectedDate.plusWeeks(1))
                } else {
                    monthState.animateScrollToMonth(selectedDate.yearMonth.plusMonths(1))
                }
            }
        },
        onSwitchCalendarType = {
            isWeekMode = !isWeekMode
            coroutineScope.launch {
                if (isWeekMode) {
                    weekState.animateScrollToWeek(selectedDate)
                } else {
                    monthState.animateScrollToMonth(selectedDate.yearMonth)
                }
            }
        })

    CalendarHeader(daysOfWeek(DayOfWeek.MONDAY))

    CalendarContent(
        scheduleMap = scheduleMap,
        currentDate = currentDate,
        selectedDate = selectedDate,
        weekState = weekState,
        monthState = monthState,
        isWeekMode = isWeekMode,
        onSelectedDate = onSelectedDate
    )

}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
private fun Day(
    day: LocalDate,
    isPastDay: Boolean,
    isSelected: Boolean,
    inDate: Boolean = true,
    isSchedule: Boolean = false,
    onClick: (LocalDate) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(if (isSchedule) Color.Yellow else Color(0xffF7F2FA))
            .clickable(enabled = inDate) {
                onClick(day)
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = dateFormatter.format(day),
                fontSize = 18.sp,
                color = if (isSelected) Color.Red else if (isPastDay || !inDate) Color.LightGray else TextColor,
                fontWeight = FontWeight.Bold,
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Color.Red)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun CalendarContent(
    scheduleMap: Map<LocalDate, List<ScheduleEntity>>,
    currentDate: LocalDate,
    selectedDate: LocalDate,
    weekState: WeekCalendarState,
    monthState: CalendarState,
    isWeekMode: Boolean,
    onSelectedDate: (LocalDate) -> Unit
) {
    val week = rememberFirstVisibleWeekAfterScroll(weekState)
    val month = rememberFirstVisibleMonthAfterScroll(monthState)

    LaunchedEffect(key1 = week) {
        if (isWeekMode) {
            val date =
                if (week.days.contains(WeekDay(currentDate, WeekDayPosition.RangeDate))) {
                    if (currentDate < week.days[selectedDate.dayOfWeek.value - 1].date) {
                        week.days[selectedDate.dayOfWeek.value - 1].date
                    } else {
                        currentDate
                    }
                } else {
                    week.days[selectedDate.dayOfWeek.value - 1].date
                }
            onSelectedDate(date)
        }

    }

    LaunchedEffect(key1 = month) {
        if (!isWeekMode) {
            val date = if (month.yearMonth.isValidDay(selectedDate.dayOfMonth)) {
                when {
                    month.yearMonth.month == currentDate.month && month.yearMonth.atDay(selectedDate.dayOfMonth) <= currentDate ->
                        currentDate
                    month.yearMonth.month != selectedDate.month ->
                        month.yearMonth.atDay(selectedDate.dayOfMonth)
                    else ->
                        selectedDate
                }
            } else {
                monthState.firstVisibleMonth.yearMonth.atStartOfMonth()
            }
            Log.i("sjh", "month :${date}")
            onSelectedDate(date)
        }
    }

    AnimatedContent(targetState = isWeekMode,
        transitionSpec = {
            slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
            ).togetherWith(
                slideOutVertically(
                    targetOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
                )
            )
        }
    ) { weekMode ->
        if (weekMode) {
            WeekCalendar(state = weekState, userScrollEnabled = true, dayContent = { day ->
                Day(
                    day.date,
                    isPastDay = currentDate > day.date, isSelected = selectedDate == day.date,
                    isSchedule = scheduleMap[day.date]?.isNotEmpty() ?: false
                ) { clicked ->
                    onSelectedDate(clicked)
                }
            })
        } else {
            HorizontalCalendar(state = monthState, dayContent = { day ->
                Day(
                    day.date,
                    isPastDay = currentDate > day.date, isSelected = selectedDate == day.date,
                    inDate = day.position == DayPosition.MonthDate,
                    isSchedule = scheduleMap[day.date]?.isNotEmpty() ?: false
                ) { clicked ->
                    if (selectedDate != clicked) {
                        onSelectedDate(day.date)
                    }
                }
            })
        }
    }

    //주 달력
//    AnimatedVisibility(visible = isWeekMode) {
//
//    }

    //월 달력
//    AnimatedVisibility(visible = !isWeekMode) {
//
//    }


}

@Composable
fun CalendarTitle(
    currentMonth: YearMonth, modifier: Modifier, goToPrevious: () -> Unit,
    goToNext: () -> Unit, onSwitchCalendarType: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .padding(start = 10.dp, end = 10.dp)
            .clickable {
                onSwitchCalendarType()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.ChevronLeft,
            contentDescription = "left",
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .clickable {
                    goToPrevious()
                })
        Text(
            modifier = Modifier
                .weight(1f)
                .testTag("MonthTitle"),
            text = currentMonth.displayText(),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "right",
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .clickable {
                    goToNext()
                })
    }
}

@Composable
fun CalendarHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}