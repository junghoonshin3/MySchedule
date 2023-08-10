package kr.sjh.myschedule.ui.screen.detail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kr.sjh.myschedule.AlarmItem
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.repository.ScheduleRepository
import kr.sjh.myschedule.utill.Common.ADD_PAGE
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val userId = savedStateHandle.get<Long>("userId")

    val selectedDate: LocalDate = savedStateHandle.get<String>("selectedDate")?.let {
        LocalDate.parse(it)
    } ?: let {
        LocalDate.now()
    }

    var title = MutableStateFlow(TextFieldValue(text = "", selection = TextRange("".length)))

    var memo = MutableStateFlow("")

    var isAlarm = MutableStateFlow(false)

    var alarmTime = MutableStateFlow(LocalDateTime.now())

    var isComplete = MutableStateFlow(false)

    init {
        userId?.let { uId ->
            Log.i("sjh", "userId >>>>>>>>>>>>>>>>>>>>>>>>> ${uId}")
            if (uId != ADD_PAGE) {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.getSchedule(uId).collectLatest { schedule ->
                        title.value = TextFieldValue(
                            text = schedule.title,
                            selection = TextRange(schedule.title.length)
                        )
                        memo.value = schedule.memo
                        isAlarm.value = schedule.isAlarm
                        alarmTime.value = schedule.alarmTime
                        isComplete.value = schedule.isComplete
                    }
                }
            }
        }
    }

    fun onSaveSchedule(savedId: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            var scheduleEntity = ScheduleEntity(
                userId ?: ADD_PAGE,
                title.value.text,
                memo.value,
                selectedDate,
                alarmTime.value,
                isAlarm.value,
                isComplete.value
            )
            repository.insertSchedule(
                scheduleEntity
            ).collectLatest {
                savedId.invoke(it)
            }
        }
    }


}
