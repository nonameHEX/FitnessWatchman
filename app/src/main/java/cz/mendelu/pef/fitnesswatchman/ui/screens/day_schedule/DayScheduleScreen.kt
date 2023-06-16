package cz.mendelu.pef.fitnesswatchman.ui.screens.day_schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.pef.fitnesswatchman.R
import cz.mendelu.pef.fitnesswatchman.model.Day
import cz.mendelu.pef.fitnesswatchman.model.Exercise
import cz.mendelu.pef.fitnesswatchman.navigation.INavigationRouter
import cz.mendelu.pef.fitnesswatchman.ui.theme.ButtonColor
import cz.mendelu.pef.fitnesswatchman.ui.theme.FABColor
import cz.mendelu.pef.fitnesswatchman.ui.theme.RowListColor
import cz.mendelu.pef.fitnesswatchman.ui.theme.TopAppBarColor
import cz.mendelu.pef.fitnesswatchman.utils.DatabaseUtils
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScheduleScreen(
    navigation: INavigationRouter,
    viewModel: DayScheduleViewModel = getViewModel(),
    id: Long
){
    val exercises = remember { mutableListOf<Exercise>() }

    val activityChangeResult = navigation.getNavController()
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("activityChanged")?.observeAsState()

    activityChangeResult?.value?.let {
        LaunchedEffect(it){
            viewModel.possibleFocusChange()
        }

        navigation.getNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>("activityChanged")
    }

    viewModel.dayScheduleUIState.value.let {
        when(it){
            DayScheduleUIState.Default -> {

            }
            DayScheduleUIState.DayLoading -> {
                LaunchedEffect(it){
                    viewModel.getDay(id)
                }
            }
            DayScheduleUIState.ExerciseLoading -> {
                LaunchedEffect(it){
                    viewModel.loadExercises()
                }
            }
            DayScheduleUIState.ExerciseDeleted -> {
                LaunchedEffect(it){
                    viewModel.loadExercises()
                }
            }
            is DayScheduleUIState.Success -> {
                exercises.clear()
                exercises.addAll(it.exercises)
            }
        }
    }
    if (viewModel.dayScheduleUIState.value is DayScheduleUIState.Success) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = viewModel.data.day.name)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigation.returnBack() }){
                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = TopAppBarColor)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = FABColor,
                    onClick = { navigation.navigateToAddEditExercise(viewModel.data.day.id!!,-1L) }
                ){
                    Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
                }
            }
        ) {
            Box(modifier = Modifier
                .padding(it)
                .fillMaxSize()){
                DayScheduleScreenContent(
                    navigation = navigation,
                    day = viewModel.data.day,
                    exercises = exercises,
                    actions = viewModel
                )
            }
        }
    }
}

@Composable
fun DayScheduleScreenContent(
    navigation: INavigationRouter,
    day: Day,
    exercises: List<Exercise>,
    actions: DayScheduleActions
){
    val context = LocalContext.current
    val vectorIconPath = DatabaseUtils.getVectorIconPath(day.activityId)
    val iconResource = context.resources.getIdentifier(vectorIconPath, "drawable", context.packageName)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier
            .padding(top = 10.dp, start = 31.dp, end = 20.dp, bottom = 10.dp)
            .height(90.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(50.dp)
                .background(color = RowListColor)
                .clickable(onClick = {
                    navigation.navigateToChoosePrimaryFocus(day.id!!)
                }),
                contentAlignment = Alignment.Center
            ) {
                Text(text = DatabaseUtils.getFocusPartString(LocalContext.current, day.activityId), fontSize = 18.sp)
            }
            Box(modifier = Modifier
                .padding(start = 30.dp)
                .size(90.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(iconResource),
                    contentDescription = null
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(exercises){
                ExerciseRow(
                    onRowClick = { navigation.navigateToAddEditExercise(day.id!!,it.id) },
                    exercise = it,
                    actions = actions
                )
            }
            item {
                Button(
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 10.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                    onClick = {
                        actions.deleteAllExercises()
                    }) {
                    Text(text = stringResource(R.string.btn_delete_all_exercise_in_day).uppercase(), fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ExerciseRow(
    onRowClick: () -> Unit,
    exercise: Exercise,
    actions: DayScheduleActions
){
    var exerciseStatus: Boolean by remember { mutableStateOf(exercise.exerciseState) }
    Row(
        modifier = Modifier
            .height(60.dp)
            .width(350.dp)
            .background(color = RowListColor)
            .clickable(onClick = onRowClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = exerciseStatus, onCheckedChange = {
            exerciseStatus = it
            actions.changeExerciseStatus(exercise.id!!, it)
        })
        Column() {
            Text(text = exercise.name, fontSize = 18.sp)
            Text(text = "${exercise.numberOfSeries}x${exercise.numberOfRepeats} ${exercise.dumbbellWeight}kg")
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.padding(end = 25.dp)){
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null)
        }
    }
}