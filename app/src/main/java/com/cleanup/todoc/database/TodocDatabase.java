package com.cleanup.todoc.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * La base de données Room qui contient les tables Tâche et Projet
 */

@Database(entities={Project.class, Task.class}, version=1, exportSchema=false)
public abstract class TodocDatabase extends RoomDatabase {

    /**
     * Instance singleton de la base de données
     */
    public static volatile TodocDatabase INSTANCE;

    /**
     * Permet de déterminer si le programme s'exécute dans un environnement de test
     * en vérifiant la présence de la classe androidx.test.espresso.Espresso
     */
    private static AtomicBoolean isRunningTest;
    public static synchronized boolean isRunningTest() {
        // faire un peu de cache pour éviter de vérifier à chaque fois
        if (null == isRunningTest) {
            boolean istest;

            try {
                // androidx uniquement
                Class.forName ("androidx.test.espresso.Espresso");
                istest = true;
            } catch (ClassNotFoundException e) {
                istest = false;
            }

            isRunningTest = new AtomicBoolean(istest);
        }

        return isRunningTest.get();
    }

    /**
     * Cette classe permet d'obtenir une instance de la classe TodocDatabase en utilisant une approche singleton.
     * Elle vérifie si l'instance existe déjà, si non, elle crée une instance de la bd en fonction du contexte d'exécution
     * (test ou application réelle)
     * @param context le contexte
     * @return l'instance singleton de la base de données pour un contexte donné
     */
    public static TodocDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                if (INSTANCE == null) {
                    // Le test d'instrumentation est-il en cours ?
                    if( isRunningTest() ) {
                        // Utiliser une base de données en mémoire
                        INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                                        TodocDatabase.class)
                                // Autoriser les requêtes de thread principal, juste pour les tests
                                .allowMainThreadQueries()
                                // Faire un rappel pour pré-remplir la base de données
                                .addCallback(prepopulateDatabase())
                                // Construire la base de données
                                .build();
                    } else {
                        // Utiliser une vraie base de données
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        TodocDatabase.class,
                                        // Nom du fichier de base de données SQLite stocké dans le répertoire de données privées de l'application
                                        "TodocDatabase.db")

                                //Faire un rappel pour pré-remplir la base de données
                                .addCallback(prepopulateDatabase())
                                .build();
                    }
                }
            }
        }
        return INSTANCE;
    }

    /**
     * On crée un rappel (Callback) pour pré-remplir la table "project" de la base de données lors de sa création.
     * On utilise un tableau de projets qu'on convertit en objets ContentValues, puis les insère dans la base de données en utilisant la stratégie de conflit "IGNORE".
     * @return le rappel pour pré-remplir la base de données
     */
    private static Callback prepopulateDatabase() {
        return new Callback() {
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                // Insérer la liste des projets dans la base de données
                Project[] projects = Project.getAllProjects();
                for (Project project : projects) {
                    // Créez un objet ContentValues où les noms de colonne sont les clés,
                    // et les attributs du projet sont les valeurs.
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", project.getId());
                    contentValues.put("name", project.getName());
                    contentValues.put("color", project.getColor());
                    // Insérez
                    // La stratégie de conflit est définie sur IGNORE : si la valeur est déjà dans la base de données, rien ne se passe
                    db.insert("project", OnConflictStrategy.IGNORE, contentValues);
                }
            }
        };
    }

    /**
     * Obtenir le DAO pour la table Project
     */
    public abstract ProjectDao projectDao();

    /**
     * Obtenir le DAO pour la table des tâches
     */
    public abstract TaskDao taskDao();
}
