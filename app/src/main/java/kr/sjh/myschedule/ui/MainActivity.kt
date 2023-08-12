package kr.sjh.myschedule.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import kr.sjh.myschedule.ui.MyScheduleApp
import kr.sjh.myschedule.ui.screen.detail.ScheduleDetailScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleScreen
import kr.sjh.myschedule.ui.screen.schedule.ScheduleViewModel
import kr.sjh.myschedule.ui.theme.MyScheduleTheme
import kr.sjh.myschedule.utill.Common.ADD_PAGE

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            MyScheduleTheme {
                //노티피케이션 퍼미션 요청
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val permissionsState = rememberMultiplePermissionsState(
                        permissions = listOf(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    )
                    RequestPermission(
                        multiplePermissionState = permissionsState
                    )
                    LaunchedEffect(Unit) {
                        permissionsState.launchMultiplePermissionRequest()
                    }
                }
                MyScheduleApp()
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission(
    multiplePermissionState: MultiplePermissionsState
) {
    PermissionsRequired(
        multiplePermissionsState = multiplePermissionState,
        permissionsNotGrantedContent = {

        },
        permissionsNotAvailableContent = {

        }
    ) {
    }
}

//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun requestPermission() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//
//        val permissionsState = rememberMultiplePermissionsState(
//            permissions = listOf(
//                Manifest.permission.POST_NOTIFICATIONS
//            )
//        )
//
//        PostNotificationPermission(
//            multiplePermissionState = permissionsState
//        )
//
//        LaunchedEffect(Unit) {
//            permissionsState.launchMultiplePermissionRequest()
//        }
//    }
//}

