package kr.sjh.myschedule.ui.screen.bottomsheet

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import kr.sjh.myschedule.components.CustomDialog
import kr.sjh.myschedule.ui.component.PeriodSpinner
import kr.sjh.myschedule.ui.screen.bottomsheet.add.ScheduleAddDialog
import kr.sjh.myschedule.ui.screen.detail.showToast
import kr.sjh.myschedule.ui.theme.FontColorNomal
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.ui.theme.VanillaIce
import kr.sjh.myschedule.utill.backgroundWithSelection
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.TemporalAccessor
import java.util.Locale

@Composable
fun BottomSheetContent(
    viewModel: BottomSheetViewModel
) {

    val title by viewModel.title.collectAsState()

    val today = LocalDate.now()

    val dateSelection by viewModel.dateSelection.collectAsState()

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var isPeriodShow by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

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
            HorizontalCalendar(modifier = Modifier.wrapContentSize(),
                userScrollEnabled = !isPeriodShow,
                state = rememberCalendarState(
                    startMonth = YearMonth.of(1950, 1),
                    endMonth = YearMonth.of(2050, 12),
                    firstDayOfWeek = DayOfWeek.MONDAY,
                    firstVisibleMonth = YearMonth.now()
                ),
                monthHeader = { month ->
                    BottomSheetCalendarHeader(month)
                },
                dayContent = { day ->
                    BottomSheetCalendarDay(day,
                        dateSelection = dateSelection,
                        day.date == today,
                        day.date == selectedDate,
                        onClickedDate = { clickDate ->
                            if (!isPeriodShow) {
                                isPeriodShow = true
                                selectedDate = clickDate
                            }
                        },
                        onLongClick = {

                        })
                })

            DateSpinnerDialog(selectedDate = selectedDate,
                visible = isPeriodShow,
                onDismissRequest = {
                    isPeriodShow = false
                },
                onSave = { selection ->
                    if (selection.startDate == null && selection.endDate == null) {
                        return@DateSpinnerDialog
                    }
                    if (selection.startDate!! > selection.endDate) {
                        showToast(context, "선택한 일보다 이전일 수 없습니다")
                        return@DateSpinnerDialog
                    }
                    viewModel.setDateSelection(selection)
                },
                onCancel = {
                    isPeriodShow = false
                })
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
    dateSelection: DateSelection,
    isToday: Boolean,
    isSelected: Boolean,
    onClickedDate: (LocalDate) -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(
                width = if (isToday) 1.dp else 0.dp,
                color = if (isToday) Color.Yellow.copy(0.5f) else Color.Unspecified,
                shape = if (isToday) CircleShape else RectangleShape
            )
            .background(
                color = when {
                    isSelected -> {
                        VanillaIce.copy(0.5f)
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

@Composable
private fun DateSpinnerDialog(
    selectedDate: LocalDate,
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onSave: (DateSelection) -> Unit,
    onCancel: () -> Unit
) {
    if (visible) {
        CustomDialog(onDismissRequest = { onDismissRequest() }) {
            ScheduleAddDialog(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(color = SoftBlue, shape = RoundedCornerShape(10.dp)),
                selectedDate,
                onSave = {
                    Log.i("sjh", "${it.startDate}, ${it.endDate}")
                    onSave(it)
                },
                onCancel = {
                    onCancel()
                })
        }
    }
}
