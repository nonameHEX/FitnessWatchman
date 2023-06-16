package cz.mendelu.pef.fitnesswatchman.ui.screens.photo_gallery

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import cz.mendelu.pef.fitnesswatchman.architecture.BaseViewModel
import cz.mendelu.pef.fitnesswatchman.datastore.IDataStoreRepository
import kotlinx.coroutines.launch
import java.io.File

class PhotoGalleryViewModel(
    private val repository: IDataStoreRepository
    ): BaseViewModel(), PhotoGalleryActions{
    val photoGalleryUIState: MutableState<PhotoGalleryUIState> = mutableStateOf(PhotoGalleryUIState.PhotosLoading)
    var deleteMode: Boolean = false

    var photoNames: MutableState<List<String>> = mutableStateOf(emptyList())

    fun loadGallery(){
        launch {
            repository.selectedPhotosFlow.collect{
                photoGalleryUIState.value = PhotoGalleryUIState.LoadSuccess(it.toList())
            }
        }
    }

    fun saveGallery(){
        launch {
            repository.saveSelectedPhotos(photoNames.value.toSet())
        }
    }

    override fun switchDeleteMode() {
        deleteMode = !deleteMode
        photoGalleryUIState.value = PhotoGalleryUIState.PhotosLoading
    }

    override fun isDeleteMode(): Boolean {
        return deleteMode
    }

    override fun deleteSinglePhoto(context: Context, name: String) {
        launch {
            deletePhotoFromFile(context, name)

            val updatedPhotoNames = photoNames.value.toMutableList()
            updatedPhotoNames.remove(name)
            photoNames.value = updatedPhotoNames

            repository.saveSelectedPhotos(photoNames.value.toSet())
            photoGalleryUIState.value = PhotoGalleryUIState.PhotosLoading
        }
    }

    private fun deletePhotoFromFile(context: Context, name: String) {
        val fileToDelete = File(context.filesDir, name)
        fileToDelete.delete()
    }
}