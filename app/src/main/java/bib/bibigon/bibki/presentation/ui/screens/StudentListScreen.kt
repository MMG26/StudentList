package bib.bibigon.bibki.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import bib.bibigon.bibki.domain.model.Student
import bib.bibigon.bibki.presentation.MainViewModel
import bib.bibigon.bibki.presentation.State
import kotlinx.coroutines.flow.StateFlow


@Composable
fun StudentListScreen(selectedStudent: (Long) -> Unit, onAddStudent: () -> Unit) {
    val viewModel = viewModel<MainViewModel>()

    StudentListContent(
        onObserveList = viewModel.studentsState,
        selectedStudent = selectedStudent,
        onAddStudent = onAddStudent
    )
}

@Composable
private fun StudentListContent(
    onObserveList: StateFlow<State>,
    selectedStudent: (Long) -> Unit,
    onAddStudent: () -> Unit
) {
    val studentsState = onObserveList.collectAsState()

    when (val state = studentsState.value) {
        is State.Error -> Snackbar { Text(text = "Error occurred") }
        State.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        State.None -> {}
        is State.Success -> StudentList(
            students = state.students,
            selectedStudent = selectedStudent,
            onAddStudent = onAddStudent
        )
    }
}

@Composable
private fun StudentList(
    modifier: Modifier = Modifier,
    students: List<Student>,
    selectedStudent: (Long) -> Unit,
    onAddStudent: () -> Unit
) {
    Box {
        LazyColumn(modifier = modifier) {
            items(students, key = { item: Student -> item.id }) { student ->
                StudentListItem(student = student, onItemClick = selectedStudent)
            }
        }

        Button(onClick = onAddStudent) {
            Text(text = "Add")
        }
    }
}

@Composable
private fun StudentListItem(
    modifier: Modifier = Modifier,
    student: Student,
    onItemClick: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(student.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Text(
                text = "${student.firstName} ${student.lastName}",
                fontSize = 20.sp,
                modifier = Modifier.align(CenterVertically)
            )
        }
    }
}

