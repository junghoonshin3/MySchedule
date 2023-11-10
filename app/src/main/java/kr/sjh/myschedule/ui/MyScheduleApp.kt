package kr.sjh.myschedule.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.receiver.MyAlarmScheduler
import kr.sjh.myschedule.ui.component.ModalBottomSheetDialog
import kr.sjh.myschedule.ui.screen.bottomsheet.BottomSheetContent
import kr.sjh.myschedule.ui.screen.bottomsheet.BottomSheetViewModel
import kr.sjh.myschedule.ui.screen.navigation.BottomNavigationBar
import kr.sjh.myschedule.ui.screen.schedule.ScheduleScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleViewModel
import kr.sjh.myschedule.ui.screen.today.TodayScreen
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.utill.MyScheduleAppState
import kr.sjh.myschedule.utill.addFocusCleaner
import kr.sjh.myschedule.utill.rememberMyScheduleAppState

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyScheduleApp(
    modifier: Modifier = Modifier,
    onKeepOnScreenCondition: () -> Unit,
    appState: MyScheduleAppState = rememberMyScheduleAppState(),
) {
    val scheduleViewModel = hiltViewModel<ScheduleViewModel>()

    val bottomSheetViewModel = hiltViewModel<BottomSheetViewModel>()

    val uiState by scheduleViewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    var bottomSheetColor by remember {
        mutableStateOf(SoftBlue)
    }

    var focusManager = LocalFocusManager.current

    val sheetState = rememberModalBottomSheetState(initialValue = Hidden, confirmValueChange = {
        when (it) {
            Expanded -> {
                true
            }

            Hidden -> {
                bottomSheetColor = SoftBlue

                true
            }

            HalfExpanded -> {
                false
            }
        }
    }, skipHalfExpanded = true)

    ModalBottomSheetDialog(modifier = Modifier
        .fillMaxSize()
        .imePadding()
        .navigationBarsPadding()
        .addFocusCleaner(focusManager),
        sheetState = sheetState,
        sheetContent = {
            BottomSheetContent(bottomSheetViewModel)
        }, content = {
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


fun addAlarm(schedule: ScheduleEntity, alarmScheduler: MyAlarmScheduler) {
    alarmScheduler.schedule(schedule)
}

fun removeAlarm(schedule: ScheduleEntity, alarmScheduler: MyAlarmScheduler) {
    alarmScheduler.cancel(schedule)
}
