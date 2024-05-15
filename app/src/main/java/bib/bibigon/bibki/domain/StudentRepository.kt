package bib.bibigon.bibki.domain

import bib.bibigon.bibki.domain.model.RequestResult
import bib.bibigon.bibki.domain.model.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {

    suspend fun add(student: Student)

    suspend fun delete(student: Student)

    suspend fun edit(student: Student)

    fun getAll(): Flow<RequestResult<List<Student>>>

    suspend fun getStudent(studentId: Long): RequestResult<Student>
}