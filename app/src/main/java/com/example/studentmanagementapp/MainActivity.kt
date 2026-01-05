package com.example.studentmanagementapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etAge = findViewById<EditText>(R.id.etAge)
        val etGrade = findViewById<EditText>(R.id.etGrade)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnView = findViewById<Button>(R.id.btnView)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter { student ->
            showOptionsDialog(student)
        }
        recyclerView.adapter = studentAdapter

        loadStudents()

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val age = etAge.text.toString().toIntOrNull()
            val grade = etGrade.text.toString()

            if (name.isNotEmpty() && age != null && grade.isNotEmpty()) {
                databaseHelper.insertStudent(name, age, grade)
                etName.text.clear()
                etAge.text.clear()
                etGrade.text.clear()
                loadStudents()
            }
        }

        btnView.setOnClickListener {
            loadStudents()
        }
    }

    private fun loadStudents() {
        val students = databaseHelper.getStudents()
        studentAdapter.submitList(students)
    }

    private fun showOptionsDialog(student: Student) {
        val options = arrayOf("Update", "Delete")

        AlertDialog.Builder(this)
            .setTitle("Choose Action")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> showUpdateDialog(student)
                    1 -> showDeleteConfirmationDialog(student)
                }
            }
            .show()
    }

    private fun showUpdateDialog(student: Student) {
        val layout = layoutInflater.inflate(R.layout.dialog_update, null)
        val etName = layout.findViewById<EditText>(R.id.etUpdateName)
        val etAge = layout.findViewById<EditText>(R.id.etUpdateAge)
        val etGrade = layout.findViewById<EditText>(R.id.etUpdateGrade)

        etName.setText(student.name)
        etAge.setText(student.age.toString())
        etGrade.setText(student.grade)

        AlertDialog.Builder(this)
            .setTitle("Update Student")
            .setView(layout)
            .setPositiveButton("Update") { dialog, which ->
                val newName = etName.text.toString()
                val newAge = etAge.text.toString().toIntOrNull()
                val newGrade = etGrade.text.toString()

                if (newName.isNotEmpty() && newAge != null && newGrade.isNotEmpty()) {
                    databaseHelper.updateStudent(student.id, newName, newAge, newGrade)
                    loadStudents()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(student: Student) {
        AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.name}?")
            .setPositiveButton("Delete") { dialog, which ->
                databaseHelper.deleteStudent(student.id)
                loadStudents()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
