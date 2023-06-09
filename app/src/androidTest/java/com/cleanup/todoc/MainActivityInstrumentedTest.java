package com.cleanup.todoc;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.cleanup.todoc.TestUtils.withRecyclerView;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.database.TodocDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Ces tests instrumentalisés vérifient le comportement de la MainActivity
 * en effectuant des actions sur les vues et en vérifiant les résultats attendus.
 * On utilise Espresso pour effectuer des actions sur les vues de l'activité
 * et des assertions pour vérifier les changements attendus.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    // lancer la MainActivity avant chaque test.
    @Rule
    public androidx.test.ext.junit.rules.ActivityScenarioRule<com.cleanup.todoc.view.MainActivity> rule = new
            androidx.test.ext.junit.rules.ActivityScenarioRule<>(com.cleanup.todoc.view.MainActivity.class);

    private com.cleanup.todoc.view.MainActivity mActivity;

    // référence à la base de données ToDoc
    private TodocDatabase database;

    // référence à l'activité
    @Before
    public void setup() {

        rule.getScenario().onActivity(activity -> mActivity = activity);
    }

    // On vérifie le comportement d'ajout et de suppression de tâches.
    @Test
    public void addAndRemoveTask() {

        android.widget.TextView lblNoTask = mActivity.findViewById(R.id.lbl_no_task);
        androidx.recyclerview.widget.RecyclerView listTasks = mActivity.findViewById(R.id.list_tasks);

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("Nouvelle tâche"));
        onView(withId(android.R.id.button1)).perform(click());

        // Vérifier que lblTask n'est plus affiché
        assertThat(lblNoTask.getVisibility(), equalTo(android.view.View.GONE));
        // Vérifiez que recyclerView est affiché
        assertThat(listTasks.getVisibility(), equalTo(android.view.View.VISIBLE));

        // Vérifier qu'il ne contient qu'un seul élément
        assertThat(listTasks.getAdapter().getItemCount(), equalTo(1));

        // Drop the task
        onView(withId(R.id.img_delete)).perform(click());

        // Vérifiez que lblTask est affiché
        assertThat(lblNoTask.getVisibility(), equalTo(android.view.View.VISIBLE));
        // Vérifier que recyclerView n'est plus affiché
        assertThat(listTasks.getVisibility(), equalTo(android.view.View.GONE));

    }

    // On vérifie le comportement de tri de tâches.
    @Test
    public void sortTasks() {

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Trier par ordre alphabétique
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));

        // Trier par ordre alphabétique inversé
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));

        // Trier les plus anciens en premier
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Trier les plus récents en premier
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));

    }
}
