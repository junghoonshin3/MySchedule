package kr.sjh.myschedule.ui.screen.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.repository.ScheduleRepository
import kr.sjh.myschedule.utill.Common.ADD_PAGE
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val userId = savedStateHandle.get<Long>("userId")

    private val _schedule = MutableStateFlow<ScheduleEntity?>(null)
    val schedule: StateFlow<ScheduleEntity?> = _schedule

    var title = MutableStateFlow("")

    var memo = MutableStateFlow("")

    var isAlarm = MutableStateFlow(false)

    var alarmTime = MutableStateFlow(LocalDateTime.now())

    var isComplete = MutableStateFlow(false)

    init {
        userId?.let { uId ->
            if (uId != ADD_PAGE) {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.getSchedule(uId).collectLatest { schdule ->
                        Log.i("sjh", "userId ok >>>>>>>>>>>>>>>>> $schdule")
                        schdule.let {
                            uId
                            title.value = it.title
                            memo.value = it.memo
                            isAlarm.value = it.isAlarm
                            alarmTime.value = it.alarmTime
                            isComplete.value = it.isComplete
                        }
                    }
                }
            }
        }
    }

    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSchedule(
                ScheduleEntity(
                    userId ?: ADD_PAGE,
                    title.value,
                    memo.value,
                    LocalDate.now(),
                    alarmTime.value,
                    isAlarm.value,
                    isComplete.value
                )
            ).collectLatest {

            }
        }
    }


}
