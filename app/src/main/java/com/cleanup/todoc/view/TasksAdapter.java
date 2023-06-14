package com.cleanup.todoc.view;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * cette classe gère l'affichage des tâches dans le RecyclerView et permet leur suppression
 * en utilisant un écouteur approprié.
 * Elle est utilisée par la classe MainActivity pour afficher et manipuler les tâches dans l'application.
 * Le TaskAdaper est un sous-traitant de la MAinActivity, seule la MainActivity communique avec le ViewModel.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    /**
     * The list of tasks the adapter deals with
     */
    @NonNull
    private List<Task> tasks;

    /**
     * The list of projects the adapter deals with
     */
    @NonNull
    // Ajout d'une liste java simple représentant les projets qui sera mise à jour dynamiquement
    // grâce à la méthode updateProject() par l'appelant (MainActivity)
    private List<Project> projects;

    /**
     * The listener for when a task needs to be deleted
     */
    @NonNull
    private final DeleteTaskListener deleteTaskListener;

    /**
     * Instantiates a new TasksAdapter
     */
    TasksAdapter(@NonNull final DeleteTaskListener deleteTaskListener) {
        this.tasks = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.deleteTaskListener = deleteTaskListener;
    }

    /**
     * cette méthode met à jour la liste de tâches actuelle avec une nouvelle liste fournie
     * et actualise l'affichage pour refléter les modifications
     */
    void updateTasks(@NonNull final List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    // cette méthode met à jour la liste de projets actuelle avec une nouvelle liste fournie
    // et force la regénération du RecyclerView pour refléter les modifications et mettre à jour l'affichage.
    // Permettre à l'appelant (MainActivity) de mettre à jour la liste de projet de cet adapteur
    void updateProjects(@NonNull final List<Project> projects) {
        this.projects = projects;
        notifyDataSetChanged(); // Forcer Android à regénérer le RecyclerView
    }

    /**
     * Méthode appelée lorsqu'un nouvel élément de la liste des tâches doit être affiché à l'écran.
     * Elle crée et retourne un TaskViewHolder qui contient la vue individuelle de l'élément de la liste à afficher.
     */
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);
        return new TaskViewHolder(view, deleteTaskListener);
    }

    /**
     * Méthode appelée lorsqu'une regénération du RecyclerView est demandée
     * Appelée autant de fois par Android qu'il y a d'éléments dans la liste (Tasks) 
     */
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int position) {
        // On ajoute la liste de projets dynamique au Bind du ViewHolder
        // pour rappel cette méthode Bind crée un élément de la liste du RecyclerView à l'écran
        taskViewHolder.bind(tasks.get(position), projects);
    }

    /**
     * la méthode renvoie le nombre d'éléments présents dans la liste
     */
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    /**
     * Ecouteur de suppression de tâches
     */
    public interface DeleteTaskListener {
       void onDeleteTask(Task task);
    }

    /**
     * <p>ViewHolder for task items in the tasks list</p>
     *
     * @author Gaëtan HERFRAY
     */
    class TaskViewHolder extends RecyclerView.ViewHolder {
        /**
         * The circle icon showing the color of the project
         */
        private final AppCompatImageView imgProject;

        /**
         * The TextView displaying the name of the task
         */
        private final TextView lblTaskName;

        /**
         * The TextView displaying the name of the project
         */
        private final TextView lblProjectName;

        /**
         * The delete icon
         */
        private final AppCompatImageView imgDelete;

        /**
         * The listener for when a task needs to be deleted
         */
        private final DeleteTaskListener deleteTaskListener;

        /**
         *  le constructeur de TaskViewHolder initialise les vues de l'élément et définit un écouteur de clic
         *  sur l'image de suppression qui lors d'un clic, la tâche associée est envoyée au deleteTaskListener pour être supprimée.
         */
        TaskViewHolder(@NonNull View itemView, @NonNull DeleteTaskListener deleteTaskListener)                              {
            super(itemView);

            this.deleteTaskListener = deleteTaskListener;

            imgProject = itemView.findViewById(R.id.img_project);
            lblTaskName = itemView.findViewById(R.id.lbl_task_name);
            lblProjectName = itemView.findViewById(R.id.lbl_project_name);
            imgDelete = itemView.findViewById(R.id.img_delete);

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Object tag = view.getTag();
                    if (tag instanceof Task) {
                        TaskViewHolder.this.deleteTaskListener.onDeleteTask((Task) tag);
                    }
                }
            });
        }

        /**
         *  la méthode bind() est utilisée pour mettre à jour les éléments de la vue du TaskViewHolder
         *  avec les données d'une tâche spécifique. Elle affiche le nom de la tâche, associe la tâche à l'image de suppression,
         *  recherche et affiche le projet associé à la tâche à partir de la liste des projets.
         */
        // Ajout de la liste projects pour utiliser une liste dynamique présente dans l'adapter
        // qui elle même est mise à jour par la MainActivty, en cas de changements sur le LiveData (Project)
        void bind(Task task, List<Project> projects){
            lblTaskName.setText(task.getName());
            imgDelete.setTag(task);

            // On recherche le projet dans la liste des projets qui a le même ID que le projet de la tâche spécifiée,
            // et stocke celui-ci dans la variable taskProject
            Project taskProject = null;
            for(Project project : projects){
                if(project.getId() == task.getProjectId()) {
                    taskProject = project;
                    break;
                }
            }
            //  lorsque le projet associé à la tâche est trouvé, on met à jour la couleur de l'image
            //  et le texte de l'étiquette pour refléter les informations du projet.
            if (taskProject != null) {
                imgProject.setSupportImageTintList(ColorStateList.valueOf(taskProject.getColor()));
                lblProjectName.setText(taskProject.getName());
            } else {
                //  lorsque aucun projet n'a été trouvé pour la tâche, on rend l'image du projet invisible
                //  et efface le texte de l'étiquette du projet dans la vue.
                imgProject.setVisibility(View.INVISIBLE);
                lblProjectName.setText("");
            }

        }
    }
}