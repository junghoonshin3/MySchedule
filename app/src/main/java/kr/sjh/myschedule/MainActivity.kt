package kr.sjh.myschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyScheduleTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(
                            backgroundColor = Color(0xffECE6F0),
                            onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Rounded.Edit, contentDescription = "write")
                        }
                    }
                ) {
                    ScheduleApp()
                }
            }
        }
    }
}

@Composable
fun ScheduleApp(scheduleViewModel: ScheduleViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    ScheduleNavHost(navController = navController, scheduleViewModel = scheduleViewModel)
}

@Composable
fun ScheduleNavHost(
    navController: NavHostController,
    scheduleViewModel: ScheduleViewModel = hiltViewModel()
) {
    NavHost(navController = navController, startDestination = "schedule") {
        composable("schedule") {
            ScheduleScreen(viewModel = scheduleViewModel, onScheduleClick = {
                navController.navigate("scheduleDetail/$it")
            })
        }
        composable("scheduleDetail/{userId}", arguments = listOf(navArgument("userId") {
            type = NavType.IntType
        })) {
            ScheduleDetailScreen(onBackClick = {
                navController.navigateUp()
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