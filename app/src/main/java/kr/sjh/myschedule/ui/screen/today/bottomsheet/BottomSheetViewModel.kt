package kr.sjh.myschedule.ui.screen.today.bottomsheet

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.repository.ScheduleRepository
import kr.sjh.myschedule.ui.screen.today.generateRandomColor
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject


@HiltViewModel
class BottomSheetViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {
    fun onSave(
        textFieldText: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        isAlarm: Boolean,
        alarmTime: LocalTime,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var currentDateTime = startDateTime
            val scheduleList = mutableListOf<ScheduleEntity>()
            val color = generateRandomColor().toArgb()
            while (!currentDateTime.isAfter(endDateTime)) {
                val item = ScheduleEntity(
                    title = textFieldText,
                    year = currentDateTime.year,
                    month = currentDateTime.monthValue,
                    regDt = currentDateTime.toLocalDate(),
                    color = color,
                    isAlarm = isAlarm,
                    alarmTime = alarmTime
                )
                scheduleList.add(
                    item
                )
                currentDateTime = currentDateTime.plusDays(1)
            }
            repository.insertScheduleList(scheduleList).collectLatest { }
        }

    }
}