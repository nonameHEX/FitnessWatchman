package cz.mendelu.pef.fitnesswatchman.ui.screens.photo_gallery

sealed class PhotoGalleryUIState {
    object Default: PhotoGalleryUIState()
    object PhotosLoading: PhotoGalleryUIState()
    class LoadSuccess(val photoNames: List<String>): PhotoGalleryUIState()
}