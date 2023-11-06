package kr.sjh.myschedule.ui.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kr.sjh.myschedule.utill.clickableSingle
import kr.sjh.myschedule.utill.common.CategoryValue

@Composable
fun Menu(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    title: String,
    image: ImageVector,
    textColor: Color = Color.White,
    onClick: (String) -> Unit
) {

    Card(
        modifier = modifier.clickableSingle {
            onClick(title)
        }, backgroundColor = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, color = textColor)
            Spacer(modifier = Modifier.height(5.dp))
            Image(imageVector = image, contentDescription = null)
        }
    }

}