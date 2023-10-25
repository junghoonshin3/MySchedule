package kr.sjh.myschedule.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.receiver.MyAlarmScheduler
import kr.sjh.myschedule.ui.component.BottomSheet
import kr.sjh.myschedule.ui.screen.navigation.BottomNavigationBar
import kr.sjh.myschedule.ui.screen.schedule.ScheduleScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleViewModel
import kr.sjh.myschedule.ui.screen.today.TodayScreen
import kr.sjh.myschedule.ui.theme.FontColorNomal
import kr.sjh.myschedule.utill.MyScheduleAppState
import kr.sjh.myschedule.utill.clickableSingle
import kr.sjh.myschedule.utill.rememberMyScheduleAppState

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyScheduleApp(
    onKeepOnScreenCondition: () -> Unit,
    appState: MyScheduleAppState = rememberMyScheduleAppState(),
) {
    val scheduleViewModel = hiltViewModel<ScheduleViewModel>()

    val sharedViewModel = hiltViewModel<SharedViewModel>()

    val uiState by scheduleViewModel.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded })

    val coroutineScope = rememberCoroutineScope()

    val title by sharedViewModel.title.collectAsState()

    val menuList = listOf(
        Icons.Default.Alarm, Icons.Default.CalendarMonth
    )

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetState = sheetState,
        sheetContent = {
            BottomSheet("일정추가") {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                modifier = Modifier.weight(1f),
                                value = title,
                                onValueChange = {
                                    sharedViewModel.changeTitle(it)
                                },
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
                        LazyRow(contentPadding = PaddingValues(10.dp),content = {
                            items(menuList) {imageVector->
                                Image(imageVector = imageVector, contentDescription = null)
                            }
                        })
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {

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
                            if (sheetState.isVisible) sheetState.hide()
                            else sheetState.show()
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
                            removeAlarm(schedule, appState.alarmScheduler)
                            scheduleViewModel.deleteSchedule(schedule)
                        },
                        onCompleteSwipe = {
                            removeAlarm(it, appState.alarmScheduler)
                            scheduleViewModel.completeSchedule(it)
                        })
                }

                composable(Screen.Settings.route) {

                }
            }
        }

    }
}

fun addAlarm(schedule: ScheduleEntity, alarmScheduler: MyAlarmScheduler) {
    alarmScheduler.schedule(schedule)
}

fun removeAlarm(schedule: ScheduleEntity, alarmScheduler: MyAlarmScheduler) {
    alarmScheduler.cancel(schedule)
}