package bib.bibigon.bibki.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import bib.bibigon.bibki.R
import bib.bibigon.bibki.domain.model.RequestResult
import bib.bibigon.bibki.domain.model.Student
import bib.bibigon.bibki.presentation.MainViewModel
import kotlin.random.Random
import kotlin.random.nextLong

@Composable
fun StudentDetailsScreen(
    modifier: Modifier = Modifier,
    studentId: Long,
    navigateUp: () -> Unit,
) {
    val viewModel = viewModel<MainViewModel>(factory = MainViewModel.Factory)

    val studentResult by produceState<RequestResult<Student>>(initialValue = RequestResult.Loading(), studentId) {
        value = viewModel.getStudent(studentId).await()
    }

    if (studentId > 0) {
        when (val student = studentResult) {
            is RequestResult.Error -> Snackbar { Text(text = "Error occurred: ${student.error}") }
            is RequestResult.Loading -> Snackbar { Text(text = "Loading") }
            is RequestResult.Success -> EditStudent(
                modifier = modifier,
                student = student.data,
                navigateUp = navigateUp,
                onEditStudent = { viewModel.editStudent(it) })
        }
    } else {
        AddStudent(
            modifier = modifier,
            navigateUp = navigateUp, onAddStudent = { viewModel.addStudent(it) })
    }
}

@Composable
fun AddStudent(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onAddStudent: (Student) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Person",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(text = stringResource(R.string.last_name_text)) },
            singleLine = true)
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(text = stringResource(R.string.first_name_text)) },
            singleLine = true)
        TextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text(text = stringResource(R.string.date_of_birth_text)) },
            singleLine = true)
        TextField(
            value = department,
            onValueChange = { department = it },
            label = { Text(text = stringResource(R.string.departament_text)) },
            singleLine = true)
        TextField(
            value = group,
            onValueChange = { group = it },
            label = { Text(text = stringResource(R.string.group_text)) },
            singleLine = true)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 16.dp),
            enabled = !(firstName.isBlank() || lastName.isBlank() || dateOfBirth.isBlank() || department.isBlank() || group.isBlank()),
            onClick = {
                onAddStudent(
                    Student(
                        id = Random.nextLong(1..1000L),
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        dateOfBirth = dateOfBirth.trim(),
                        department = department.trim(),
                        group = group.trim()
                    )
                )
                navigateUp()
            }) {
            Text(text = stringResource(R.string.add_student_text_button))
        }
    }
}

@Composable
fun EditStudent(
    modifier: Modifier = Modifier,
    student: Student,
    navigateUp: () -> Unit,
    onEditStudent: (Student) -> Unit
) {
    var firstName by remember { mutableStateOf(student.firstName) }
    var lastName by remember { mutableStateOf(student.lastName) }
    var dateOfBirth by remember { mutableStateOf(student.dateOfBirth) }
    var department by remember { mutableStateOf(student.department) }
    var group by remember { mutableStateOf(student.group) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Person",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(text = stringResource(R.string.last_name_text)) },
            singleLine = true)
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(text = stringResource(R.string.first_name_text)) },
            singleLine = true)
        TextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text(text = stringResource(R.string.date_of_birth_text)) },
            singleLine = true)
        TextField(
            value = department,
            onValueChange = { department = it },
            label = { Text(text = stringResource(R.string.departament_text)) },
            singleLine = true)
        TextField(
            value = group,
            onValueChange = { group = it },
            label = { Text(text = stringResource(R.string.group_text)) },
            singleLine = true)


        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 16.dp),
            enabled = !(firstName.isBlank() || lastName.isBlank() || dateOfBirth.isBlank() || department.isBlank() || group.isBlank()),
            onClick = {
                onEditStudent(
                    Student(
                        id = student.id,
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        dateOfBirth = dateOfBirth.trim(),
                        department = department.trim(),
                        group = group.trim()
                    )
                )

                navigateUp()
            }) {
            Text(text = stringResource(R.string.edit_student_text_button))
        }
    }
}