package com.dra.speakeaseapppatient.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dra.speakeaseapppatient.model.LocalizedStrings
import com.dra.speakeaseapppatient.ui.components.IconTextButton
import com.dra.speakeaseapppatient.utils.TextToSpeechHelper
import com.dra.speakeaseapppatient.viewmodel.ProfileViewModel
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PersonScreen(
    textToSpeechHelper: TextToSpeechHelper,
    viewModel: ProfileViewModel = viewModel(),
    selectedLocale: MutableState<Locale>
) {
    val buttonItems = LocalizedStrings.getPersonButtonLabels(selectedLocale.value)
    var showLanguageDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showLanguageDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Select Language"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(buttonItems) { (iconRes, description) ->
                IconTextButton(
                    iconRes = iconRes,
                    text = description,
                    onClick = {
                        textToSpeechHelper.speak(description)

                        val timestamp = System.currentTimeMillis()
                        viewModel.saveHistoryItem(description, timestamp)
                    }
                )
            }
        }
    }

    if (showLanguageDialog) {
        LanguagePickerDialog(
            currentLocale = selectedLocale.value,
            onLanguageSelected = { locale ->
                textToSpeechHelper.setLanguage(locale)
                selectedLocale.value = locale
            },
            onDismiss = { showLanguageDialog = false }
        )
    }
}

