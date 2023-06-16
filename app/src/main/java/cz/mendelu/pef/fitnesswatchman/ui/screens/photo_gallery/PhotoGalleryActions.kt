package cz.mendelu.pef.fitnesswatchman.ui.screens.photo_gallery

import android.content.Context

interface PhotoGalleryActions {
    fun switchDeleteMode()
    fun isDeleteMode(): Boolean
    fun deleteSinglePhoto(context: Context, name: String)
}