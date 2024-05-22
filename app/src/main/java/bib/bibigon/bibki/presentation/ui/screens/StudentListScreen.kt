package bib.bibigon.bibki.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
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
fun StudentListScreen(
    modifier: Modifier = Modifier,
    selectedStudent: (Long) -> Unit,
    onAddStudent: () -> Unit
) {
    val viewModel = viewModel<MainViewModel>(
        factory = MainViewModel.Factory
    )

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddStudent) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddings ->
        StudentListContent(
            modifier = Modifier.padding(paddings),
            onObserveList = viewModel.studentsState,
            selectedStudent = selectedStudent,
            onDeletedButtonPressed = { viewModel.deleteStudent(it) }
        )
    }
}

@Composable
private fun StudentListContent(
    modifier: Modifier = Modifier,
    onObserveList: StateFlow<State>,
    selectedStudent: (Long) -> Unit,
    onDeletedButtonPressed: (Student) -> Unit
) {
    val studentsState = onObserveList.collectAsState()

    when (val state = studentsState.value) {
        is State.Error -> EmptyStudentList(
            text = "${state.error}"
        )
        is State.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(modifier = Modifier.size(60.dp)) }
        is State.None -> EmptyStudentList(modifier = modifier, text = "None")
        is State.Success -> StudentList(
            students = state.students,
            selectedStudent = selectedStudent,
            onDeletedButtonPressed = onDeletedButtonPressed
        )
    }
}

@Composable
fun EmptyStudentList(
    modifier: Modifier = Modifier, text: String
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = if (text == "null") "Empty list" else text)
        Spacer(
            modifier = Modifier
                .height(24.dp)
                .weight(1f)
        )
    }
}

@Composable
private fun StudentList(
    modifier: Modifier = Modifier,
    students: List<Student>,
    selectedStudent: (Long) -> Unit,
    onDeletedButtonPressed: (Student) -> Unit
) {
    Column {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            items(students, key = { item: Student -> item.id }) { student ->
                StudentListItem(
                    student = student,
                    onItemClick = selectedStudent,
                    onDeletedButtonPressed = onDeletedButtonPressed
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(24.dp)
                .weight(1f)
        )
    }
}

@Composable
private fun StudentListItem(
    modifier: Modifier = Modifier,
    student: Student,
    onItemClick: (Long) -> Unit,
    onDeletedButtonPressed: (Student) -> Unit
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
            Column(modifier = Modifier.align(CenterVertically)) {
                Text(
                    text = "${student.lastName} ${student.firstName}",
                    fontSize = 20.sp,
                )
                Text(
                    text = student.group,
                    fontSize = 12.sp,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                onClick = { onDeletedButtonPressed(student) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .clip(CircleShape)
                )
            }
        }
    }
}

