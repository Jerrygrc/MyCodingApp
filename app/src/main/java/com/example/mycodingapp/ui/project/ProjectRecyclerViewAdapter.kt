package com.example.mycodingapp.ui.project

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycodingapp.databinding.ItemProjectBinding
import com.example.mycodingapp.model.Project

class ProjectRecyclerViewAdapter(
    private val projects: MutableList<Project>,
    private val onDeleteClick: (Project) -> Unit,
    private val onProjectClick: (Project) -> Unit
): RecyclerView.Adapter<ProjectRecyclerViewAdapter.ProjectViewHolder>() {

        inner class ProjectViewHolder( val binding: ItemProjectBinding) :
            RecyclerView.ViewHolder(binding.root){
                init {
                    binding.btnBorrarProyecto.setOnClickListener {
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            onDeleteClick(projects[position])
                        }
                    }
                    binding.root.setOnClickListener {
                        val position = adapterPosition

                        if (position != RecyclerView.NO_POSITION) {
                            onProjectClick(projects[position])
                        }
                    }
                }
            }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectRecyclerViewAdapter.ProjectViewHolder {
        val binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ProjectRecyclerViewAdapter.ProjectViewHolder,
        position: Int
    ) {
        val project = projects[position]
        holder.binding.tvItemProjectName.text = project.nombre
        holder.binding.tvItemDescriptionProject.text = project.descripcion
        holder.binding.tvItemInitialDate.text = project.fecha
        holder.binding.tvItemPriority.text = project.prioridad
        holder.binding.tvItemTimeHours.text = project.tiempoEmplear.toString()
        holder.binding.tvItemLanguage.text = project.lenguaje
    }

    override fun getItemCount(): Int = projects.size

}