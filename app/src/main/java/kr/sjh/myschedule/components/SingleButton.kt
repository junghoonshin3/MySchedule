package kr.sjh.myschedule.components

import android.os.SystemClock
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SingleButton(
    modifier: Modifier,
    enable: Boolean,
    colors: ButtonColors,
    clickDisablePeriod: Long = 1500L,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    Button(
        enabled = enable, colors = colors, modifier = modifier, onClick = {
            if (SystemClock.elapsedRealtime() - lastClickTime < clickDisablePeriod) {

            } else {
                lastClickTime = SystemClock.elapsedRealtime()
                onClick()
            }

        }, content = content
    )
}