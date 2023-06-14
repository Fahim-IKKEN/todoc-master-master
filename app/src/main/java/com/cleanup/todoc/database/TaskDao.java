package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.List;

/**
 *  Cette interface définit les méthodes pour récupérer toutes les tâches de la base de données, insérer
 *  nouvelle, et supprimer une existante.
 *  Elle utilise des annotations Room pour définir les requêtes SQL correspondantes.
 *  Le DAO permet d'abstraire les détails de l'accès aux données et de fournir une interface simple pour interagir avec la table "task" de la base de données.
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
