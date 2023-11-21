package kr.sjh.myschedule.ui

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.repository.Result
import kr.sjh.myschedule.data.repository.ScheduleRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ScheduleRepository) : ViewModel() {

    private var _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private var _alarmTime = MutableStateFlow(LocalTime.now().withSecond(0))
    val alarmTime: StateFlow<LocalTime> = _alarmTime

    private var _dateSelection = MutableStateFlow(DateSelection())
    val dateSelection: StateFlow<DateSelection> = _dateSelection

    private var _monthScheduleMap = MutableStateFlow(emptyMap<Int, List<ScheduleEntity>>())
    val monthScheduleMap: StateFlow<Map<Int, List<ScheduleEntity>>> = _monthScheduleMap

    init {
        getYearScheduleMap(YearMonth.now())
    }

    fun getYearScheduleMap(yearMonth: YearMonth) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getYearSchedules(yearMonth.year).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _monthScheduleMap.update {
                            result.data.groupBy {
                                it.month
                            }
                        }
                    }

                    is Result.Fail -> {
                        result.throwable
                    }

                    is Result.Loading -> {

                    }
                }
            }
        }
    }

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

    fun onSave(
        yearMonth: YearMonth,
        regDt: LocalDate,
        color: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertOrUpdate(
                ScheduleEntity(
                    title = title.value,
                    memo = "",
                    year = yearMonth.year,
                    month = yearMonth.month.value,
                    regDt = regDt,
                    color = color,
                    alarmTime = alarmTime.value,
                    isAlarm = false,
                    isComplete = false
                ), dateSelection.value
            ).collectLatest {

            }
        }
    }
}

data class DateSelection(var startDate: LocalDateTime? = null, var endDate: LocalDateTime? = null)