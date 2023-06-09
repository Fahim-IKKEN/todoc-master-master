package com.cleanup.todoc.injection;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.repository.ProjectDataRepository;
import com.cleanup.todoc.repository.TaskDataRepository;
import com.cleanup.todoc.viewmodel.TaskViewModel;

import java.util.concurrent.Executor;

/**
 * La Factory pour ViewModels indispensable pour que le ViewModel puisse accéder à la source de données
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    /**
     * La source de données du projet
     */
    private final ProjectDataRepository mProjectDataSource;

    /**
     * La source de données de la tâche
     */
    private final TaskDataRepository mTaskDataSource;

    /**
     * L'exécuteur
     */
    private final Executor mExecutor;

    /**
     * Constructeur
     * @param projectDataSource la source de données du projet
     * @param taskDataSource la source de données de la tâche
     * @param executor l'exécuteur testamentaire
     */
    public ViewModelFactory(ProjectDataRepository projectDataSource, TaskDataRepository taskDataSource, Executor executor) {
        mProjectDataSource = projectDataSource;
        mTaskDataSource = taskDataSource;
        mExecutor = executor;
    }

    /**
     * Créer une nouvelle instance {@code Class}.
     * @param modelClass le {@code Class} dont l'instance est demandée
     * @param <T> Le paramètre de type pour le ViewModel.
     * @return un ViewModel nouvellement créé
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        // Créer un ViewModel pour la classe TaskViewModel
        if (modelClass.isAssignableFrom(TaskViewModel.class)) {
            return (T) new TaskViewModel(mProjectDataSource, mTaskDataSource, mExecutor);
        }

        // Si la classe ViewModel est inconnue, lancez une exception
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
