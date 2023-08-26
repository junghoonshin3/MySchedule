package kr.sjh.myschedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kr.sjh.myschedule.ui.theme.Crayola
import kr.sjh.myschedule.ui.theme.SoftBlue


@Composable
fun CheckCircle(
    modifier: Modifier = Modifier
) {

    Card(
        shape = CircleShape, modifier = modifier.size(20.dp), elevation = 0.dp
    ) {
        Box(modifier = Modifier.background(SoftBlue))
    }

}

@Composable
fun CustomToggleButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onUpdate: (Boolean) -> Unit
) {

    Card(
        modifier = modifier
            .width(50.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onUpdate(!selected)
            }, shape = RoundedCornerShape(16.dp), elevation = 0.dp
    ) {
        Box(
            modifier = Modifier.background(
                if (selected) Crayola else Color.LightGray.copy(0.4f)
            ), contentAlignment = if (selected) Alignment.TopEnd else Alignment.TopStart
        ) {
            CheckCircle(modifier = Modifier.padding(5.dp))
        }
    }

}