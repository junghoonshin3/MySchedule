package kr.sjh.myschedule.utill

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kr.sjh.myschedule.receiver.MyAlarmScheduler
import kr.sjh.myschedule.ui.Screen
import java.time.LocalDate
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <T> MutableStateFlow<T>.collectAsMutableState(
    context: CoroutineContext = EmptyCoroutineContext
): MutableState<T> = MutableStateAdapter(
    state = collectAsState(context),
    mutate = { value = it }
)

@Composable
fun Modifier.clickableWithoutRipple(doSomeThing: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return clickable(
        interactionSource = interactionSource,
        indication = null
    ) {
        doSomeThing()
    }
}

fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val routeLink = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(route).toUri())
        .build()

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