package kr.sjh.myschedule.ui.screen.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kr.sjh.myschedule.ui.Screen
import kr.sjh.myschedule.ui.theme.SoftBlue


@Composable
fun BottomNavigationBar(
    items: List<Screen>,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (Screen) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(modifier = modifier, backgroundColor = Color.DarkGray, elevation = 5.dp) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(selected = selected,
                selectedContentColor = SoftBlue,
                unselectedContentColor = Color.Gray,
                onClick = { onItemClick(item) },
                icon = {
                    BottomNavIcon(item, selected)
                })
        }
    }
}

@Composable
fun BottomNavIcon(item: Screen, selected: Boolean) {
    Column(horizontalAlignment = CenterHorizontally) {
        // 뱃지카운트가 1이상이면, 아이콘에 뱃지카운트가 표시됩니다.
        if (item.badgeCount > 0) {
            BadgedBox(badge = {
                Text(text = item.badgeCount.toString())
            }) {
                Icon(
                    imageVector = item.icon, contentDescription = item.name
                )
            } // 뱃지 카운트가 0이면, 아이콘만 표시합니다.
        } else {
            Icon(
                imageVector = item.icon, contentDescription = item.name
            )
        }
        // 아이콘이 선택 되었을 때, 아이콘 밑에 텍스트를 표시합니다.
        if (selected) {
            Text(
                text = item.name, textAlign = TextAlign.Center, fontSize = 10.sp
            )
        }
    }
}