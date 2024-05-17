package bib.bibigon.bibki.data.model

data class StudentDbo(
    val id: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: String = "",
    val department: String = "",
    val group: String = ""
)
