package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;

/**
 *  Cette classe fournit une interface pour accéder aux données des projets.
 *  Elle utilise un objet ProjectDao pour interagir avec la source de données des projets
 *  et expose une méthode getProjects() qui retourne un objet LiveData<List<Project>>
 *  contenant la liste des projets.
 */

// Le Repository encapsule la logique de récupération et de gestion des données provenant de différentes
// sources telles que des bases de données, des services Web ou des caches.
// Favorise une bonne séparation des responsabilités
public class ProjectDataRepository {
    private final ProjectDao mProjectDao;

    public ProjectDataRepository(ProjectDao projectDao) {
        mProjectDao = projectDao;
    }

    public LiveData<List<Project>> getProjects() {
        return mProjectDao.getProjects();
    }
}
