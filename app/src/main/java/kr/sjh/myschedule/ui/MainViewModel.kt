package kr.sjh.myschedule.ui

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private var _yearScheduleList = MutableStateFlow(emptyList<ScheduleEntity>())
    val yearScheduleList: StateFlow<List<ScheduleEntity>> = _yearScheduleList

    private var _schedule = MutableStateFlow<ScheduleEntity?>(null)
    val schedule: StateFlow<ScheduleEntity?> = _schedule.asStateFlow()

    fun setSchedule(scheduleEntity: ScheduleEntity) {
        _schedule.value = scheduleEntity
    }

    fun getYearSchedulesInRange(startYear: Int, endYear: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getYearSchedulesInRange(startYear, endYear).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _yearScheduleList.value = result.data
                    }

                    is Result.Fail -> {
                        result.throwable.printStackTrace()
                    }

                    is Result.Loading -> {

                    }
                }
            }
        }
    }
}