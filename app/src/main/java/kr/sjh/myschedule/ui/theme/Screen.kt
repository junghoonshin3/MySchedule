package kr.sjh.myschedule.ui.theme

sealed class Screen(val route: String) {

    object Schedule : Screen(
        route = "schedule"
    )

    object Detail : Screen(
        route = "scheduleDetail/{userId}"
    ) {
        fun createRoute(userId: Long) = "scheduleDetail/$userId"
    }
}