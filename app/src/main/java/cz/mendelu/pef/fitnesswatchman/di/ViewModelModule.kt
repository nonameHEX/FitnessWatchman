package cz.mendelu.pef.fitnesswatchman.di

import cz.mendelu.pef.fitnesswatchman.ui.screens.choose_primary_focus.ChoosePrimaryFocusViewModel
import cz.mendelu.pef.fitnesswatchman.ui.screens.day_schedule.DayScheduleViewModel
import cz.mendelu.pef.fitnesswatchman.ui.screens.add_edit_exercise.AddEditExerciseViewModel
import cz.mendelu.pef.fitnesswatchman.ui.screens.main_menu.MainMenuViewModel
import cz.mendelu.pef.fitnesswatchman.ui.screens.options.OptionsViewModel
import cz.mendelu.pef.fitnesswatchman.ui.screens.photo_gallery.PhotoGalleryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainMenuViewModel(get()) }
    viewModel { DayScheduleViewModel(get(),get()) }
    viewModel { ChoosePrimaryFocusViewModel(get()) }
    viewModel { AddEditExerciseViewModel(get()) }
    viewModel { OptionsViewModel(get(),get(),get()) }
    viewModel { PhotoGalleryViewModel(get()) }
}