package kr.sjh.myschedule

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
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
            scheduleViewModel = scheduleViewModel
        )
    }
}

@Composable
fun ScheduleNavHost(
    navController: NavHostController,
    scheduleViewModel: ScheduleViewModel = hiltViewModel()
) {
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