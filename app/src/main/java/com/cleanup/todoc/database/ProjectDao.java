package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;

import java.util.List;

/**
 * DAO pour l'entité Projet
 */
@Dao // Objet d'accès aux données.
public interface ProjectDao {

    /**
     * Obtenir tous les projets de la base de données
     * @return tous les projets
     */
    @Query("SELECT * FROM project") // Requête de sélection
    LiveData<List<Project>> getProjects();

    /**
     * Insérer un projet dans la base de données
     * @param projects les projets à insérer
     */
    @Insert // Requête d'insertion.
    void insertProjects(Project... projects);
}