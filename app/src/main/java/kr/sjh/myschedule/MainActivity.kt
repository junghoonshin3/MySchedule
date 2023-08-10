package kr.sjh.myschedule

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import kr.sjh.myschedule.ui.screen.detail.ScheduleDetailScreen
import kr.sjh.myschedule.ui.screen.detail.ScheduleDetailViewModel
import kr.sjh.myschedule.ui.screen.schedule.ScheduleScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleViewModel
import kr.sjh.myschedule.ui.theme.MyScheduleTheme
import kr.sjh.myschedule.ui.theme.Screen
import kr.sjh.myschedule.utill.Common.ADD_PAGE
import java.time.LocalDate
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyScheduleTheme {
                ScheduleApp()
            }
        }
    }
}

@Composable
fun ScheduleApp(
    scheduleViewModel: ScheduleViewModel = hiltViewModel()
) {

    val navController = rememberNavController()

    ScheduleNavHost(
        navController = navController,
        scheduleViewModel = scheduleViewModel
    )

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScheduleNavHost(
    navController: NavHostController,
    scheduleViewModel: ScheduleViewModel = hiltViewModel(),
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        val permissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.POST_NOTIFICATIONS
            )
        )

        PostNotificationPermission(
            multiplePermissionState = permissionsState
        )

        LaunchedEffect(Unit) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    val context = LocalContext.current

    val alarmScheduler = MyAlarmScheduler(context)

    NavHost(navController = navController, startDestination = Screen.Schedule.route) {
        composable(Screen.Schedule.route) {
            ScheduleScreen(
                navController = navController,
                scheduleViewModel = scheduleViewModel,
                onDeleteSwipe = {
                    if (it.isAlarm) {
                        Log.i("sjh", "delete")
                        alarmScheduler.cancel(
                            AlarmItem(
                                it.id.toInt(),
                                it.alarmTime,
                                it.title,
                                it.memo
                            )
                        )
                    }
                    scheduleViewModel.deleteSchedule(it)

                },
                onCompleteSwipe = {
                    scheduleViewModel.updateSchedule(it)
                }
            )
        }

        composable(Screen.Detail.route, arguments = listOf(navArgument("userId") {
            type = NavType.LongType
            defaultValue = ADD_PAGE
        }, navArgument("selectedDate") {
            type = NavType.StringType
        })) {
            ScheduleDetailScreen(onBackClick = {
                navController.navigateUp()
            }, onSaveSchedule = { alarmItem, isAlarm ->
                if (isAlarm) {
                    alarmScheduler.schedule(alarmItem)
                } else {
                    alarmScheduler.cancel(alarmItem)
                }

            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyScheduleTheme {
        ScheduleApp()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PostNotificationPermission(
    multiplePermissionState: MultiplePermissionsState
) {
    PermissionsRequired(
        multiplePermissionsState = multiplePermissionState,
        permissionsNotGrantedContent = {

        },
        permissionsNotAvailableContent = {

        }
    ) {
    }
}

