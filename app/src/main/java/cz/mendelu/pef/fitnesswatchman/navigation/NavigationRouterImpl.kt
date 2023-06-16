package cz.mendelu.pef.fitnesswatchman.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController): INavigationRouter{
    override fun getNavController(): NavController = navController

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun navigateToDaySchedule(id: Long) {
        navController.navigate(Destination.DayScheduleScreen.route + "/" + id)
    }

    override fun navigateToChoosePrimaryFocus(id: Long) {
        navController.navigate(Destination.ChoosePrimaryFocusScreen.route + "/" + id)
    }

    override fun returnFromChoosePrimaryFocus(activityChanged: Boolean) {
        navController.previousBackStackEntry?.savedStateHandle?.set("activityChanged", activityChanged)
        returnBack()
    }

    override fun navigateToAddEditExercise(dayId: Long, id: Long?) {
        navController.navigate(Destination.AddEditExerciseScreen.route + "/" + dayId + "/" + id)
    }

    override fun navigateToOptions() {
        navController.navigate(Destination.OptionsScreen.route)
    }

    override fun navigateToPhotoGallery() {
        navController.navigate(Destination.PhotoGalleryScreen.route)
    }

}