package kr.sjh.myschedule.ui

sealed class Screen(val route: String) {

    object Schedule : Screen(
        route = "schedule"
    )

    object Detail : Screen(
        route = "detail"
    )
}



