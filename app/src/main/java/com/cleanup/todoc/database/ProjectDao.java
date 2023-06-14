package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;

import java.util.List;

/**
 * Cette interface définit les méthodes pour récupérer tous les projets de la base de données
 * et insérer de nouveaux. Elle utilise des annotations Room pour définir les requêtes SQL correspondantes.
 * Le DAO permet d'abstraire les détails de l'accès aux données et de fournir une interface simple pour interagir avec la table "project" de la base de données.
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