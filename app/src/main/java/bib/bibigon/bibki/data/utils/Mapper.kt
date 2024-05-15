package bib.bibigon.bibki.data.utils

import bib.bibigon.bibki.data.model.StudentDbo
import bib.bibigon.bibki.domain.model.Student

fun Student.toStudentDbo() = StudentDbo(
    id, firstName, lastName, dateOfBirth, department, group
)

fun StudentDbo.toStudent() = Student(id, firstName, lastName, dateOfBirth, department, group)