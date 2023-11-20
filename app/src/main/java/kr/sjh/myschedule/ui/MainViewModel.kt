package kr.sjh.myschedule.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kr.sjh.myschedule.data.repository.ScheduleRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ScheduleRepository) : ViewModel() {

    private var _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private var _alarmTime = MutableStateFlow(LocalTime.now().withSecond(0))
    val alarmTime: StateFlow<LocalTime> = _alarmTime

    private var _dateSelection = MutableStateFlow(DateSelection())
    val dateSelection: StateFlow<DateSelection> = _dateSelection

    fun setDateSelection(dateSelection: DateSelection) {
        _dateSelection.update { dateSelection }
    }

    fun setAlarmTime(alarmTime: LocalTime) {
        _alarmTime.update { alarmTime }
    }

    fun changeTitle(title: String) {
        _title.update {
            title
        }
    }
}

data class DateSelection(var startDate: LocalDateTime? = null, var endDate: LocalDateTime? = null)