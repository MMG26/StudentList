package bib.bibigon.bibki.data.database

import android.util.Log
import bib.bibigon.bibki.data.model.StudentDbo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StudentFirebase {
    private val studentCollectionRef = Firebase.firestore.collection("students")

    fun add(student: StudentDbo) = CoroutineScope(Dispatchers.IO).launch {
        try {
            studentCollectionRef.add(student).await()
        } catch (e: Exception) {
            Log.w("Firebase", "${e.message}")
        }
    }

    fun delete(student: StudentDbo) = CoroutineScope(Dispatchers.IO).launch {
        val query = studentCollectionRef.whereEqualTo("student", student).get().await()

        try {
            if (query.documents.isNotEmpty()) {
                for (document in query.documents) {
                    studentCollectionRef.document(document.id).delete().await()
                }
            }
        } catch (e: Exception) {
            Log.w("Firebase", "${e.message}")
        }
    }

    fun edit(student: StudentDbo) = CoroutineScope(Dispatchers.IO).launch {
        val studentQuery = studentCollectionRef.whereEqualTo("student", student).get()
            .await()

        if (studentQuery.documents.isNotEmpty()) {
            for (document in studentQuery) {
                try {
                    //personCollectionRef.document(document.id).update("age", newAge).await()
                    studentCollectionRef.document(document.id).set(
                        student
                    ).await()
                } catch (e: Exception) {
                    Log.w("Firebase", "${e.message}")
                }
            }
        } else {
            Log.w("Firebase", "Nothing found")
        }
    }

    fun getAll() = flow {
        val query = studentCollectionRef.get().await()
        val studentList = mutableListOf<StudentDbo>()

        if (query.documents.isNotEmpty()) {
            try {
                for (document in query.documents) {
                    document.toObject<StudentDbo>()?.let {
                        studentList.add(it)
                    }
                }

                emit(Result.success(studentList.toList()))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        } else {
            emit(Result.failure(Throwable("Empty list of students")))
        }
    }


    suspend fun getStudent(studentId: Long): Result<StudentDbo> {
        val query = studentCollectionRef.whereEqualTo("id", studentId).get().await()

        if (query.documents.isNotEmpty()) {
            try {
                query.documents.last().toObject<StudentDbo>()?.let {
                    return Result.success(it)
                }
            } catch (e: Exception) {
                Log.w("Firebase", "${e.message}")
            }
        }
        return Result.failure(Throwable("Student with $studentId not found"))
    }
}