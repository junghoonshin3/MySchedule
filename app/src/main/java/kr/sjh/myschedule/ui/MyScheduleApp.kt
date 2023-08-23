package kr.sjh.myschedule.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.receiver.MyAlarmScheduler
import kr.sjh.myschedule.ui.screen.detail.ScheduleDetailScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleViewModel
import kr.sjh.myschedule.utill.MyScheduleAppState
import kr.sjh.myschedule.utill.navigate
import kr.sjh.myschedule.utill.rememberMyScheduleAppState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyScheduleApp(
    appState: MyScheduleAppState = rememberMyScheduleAppState()
) {

    val scheduleViewModel = hiltViewModel<ScheduleViewModel>()

    val uiState by scheduleViewModel.uiState.collectAsState()

    NavHost(navController = appState.navController, startDestination = Screen.Schedule.route) {
        composable(Screen.Schedule.route) {
            ScheduleScreen(
                allYearSchedules = uiState.allYearSchedules,
                selectedDate = appState.selectedDate.value,
                onScheduleClick = { scheduleEntity ->
                    appState.navController.navigate(
                        Screen.Detail.route,
                        bundleOf(
                            "schedule" to scheduleEntity,
                            "selectedDate" to appState.selectedDate.value
                        )
                    )
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
                }
            )
        }

        composable(Screen.Detail.route) {
            ScheduleDetailScreen(
                onBackClick = { appState.navigateBack() },
                onSave = { schedule ->
                    if (schedule.isAlarm) {
                        addAlarm(schedule, appState.alarmScheduler)
                    }
                    scheduleViewModel.insertOrUpdate(schedule)
                },
                viewModel = hiltViewModel()
            )
        }

    }
}

fun addAlarm(schedule: ScheduleEntity, alarmScheduler: MyAlarmScheduler) {
    alarmScheduler.schedule(schedule)
}

fun removeAlarm(schedule: ScheduleEntity, alarmScheduler: MyAlarmScheduler) {
    alarmScheduler.cancel(schedule)
}