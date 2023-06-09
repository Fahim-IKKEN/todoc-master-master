package com.cleanup.todoc.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.ProjectDataRepository;
import com.cleanup.todoc.repository.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Cette classe utilise les repositories de manière à proposer des méthodes getTasks ou encore getProjects.
 * Son constructeur permet de passer les repositories.
 */
public class TaskViewModel extends ViewModel {

    // Références à des sources de données pour les projets et les tâches (Repositories)
    private final ProjectDataRepository mProjectDataSource;
    private final TaskDataRepository mTaskDataSource;

    // Exécution des tâches asynchrones
    private final Executor mExecutor;

    // Classe du framework Android qui représente une donnée observable,
    @Nullable
    private LiveData<List<Project>> mProjects;

    // Initialise des variables membres avec les sources de données des projets et tâches, ainsi qu'un objet Executor
    public TaskViewModel(ProjectDataRepository projectDataSource, TaskDataRepository taskDataSource, Executor executor) {
        mProjectDataSource = projectDataSource;
        mTaskDataSource = taskDataSource;
        mExecutor = executor;
    }

    // la méthode init() vérifie si les projets ont été initialisés,
    // sinon elle les récupère à partir de la source de données des projets et les stocke dans la variable mProjects.
    public void init() {
        if (mProjects == null)
            mProjects = mProjectDataSource.getProjects();
    }


    // Cette méthode retourne un objet LiveData contenant une liste de projets.
    // Elle est utilisée pour accéder aux projets de manière observable, de sorte que les observateurs peuvent être informés
    // des changements de la liste de projets et prendre des mesures appropriées.
    @Nullable
    public LiveData<List<Project>> getProjects() {
        return mProjects;
    }

    // Cette méthode retourne un objet LiveData contenant une liste de tâches.
    // Elle est utilisée pour accéder aux tâches de manière observable, de sorte que les observateurs peuvent être informés
    // des changements de la liste de tâches et prendre des mesures appropriées.
    public LiveData<List<Task>> getTasks() {
        return mTaskDataSource.getTasks();
    }


     // Création d'une nouvelle tâche de manière asynchrone en utilisant un Executor
    public void createTask(Task task) {
        mExecutor.execute(() -> mTaskDataSource.createTask(task));
    }

    // Cette méthode est appelée pour l'exécution de la tâche de suppression de manière asynchrone en utilisant l'objet mExecutor.
    // Cela permet de déléguer la suppression tout en continuant à exécuter le reste du code de manière non bloquante.
    public void deleteTask(Task task) {
        mExecutor.execute(() -> mTaskDataSource.deleteTask(task));
    }
}
