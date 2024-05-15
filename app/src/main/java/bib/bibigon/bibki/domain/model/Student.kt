package bib.bibigon.bibki.domain.model

data class Student(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val department: String,
    val group: String
)
