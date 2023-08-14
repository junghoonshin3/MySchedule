package kr.sjh.myschedule.utill

import android.util.Log
import androidx.compose.foundation.MutatePriority
import androidx.compose.runtime.*
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.flow.filter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.KOREAN)
}

fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    val lastDate = week.days.last().date
    return when {
        firstDate.yearMonth == lastDate.yearMonth -> {
            firstDate.yearMonth.displayText()
        }
        firstDate.year == lastDate.year -> {
            "${firstDate.month.displayText(short = false)} - ${lastDate.yearMonth.displayText()}"
        }
        else -> {
            "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
        }
    }
}

@Composable
fun rememberFirstVisibleWeekAfterScroll(
    state: WeekCalendarState,
    selectedDate: LocalDate,
    scrollListener: (LocalDate) -> Unit
): Week {
    val visibleWeek = remember(state) { mutableStateOf(state.firstVisibleWeek) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect {
                visibleWeek.value = state.firstVisibleWeek
                //현재 날짜가 스크롤한 주에 포함되어있는지
                if (!state.firstVisibleWeek.days.contains(
                        WeekDay(selectedDate, WeekDayPosition.RangeDate)
                    )
                ) {
                    //보여줄 날짜 리스트
                    val visibleWeekList = state.firstVisibleWeek.days
                    scrollListener.invoke(visibleWeekList.first().date)
                } else {
                    //현재 날짜
                    scrollListener.invoke(selectedDate)
                }
            }
    }
    return visibleWeek.value
}

@Composable
fun rememberFirstVisibleMonthAfterScroll(
    state: CalendarState,
    selectedDate: LocalDate,
    onMonthlyListener: (LocalDate) -> Unit
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect {
                if (selectedDate.month != state.firstVisibleMonth.yearMonth.month) {
                    onMonthlyListener(state.firstVisibleMonth.yearMonth.atStartOfMonth())
                } else {
                    onMonthlyListener(selectedDate)
                }
                visibleMonth.value = state.firstVisibleMonth
            }
    }
    return visibleMonth.value
}
