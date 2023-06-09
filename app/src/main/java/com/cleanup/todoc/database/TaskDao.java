package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.List;

/**
 * DAO pour l'entité Tâche
 */
@Dao // Objet d'accès aux données.
public interface TaskDao {

    /**
     * Obtenir toutes les tâches de la base de données
     * @return Toutes les tâches
     */
    @Query("SELECT * FROM task") // Méthode de requête
    LiveData<List<Task>> getTasks();

    /**
     * Insérer une tâche dans la base de données
     * @param task la tâche à insérer
     */
    @Insert // Méthode d'insertion.
    void insertTask(Task task);

    /**
     * Supprimer une tâche de la base de données
     * @param task la tâche à supprimer
     */
    @Delete // Méthode de suppression.
    void deleteTask(Task task);
}
