package com.example.studentmanagementapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val onItemClick: (Student) -> Unit
) : ListAdapter<Student, StudentAdapter.StudentViewHolder>(StudentDiffCallback()) {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameText)
        val age: TextView = itemView.findViewById(R.id.ageText)
        val grade: TextView = itemView.findViewById(R.id.gradeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = getItem(position)
        holder.name.text = student.name
        holder.age.text = student.age.toString()
        holder.grade.text = student.grade
        holder.itemView.setOnClickListener { onItemClick(student) }
    }
}

class StudentDiffCallback : DiffUtil.ItemCallback<Student>() {
    override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem == newItem
    }
}