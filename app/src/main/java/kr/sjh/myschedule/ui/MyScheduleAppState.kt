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
    )
}

