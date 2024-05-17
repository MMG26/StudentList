package bib.bibigon.bibki.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import bib.bibigon.bibki.data.StudentRepositoryImplementation
import bib.bibigon.bibki.data.database.StudentFirebase
import bib.bibigon.bibki.domain.model.Student
import kotlinx.coroutines.async
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

    fun getStudent(studentId: Long) = viewModelScope.async {
        repositoryImplementation.getStudent(studentId)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return MainViewModel(
                    repositoryImplementation = StudentRepositoryImplementation(StudentFirebase())
                ) as T
            }
        }
    }
}

