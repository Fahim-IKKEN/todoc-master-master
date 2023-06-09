package com.cleanup.todoc.db.utils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

// Comment tester LiveData : https://stackoverflow.com/a/44271247
public class LiveDataTestUtil {
    // Cette méthode est utilisée pour obtenir la valeur d'un objet LiveData.
    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        // Créer un tableau d'objets pour stocker les données
        final Object[] data = new Object[1];
        // Créez un objet CountDownLatch en attendant que les données soient définies
        final CountDownLatch latch = new CountDownLatch(1);
        // Créer un objet Observer pour observer l'objet LiveData
        Observer<T> observer = new Observer<T>() {
            // Cette méthode est appelée lorsque l'objet LiveData est modifié
            @Override
            public void onChanged(@Nullable T o) {
                // Définir les données dans le tableau
                data[0] = o;
                // Décomptez le verrou jusqu'à 0
                latch.countDown();
                // Supprimer l'observateur
                liveData.removeObserver(this);
            }
        };
        // Observez l'objet LiveData
        liveData.observeForever(observer);
        //Attendre que les données soient définies
        latch.await(2, TimeUnit.SECONDS);
        //pas d'inspection décochée
        return (T) data[0];
    }
}
