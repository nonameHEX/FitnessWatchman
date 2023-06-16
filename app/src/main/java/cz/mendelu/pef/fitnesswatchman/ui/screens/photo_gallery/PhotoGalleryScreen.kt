package cz.mendelu.pef.fitnesswatchman.ui.screens.photo_gallery

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import cz.mendelu.pef.fitnesswatchman.R
import cz.mendelu.pef.fitnesswatchman.navigation.INavigationRouter
import cz.mendelu.pef.fitnesswatchman.ui.theme.ButtonColor
import cz.mendelu.pef.fitnesswatchman.ui.theme.FABColor
import cz.mendelu.pef.fitnesswatchman.ui.theme.TopAppBarColor
import org.koin.androidx.compose.getViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryScreen(
    navigation: INavigationRouter,
    viewModel: PhotoGalleryViewModel = getViewModel()
){
    viewModel.photoGalleryUIState.value.let {
        when(it){
            PhotoGalleryUIState.Default -> {

            }
            PhotoGalleryUIState.PhotosLoading -> {
                LaunchedEffect(it){
                    viewModel.loadGallery()
                }
            }
            is PhotoGalleryUIState.LoadSuccess -> {
                LaunchedEffect(it){
                    viewModel.photoNames.value = it.photoNames
                    viewModel.photoGalleryUIState.value = PhotoGalleryUIState.Default
                }
            }
        }
    }

    val context = LocalContext.current
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            val updatedUris = uris.filter { uri ->
                !viewModel.photoNames.value.contains(uri.lastPathSegment)
            }
            if(updatedUris.isNotEmpty()){
                for(uri in updatedUris){
                    val input = context.contentResolver.openInputStream(uri)
                    val currentTimeStamp = System.currentTimeMillis()
                    val fileName = "image$currentTimeStamp"
                    val outputFile = File(context.filesDir, fileName)
                    outputFile.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                    viewModel.photoNames.value = viewModel.photoNames.value + fileName
                    viewModel.saveGallery()
                }
            }
        }
    )

    if(viewModel.photoGalleryUIState.value is PhotoGalleryUIState.Default){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.photo_gallery_title)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigation.returnBack()
                        }){
                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                        } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = TopAppBarColor)
                ) },
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = FABColor,
                    onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ){
                    Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
                }
            }
        ){
            Box(modifier = Modifier
                .padding(it)
                .fillMaxSize()){
                PhotoGalleryScreenContent(
                    actions = viewModel,
                    photoList = viewModel.photoNames.value
                )
            }
        }
    }
}

@Composable
fun PhotoGalleryScreenContent(
    actions: PhotoGalleryActions,
    photoList: List<String>
) {
    Box(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(top = 65.dp, bottom = 60.dp),
            columns = GridCells.Fixed(4)
        ) {
            items(photoList) { name ->
                PhotoItem(
                    actions = actions,
                    name = name
                )
            }
        }
        Button(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(50.dp)
                .width(360.dp),
            colors = if (!actions.isDeleteMode()) {
                ButtonDefaults.buttonColors(containerColor = ButtonColor)
            } else {
                ButtonDefaults.buttonColors(containerColor = Color.Red)
            },
            onClick = { actions.switchDeleteMode() }
        ) {
            Text(
                text = if (!actions.isDeleteMode()) {
                    stringResource(R.string.btn_delete_photos_from_gallery).uppercase()
                } else {
                    stringResource(R.string.btn_stop_deleting_photos_from_gallery).uppercase()
                },
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun PhotoItem(
    actions: PhotoGalleryActions,
    name: String
) {
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDialog.value) {
        Dialog(
            onDismissRequest = { showDialog.value = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showDialog.value = false },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = File(context.filesDir,name).absolutePath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }

    Box(modifier = Modifier
        .size(85.dp)
        .padding(5.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        AsyncImage(
            model = File(context.filesDir,name).absolutePath,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
                .clickable {
                    if (actions.isDeleteMode()) {
                        actions.deleteSinglePhoto(context, name)
                    } else {
                        showDialog.value = true
                    }
                },
            contentScale = ContentScale.Crop,
        )
        if(actions.isDeleteMode()){
            Image(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Red)
            )
        }
    }
}