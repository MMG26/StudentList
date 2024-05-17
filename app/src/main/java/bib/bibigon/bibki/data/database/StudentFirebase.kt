package bib.bibigon.bibki.data.database

import android.util.Log
import bib.bibigon.bibki.data.model.StudentDbo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
        val query = studentCollectionRef.whereEqualTo("id", student.id).get().await()

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
        val studentQuery = studentCollectionRef.whereEqualTo("id", student.id).get()
            .await()

        if (studentQuery.documents.isNotEmpty()) {
            for (document in studentQuery) {
                try {
                    studentCollectionRef.document(document.id)
                        .update("firstName", student.firstName).await()
                    studentCollectionRef.document(document.id).update("lastName", student.lastName)
                        .await()
                    studentCollectionRef.document(document.id)
                        .update("dateOfBirth", student.dateOfBirth).await()
                    studentCollectionRef.document(document.id)
                        .update("department", student.department).await()
                    studentCollectionRef.document(document.id).update("group", student.group)
                        .await()
                } catch (e: Exception) {
                    Log.w("Firebase", "${e.message}")
                }
            }
        } else {
            Log.w("Firebase", "Nothing found")
        }
    }

    fun getAll(): Flow<Result<List<StudentDbo>>> = callbackFlow {
        val registration = studentCollectionRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                trySend(Result.failure(e)).isFailure
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val studentList =
                    snapshot.documents.mapNotNull { it.toObject(StudentDbo::class.java) }
                trySend(Result.success(studentList)).isSuccess
            } else {
                trySend(Result.failure(Throwable("No data found"))).isSuccess
            }
        }

        awaitClose { registration.remove() }
    }

    suspend fun getStudent(studentId: Long): Result<StudentDbo> {
        val query = studentCollectionRef.whereEqualTo("id", studentId).get().await()
        if (query.documents.isNotEmpty()) {
            try {
                query.documents.last().toObject<StudentDbo>()?.let {
                    return Result.success(it)
                }
            } catch (e: Exception) {
                Result.failure<StudentDbo>(Throwable("${e.message}"))
            }
        } else {
            Result.failure<StudentDbo>(Throwable("Student with $studentId not found"))
        }

        return Result.failure(Throwable("Student with $studentId not found"))
    }
}