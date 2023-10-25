@file:OptIn(ExperimentalMaterialApi::class)

package kr.sjh.myschedule.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import kr.sjh.myschedule.ui.theme.MyScheduleTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var keepOnScreenCondition = true


    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                return@setKeepOnScreenCondition keepOnScreenCondition
            }
            setOnExitAnimationListener { splashScreenView ->
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView.iconView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.iconView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 500L
                slideUp.doOnEnd {
                    splashScreenView.remove()
                }
                slideUp.start()
            }
        }

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
                MyScheduleApp(onKeepOnScreenCondition = {
                    keepOnScreenCondition = false
                })
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission(
    multiplePermissionState: MultiplePermissionsState
) {
    PermissionsRequired(multiplePermissionsState = multiplePermissionState,
        permissionsNotGrantedContent = {

        },
        permissionsNotAvailableContent = {

        }) {}
}


