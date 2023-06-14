package com.cleanup.todoc.model;


import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Comparator;

/**
 * Cette classe représente un modèle de tâche dans l'application et fournit des méthodes pour accéder et manipuler
 * les informations de la tâche, telles que son identifiant, son nom, son projet associé et son horodatage de création.
 * Elle offre également des comparateurs pour trier les tâches selon différents critères.
 */
@Entity(tableName = "task", // Marque cette classe en tant qu'entité.
        foreignKeys = @ForeignKey(entity = Project.class,
        parentColumns = "id",
        childColumns = "project_id",
        onDelete = CASCADE))
public class Task {
    /**
     * L'identifiant unique de la tâche
     */
    @PrimaryKey(autoGenerate = true) // Marque une propriété comme clé primaire.
    private long id;

    /**
     * L'identifiant unique du projet associé à la tâche
     */
    @ColumnInfo(name = "project_id", index = true) // personnalise la colonne associée à la propriété.
    private long projectId;

    /**
     * Le nom de la tâche
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    private String name;

    /**
     * L'horodatage de la création de la tâche
     */
    private long creationTimestamp;

    /**
     * Instancie une nouvelle tâche.
     *
     * @param id                l'identifiant unique de la tâche à paramétrer
     * @param projectId         l'identifiant unique du projet associé à la tâche à paramétrer
     * @param name              le nom de la tâche à définir
     * @param creationTimestamp l'horodatage de la création de la tâche à définir
     */
    @Ignore // Permet d'ignorer la propriété.
    public Task(long id, long projectId, @NonNull String name, long creationTimestamp) {
        this.setId(id);
        this.setProjectId(projectId);
        this.setName(name);
        this.setCreationTimestamp(creationTimestamp);
    }

    public Task(long projectId, @NonNull String name, long creationTimestamp) {
        this.setId(0);
        this.setProjectId(projectId);
        this.setName(name);
        this.setCreationTimestamp(creationTimestamp);
    }

    /**
     * Renvoie l'identifiant unique de la tâche.
     */
    public long getId() {
        return id;
    }

    /**
     * Renvoie l'horodatage lorsque la tâche a été créée.
     */
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * Définit l'identifiant unique de la tâche.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Définit l'identifiant unique du projet associé à la tâche.
     */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    /**
     * Renvoie l'identifiant unique de la tâche.
     */
    public long getProjectId() {
        return projectId;
    }

    /**
     * Renvoie le projet associé à la tâche.
     */
    @Nullable
    public Project getProject() {
        return Project.getProjectById(projectId);
    }

    /**
     * Renvoie le nom de la tâche.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Définit le nom de la tâche.
     */
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * Définit l'horodatage lorsque la tâche a été créée.
     */
    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    /**
     * Comparateur pour trier les tâches de A à Z
     */
    public static class TaskAZComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return left.name.compareTo(right.name);
        }
    }

    /**
     * Comparateur pour trier les tâches de Z à A
     */
    public static class TaskZAComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return right.name.compareTo(left.name);
        }
    }

    /**
     * Comparateur pour trier les tâches de la dernière créée à la première créée
     */
    public static class TaskRecentComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (right.creationTimestamp - left.creationTimestamp);
        }
    }

    /**
     * Comparateur pour trier les tâches du premier créé au dernier créé
     */
    public static class TaskOldComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (left.creationTimestamp - right.creationTimestamp);
        }
    }
}
