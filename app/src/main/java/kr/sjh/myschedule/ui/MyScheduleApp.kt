package kr.sjh.myschedule.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalBottomSheetValue.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.receiver.MyAlarmScheduler
import kr.sjh.myschedule.ui.screen.navigation.BottomNavigationBar
import kr.sjh.myschedule.ui.screen.schedule.ScheduleScreen
import kr.sjh.myschedule.utill.MyScheduleAppState
import kr.sjh.myschedule.utill.rememberMyScheduleAppState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyScheduleApp(
    onKeepOnScreenCondition: () -> Unit,
    appState: MyScheduleAppState = rememberMyScheduleAppState(),
) {
    Scaffold(bottomBar = {
        BottomNavigationBar(items = listOf(
            Screen.Schedule, Screen.Settings
        ),
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
                .imePadding()
            ,
            navController = appState.navController,
            onItemClick = {
                appState.navController.navigate(it.route)
            })
    }) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = appState.navController,
            startDestination = Screen.Schedule.route,
        ) {
            composable(
                Screen.Schedule.route
            ) {
                ScheduleScreen(
                    onKeepOnScreenCondition = onKeepOnScreenCondition
                )
            }
            composable(Screen.Settings.route) {

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
