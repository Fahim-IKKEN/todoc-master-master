package com.cleanup.todoc.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.room.Room;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.db.utils.LiveDataTestUtil;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
public class TaskDaoTest {
    @Rule
    // Règle pour exécuter des tâches de manière synchrone
    public androidx.arch.core.executor.testing.InstantTaskExecutorRule instantTaskExecutorRule = new
            androidx.arch.core.executor.testing.InstantTaskExecutorRule();

    // Référence à la base de données ToDoc
    private TodocDatabase database;

    // Obtenir la liste des projets initiaux statiques à partir de la classe Project du modèle
    private Project[] projects = Project.getAllProjects();

    // Créer des modèles de tâches statiques
    private Task task1 = new Task(projects[0].getId(), "Tache 1", new Date().getTime());
    private Task task2 = new Task(projects[0].getId(), "Tache 2", new Date().getTime());
    private Task task3 = new Task(projects[0].getId(), "Tache 3", new Date().getTime());
    private Task task4 = new Task(projects[2].getId(), "Tache 4", new Date().getTime());
    private Task task5 = new Task(projects[2].getId(), "Tache 5", new Date().getTime());

    /**
     * On crée une base de données Todoc en mémoire, insère les projets initiaux,
     * vérifie que la liste des tâches est vide et prépare ainsi l'état initial pour chaque test.
     */
    @Before
    public void initDatabase() throws InterruptedException {
        // Créer une version vierge en mémoire de la base de données
        this.database = Room.inMemoryDatabaseBuilder(androidx.test.InstrumentationRegistry.getContext(),
                        TodocDatabase.class)
                // Autoriser les requêtes de thread principal, juste pour les tests
                .allowMainThreadQueries()
                // Construire la base de données
                .build();

        // Insérer les projets dans la base de données
        this.database.projectDao().insertProjects(this.projects);

        //Obtenir la liste des projets de la base de données
        List<Task> tasks = LiveDataTestUtil.getOrAwaitValue(this.database.taskDao().getTasks());

        // Vérifiez que la liste est vide dans un premier temps
        assertTrue(tasks.isEmpty());
    }

    /**
     * On ferme la base de données Todoc après l'exécution de chaque test,
     * garantissant ainsi que les ressources sont libérées correctement.
     */
    @After
    public void closeDatabase() {
        // Fermez la base de données après l'exécution des tests
        this.database.close();
    }

    /**
     * Test qui vérifie l'insertion et la récupération des tâches dans la base de données.
     */
    @Test
    public void insertAndGetTask() throws InterruptedException {
        // Obtenir la liste des projets de la base de données
        List<Project> projects = LiveDataTestUtil.getOrAwaitValue(this.database.projectDao().getProjects());

        // Insérer les tâches dans la base de données
        this.database.taskDao().insertTask(this.task1);
        this.database.taskDao().insertTask(this.task2);

        // Obtenir la liste des tâches de la base de données
        List<Task> tasks = LiveDataTestUtil.getOrAwaitValue(this.database.taskDao().getTasks());

        // Vérifier que la liste contient 2 tâches
        assertEquals(2, tasks.size());

        // Ajouter les tâches restantes à la base de données
        this.database.taskDao().insertTask(this.task3);
        this.database.taskDao().insertTask(this.task4);
        this.database.taskDao().insertTask(this.task5);

        // Obtenir la liste des tâches de la base de données
        tasks = LiveDataTestUtil.getOrAwaitValue(this.database.taskDao().getTasks());

        // Vérifier que la liste contient 5 tâches
        assertEquals(5, tasks.size());

        // Vérifier que les tâches sont associées aux bons projets
        assertEquals(projects.get(0).getId(), tasks.get(0).getProjectId());
        assertEquals(projects.get(0).getId(), tasks.get(1).getProjectId());
        assertEquals(projects.get(0).getId(), tasks.get(2).getProjectId());
        assertEquals(projects.get(2).getId(), tasks.get(3).getProjectId());
        assertEquals(projects.get(2).getId(), tasks.get(4).getProjectId());

        // Vérifiez si les attributs des tâches du tableau statique et à l'intérieur de la base de données sont les mêmes
        assertEquals(task1.getName(), tasks.get(0).getName());
        assertEquals(task3.getName(), tasks.get(2).getName());
        assertEquals(task5.getName(), tasks.get(4).getName());

        // Vérifiez si les horodatages de création des tâches à partir du tableau statique et à l'intérieur de la base de données sont les mêmes
        assertEquals(task1.getCreationTimestamp(), tasks.get(0).getCreationTimestamp());
        assertEquals(task3.getCreationTimestamp(), tasks.get(2).getCreationTimestamp());
        assertEquals(task5.getCreationTimestamp(), tasks.get(4).getCreationTimestamp());
    }

    /**
     * Test qui vérifie la suppression et la récupération des tâches dans la base de données.
     */
    @Test
    public void deleteAndGetTask() throws InterruptedException {

        // Insérer les tâches dans la base de données
        this.database.taskDao().insertTask(this.task1);
        this.database.taskDao().insertTask(this.task2);
        this.database.taskDao().insertTask(this.task3);
        this.database.taskDao().insertTask(this.task4);
        this.database.taskDao().insertTask(this.task5);

        // Obtenir la liste des tâches de la base de données
        List<Task> tasks = LiveDataTestUtil.getOrAwaitValue(this.database.taskDao().getTasks());

        // Vérifier que la liste contient 5 tâches
        assertEquals(5, tasks.size());

        assertEquals(task2.getName(), tasks.get(1).getName());
        assertEquals(task2.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());

        // Supprimer la deuxième tâche de la base de données
        this.database.taskDao().deleteTask(tasks.get(1));

        // Récupérer la liste des tâches de la base de données
        tasks = LiveDataTestUtil.getOrAwaitValue(this.database.taskDao().getTasks());

        // Vérifier que la liste contient 4 tâches (une tâche a été supprimée)
        assertEquals(4, tasks.size());

        // Vérifiez que la deuxième tâche a été supprimée (le nom correspond à la troisième tâche)
        assertEquals(task3.getName(), tasks.get(1).getName());
        assertEquals(task3.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());
    }
}