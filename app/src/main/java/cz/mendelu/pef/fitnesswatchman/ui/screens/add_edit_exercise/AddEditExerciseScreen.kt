package cz.mendelu.pef.fitnesswatchman.ui.screens.add_edit_exercise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.pef.fitnesswatchman.R
import cz.mendelu.pef.fitnesswatchman.navigation.INavigationRouter
import cz.mendelu.pef.fitnesswatchman.ui.elements.BackArrowScreen
import cz.mendelu.pef.fitnesswatchman.ui.elements.CustomTextField
import cz.mendelu.pef.fitnesswatchman.ui.theme.ButtonColor
import org.koin.androidx.compose.getViewModel

@Composable
fun AddEditExerciseScreen(
    navigation: INavigationRouter,
    dayId: Long,
    id: Long?,
    viewModel: AddEditExerciseViewModel = getViewModel()
){
    viewModel.exerciseId = id
    viewModel.dayId = dayId

    var data: AddEditExerciseScreenData by remember{ mutableStateOf(viewModel.data) }

    viewModel.addEditExerciseUIState.value.let {
        when(it){
            AddEditExerciseUIState.Default -> {
            }
            AddEditExerciseUIState.Loading -> {
                LaunchedEffect(it) {
                    viewModel.initExercise()
                }
            }
            AddEditExerciseUIState.Changed -> {
                data = viewModel.data
                viewModel.addEditExerciseUIState.value = AddEditExerciseUIState.Default
            }
            AddEditExerciseUIState.Saved -> {
                LaunchedEffect(it) {
                    navigation.returnBack()
                }
            }
            AddEditExerciseUIState.Deleted -> {
                LaunchedEffect(it) {
                    navigation.returnBack()
                }
            }
        }
    }

    BackArrowScreen(
        topBarTitle = if(viewModel.exerciseId == null) stringResource(R.string.add_exercise_title) else stringResource(
                    R.string.edit_exercise_title),
        onBackClick = { navigation.returnBack() }
    ) {
        AddEditExerciseScreenContent(
            navigation = navigation,
            actions = viewModel,
            data = data
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExerciseScreenContent(
    navigation: INavigationRouter,
    actions: AddEditExerciseActions,
    data: AddEditExerciseScreenData
){
    if (!data.loading){
        LazyColumn(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            item {
                CustomTextField(
                    value = data.exercise.name,
                    label = stringResource(R.string.exercise_name),
                    onValueChange = { actions.onExerciseNameChange(it) },
                    onClearClick = { actions.onExerciseNameClear() }
                )
            }
            item {
                CustomTextField(
                    value = data.exercise.numberOfSeries.toString(),
                    label = stringResource(R.string.series_count),
                    onValueChange = {
                        try {
                            if (it.isBlank()){
                                actions.onSeriesChange("0")
                            }else{
                                actions.onSeriesChange(it)
                            }
                        }catch (e: NumberFormatException){
                            // Ignorujeme jakýkoliv jiný znak
                        }
                    },
                    onClearClick = { actions.onSeriesClear() },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
            item {
                CustomTextField(
                    value = data.exercise.numberOfRepeats.toString(),
                    label = stringResource(R.string.repeat_count),
                    onValueChange = {
                        try {
                            if (it.isBlank()){
                                actions.onRepeatsChange("0")
                            }else{
                                actions.onRepeatsChange(it)
                            }
                        }catch (e: NumberFormatException){
                            // Ignorujeme jakýkoliv jiný znak
                        }
                    },
                    onClearClick = { actions.onRepeatsClear() },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
            item {
                CustomTextField(
                    value = data.exercise.dumbbellWeight.toString(),
                    label = stringResource(R.string.weight_count),
                    onValueChange = {
                        try {
                            if (it.isBlank()){
                                actions.onWeightChange("0")
                            }else{
                                actions.onWeightChange(it)
                            }
                        }catch (e: NumberFormatException){
                            // Ignorujeme jakýkoliv jiný znak
                        }
                    },
                    onClearClick = { actions.onWeightClear() },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
            item {
                CustomTextField(
                    value = data.exercise.infoForNextSession,
                    label = stringResource(R.string.reminder_info),
                    onValueChange = { actions.onCommentChange(it) },
                    onClearClick = { actions.onCommentClear() },
                    multiLine = true
                )
            }
            item {
                Row {
                    Button(
                        modifier = Modifier
                            .padding(start = 40.dp)
                            .height(50.dp)
                            .width(120.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        onClick = { actions.exerciseDelete() }
                    ) {
                        Text(text = stringResource(R.string.btn_delete).uppercase(), fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier
                            .padding(end = 40.dp)
                            .height(50.dp)
                            .width(120.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        onClick = { actions.exerciseSave() },
                        enabled = data.exercise.name.isNotEmpty()
                    ) {
                        Text(text = stringResource(R.string.btn_save).uppercase(), fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
    }
}