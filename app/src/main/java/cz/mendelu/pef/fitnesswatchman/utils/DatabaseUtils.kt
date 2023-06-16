package cz.mendelu.pef.fitnesswatchman.utils

import android.content.Context
import cz.mendelu.pef.fitnesswatchman.R

object DatabaseUtils {
    private val focusPartMap = mapOf(
        1L to R.string.focus_part_rest_day,
        2L to R.string.focus_part_shoulders,
        3L to R.string.focus_part_arm,
        4L to R.string.focus_part_back,
        5L to R.string.focus_part_abdomen,
        6L to R.string.focus_part_legs
    )

    fun getVectorIconPath(activityId: Long): String {
        return when (activityId) {
            1L -> "icon_rest_day"
            2L -> "icon_shoulders"
            3L -> "icon_arm"
            4L -> "icon_back"
            5L -> "icon_abdomen"
            6L -> "icon_legs"
            else -> "icon_rest_day" // výchozí hodnota
        }
    }
    fun getDefaultFocusPartId(): Long {
        return 1L // Vrací hodnotu co véme že je rest day
    }

    fun getFocusPartString(context: Context, activityId: Long): String {
        val resourceId = focusPartMap[activityId] ?: R.string.focus_part_rest_day
        return context.getString(resourceId)
    }

    fun getFocusPartMap(context: Context): List<Pair<Long, String>> {
        return focusPartMap.map { entry ->
            val resourceId = entry.value
            val stringValue = context.getString(resourceId)
            entry.key to stringValue
        }
    }
}