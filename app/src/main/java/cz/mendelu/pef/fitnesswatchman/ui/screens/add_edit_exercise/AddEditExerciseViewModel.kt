package cz.mendelu.pef.fitnesswatchman.ui.screens.add_edit_exercise

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.fitnesswatchman.architecture.BaseViewModel
import cz.mendelu.pef.fitnesswatchman.database.exercise.IExerciseRepository
import kotlinx.coroutines.launch

class AddEditExerciseViewModel(
    private val repository: IExerciseRepository
    ): BaseViewModel(), AddEditExerciseActions{

    var exerciseId: Long? = null

    var dayId: Long? = null

    var data: AddEditExerciseScreenData = AddEditExerciseScreenData()

    var addEditExerciseUIState: MutableState<AddEditExerciseUIState> = mutableStateOf(AddEditExerciseUIState.Loading)

    fun initExercise(){
        if(exerciseId != null){
            launch {
                // exercise už existuje, proto příprava na edit
                data.exercise = repository.getExerciseById(exerciseId!!)
                data.loading = false
                addEditExerciseUIState.value = AddEditExerciseUIState.Changed
            }
        }else{
            data.loading = false
            addEditExerciseUIState.value = AddEditExerciseUIState.Changed
        }
    }

    override fun onExerciseNameChange(name: String) {
        if(name.length < 21){
            data.exercise.name = name
            addEditExerciseUIState.value = AddEditExerciseUIState.Changed
        }
    }

    override fun onExerciseNameClear() {
        data.exercise.name = ""
        addEditExerciseUIState.value = AddEditExerciseUIState.Changed
    }

    override fun onSeriesChange(series: String) {
        if(series.length < 5){
            data.exercise.numberOfSeries = series.toInt()
            addEditExerciseUIState.value = AddEditExerciseUIState.Changed
        }
    }

    override fun onSeriesClear() {
        data.exercise.numberOfSeries = 0
        addEditExerciseUIState.value = AddEditExerciseUIState.Changed
    }

    override fun onRepeatsChange(repeats: String) {
        if(repeats.length < 5){
            data.exercise.numberOfRepeats = repeats.toInt()
            addEditExerciseUIState.value = AddEditExerciseUIState.Changed
        }
    }

    override fun onRepeatsClear() {
        data.exercise.numberOfRepeats = 0
        addEditExerciseUIState.value = AddEditExerciseUIState.Changed
    }

    override fun onWeightChange(weight: String) {
        if(weight.length < 5){
            data.exercise.dumbbellWeight = weight.toInt()
            addEditExerciseUIState.value = AddEditExerciseUIState.Changed
        }
    }

    override fun onWeightClear() {
        data.exercise.dumbbellWeight = 0
        addEditExerciseUIState.value = AddEditExerciseUIState.Changed
    }

    override fun onCommentChange(comment: String) {
        if(comment.length < 271){
            data.exercise.infoForNextSession = comment
            addEditExerciseUIState.value = AddEditExerciseUIState.Changed
        }
    }

    override fun onCommentClear() {
        data.exercise.infoForNextSession = ""
        addEditExerciseUIState.value = AddEditExerciseUIState.Changed
    }

    override fun exerciseSave() {
        if (data.exercise.name.isEmpty()){

        }else{
            launch {
                if (exerciseId != null){
                    // UPDATE
                    repository.update(data.exercise)
                    addEditExerciseUIState.value = AddEditExerciseUIState.Saved
                }else{
                    // INSERT
                    data.exercise.atDayId = dayId
                    val id = repository.insert(data.exercise)
                    if (id > 0){
                        addEditExerciseUIState.value = AddEditExerciseUIState.Saved
                    }else{
                        // error
                    }
                }
            }
        }
    }

    override fun exerciseDelete() {
        launch {
            repository.delete(data.exercise)
            addEditExerciseUIState.value = AddEditExerciseUIState.Deleted
        }
    }
}