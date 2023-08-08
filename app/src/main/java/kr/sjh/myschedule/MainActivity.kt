package kr.sjh.myschedule

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kr.sjh.myschedule.components.MyWeekCalendar
import kr.sjh.myschedule.ui.screen.detail.ScheduleDetailScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleViewModel
import kr.sjh.myschedule.ui.theme.MyScheduleTheme
import kr.sjh.myschedule.ui.theme.Screen
import kr.sjh.myschedule.utill.Common
import kr.sjh.myschedule.utill.Common.ADD_PAGE

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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScheduleApp(
    scheduleViewModel: ScheduleViewModel = hiltViewModel()
) {

    val navController = rememberNavController()

    var isFabShow = scheduleViewModel.isFabShow.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AnimatedVisibility(visible = isFabShow.value) {
                FloatingActionButton(
                    backgroundColor = Color(0xffECE6F0),
                    onClick = {
                        navController.navigate(Screen.Detail.createRoute(ADD_PAGE))
                        scheduleViewModel.isFabShow(false)
                    }) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = "write")
                }
            }
        }
    ) {

        ScheduleNavHost(
            navController = navController,
            scheduleViewModel = scheduleViewModel,
            snackBarHostState = rememberScaffoldState().snackbarHostState
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScheduleNavHost(
    navController: NavHostController,
    scheduleViewModel: ScheduleViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState
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

    NavHost(navController = navController, startDestination = "schedule") {
        composable(Screen.Schedule.route) {
            ScheduleScreen(viewModel = scheduleViewModel, onScheduleClick = {
                navController.navigate(Screen.Detail.createRoute(it))
                scheduleViewModel.isFabShow(false)
            }, onDateClick = {
                scheduleViewModel.getAllSchedules(it)
            })
        }

        composable(Screen.Detail.route, arguments = listOf(navArgument("userId") {
            type = NavType.LongType
            defaultValue = ADD_PAGE
        })) {
            ScheduleDetailScreen(onBackClick = {
                navController.navigateUp()
                scheduleViewModel.isFabShow(true)
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

