package kr.sjh.myschedule.ui.screen.detail

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import kotlinx.coroutines.launch
import kr.sjh.myschedule.components.CustomToggleButton
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.ui.theme.*
import kr.sjh.myschedule.utill.clickableSingle
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ScheduleDetailScreen(
    onBackClick: () -> Unit,
    onSave: (ScheduleEntity) -> Unit,
    viewModel: ScheduleDetailViewModel = hiltViewModel(),
) {

    val activity = LocalContext.current as Activity
    //transparent
    val color = 0xff00000000
    setTransparentActivity(activity, color)


    val scrollState = rememberScrollState()

    val title by viewModel._title.collectAsState()

    val memo by viewModel._memo.collectAsState()

    val isAlarm by viewModel._isAlarm.collectAsState()

    val alarmTime by viewModel._alarmTime.collectAsState()

    DetailContent(
        scrollState, onBackClick, viewModel, title, memo, isAlarm, alarmTime, onSave
    )
}

@Composable
fun DetailTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
        elevation = 0.dp,
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.CenterStart) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = FontColorNomal,
                    modifier = Modifier.clickableSingle {
                        onBackClick()
                    })
                Text(
                    textAlign = TextAlign.Center,
                    text = "스케쥴 등록",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp,
                    color = FontColorNomal,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                )
            }
        }
    }
}

@Composable
fun DetailContent(
    scrollState: ScrollState,
    onBackClick: () -> Unit,
    viewModel: ScheduleDetailViewModel,
    title: TextFieldValue,
    memo: String,
    isAlarm: Boolean,
    alarmTime: LocalDateTime,
    onSave: (ScheduleEntity) -> Unit
) {

    val focusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    var isError by rememberSaveable {
        mutableStateOf(false)
    }

    fun validDate(text: String) {
        isError = text.isEmpty()

    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBlue)
            .padding(10.dp)

    ) {
        DetailTopBar(onBackClick)
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color(0xff001c2d),
                focusedBorderColor = FontColorNomal,
                unfocusedBorderColor = FontColorNomal
            ),
                value = title,
                maxLines = 2,
                onValueChange = {
                    validDate(it.text)
                    viewModel._title.value = it
                },
                label = {
                    Text(
                        text = "제목",
                        color = if (isError) MaterialTheme.colors.error else FontColorNomal
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, autoCorrect = false),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                isError = isError,
                trailingIcon = {
                    if (isError) Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "제목을 입력해주세요",
                        tint = MaterialTheme.colors.error
                    )
                })
            if (isError) {
                Text(
                    text = "제목을 입력해주세요",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .height(22.dp),
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                )
            }

            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = FontColorNomal, unfocusedBorderColor = FontColorNomal
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                value = memo,
                onValueChange = {
                    viewModel._memo.value = it
                },
                label = {
                    Text(
                        text = "내용", color = FontColorNomal
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, autoCorrect = false),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "알람 ON/OFF",
                fontSize = TextUnit(20f, TextUnitType.Sp),
                color = FontColorNomal
            )
            CustomToggleButton(selected = isAlarm, modifier = Modifier.padding(top = 10.dp)) {
                viewModel._isAlarm.value = it
            }
            if (isAlarm) {
                WheelDateTimePicker(
                    modifier = Modifier.fillMaxWidth(),
                    startDateTime = alarmTime,
                    timeFormat = TimeFormat.AM_PM,
                    size = DpSize(300.dp, 200.dp),
                    rowCount = 3,
                    textColor = Color.Black,
                    textStyle = TextStyle(fontSize = 18.sp),
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        enabled = true,
                        shape = RoundedCornerShape(0.dp),
                        color = Color(0xffF7F2FA).copy(alpha = 0.2f),
                        border = BorderStroke(2.dp, Color.Red)
                    )
                ) {
                    viewModel._alarmTime.value = it
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White, disabledBackgroundColor = Color.LightGray
                ), enabled = !isError && title.text.isNotEmpty(), onClick = {
                    viewModel.onSaveSchedule(onSave = {
                        onSave(it)
                        onBackClick()
                    }, onError = {
                        showToast(context, it)
                    })
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
            ) {
                Text(text = "저장")
            }
        }

    }
}

@Composable
fun setTransparentActivity(activity: Activity, color: Long) {
    val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        bitmap.eraseColor(color)
    } else {
        bitmap.eraseColor(color.toInt())
    }
    val bitmapDrawable = BitmapDrawable(LocalContext.current.resources, bitmap)

    LaunchedEffect(activity) {
        activity.window.setBackgroundDrawable(bitmapDrawable)
    }
}

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}