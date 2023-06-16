package cz.mendelu.pef.fitnesswatchman.navigation

sealed class Destination(val route: String){
    object MainMenuScreen: Destination(route = "main_menu")
    object DayScheduleScreen: Destination(route = "day_schedule")
    object ChoosePrimaryFocusScreen: Destination(route = "choose_primary_focus")
    object AddEditExerciseScreen: Destination(route = "add_edit_exercise")
    object OptionsScreen: Destination(route = "options")
    object PhotoGalleryScreen: Destination(route = "photo_gallery")
}