package kr.sjh.myschedule.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.util.DebugLogger
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.receiver.MyAlarmScheduler
import kr.sjh.myschedule.ui.screen.detail.ScheduleDetailScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleViewModel
import kr.sjh.myschedule.utill.Common.TWEEN_DELAY
import kr.sjh.myschedule.utill.MyScheduleAppState
import kr.sjh.myschedule.utill.navigate
import kr.sjh.myschedule.utill.rememberMyScheduleAppState
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyScheduleApp(
    appState: MyScheduleAppState = rememberMyScheduleAppState(),
) {

    val scheduleViewModel = hiltViewModel<ScheduleViewModel>()


    val uiState by scheduleViewModel.uiState.collectAsState()
    NavHost(navController = appState.navController,
        startDestination = Screen.Schedule.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {

        composable(
            Screen.Schedule.route,
        ) {
            ScheduleScreen(allYearSchedules = uiState.allYearSchedules,
                selectedDate = appState.selectedDate.value,
                onScheduleClick = { scheduleEntity ->
                    appState.navController.navigate(
                        Screen.Detail.route, bundleOf(
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
                })
        }

        composable(Screen.Detail.route, enterTransition = {
            when (initialState.destination.route) {
                Screen.Schedule.route -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(TWEEN_DELAY)
                )

                else -> null
            }
        }, exitTransition = {
            when (targetState.destination.route) {
                Screen.Schedule.route -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(TWEEN_DELAY)
                )

                else -> null
            }
        }, popEnterTransition = {
            when (initialState.destination.route) {
                Screen.Schedule.route -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(TWEEN_DELAY)
                )

                else -> null
            }
        }, popExitTransition = {
            when (targetState.destination.route) {
                Screen.Schedule.route -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(TWEEN_DELAY)
                )

                else -> null
            }
        }) {
            ScheduleDetailScreen(onBackClick = {
                appState.navigateBack(Screen.Schedule.route, false)
            }, onSave = { schedule ->
                if (schedule.isAlarm) {
                    addAlarm(schedule, appState.alarmScheduler)
                } else {
                    removeAlarm(schedule, appState.alarmScheduler)
                }

                scheduleViewModel.insertOrUpdate(schedule)
            }, viewModel = hiltViewModel()
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