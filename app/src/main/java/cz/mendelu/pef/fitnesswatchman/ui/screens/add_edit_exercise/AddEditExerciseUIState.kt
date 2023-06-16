package cz.mendelu.pef.fitnesswatchman.ui.screens.add_edit_exercise

sealed class AddEditExerciseUIState {
    object Default: AddEditExerciseUIState()
    object Saved: AddEditExerciseUIState()
    object Deleted: AddEditExerciseUIState()
    object Changed: AddEditExerciseUIState()
    object Loading: AddEditExerciseUIState()
}