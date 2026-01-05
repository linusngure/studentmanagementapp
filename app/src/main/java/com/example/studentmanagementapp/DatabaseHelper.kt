package com.example.studentmanagementapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1 // Resetting version for the new DB
        private const val DATABASE_NAME = "students-v2.db" // New database name
        private const val TABLE_STUDENTS = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_GRADE = "grade"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_STUDENTS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_AGE INTEGER," +
                "$COLUMN_GRADE TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    fun insertStudent(name: String, age: Int, grade: String) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_AGE, age)
            put(COLUMN_GRADE, grade)
        }
        db.insert(TABLE_STUDENTS, null, cv)
    }

    fun getStudents(): List<Student> {
        val studentList = mutableListOf<Student>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_STUDENTS"
        db.rawQuery(query, null).use { cursor ->
            if (cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_ID)
                val nameColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME)
                val ageColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_AGE)
                val gradeColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_GRADE)

                do {
                    val id = cursor.getInt(idColumnIndex)
                    val name = cursor.getString(nameColumnIndex)
                    val age = cursor.getInt(ageColumnIndex)
                    val grade = cursor.getString(gradeColumnIndex)
                    studentList.add(Student(id, name, age, grade))
                } while (cursor.moveToNext())
            }
        }
        return studentList
    }

    fun updateStudent(id: Int, name: String, age: Int, grade: String) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_AGE, age)
            put(COLUMN_GRADE, grade)
        }
        db.update(TABLE_STUDENTS, cv, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteStudent(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_STUDENTS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
