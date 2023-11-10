package kr.sjh.myschedule.ui.screen.bottomsheet

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kr.sjh.myschedule.data.repository.ScheduleRepository
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(private val repository: ScheduleRepository) :
    ViewModel() {
    private var _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private var _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    fun changeTitle(title: String) {
        _title.update {
            title
        }
    }

    fun clickDate(date: LocalDate) {
        _selectedDate.update {
            date
        }
    }
}
