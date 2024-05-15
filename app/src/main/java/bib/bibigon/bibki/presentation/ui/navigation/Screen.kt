package bib.bibigon.bibki.presentation.ui.navigation

sealed class Screen(val route: String) {
    object StudentList: Screen("studentList")
    object StudentDetails: Screen("studentDetails")
}