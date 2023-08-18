package kr.sjh.myschedule.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.yearMonth
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
                uiState.monthSchedule,
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
                    if (appState.selectedDate.value.month != date.month) {
                        scheduleViewModel.getAllBetweenSchedulesByGroup(
                            date.yearMonth.atStartOfMonth(),
                            date.yearMonth.atEndOfMonth()
                        )
                    }
                    appState.selectedDate.value = date
                },
                onDeleteSwipe = {
                    if (it.isAlarm) {
                        appState.alarmScheduler.cancel(it)
                    }
                    scheduleViewModel.deleteSchedule(it)
                },
                onCompleteSwipe = {
                    if (it.isAlarm) {
                        appState.alarmScheduler.cancel(it)
                    }
                    it.isComplete = true
                    scheduleViewModel.updateSchedule(it)
                }
            )
        }

        composable(Screen.Detail.route) {
            ScheduleDetailScreen(
                onBackClick = { appState.navigateBack() },
                onSave = { schedule ->
                    if (schedule.isAlarm) {
                        appState.alarmScheduler.schedule(schedule)
                    } else {
                        appState.alarmScheduler.cancel(schedule)
                    }
                    scheduleViewModel.updateSchedule(schedule = schedule)
                },
                viewModel = hiltViewModel()
            )
        }

    }
}