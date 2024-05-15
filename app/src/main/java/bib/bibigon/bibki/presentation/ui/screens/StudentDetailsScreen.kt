package bib.bibigon.bibki.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bib.bibigon.bibki.data.StudentRepositoryImplementation
import bib.bibigon.bibki.data.database.StudentFirebase
import bib.bibigon.bibki.domain.model.Student
import bib.bibigon.bibki.presentation.MainViewModel

@Composable
fun StudentDetailsScreen(
    studentId: Long,
    navigateUp: () -> Unit,
) {
    val viewModel = viewModel<MainViewModel>(StudentRepositoryImplementation(StudentFirebase()))

    AnimatedVisibility(
        visible = true, enter = expandVertically(expandFrom = Alignment.Top,
            initialHeight = { 0 })
    ) {
//        LaunchedEffect(Unit) {
//            when(val student = viewModel.getStudent(studentId)) {
//                is RequestResult.Error -> StudentDetails(navigateUp = navigateUp)
//                is RequestResult.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
//                is RequestResult.Success -> StudentDetails(student = student, navigateUp = navigateUp)
//            }
//        }
    }
}

@Composable
private fun StudentDetails(
    modifier: Modifier = Modifier,
    student: Student? = null,
    navigateUp: () -> Unit
) {
    var firstName by remember { mutableStateOf(student?.firstName ?: "") }
    var lastName by remember { mutableStateOf(student?.lastName ?: "") }
    var dateOfBirth by remember { mutableStateOf(student?.dateOfBirth ?: "") }
    var department by remember { mutableStateOf(student?.department ?: "") }
    var group by remember { mutableStateOf(student?.group ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Person",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )

        if (student == null) {
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(text = "Имя") })
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(text = "Фамилия") })
            TextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = { Text(text = "Дата рождения") })
            TextField(
                value = department,
                onValueChange = { department = it },
                label = { Text(text = "Факультет") })
            TextField(
                value = group,
                onValueChange = { group = it },
                label = { Text(text = "Группа") })
        }
    }
}