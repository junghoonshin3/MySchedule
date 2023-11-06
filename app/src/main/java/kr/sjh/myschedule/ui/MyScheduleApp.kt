package kr.sjh.myschedule.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.receiver.MyAlarmScheduler
import kr.sjh.myschedule.ui.component.Menu
import kr.sjh.myschedule.ui.component.ModalBottomSheetDialog
import kr.sjh.myschedule.ui.component.PrioritySpinner
import kr.sjh.myschedule.ui.screen.navigation.BottomNavigationBar
import kr.sjh.myschedule.ui.screen.schedule.ScheduleScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleViewModel
import kr.sjh.myschedule.ui.screen.today.TodayScreen
import kr.sjh.myschedule.ui.theme.FontColorNomal
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.utill.MyScheduleAppState
import kr.sjh.myschedule.utill.addFocusCleaner
import kr.sjh.myschedule.utill.clickableSingle
import kr.sjh.myschedule.utill.common.CategoryValue
import kr.sjh.myschedule.utill.common.MenuValue
import kr.sjh.myschedule.utill.rememberMyScheduleAppState
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyScheduleApp(
    modifier: Modifier = Modifier,
    onKeepOnScreenCondition: () -> Unit,
    appState: MyScheduleAppState = rememberMyScheduleAppState(),
) {
    val scheduleViewModel = hiltViewModel<ScheduleViewModel>()

    val sharedViewModel = hiltViewModel<SharedViewModel>()

    val uiState by scheduleViewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val title by sharedViewModel.title.collectAsState()

    var bottomSheetColor by remember {
        mutableStateOf(SoftBlue)
    }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(initialValue = Hidden, confirmValueChange = {
        when (it) {
            Expanded -> {
                focusRequester.requestFocus()
                true
            }

            Hidden -> {
                bottomSheetColor = SoftBlue
                focusManager.clearFocus()
                true
            }

            HalfExpanded -> {
                false
            }
        }
    })

    ModalBottomSheetDialog(modifier = Modifier
        .fillMaxSize()
        .imePadding()
        .navigationBarsPadding()
        .addFocusCleaner(focusManager),
        sheetState = sheetState,
        sheetContent = {
            ScheduleAddWithBottomSheet(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                title,
                sharedViewModel,
                MenuValue.values(),
                focusRequester
            ) {
                bottomSheetColor = it
            }
        },
        content = {
            Scaffold(bottomBar = {
                BottomNavigationBar(items = listOf(
                    Screen.Today, Screen.Schedule, Screen.Settings
                ),
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth(),
                    navController = appState.navController,
                    onItemClick = {
                        appState.navController.navigate(it.route)
                    })
            }) {
                NavHost(
                    modifier = Modifier.padding(it),
                    navController = appState.navController,
                    startDestination = Screen.Today.route,
                ) {
                    composable(
                        Screen.Today.route
                    ) {
                        TodayScreen(onKeepOnScreenCondition) {
                            coroutineScope.launch {
                                if (sheetState.isVisible) {
                                    sheetState.hide()
                                } else {
                                    sheetState.show()
                                }
                            }
                        }
                    }
                    composable(
                        Screen.Schedule.route,
                    ) {
                        ScheduleScreen(onKeepOnScreenCondition = onKeepOnScreenCondition,
                            allYearSchedules = uiState.allYearSchedules,
                            selectedDate = appState.selectedDate.value,
                            onScheduleClick = { scheduleEntity ->
                            },
                            onSelectedDate = { date ->
                                // 선택한 달과 동일한 달이 아니면 데이터를 불러오지않기
                                if (appState.selectedDate.value.year != date.year) {
                                    scheduleViewModel.getAllYearSchedules(date)
                                }
                                appState.selectedDate.value = date
                            },
                            onDeleteSwipe = { schedule ->
//                            removeAlarm(schedule, appState.alarmScheduler)
                                scheduleViewModel.deleteSchedule(schedule)
                            },
                            onCompleteSwipe = {
//                            removeAlarm(it, appState.alarmScheduler)
                                scheduleViewModel.completeSchedule(it)
                            })
                    }

                    composable(Screen.Settings.route) {

                    }
                }
            }
        })


}


@Composable
fun ScheduleAddWithBottomSheet(
    modifier: Modifier = Modifier,
    text: String,
    viewModel: SharedViewModel,
    menuList: Array<MenuValue>,
    focusRequester: FocusRequester,
    onPriorityColor: (Color) -> Unit,

    ) {


    var isCalendarShow by remember {
        mutableStateOf(false)
    }

    var isPriorityShow by remember {
        mutableStateOf(false)
    }

    var isAlarmShow by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = modifier.padding(start = 5.dp, end = 5.dp), userScrollEnabled = false
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester = focusRequester),
                    value = text,
                    onValueChange = {
                        viewModel.changeTitle(it)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    label = {
                        Text(
                            text = "할일을 적어주세요", color = FontColorNomal
                        )
                    },
                )
                Image(imageVector = Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(5.dp)
                        .clickableSingle {

                        })
            }
        }

        item {
            Box(modifier = Modifier.fillMaxSize()) {
                PrioritySpinner(isPriorityShow,
                    categories = CategoryValue.values(),
                    onPriorityClick = {

                    })
                LazyRow(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    content = {
                        items(menuList) {
                            val imageVector = when (it) {
                                MenuValue.Category -> {
                                    Icons.Default.Category
                                }

                                MenuValue.Alarm -> {
                                    Icons.Default.Alarm
                                }

                                MenuValue.Calendar -> {
                                    Icons.Default.CalendarMonth
                                }
                            }

                            Menu(
                                Modifier
                                    .width(70.dp)
                                    .height(70.dp),
                                Color.LightGray,
                                it.value,
                                imageVector
                            ) { name ->
                                when (name) {
                                    MenuValue.Category.value -> {
                                        isPriorityShow = !isPriorityShow
                                    }

                                    MenuValue.Alarm.value -> {
                                        isAlarmShow = !isAlarmShow
                                    }

                                    MenuValue.Calendar.value -> {
                                        isCalendarShow = !isCalendarShow
                                    }
                                }
                            }
                        }
                    })
            }
        }
        item {
            if (isAlarmShow) {

            }
        }
        item {
            val state = rememberCalendarState(
                startMonth = YearMonth.now(), firstDayOfWeek = DayOfWeek.MONDAY
            )
            if (isCalendarShow) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HorizontalCalendar(modifier = Modifier.fillMaxSize(),state = state, monthHeader = {
                        Text(
                            modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            text = "${it.yearMonth.year}.${
                                it.yearMonth.month.getDisplayName(
                                    TextStyle.FULL, Locale.KOREAN
                                )
                            }"
                        )
                        Row(
                            modifier = modifier.fillMaxWidth()
                        ) {
                            it.weekDays.first().map { calendarDay ->
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
                    }, dayContent = {
                        Day(it) {

                        }
                    })
                }
            }
        }

    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
//    colors: List<Color> = emptyList(),
    onClick: (CalendarDay) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
    ) {
        val textColor = when (day.position) {
            DayPosition.MonthDate -> Color.White
            DayPosition.InDate, DayPosition.OutDate -> Color.LightGray
        }
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize(),
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 15.sp,
        )
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth()
//                .padding(bottom = 8.dp),
//            verticalArrangement = Arrangement.spacedBy(6.dp),
//        ) {
//            for (color in colors) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(5.dp)
//                        .background(color),
//                )
//            }
//        }
    }
}

fun addAlarm(schedule: ScheduleEntity, alarmScheduler: MyAlarmScheduler) {
    alarmScheduler.schedule(schedule)
}

fun removeAlarm(schedule: ScheduleEntity, alarmScheduler: MyAlarmScheduler) {
    alarmScheduler.cancel(schedule)
}
