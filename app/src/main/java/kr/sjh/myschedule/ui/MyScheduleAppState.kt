package kr.sjh.myschedule.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kr.sjh.myschedule.receiver.MyAlarmScheduler
import java.time.LocalDate

sealed class Screen(val route: String) {

    object Schedule : Screen(
        route = "schedule"
    )

    object Detail : Screen(
        route = "detail"
    ) {
        fun createRoute(userId: Long, selectedDate: LocalDate? = LocalDate.now()) =
            "scheduleDetail"
    }
}

@Composable
fun rememberMyScheduleAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
    selectedDate: MutableState<LocalDate> = remember {
        mutableStateOf(LocalDate.now())
    },
    alarmScheduler: MyAlarmScheduler = remember {
        MyAlarmScheduler(context)
    }
) = remember(navController, context) {
    MyScheduleAppState(navController, context, selectedDate, alarmScheduler)
}

class MyScheduleAppState(
    val navController: NavHostController,
    private val context: Context,
    var selectedDate: MutableState<LocalDate>,
    val alarmScheduler: MyAlarmScheduler
) {
    fun navigateToDetail(id: Long, selectedDate: LocalDate?, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(Screen.Detail.createRoute(id, selectedDate))
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.getLifecycle().currentState == Lifecycle.State.RESUMED