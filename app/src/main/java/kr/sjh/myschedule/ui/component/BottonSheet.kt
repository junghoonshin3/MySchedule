package kr.sjh.myschedule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.sjh.myschedule.ui.theme.SoftBlue

@Composable
fun BottomSheet(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .background(SoftBlue)
            .padding(15.dp)
    ) {
        content()
    }
}