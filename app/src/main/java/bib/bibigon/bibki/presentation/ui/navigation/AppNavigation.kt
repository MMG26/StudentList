package bib.bibigon.bibki.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import bib.bibigon.bibki.presentation.ui.screens.StudentDetailsScreen
import bib.bibigon.bibki.presentation.ui.screens.StudentListScreen

private const val STUDENT_DETAIL_ID_KEY = "studentId"

@Composable
fun AppNavigation(startDestination: String = Screen.StudentList.route) {
    val navController = rememberNavController()
    val actions = remember(navController) { AppActions(navController) }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.StudentList.route) {
            StudentListScreen(selectedStudent = actions.selectedStudent, onAddStudent = actions.navigateUp)
        }
        composable("${Screen.StudentDetails}/$STUDENT_DETAIL_ID_KEY}", arguments = listOf(
            navArgument(STUDENT_DETAIL_ID_KEY) { type = NavType.LongType }
        )) { navBackStackEntry ->
            val arguments = requireNotNull(navBackStackEntry.arguments)

            StudentDetailsScreen(
                studentId = arguments.getLong(STUDENT_DETAIL_ID_KEY),
                navigateUp = actions.navigateUp
            )
        }
    }
}

private class AppActions(
    navController: NavHostController
) {
    val selectedStudent: (Long) -> Unit = { studentId: Long ->
        navController.navigate("${Screen.StudentDetails.route}/$studentId")
    }
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}
