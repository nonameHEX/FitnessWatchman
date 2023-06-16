package cz.mendelu.pef.fitnesswatchman.ui.screens.options

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.pef.fitnesswatchman.BuildConfig
import cz.mendelu.pef.fitnesswatchman.R
import cz.mendelu.pef.fitnesswatchman.navigation.INavigationRouter
import cz.mendelu.pef.fitnesswatchman.ui.elements.BackArrowScreen
import cz.mendelu.pef.fitnesswatchman.ui.elements.CustomTextField
import cz.mendelu.pef.fitnesswatchman.ui.theme.ButtonColor
import cz.mendelu.pef.fitnesswatchman.ui.theme.RowListColor
import org.koin.androidx.compose.getViewModel

@Composable
fun OptionsScreen(
    navigation: INavigationRouter,
    viewModel: OptionsViewModel = getViewModel()
){
    var data: OptionsScreenData by remember{ mutableStateOf(viewModel.data) }

    viewModel.optionsUIState.value.let {
        when(it){
            OptionsUIState.Default -> {

            }
            OptionsUIState.OptionsLoading -> {
                LaunchedEffect(it){
                    viewModel.loadOptions()
                }
            }
            OptionsUIState.OptionsChanged -> {
                LaunchedEffect(it){
                    data = viewModel.data
                    viewModel.optionsUIState.value = OptionsUIState.Default
                }
            }
        }
    }
    BackArrowScreen(
        topBarTitle = stringResource(R.string.options_title),
        onBackClick = {
            viewModel.saveOptions()
            navigation.returnBack()
        }
    ) {
        OptionsScreenContent(
            actions = viewModel,
            data = data,
            appVersion = BuildConfig.VERSION_NAME
        )
    }
}

@Composable
fun OptionsScreenContent(
    actions: OptionsActions,
    data: OptionsScreenData,
    appVersion: String
){
    Column(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Text(
            text = "${stringResource(R.string.version)}: $appVersion",
            fontSize = 16.sp,
            color = Color.Gray
        )
        CustomTextField(
            value = data.userName,
            label = stringResource(R.string.welcome_name),
            onValueChange = { actions.onNameChange(it) },
            onClearClick = { actions.onNameClear() }
        )
        Row(
            modifier = Modifier
                .height(60.dp)
                .width(360.dp)
                .background(color = RowListColor),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(text = stringResource(R.string.rotate_exercises))
                Text(text = stringResource(R.string.rotate_exercises_info), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = data.rotateStatus,
                onCheckedChange = { actions.changeRotateStatus(it) }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .height(50.dp)
                .width(360.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            onClick = {
                actions.deleteAllPhotos()
            }) {
            Text(text = stringResource(R.string.btn_delete_all_photos_from_gallery).uppercase(), fontSize = 18.sp, color = Color.White)
        }

        Button(
            modifier = Modifier
                .height(50.dp)
                .width(360.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            onClick = {
                actions.resetAllSchedule()
            }) {
            Text(text = stringResource(R.string.btn_reset_all_schedule).uppercase(), fontSize = 18.sp, color = Color.White)
        }
    }
}