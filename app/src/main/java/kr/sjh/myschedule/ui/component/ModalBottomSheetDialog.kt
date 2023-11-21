package kr.sjh.myschedule.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kr.sjh.myschedule.ui.theme.SoftBlue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetDialog(
    modifier: Modifier = Modifier,
    sheetContent: @Composable BoxScope.() -> Unit,
    content: @Composable () -> Unit,
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
    sheetShape: Shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
    sheetGesturesEnabled: Boolean = true,
    sheetElevation: Dp = 0.dp,
    sheetBackgroundColor: Color = SoftBlue,
    sheetContentColor: Color = SoftBlue,
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
) {
    BoxWithConstraints {
        ModalBottomSheetLayout(
            modifier = modifier,
            scrimColor = scrimColor,
            sheetContentColor = sheetContentColor,
            sheetBackgroundColor = sheetBackgroundColor,
            sheetElevation = sheetElevation,
            sheetGesturesEnabled = sheetGesturesEnabled,
            sheetShape = sheetShape,
            sheetState = sheetState,
            sheetContent = {
                Box(modifier = Modifier.wrapContentSize()) {
                    sheetContent(this)
                }
            },
            content = content
        )
    }

}