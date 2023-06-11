package com.cleanup.todoc.repository;


import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

/**
 * Cette classe fournit une interface pour accéder et manipuler les données des tâches.
 * Elle utilise un objet TaskDao pour interagir avec la source de données des tâches
 * et expose des méthodes pour obtenir, créer et supprimer des tâches.
 */

// Le Repository encapsule la logique de récupération et de gestion des données provenant de différentes
// sources telles que des bases de données, des services Web ou des caches.
// Favorise une bonne séparation des responsabilités
public class TaskDataRepository {

    private final TaskDao mTaskDao;

    public TaskDataRepository(TaskDao taskDao) {
        mTaskDao = taskDao;
    }

    public LiveData<List<Task>> getTasks() {
        return mTaskDao.getTasks();
    }

    public void createTask(Task task) {
        mTaskDao.insertTask(task);
    }

    public void deleteTask(Task task) {
        mTaskDao.deleteTask(task);
    }
}
