package bib.bibigon.bibki.data

import bib.bibigon.bibki.data.database.StudentFirebase
import bib.bibigon.bibki.data.model.StudentDbo
import bib.bibigon.bibki.data.utils.toStudent
import bib.bibigon.bibki.data.utils.toStudentDbo
import bib.bibigon.bibki.domain.StudentRepository
import bib.bibigon.bibki.domain.model.RequestResult
import bib.bibigon.bibki.domain.model.Student
import bib.bibigon.bibki.domain.model.map
import bib.bibigon.bibki.domain.model.toRequestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class StudentRepositoryImplementation(
    private val database: StudentFirebase
) : StudentRepository {
    override suspend fun add(student: Student) {
        database.add(student.toStudentDbo())
    }

    override suspend fun delete(student: Student) {
        database.delete(student.toStudentDbo())
    }

    override suspend fun edit(student: Student) {
        database.edit(student.toStudentDbo())
    }

    override fun getAll(): Flow<RequestResult<List<Student>>> {
        val start = flowOf<RequestResult<List<StudentDbo>>>(RequestResult.Loading())

        val request = database.getAll().map { result ->
            result.toRequestResult()
        }

        return merge(start, request).map { requests ->
            requests.map { students ->
                students.map {
                    it.toStudent()
                }
            }
        }
    }

    override suspend fun getStudent(studentId: Long): RequestResult<Student> {
        return database.getStudent(studentId).toRequestResult().map { it.toStudent() }
    }
}