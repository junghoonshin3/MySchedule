package kr.sjh.myschedule.utill

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import kr.sjh.myschedule.receiver.MyAlarmScheduler
import java.time.LocalDate

fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val routeLink =
        NavDeepLinkRequest.Builder.fromUri(NavDestination.createRoute(route).toUri()).build()

    val deepLinkMatch = graph.matchDeepLink(routeLink)
    if (deepLinkMatch != null) {
        val destination = deepLinkMatch.destination
        val id = destination.id
        navigate(id, args, navOptions, navigatorExtras)
    } else {
        navigate(route, navOptions, navigatorExtras)
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

    fun navigateBack(route: String, inclusive: Boolean) {
        navController.popBackStack(route, inclusive)
    }
}
