package kr.sjh.myschedule.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

    NavHost(navController = appState.navController, startDestination = Screen.Schedule.route) {
        composable(Screen.Schedule.route) {
            ScheduleScreen(
                viewModel = scheduleViewModel,
                selectedDate = appState.selectedDate.value,
                onScheduleClick = { scheduleEntity ->
                    appState.navController.navigate(
                        Screen.Detail.route,
                        bundleOf(
                            "schedule" to scheduleEntity
                        )
                    )
                },
                onSelectedDate = {
                    appState.selectedDate.value = it
                },
                onDeleteSwipe = {
                    if (it.isAlarm) {
                        scheduleViewModel.deleteSchedule(it)
                        appState.alarmScheduler.cancel(it)
                    }
                },
                onCompleteSwipe = {
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
                },
                viewModel = hiltViewModel()
            )
        }

    }
}