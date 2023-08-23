package kr.sjh.myschedule.components

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kr.sjh.myschedule.ui.theme.*
import kr.sjh.myschedule.utill.displayText
import kr.sjh.myschedule.utill.rememberFirstVisibleMonthAfterScroll
import kr.sjh.myschedule.utill.rememberFirstVisibleWeekAfterScroll
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun MyWeekCalendar(
    modifier: Modifier,
    scheduleMap: Map<Month, List<ScheduleEntity>>,
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
        modifier = modifier
            .fillMaxWidth(),
        onSwitchCalendarType = {
            isWeekMode = !isWeekMode
            coroutineScope.launch {
                if (isWeekMode) {
                    weekState.animateScrollToWeek(selectedDate)
                } else {
                    monthState.animateScrollToMonth(selectedDate.yearMonth)
                }
            }
        }, isWeekMode = isWeekMode
    )

    CalendarHeader(modifier, daysOfWeek(DayOfWeek.MONDAY))

    CalendarContent(
        modifier = modifier,
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
    modifier: Modifier,
    day: LocalDate,
    isPastDay: Boolean,
    isSelected: Boolean,
    inDate: Boolean = true,
    isSchedule: Boolean = false,
    onClick: (LocalDate) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(enabled = inDate) {
                onClick(day)
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = modifier
                .width(50.dp)
                .height(50.dp)
                .background(
                    color = if (isSelected) PaleRobinEggBlue else SoftBlue,
                    shape = CircleShape
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = dateFormatter.format(day),
                fontSize = 18.sp,
                color = if (isSelected) Crayola else if (isPastDay || !inDate) Color.LightGray else FontColorNomal,
                fontWeight = FontWeight.Bold,
            )
        }
        if (isSchedule) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(5.dp)
                    .background(
                        color = VanillaIce,
                        shape = CircleShape
                    )
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun CalendarContent(
    modifier: Modifier,
    scheduleMap: Map<Month, List<ScheduleEntity>>,
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

    AnimatedContent(modifier = modifier, targetState = isWeekMode,
        transitionSpec = {
            slideInVertically(
                initialOffsetY = { 0 },
                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
            ).togetherWith(
                slideOutVertically(
                    targetOffsetY = { 0 },
                    animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
                )
            )
        }
    ) { weekMode ->
        if (weekMode) {
            WeekCalendar(
                state = weekState, userScrollEnabled = true,
                dayContent = { day ->
                    var monthSchedule =
                        scheduleMap[day.date.month]?.groupBy { it.regDt } ?: emptyMap()
                    Day(
                        modifier = Modifier.background(SoftBlue),
                        day = day.date,
                        isPastDay = currentDate > day.date, isSelected = selectedDate == day.date,
                        isSchedule = monthSchedule[day.date]?.isNotEmpty() ?: false
                    ) { clicked ->
                        onSelectedDate(clicked)
                    }
                },
            )
        } else {
            HorizontalCalendar(state = monthState, dayContent = { day ->
                var monthSchedule =
                    scheduleMap[day.date.month]?.groupBy { it.regDt } ?: emptyMap()
                Day(
                    modifier = Modifier.background(SoftBlue),
                    day = day.date,
                    isPastDay = currentDate > day.date, isSelected = selectedDate == day.date,
                    inDate = day.position == DayPosition.MonthDate,
                    isSchedule = monthSchedule[day.date]?.isNotEmpty() ?: false
                ) { clicked ->
                    if (selectedDate != clicked) {
                        onSelectedDate(day.date)
                    }
                }
            })
        }
    }
}

@Composable
fun CalendarTitle(
    currentMonth: YearMonth,
    isWeekMode: Boolean,
    modifier: Modifier,
    onSwitchCalendarType: () -> Unit
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .padding(start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
//        Icon(
//            imageVector = Icons.Default.ChevronLeft,
//            contentDescription = "left",
//            modifier = Modifier
//                .width(40.dp)
//                .height(40.dp)
//                .clickable {
//                    goToPrevious()
//                })
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            text = currentMonth.displayText(),
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Medium,
            color = FontColorNomal
        )
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = "calendarMonth",
            tint = if (isWeekMode) Color.White else Crayola,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .padding(end = 10.dp)
                .clickable { onSwitchCalendarType() })
    }
}

@Composable
fun CalendarHeader(modifier: Modifier, daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = FontColorNomal,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}