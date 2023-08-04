package kr.sjh.myschedule.ui.screen.schedule

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleDetailEntity
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.repository.ScheduleRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {

    private val _scheduleList = MutableStateFlow<List<ScheduleEntity>>(emptyList())
    val scheduleList: StateFlow<List<ScheduleEntity>> = _scheduleList

    init {
        getAllScheduleWithDetailDao(LocalDate.now())

    }

    private fun getAllScheduleWithDetailDao(localDate: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllScheduleWithDetailDao(LocalDate.now()).collectLatest {
                _scheduleList.value = it
            }
        }
    }

    fun insertSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSchedule(schedule)
        }
    }

}