package kr.sjh.myschedule.ui.component

import android.util.Log
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.utill.common.CategoryValue
import kr.sjh.myschedule.utill.common.CategoryValue.DailyLife
import kr.sjh.myschedule.utill.common.CategoryValue.Important
import kr.sjh.myschedule.utill.common.CategoryValue.Study

@Composable
fun PrioritySpinner(
    isShow: Boolean,
    offset: DpOffset = DpOffset.Zero,
    categories: Array<CategoryValue>,
    onPriorityClick: (Color) -> Unit
) {
    var expanded by remember { mutableStateOf(isShow) }.apply {
        value = isShow
    }
    DropdownMenu(offset = offset,
        properties = PopupProperties(focusable = false),
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        }) {
        categories.forEach { item ->
            val color = when (item) {
                DailyLife -> {
                    SoftBlue
                }

                Study -> {
                    Color.Yellow
                }

                Important -> {
                    Color.Red
                }
            }
            DropdownMenuItem(modifier = Modifier.fillMaxWidth(), onClick = {
                onPriorityClick(color)
                expanded = false
            }) {
                Image(
                    imageVector = Icons.Default.Circle,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(color)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = item.value)
            }
        }
    }
}


