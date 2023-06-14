package com.cleanup.todoc.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.injection.Injection;
import com.cleanup.todoc.injection.ViewModelFactory;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.ViewModelProvider;

/**
 * la classe MainActivity gère l'interface utilisateur de l'application,
 * les événements liés aux tâches et la communication avec le ViewModel
 * pour effectuer des opérations sur les tâches.
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {

    /**
     * Cette variable mTaskViewModel permet à l'activité d'accéder au TaskViewModel
     * pour gérer les données de la tâche et effectuer des opérations telles que
     * l'ajout, la suppression ou la mise à jour des tâches.
     */
    private com.cleanup.todoc.viewmodel.TaskViewModel mTaskViewModel;

    /**
     * cette variable tasks est utilisée pour stocker une liste de tâches dans l'activité.
     * Elle est initialisée avec une instance d'ArrayList<Task> et permet d'ajouter, supprimer
     * ou accéder aux tâches dans l'activité.
     */
    @NonNull
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * cette variable adapter est utilisée pour gérer l'affichage des tâches dans l'interface utilisateur de l'activité
     * en utilisant un adaptateur personnalisé TasksAdapter.
     * Elle permet de fournir les données des tâches à la vue et de mettre à jour l'affichage lorsque les données changent.
     */
    private TasksAdapter adapter;

    /**
     * cette variable sortMethod est utilisée pour stocker la méthode de tri actuellement sélectionnée
     * dans l'activité.
     */
    @NonNull
    private SortMethod sortMethod = SortMethod.NONE;

    /**
     *  cette variable dialog est utilisée pour stocker une instance de boîte de dialogue AlertDialog dans l'activité.
     */
    @Nullable
    public AlertDialog dialog = null;

    /**
     * cette variable dialogEditText est utilisée pour stocker une instance de EditText dans l'activité.
     * Elle est utilisée pour référencer et manipuler un champ de texte éditable spécifique EditText.
     */
    @Nullable
    private EditText dialogEditText = null;

    /**
     * cette variable dialogSpinner est utilisée pour associer un projet à une tâche.
     */
    @Nullable
    private Spinner dialogSpinner = null;

    /**
     * cette variable listTasks est utilisée pour stocker une instance de RecyclerView dans l'activité.
     * Elle sera utilisée pour afficher et gérer la liste des tâches dans l'interface utilisateur de l'activité.
     */

    @SuppressWarnings("NullableProblems")
    @NonNull
    private RecyclerView listTasks;

    /**
     * cette variable lblNoTasks est utilisée pour stocker une instance de TextView dans l'activité.
     * Elle sera utilisée pour afficher un message indiquant l'absence de tâches dans l'interface utilisateur de l'activité
     */

    @SuppressWarnings("NullableProblems")
    @NonNull
    private TextView lblNoTasks;

    /**
     * configuration de l'interface utilisateur de l'activité en associant des vues (comme le RecyclerView et le TextView) à des variables,
     * en créant et en configurant un adaptateur pour afficher les tâches, en définissant des écouteurs de clic sur des boutons,
     * en configurant le ViewModel et en observant les projets et les tâches pour les mises à jour.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // configure le layout de l'activité en définissant le fichier XML activity_main.xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  récupère les références aux éléments d'interface utilisateur list_tasks et lbl_no_task du layout XML
        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);

        // Création d'un TasksAdapter pour gérer l'affichage des tâches
        adapter = new TasksAdapter(this);
        // configure un LinearLayoutManager pour la disposition verticale du RecyclerView
        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // associe l'adaptateur au RecyclerView pour afficher les tâches dans l'interface utilisateur de l'activité
        listTasks.setAdapter(adapter);
        // configuration d'un écouteur de clic pour un bouton d'ajout de tâche.
        // la méthode showAddTaskDialog() est appelée pour afficher une interaction permettant
        // à l'utilisateur d'ajouter une nouvelle tâche.
        findViewById(R.id.fab_add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });

        // en appelant configureViewModel(), on initialise et configure le ViewModel utilisé dans l'activité.
        configureViewModel();

        // initial observe of projects
        observeProjects();

        // en appelant observeTasks(), on met en place une observation des tâches à partir du ViewModel,
        // pour mettre à jour en cas de modifications ultérieures de la liste des tâches
        observeTasks();
    }

    /**
     * On configure et initialise le TaskViewModel en utilisant ViewModelProvider et une instance du ViewModelFactory.
     * On y associe ensuite le ViewModel à l'activité en tant que propriétaire et appelle la méthode init() du ViewModel
     */
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mTaskViewModel = new ViewModelProvider(this, viewModelFactory).get(com.cleanup.todoc.viewmodel.TaskViewModel.class);
        mTaskViewModel.init();
    }

    /** On met en place une observation des projets à partir du ViewModel.
     * Lorsque des changements sont détectés dans les projets, la méthode updateProjects de l'activité
     * sera appelée pour mettre à jour les projets affichés dans l'interface utilisateur.
     */
    private void observeProjects() {
        mTaskViewModel.getProjects().observe(this, this::updateProjects);
    }

    /**
     * On met en place une observation des tâches à partir du ViewModel.
     * Lorsque des changements sont détectés dans les tâches, la méthode updateTasks de l'activité
     * sera appelée pour mettre à jour les tâches affichées dans l'interface utilisateur.
     */
    private void observeTasks() {
        mTaskViewModel.getTasks().observe(this, this::updateTasks);
    }

    /**
     * On crée le menu des options de l'activité en inflatant le fichier de ressources XML spécifié, "actions.xml".
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    /**
     * On gère les sélections d'éléments du menu des options.
     * Selon l'élément sélectionné, on met à jour la variable sortMethod avec la méthode de tri correspondante.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            sortMethod = SortMethod.ALPHABETICAL;
        } else if (id == R.id.filter_alphabetical_inverted) {
            sortMethod = SortMethod.ALPHABETICAL_INVERTED;
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
        } else if (id == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
        }

        // On met en place l'observation des tâches et
        // appelle ensuite la méthode par défaut pour gérer l'élément
        // du menu sélectionné dans la classe parente de l'activité.
        observeTasks();
        return super.onOptionsItemSelected(item);
    }

    /**
     * On supprime la tâche spécifiée en utilisant le ViewModel, puis on rétablit l'observation des tâches
     * pour mettre à jour la liste des tâches affichée dans l'interface utilisateur.
     */
    @Override
    public void onDeleteTask(Task task) {
        mTaskViewModel.deleteTask(task);
        // On s'assure que la liste des tâches est mise à jour à ce stade.
        observeTasks();
    }

    /**
     *  On gère le clic sur le bouton d'ajout (+) de la boîte de dialogue et
     *  on effectue diverses vérifications pour s'assurer que les informations fournies sont valides
     *  avant d'ajouter une nouvelle tâche et de fermer la boîte de dialogue.
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // Si la boîte de dialogue est ouverte.
        if (dialogEditText != null && dialogSpinner != null) {
            // Obtenir le nom de la tâche.
            String taskName = dialogEditText.getText().toString();

            // Obtenir le projet sélectionné qui sera associé à la tâche.
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // Si aucun nom n'a été défini.
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // Si à la fois le projet et le nom de la tâche ont été définis.
            else if (taskProject != null) {

                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                addTask(task);

                dialogInterface.dismiss();
            }
            // Si le nom a été défini, mais pas le projet (ce cas ne devrait jamais se produire).
            else{
                dialogInterface.dismiss();
            }
        }
        // Si la boîte de dialogue est déjà fermée.
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * On ouvre une boîte de dialogue pour permettre à l'utilisateur d'ajouter une nouvelle tâche.
     * On récupère les références des éléments de la boîte de dialogue et on peuple le spinner
     * avec les projets disponibles.
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    /**
     * On crée une nouvelle tâche en utilisant le ViewModel et on met à jour immédiatement la liste des tâches affichée
     * dans l'interface utilisateur en rétablissant l'observation des tâches.
     */
    private void addTask(@NonNull Task task) {
        mTaskViewModel.createTask(task);
        // On s'assure que la liste des tâches est mise à jour à ce stade.
        observeTasks();
    }

    /**
     * On met à jour la liste des projets dans l'interface utilisateur en utilisant l'adapter,
     * et en remplaçant la liste actuelle par la nouvelle liste de projets fournie.
     */
    private void updateProjects(List<Project> projects){
        adapter.updateProjects(projects);
    }

    /**
     * On met à jour la liste des tâches dans l'UI en fonction de la liste de tâches fournie,
     * et en appliquant un tri si nécessaire, et en rendant les éléments visibles ou non en fonction de l'état de la liste de tâches.
     */
    private void updateTasks(List<Task> tasks) {
        //  lorsque la liste de tâches est vide, le libellé "No Tasks" sera affiché à la place de la liste des tâches dans l'UI.
        if (tasks.size() == 0) {
            adapter.updateTasks(tasks);
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
            // On ajuste la visibilité des éléments dans l'UI en fonction de la présence de tâches,
            // puis effectue un tri sur la liste de tâches si nécessaire en utilisant la méthode de tri spécifiée par sortMethod.
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(tasks, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new Task.TaskOldComparator());
                    break;

            }
            // On met à jour la liste des tâches dans l'adaptateur (adapter) avec la nouvelle liste fournie (tasks).
            adapter.updateTasks(tasks);
        }
    }

    /**
     * Cette méthode crée et configure une boîte de dialogue pour l'ajout de nouvelles tâches,
     * en définissant le titre, la vue et les actions à effectuer lorsque la boîte de dialogue est fermée.
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);
        // On configure les propriétés de la boîte de dialogue, le titre, la vue
        // et le comportement lors de la fermeture de cette boîte de dialogue.
        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogEditText = null;
                dialogSpinner = null;
                dialog = null;
            }
        });

        // Une fois que dialog est créé, elle peut être affichée à l'aide de la méthode show()
        // pour que l'utilisateur puisse interagir avec cette boîte de dialogue.
        dialog = alertBuilder.create();

        // On ajoute un écouteur de clic sur le bouton d'ajout (+) de la boîte de dialogue
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });

        return dialog;
    }

    /**
     * cette méthode permet de peupler un spinner avec la liste des projets disponibles en utilisant un adaptateur.
     * Cela permet à l'utilisateur de sélectionner un projet lors de l'ajout d'une tâche dans le programme.
     */
    private void populateDialogSpinner() {
        // On crée un observateur
        final Observer<List<Project>> projectObserver = new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable final List<Project> projects) {
                if (projects != null) { // vérifie d'abord si la liste des projets (projects) n'est pas nulle.
                    final ArrayAdapter<Project> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, projects);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Si la liste des projets n'est pas nulle, un nouvel objet ArrayAdapter<Project> est créé.
                    if (dialogSpinner != null) {
                        dialogSpinner.setAdapter(adapter);
                    }
                }
                mTaskViewModel.getProjects().removeObserver(this);
            }
        };
        mTaskViewModel.getProjects().observe(this, projectObserver);
    }

    /**
     * Ces valeurs de tri sont utilisées pour déterminer l'ordre dans lequel les éléments doivent être affichés.
     */
    private enum SortMethod {
        /**
         * Trie les éléments par ordre alphabétique selon leur nom.
         */
        ALPHABETICAL,

        /**
         * Trie les éléments par ordre alphabétique inverse selon leur nom.
         */
        ALPHABETICAL_INVERTED,

        /**
         * Trie les éléments en plaçant les plus récents en premier.
         */
        RECENT_FIRST,

        /**
         * Trie les éléments en plaçant les plus anciens en premier.
         */
        OLD_FIRST,

        /**
         * Indique qu'aucun tri n'est appliqué.
         */
        NONE
    }
}