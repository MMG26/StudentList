package bib.bibigon.bibki.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bib.bibigon.bibki.data.StudentRepositoryImplementation
import bib.bibigon.bibki.domain.model.Student
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repositoryImplementation: StudentRepositoryImplementation
) : ViewModel() {

    val studentsState: StateFlow<State> = repositoryImplementation.getAll()
        .map { requestResult -> requestResult.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    fun editStudent(student: Student) {
        viewModelScope.launch {
            repositoryImplementation.edit(student)
        }
    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            repositoryImplementation.add(student)
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            repositoryImplementation.delete(student)
        }
    }

    suspend fun getStudent(studentId: Long) = repositoryImplementation.getStudent(studentId)
}

