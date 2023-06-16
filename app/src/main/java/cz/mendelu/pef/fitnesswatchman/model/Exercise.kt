package cz.mendelu.pef.fitnesswatchman.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "exercises",
    foreignKeys = [ForeignKey(entity = Day::class,
    parentColumns = ["id"],
    childColumns = ["at_day_id"]
    )]
)
data class Exercise(
    @ColumnInfo("name")
    var name: String
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Long? = null

    @ColumnInfo("at_day_id")
    var atDayId: Long? = null // Klíč odkazující na ID dne

    @ColumnInfo("number_of_series")
    var numberOfSeries: Int = 0

    @ColumnInfo("number_of_repeats")
    var numberOfRepeats: Int = 0

    @ColumnInfo("dumbbell_weight")
    var dumbbellWeight: Int = 0

    @ColumnInfo("info_for_next_session")
    var infoForNextSession: String = ""

    @ColumnInfo("exercise_state")
    var exerciseState: Boolean = false
}