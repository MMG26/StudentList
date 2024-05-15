package bib.bibigon.bibki.presentation

import bib.bibigon.bibki.domain.model.RequestResult
import bib.bibigon.bibki.domain.model.Student

sealed class State {
    object None: State()
    class Error(val error: Throwable? = null): State()
    object Loading: State()
    class Success(val students: List<Student>): State()
}

fun RequestResult<List<Student>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error()
        is RequestResult.Loading -> State.Loading
        is RequestResult.Success -> State.Success(data)
    }
}