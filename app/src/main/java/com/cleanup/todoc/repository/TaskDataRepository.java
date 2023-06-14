package com.cleanup.todoc.repository;


import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

/**
 *Cette classe agit comme une couche intermédiaire entre la source de données des tâches
 * et les composants qui ont besoin d'accéder à ces données.
 * Elle encapsule la logique de récupération, création et suppression des tâches en exposant
 * des méthodes pour effectuer ces opérations.
 */
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
