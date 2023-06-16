package cz.mendelu.pef.fitnesswatchman.ui.screens.choose_primary_focus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.pef.fitnesswatchman.R
import cz.mendelu.pef.fitnesswatchman.navigation.INavigationRouter
import cz.mendelu.pef.fitnesswatchman.ui.elements.BackArrowScreen
import cz.mendelu.pef.fitnesswatchman.ui.theme.RowListColor
import cz.mendelu.pef.fitnesswatchman.utils.DatabaseUtils
import org.koin.androidx.compose.getViewModel

@Composable
fun ChoosePrimaryFocusScreen(
    navigation: INavigationRouter,
    id: Long,
    viewModel: ChoosePrimaryFocusViewModel = getViewModel()
){
    BackArrowScreen(
        topBarTitle = stringResource(R.string.primary_focus_title),
        onBackClick = { navigation.returnBack() }
    ) {
        ChoosePrimaryFocusScreenContent(
            navigation = navigation,
            id = id,
            actions = viewModel
        )
    }
}

@Composable
fun ChoosePrimaryFocusScreenContent(
    navigation: INavigationRouter,
    id: Long,
    actions: ChoosePrimaryFocusActions
){
    val focusPartMap = DatabaseUtils.getFocusPartMap(LocalContext.current)
    LazyColumn(modifier = Modifier
        .padding(top = 40.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ){
        items(focusPartMap){(partId, partString) ->
            PartRow(
                partString = partString,
                partId = partId,
                onRowClick = {
                    actions.changePrimaryFocus(id, partId)
                    navigation.returnFromChoosePrimaryFocus(true)
                }
            )
        }
    }
}

@Composable
fun PartRow(
    partString: String,
    partId: Long,
    onRowClick: () -> Unit
) {
    val context = LocalContext.current
    val vectorIconPath = DatabaseUtils.getVectorIconPath(partId)
    val iconResource = context.resources.getIdentifier(vectorIconPath, "drawable", context.packageName)

    Row(
        modifier = Modifier
            .height(80.dp)
            .width(340.dp)
            .background(color = RowListColor)
            .clickable(onClick = onRowClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .padding(3.dp),
            painter = painterResource(iconResource),
            contentDescription = null
        )
        Column(modifier = Modifier.padding(start = 80.dp)) {
            Text(text = partString, fontSize = 18.sp)
        }
    }
}