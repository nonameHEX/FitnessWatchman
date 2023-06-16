package cz.mendelu.pef.fitnesswatchman.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.pef.fitnesswatchman.ui.screens.add_edit_exercise.AddEditExerciseScreen
import cz.mendelu.pef.fitnesswatchman.ui.screens.choose_primary_focus.ChoosePrimaryFocusScreen
import cz.mendelu.pef.fitnesswatchman.ui.screens.day_schedule.DayScheduleScreen
import cz.mendelu.pef.fitnesswatchman.ui.screens.main_menu.MainMenuScreen
import cz.mendelu.pef.fitnesswatchman.ui.screens.options.OptionsScreen
import cz.mendelu.pef.fitnesswatchman.ui.screens.photo_gallery.PhotoGalleryScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember {
        NavigationRouterImpl(navController)
    },
    startDestination: String
){
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        // Navigation to main menu
        composable(
            route = Destination.MainMenuScreen.route
        ){
            MainMenuScreen(navigation = navigation)
        }

        // Navigation to day schedule
        composable(
            route = Destination.DayScheduleScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                }
            ) // END OF ARGUMENT LIST
        ){
            val id = it.arguments?.getLong("id")
            DayScheduleScreen(
                navigation = navigation,
                id = id!!
            )
        }

        // Navigation to choose primary focus
        composable(
            route = Destination.ChoosePrimaryFocusScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                }
            ) // END OF ARGUMENT LIST
        ){
            val id = it.arguments?.getLong("id")
            ChoosePrimaryFocusScreen(
                navigation = navigation,
                id = id!!
            )
        }

        // Navigation to choose add edit exercise
        composable(
            route = Destination.AddEditExerciseScreen.route + "/{dayId}" + "/{id}",
            arguments = listOf(
                navArgument("dayId"){
                    type = NavType.LongType
                },
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            ) // END OF ARGUMENT LIST
        ){
            val dayId = it.arguments?.getLong("dayId")
            val id = it.arguments?.getLong("id")
            AddEditExerciseScreen(
                navigation = navigation,
                dayId = dayId!!,
                id = if( id != -1L) id else null
            )
        }

        // Navigation to options
        composable(
            route = Destination.OptionsScreen.route
             // END OF ARGUMENT LIST
        ){
            OptionsScreen(
                navigation = navigation
            )
        }

        // Navigation to photo gallery
        composable(
            route = Destination.PhotoGalleryScreen.route,
             // END OF ARGUMENT LIST
        ){
            PhotoGalleryScreen(
                navigation = navigation
            )
        }
    }
}