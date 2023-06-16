package cz.mendelu.pef.fitnesswatchman.ui.screens.day_schedule

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.fitnesswatchman.architecture.BaseViewModel
import cz.mendelu.pef.fitnesswatchman.database.day.IDayRepository
import cz.mendelu.pef.fitnesswatchman.database.exercise.IExerciseRepository
import kotlinx.coroutines.launch

class DayScheduleViewModel(
    private val exerciseRepository: IExerciseRepository,
    private val dayRepository: IDayRepository
    ): BaseViewModel(), DayScheduleActions{
    val dayScheduleUIState: MutableState<DayScheduleUIState> = mutableStateOf(DayScheduleUIState.DayLoading)

    var data: DayScheduleScreenData = DayScheduleScreenData()
    fun getDay(id: Long){
        launch {
            data.day = dayRepository.getDayById(id)
            dayScheduleUIState.value = DayScheduleUIState.ExerciseLoading
        }
    }

    fun loadExercises(){
        launch {
            exerciseRepository.getAllByDay(data.day.id!!).collect(){
                dayScheduleUIState.value = DayScheduleUIState.Success(it)
            }
        }
    }

    override fun deleteAllExercises(){
        launch {
            exerciseRepository.deleteAllByDay(data.day.id!!)
            dayScheduleUIState.value = DayScheduleUIState.ExerciseDeleted
        }
    }

    override fun changeExerciseStatus(id: Long, state: Boolean) {
        launch {
            exerciseRepository.changeState(id, state)
        }
    }

    override fun possibleFocusChange() {
        dayScheduleUIState.value = DayScheduleUIState.DayLoading
    }
}