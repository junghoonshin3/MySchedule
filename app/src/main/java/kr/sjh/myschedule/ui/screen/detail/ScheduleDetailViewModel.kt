package kr.sjh.myschedule.ui.screen.detail

import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.repository.ScheduleRepository
import kr.sjh.myschedule.utill.Common.ADD_PAGE
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

data class DetailUiState(
    val schedule: ScheduleEntity = ScheduleEntity()
)

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private var schedule = savedStateHandle.get<ScheduleEntity>("schedule") ?: ScheduleEntity()

//    private var _selectedDate = savedStateHandle.get<LocalDate>("selectedDate") ?: LocalDate.now()

    var _title =
        MutableStateFlow(
            TextFieldValue(
                text = schedule.title,
                selection = TextRange(schedule.title.length)
            )
        )

    var _memo = MutableStateFlow(schedule.memo)

    var _isAlarm = MutableStateFlow(schedule.isAlarm)

    var _alarmTime = MutableStateFlow(schedule.alarmTime)

    var _isComplete = MutableStateFlow(schedule.isComplete)


    fun onSaveSchedule(onSavedId: (ScheduleEntity) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            if (schedule.id == ADD_PAGE) {
                val newItem = schedule.apply {
                    title = _title.value.text
                    memo = _memo.value
                    isAlarm = _isAlarm.value
                    regDt = _alarmTime.value.toLocalDate()
                    alarmTime = _alarmTime.value
                    isComplete = _isComplete.value
                }
                repository.insertSchedule(newItem).map {
                    newItem.id = it
                }.flowOn(Dispatchers.IO).collectLatest {
                    onSavedId(newItem)
                }
            } else {
                val currentItem = schedule.apply {
                    title = _title.value.text
                    memo = _memo.value
                    isAlarm = _isAlarm.value
                    regDt = _alarmTime.value.toLocalDate()
                    alarmTime = _alarmTime.value
                    isComplete = _isComplete.value
                }
                repository.updateSchedule(currentItem).flowOn(Dispatchers.IO).collectLatest {
                    onSavedId(currentItem)
                }
            }
        }
    }
}
