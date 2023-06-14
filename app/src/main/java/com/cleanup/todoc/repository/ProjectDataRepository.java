package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;

/**
 * Cette classe agit comme une couche intermédiaire entre la source de données des projets
 * et les composants qui ont besoin d'y accéder.
 * Elle encapsule la logique de récupération des données des projets
 * et expose une méthode pour obtenir les projets sous forme d'objet LiveData<List<Project>>.
 */
public class ProjectDataRepository {
    private final ProjectDao mProjectDao;

    public ProjectDataRepository(ProjectDao projectDao) {
        mProjectDao = projectDao;
    }

    public LiveData<List<Project>> getProjects() {
        return mProjectDao.getProjects();
    }
}
