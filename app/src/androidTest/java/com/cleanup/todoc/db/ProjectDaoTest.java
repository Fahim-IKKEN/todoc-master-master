package com.cleanup.todoc.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.db.utils.LiveDataTestUtil;
import com.cleanup.todoc.model.Project;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Le test vérifie l'insértion d'une liste de projets dans la base de données.
 */
@RunWith(AndroidJUnit4.class)
public class ProjectDaoTest {

    @Rule
    // Règle pour exécuter des tâches de manière synchrone
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    // Obtenir une référence à la base de données ToDoc
    private TodocDatabase database;
    // Obtenir la liste des projets initiaux statiques à partir de la classe Project du modèle
    private final Project[] projects = Project.getAllProjects();

    @Before
    public void initDatabase() {
        // Créer une version vierge en mémoire de la base de données
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                        TodocDatabase.class)
                // Allow main thread queries, just for testing
                .allowMainThreadQueries()
                // Construire la base de données
                .build();
    }

    // Ferme la base de données après l'exécution des tests
    @After
    public void closeDatabase() {
        this.database.close();
    }

    @Test
    public void insertAndGetProject() throws InterruptedException {
        // Obtenir la liste des projets
        List<Project> projects = LiveDataTestUtil.getOrAwaitValue(this.database.projectDao().getProjects());
        // Vérifier que la liste est vide dans un premier temps
        assertTrue(projects.isEmpty());
        // Insérer les projets dans la base de données
        this.database.projectDao().insertProjects(this.projects);
        // Obtenir la liste des projets de la base de données
        projects = LiveDataTestUtil.getOrAwaitValue(this.database.projectDao().getProjects());

        // Vérifier que la liste contient les 3 projets
        assertEquals(projects.get(0).getName(), this.projects[0].getName());
        assertEquals(projects.get(0).getId(), this.projects[0].getId());
        assertEquals(projects.get(0).getColor(), this.projects[0].getColor());

        assertEquals(projects.get(1).getName(), this.projects[1].getName());
        assertEquals(projects.get(1).getId(), this.projects[1].getId());
        assertEquals(projects.get(1).getColor(), this.projects[1].getColor());

        assertEquals(projects.get(2).getName(), this.projects[2].getName());
        assertEquals(projects.get(2).getId(), this.projects[2].getId());
        assertEquals(projects.get(2).getColor(), this.projects[2].getColor());
    }


}
