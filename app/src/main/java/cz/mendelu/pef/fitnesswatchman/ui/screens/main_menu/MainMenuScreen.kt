package cz.mendelu.pef.fitnesswatchman.ui.screens.main_menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import cz.mendelu.pef.fitnesswatchman.navigation.INavigationRouter
import cz.mendelu.pef.fitnesswatchman.ui.theme.ButtonColor
import cz.mendelu.pef.fitnesswatchman.ui.theme.RowListColor
import cz.mendelu.pef.fitnesswatchman.ui.theme.TopAppBarColor
import cz.mendelu.pef.fitnesswatchman.utils.DatabaseUtils
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    navigation: INavigationRouter,
    viewModel: MainMenuViewModel = getViewModel()
){
    val days = remember{ mutableListOf<Day>() }

    viewModel.mainMenuState.value.let {
        when(it){
            MainMenuUIState.Default -> {
                LaunchedEffect(it){
                    viewModel.loadDays()
                }
            }
            is MainMenuUIState.Success -> {
                days.clear()
                days.addAll(it.days)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.main_menu_title))
                },
                navigationIcon = {
                    IconButton(enabled = false, onClick = {}){}
                },
                actions = {
                    IconButton(onClick = { navigation.navigateToOptions() }) {
                        Icon(Icons.Filled.Settings, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TopAppBarColor)
            )
        }
    ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()){
            MainMenuScreenContent(days = days, navigation = navigation)
        }
    }
}

@Composable
fun MainMenuScreenContent(
    days: List<Day>,
    navigation: INavigationRouter,
    ){
    LazyColumn(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ){
        items(days) { day ->
            DayRow(
                onRowClick = { navigation.navigateToDaySchedule(day.id!!) },
                day = day
            )
        }
        item {
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .width(360.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                onClick = {
                    navigation.navigateToPhotoGallery()
                }) {
                Text(text = stringResource(R.string.photo_gallery_title).uppercase(), fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun DayRow(
    onRowClick: () -> Unit,
    day: Day
){
    val context = LocalContext.current
    val vectorIconPath = DatabaseUtils.getVectorIconPath(day.activityId)
    val iconResource = context.resources.getIdentifier(vectorIconPath, "drawable", context.packageName)
    Row(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(color = RowListColor)
            .clickable(onClick = onRowClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.padding(start = 20.dp, top = 3.dp, bottom = 3.dp),
            painter = painterResource(iconResource),
            contentDescription = null
        )
        Column(modifier = Modifier.padding(start = 30.dp)) {
            Text(text = day.name, fontSize = 18.sp)
            Text(text = DatabaseUtils.getFocusPartString(LocalContext.current, day.activityId))
        }
    }
}