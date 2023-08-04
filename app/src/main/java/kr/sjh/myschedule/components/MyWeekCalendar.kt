package kr.sjh.myschedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kr.sjh.myschedule.ui.theme.TextColor
import kr.sjh.myschedule.utill.getWeekPageTitle
import kr.sjh.myschedule.utill.rememberFirstVisibleWeekAfterScroll
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun MyWeekCalendar(selectedDay: (LocalDate) -> Unit) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember {
        mutableStateOf(currentDate)
    }

    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = DayOfWeek.MONDAY
    )

    val visibleWeek = rememberFirstVisibleWeekAfterScroll(
        state,
        currentDate
    ) { currentPosition ->
        selection = currentPosition
        selectedDay(currentPosition)
    }

    TopAppBar(
        backgroundColor = Color(0xffF7F2FA),
        elevation = 0.dp,
        title = {
            Text(text = getWeekPageTitle(visibleWeek), color = TextColor)
        })
    WeekCalendar(state = state, userScrollEnabled = true, dayContent = { day ->
        Day(
            day.date,
            isPastDay = currentDate > day.date, isSelected = selection == day.date
        ) { clicked ->
            if (selection != clicked) {
                selection = clicked
                selectedDay(clicked)
            }

        }
    })
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
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                fontSize = 12.sp,
                color = if (isPastDay) Color.LightGray else TextColor,
                fontWeight = FontWeight.Light,
            )
            Text(
                text = dateFormatter.format(day),
                fontSize = 14.sp,
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
