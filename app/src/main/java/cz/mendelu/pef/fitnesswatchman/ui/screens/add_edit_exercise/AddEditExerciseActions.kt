package cz.mendelu.pef.fitnesswatchman.ui.screens.add_edit_exercise

interface AddEditExerciseActions {
    fun onExerciseNameChange(name: String)
    fun onExerciseNameClear()
    fun onSeriesChange(series: String)
    fun onSeriesClear()
    fun onRepeatsChange(repeats: String)
    fun onRepeatsClear()
    fun onWeightChange(weight: String)
    fun onWeightClear()
    fun onCommentChange(comment: String)
    fun onCommentClear()
    fun exerciseSave()
    fun exerciseDelete()
}