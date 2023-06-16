package cz.mendelu.pef.fitnesswatchman.ui.screens.day_schedule

interface DayScheduleActions {
    fun deleteAllExercises()
    fun changeExerciseStatus(id: Long, state: Boolean)
    fun possibleFocusChange()
}