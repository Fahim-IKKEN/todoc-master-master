package com.cleanup.todoc.ui;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest // test de grande envergure qui peut nécessiter plus de temps pour s'exécuter.
@RunWith(AndroidJUnit4.class) // utilisation du framework de tests instrumentalisés Android.
public class MainActivityTest {

    // lance la MainActivity avant le test et la ferme après.
    @Rule
    public ActivityScenarioRule<com.cleanup.todoc.view.MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(com.cleanup.todoc.view.MainActivity.class);

    // Test vide, il ne contient aucune logique de test pour le moment.
    @Test
    public void mainActivityTest() {
    }
}
