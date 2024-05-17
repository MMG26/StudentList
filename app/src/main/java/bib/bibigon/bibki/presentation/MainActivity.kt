package bib.bibigon.bibki.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import bib.bibigon.bibki.presentation.ui.navigation.AppNavigation
import bib.bibigon.bibki.presentation.ui.theme.BibkiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibkiTheme {
                Scaffold {
                    AppNavigation(modifier = Modifier.padding(it))
                }
            }
        }
    }
}