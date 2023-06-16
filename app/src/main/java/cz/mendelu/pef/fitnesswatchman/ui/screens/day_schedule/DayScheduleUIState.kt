package cz.mendelu.pef.fitnesswatchman.ui.screens.day_schedule

import cz.mendelu.pef.fitnesswatchman.model.Exercise

sealed class DayScheduleUIState {
    object Default: DayScheduleUIState()
    object DayLoading: DayScheduleUIState()
    object ExerciseLoading: DayScheduleUIState()
    object ExerciseDeleted: DayScheduleUIState()
    class Success(val exercises: List<Exercise>): DayScheduleUIState()
}