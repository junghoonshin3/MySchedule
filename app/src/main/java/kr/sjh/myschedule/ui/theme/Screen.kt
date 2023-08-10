package kr.sjh.myschedule.ui.theme

import java.time.LocalDate

sealed class Screen(val route: String) {

    object Schedule : Screen(
        route = "schedule"
    )

    object Detail : Screen(
        route = "scheduleDetail/{userId}/{selectedDate}"
    ) {
        fun createRoute(userId: Long, selectedDate: LocalDate? = LocalDate.now()) =
            "scheduleDetail/$userId/$selectedDate"
    }
}