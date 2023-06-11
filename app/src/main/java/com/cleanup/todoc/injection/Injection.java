package com.cleanup.todoc.injection;

import android.content.Context;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.repository.ProjectDataRepository;
import com.cleanup.todoc.repository.TaskDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Injecteur de dépendances pour obtenir des instances de modèles de vue.
 */

// Permet de simplifier la gestion des dépendances.
// Facilite la création et la fourniture d'instances de dépendances,
// réduit les dépendances directes entre les classes et favorise la modularité et la réutilisabilité du code
public class Injection {

    /**
     * Crée une instance de {@link ProjectDataRepository} basé sur la base de données Project DAO.
     * @param context le contexte
     * @return l'instance de {@link ProjectDataRepository}
     */
    private static ProjectDataRepository provideProjectDataSource(Context context) {
        // Obtient une instance de base de données via son singleton
        TodocDatabase database = TodocDatabase.getInstance(context);

        // Obtient DAO à partir de la base de données
        // Renvoie une nouvelle instance de ProjectDataRepository avec son DAO
        return new ProjectDataRepository(database.projectDao());
    }

    /**
     * Crée une instance de{@link TaskDataRepository} basé sur la base de données Task DAO.
     * @param context le contexte
     * @return l'instance de {@link TaskDataRepository}
     */
    private static TaskDataRepository provideTaskDataSource(Context context) {
        // Obtient une instance de base de données via son singleton
        TodocDatabase database = TodocDatabase.getInstance(context);

        // Obtient DAO à partir de la base de données
        // Renvoie une nouvelle instance de ProjectDataRepository avec son DAO
        return new TaskDataRepository(database.taskDao());
    }

    /**
     * Crée une instance de {@link Executor}
     * @return l'instance de {@link Executor}
     */
    private static Executor provideExecutor() {
        // Renvoie une nouvelle instance d'un seul exécuteur de thread
        return Executors.newSingleThreadExecutor();
    }

    /**
     * Fournit le {@link ViewModelFactory} afin que le modèle puisse accéder aux sources de données.
     * @param context the context
     * @return the {@link ViewModelFactory}
     */
    public static ViewModelFactory provideViewModelFactory(Context context) {
        // Obtient des instances de ProjectDataRepository et TaskDataRepository
        ProjectDataRepository projectDataSource = provideProjectDataSource(context);
        TaskDataRepository taskDataSource = provideTaskDataSource(context);

        // Obtient l'instance de l'exécuteur
        Executor executor = provideExecutor();

        // Renvoie une nouvelle instance de ViewModelFactory avec ProjectDataRepository, TaskDataRepository et Executor
        return new ViewModelFactory(projectDataSource, taskDataSource, executor);
    }
}
