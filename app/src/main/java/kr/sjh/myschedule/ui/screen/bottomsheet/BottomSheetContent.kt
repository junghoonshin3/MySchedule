package kr.sjh.myschedule.ui.screen.bottomsheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import kr.sjh.myschedule.ui.component.PeriodSpinner
import kr.sjh.myschedule.ui.theme.FontColorNomal
import kr.sjh.myschedule.ui.theme.PaleRobinEggBlue
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.utill.common.MenuValue
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun BottomSheetContent(
    viewModel: BottomSheetViewModel
) {

    var isPriorityShow by remember {
        mutableStateOf(false)
    }

    val title by viewModel.title.collectAsState()

    val today = LocalDate.now()

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var isPeriodShow by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.weight(0.8f),
                value = title,
                onValueChange = viewModel::changeTitle,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                label = {
                    Text(
                        text = "할일을 적어주세요", color = FontColorNomal
                    )
                },
            )
            Spacer(modifier = Modifier.width(5.dp))
            Button(elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SoftBlue, contentColor = SoftBlue
                ),
                modifier = Modifier
                    .weight(0.2f)
                    .aspectRatio(1f)
                    .padding(5.dp),
                onClick = {

                }) {
                Image(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
        Box(
            modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalCalendar(state = rememberCalendarState(
                startMonth = YearMonth.of(1950, 1),
                endMonth = YearMonth.of(2050, 12),
                firstDayOfWeek = DayOfWeek.MONDAY,
                firstVisibleMonth = YearMonth.now()
            ), monthHeader = { month ->
                BottomSheetCalendarHeader(month)
            }, dayContent = { day ->
                BottomSheetCalendarDay(day,
                    day.date == today,
                    day.date == selectedDate,
                    onClickedDate = { clickDate ->
                        if (!isPeriodShow) {
                            selectedDate = clickDate
                            isPeriodShow = true
                        }
                    },
                    onLongClick = {

                    })
            })
            AnimatedContent(
                modifier = Modifier.background(
                    color = PaleRobinEggBlue, shape = RoundedCornerShape(10.dp)
                ),
                targetState = isPeriodShow,
                label = "",
            ) {
                if (it) {
                    PeriodSpinner(startDateTime = selectedDate.atTime(0, 0, 0, 0), onSave = {
                        isPeriodShow = false
                    }, onCancel = {
                        isPeriodShow = false
                    })
                }
            }
        }
    }
}

@Composable
private fun BottomSheetCalendarHeader(calendarMonth: CalendarMonth) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        text = "${calendarMonth.yearMonth.year}.${calendarMonth.yearMonth.monthValue}"
    )
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        calendarMonth.weekDays.first().map { calendarDay ->
            Text(
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                color = Color.White,
                text = calendarDay.date.dayOfWeek.getDisplayName(
                    TextStyle.FULL, Locale.KOREAN
                ).first().toString()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomSheetCalendarDay(
    day: CalendarDay,
    isToday: Boolean,
    isSelected: Boolean,
    onClickedDate: (LocalDate) -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                color = when {
                    isToday -> {
                        Color.Yellow.copy(0.5f)
                    }

                    isSelected -> {
                        Color.Green.copy(0.5f)
                    }

                    else -> {
                        Color.Unspecified
                    }
                }, shape = CircleShape
            )
            .combinedClickable(
                onClick = { onClickedDate(day.date) }, onLongClick = onLongClick
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (day.position == DayPosition.MonthDate) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = day.date.dayOfMonth.toString(),
                color = Color.White,
                fontSize = 15.sp,
            )
        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = day.date.dayOfMonth.toString(),
                color = Color.LightGray,
                fontSize = 15.sp,
            )
        }
    }
}


@Preview
@Composable
private fun BottomSheetContentPreview() {
//    BottomSheetContent()
}

