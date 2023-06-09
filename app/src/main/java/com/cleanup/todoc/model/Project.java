package com.cleanup.todoc.model;


import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Modèle de projet dans lequel des tâches sont incluses.
 */
@Entity(tableName = "project") // Marque cette classe en tant qu'entité.
public class Project {
    /**
     * L'identifiant unique du projet
     */
    @PrimaryKey // Marque une propriété comme clé primaire.
    private final long id;

    /**
     * Le nom du projet
     */
    @NonNull
    private final String name;

    /**
     * Le code hexadécimal (ARGB) de la couleur associée au projet
     */
    @ColorInt
    private final int color;

    /**
     * Instancie un nouveau projet.
     *
     * @param id    l'identifiant unique du projet à définir
     * @param name  le nom du projet à définir
     * @param color le code hexadécimal (ARGB) de la couleur associée au projet à définir
     */
    public Project(long id, @NonNull String name, @ColorInt int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    /**
     * Renvoie tous les projets de l'application.
     * @return tous les projets de l'application
     */
    @NonNull
    public static Project[] getAllProjects() {
        return new Project[]{
                new Project(1L, "Projet Tartampion", 0xFFEADAD1),
                new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
                new Project(3L, "Projet Circus", 0xFFA3CED2),
        };
    }

    /**
     * Renvoie le projet avec l'identifiant unique donné, ou null si aucun projet avec cet identifiant n'est trouvé
     * @param id l'identifiant unique du projet à renvoyer
     * @return le projet avec l'identifiant unique donné, ou null s'il n'a pas été trouvé
     */
    @Nullable
    public static Project getProjectById(long id) {
        for (Project project : getAllProjects()) {
            if (project.id == id)
                return project;
        }
        return null;
    }

    /**
     * Renvoie l'identifiant unique du projet.
     */
    public long getId() {
        return id;
    }

    /**
     * Renvoie le nom du projet.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Renvoie le code hexadécimal (ARGB) de la couleur associée au projet.
     */
    @ColorInt
    public int getColor() {
        return color;
    }

    /**
     * Renvoie le nom du projet sous forme de représentation de chaîne d'objet.
     */
    @Override
    @NonNull
    public String toString() {
        return getName();
    }
}
