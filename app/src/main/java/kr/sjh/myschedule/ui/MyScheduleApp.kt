package kr.sjh.myschedule.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.receiver.MyAlarmScheduler
import kr.sjh.myschedule.ui.component.ModalBottomSheetDialog
import kr.sjh.myschedule.ui.screen.navigation.BottomNavigationBar
import kr.sjh.myschedule.ui.screen.today.TodayScreen
import kr.sjh.myschedule.ui.screen.today.bottomsheet.BottomSheetContent
import kr.sjh.myschedule.ui.screen.today.bottomsheet.BottomSheetViewModel
import kr.sjh.myschedule.ui.screen.today.generateRandomColor
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.utill.MyScheduleAppState
import kr.sjh.myschedule.utill.addFocusCleaner
import kr.sjh.myschedule.utill.rememberMyScheduleAppState
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyScheduleApp(
    onKeepOnScreenCondition: () -> Unit,
    appState: MyScheduleAppState = rememberMyScheduleAppState(),
) {

    val mainViewModel = hiltViewModel<MainViewModel>()

    val bottomSheetViewModel = hiltViewModel<BottomSheetViewModel>()

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val focusManager = LocalFocusManager.current

    val sheetState = rememberModalBottomSheetState(initialValue = Hidden, confirmValueChange = {
        when (it) {
            Expanded -> {
                true
            }

            Hidden -> {
                focusManager.clearFocus()
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
        .addFocusCleaner(focusManager), sheetState = sheetState, sheetContent = {
        BottomSheetContent(
            selectedDate = selectedDate,
            viewModel = bottomSheetViewModel,
            sheetState = sheetState,
            focusManager = focusManager
        )
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
                    TodayScreen(
                        viewModel = mainViewModel,
                        onKeepOnScreenCondition = onKeepOnScreenCondition,
                        selectedDate = selectedDate,
                        onSelectedDate = {
                            selectedDate = it
                        },
                        modalBottomSheetState = sheetState
                    )
                }
                composable(
                    Screen.Schedule.route,
                ) {}

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
