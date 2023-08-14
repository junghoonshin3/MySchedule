package kr.sjh.myschedule.components

import androidx.compose.animation.AnimatedVisibility
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
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
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
fun MyWeekCalendar(selectedDay: (LocalDate) -> Unit) {

    val currentDate = remember { LocalDate.now() }

    val startDate = remember { currentDate.minusDays(500) }

    val endDate = remember { currentDate.plusDays(500) }


    var isWeekMode by remember { mutableStateOf(true) }

    // recomposition시 LocalDate 유지
    var selectionWeekAndMonthly by rememberSaveable {
        mutableStateOf(currentDate)
    }

    val coroutineScope = rememberCoroutineScope()

    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = DayOfWeek.MONDAY
    )

    val monthlyState = rememberCalendarState(
        startMonth = startDate.yearMonth,
        endMonth = endDate.yearMonth,
        firstVisibleMonth = currentDate.yearMonth,
        firstDayOfWeek = DayOfWeek.MONDAY,

        )

    rememberFirstVisibleWeekAfterScroll(
        state,
        selectionWeekAndMonthly
    ) { date ->
        if (isWeekMode) {
            selectionWeekAndMonthly = date
            selectedDay(date)
        }
    }

    rememberFirstVisibleMonthAfterScroll(monthlyState, selectionWeekAndMonthly) { date ->
        if (!isWeekMode) {
            selectionWeekAndMonthly = date
            selectedDay(date)
        }

    }



    CalendarTitle(
        selectionWeekAndMonthly.yearMonth,
        modifier = Modifier.fillMaxWidth(),
        goToPrevious = {

        }, goToNext = {

        },
        onSwitchCalendarType = {
            isWeekMode = !isWeekMode
        })

    CalendarHeader(daysOfWeek(DayOfWeek.MONDAY))

    LaunchedEffect(key1 = isWeekMode) {
        if (isWeekMode) {
            state.scrollToWeek(selectionWeekAndMonthly)
        } else {
            monthlyState.scrollToMonth(selectionWeekAndMonthly.yearMonth)
        }
    }

    //월 달력
    AnimatedVisibility(visible = !isWeekMode) {
        HorizontalCalendar(state = monthlyState, dayContent = { day ->
            Day(
                day.date,
                isPastDay = currentDate > day.date, isSelected = selectionWeekAndMonthly == day.date
            ) { clicked ->
                if (selectionWeekAndMonthly != clicked) {
                    selectionWeekAndMonthly = clicked
                    selectedDay(clicked)
                }

            }
        })
    }

    //주 달력
    AnimatedVisibility(visible = isWeekMode) {


        WeekCalendar(state = state, userScrollEnabled = true, dayContent = { day ->
            Day(
                day.date,
                isPastDay = currentDate > day.date, isSelected = selectionWeekAndMonthly == day.date
            ) { clicked ->
                if (selectionWeekAndMonthly != clicked) {
                    selectionWeekAndMonthly = clicked
                    selectedDay(clicked)
                }

            }
        })
    }

}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
private fun Day(
    day: LocalDate,
    isPastDay: Boolean,
    isSelected: Boolean,
    onClick: (LocalDate) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(0xffF7F2FA))
            .clickable { onClick(day) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = dateFormatter.format(day),
                fontSize = 18.sp,
                color = if (isSelected) Color.Red else if (isPastDay) Color.LightGray else TextColor,
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