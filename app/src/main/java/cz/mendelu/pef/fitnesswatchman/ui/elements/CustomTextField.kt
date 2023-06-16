package cz.mendelu.pef.fitnesswatchman.ui.elements

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.fitnesswatchman.ui.theme.TextFieldContainerColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    label: String,
    onValueChange: ((String) -> Unit),
    onClearClick: () -> Unit,
    multiLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
){
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        label = {
            Text(text = label)
        },
        onValueChange = onValueChange,
        trailingIcon = { if (value != null){
            IconButton(onClick = {
                onClearClick()
                focusManager.clearFocus()
            }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }
        }else{
            null
        }
        },
        singleLine = !multiLine,
        minLines = if (multiLine) 9 else 1,
        maxLines = if (multiLine) 9 else 1,
        modifier = if (multiLine) {
            Modifier
                .width(360.dp)
        }else{
            Modifier
                .height(60.dp)
                .width(360.dp)
             },
        colors = TextFieldDefaults.textFieldColors(containerColor = TextFieldContainerColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onAny = { focusManager.clearFocus() })
    )
}