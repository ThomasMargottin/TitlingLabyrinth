package com.example.myprojectandroid;

/* Importation des bibliothèques à utiliser */
import android.util.Log;

// Gestion du temps de lecture de l'accelerometre --> thread différent du thread UI.
// Pour simuler temps nous avons besoin de choisir un pas qui va déterminer la fréquence d’actualisation de l’affichage
public class myThread extends Thread {

    // Objet de la bille pour actualiser sa position
    Bille myBilleThread;
    // Recuperation de la bille pour pouvoir l'actualiser
    public void recup (Bille billeThread) {
        myBilleThread = billeThread;
    }

    @Override
    public void run() {
        // Le thread tourne à l'infini
        boolean isRunning = true;
        // pas de la fréquence d'actualisation
        int rate = 10;

        while (isRunning) {
            // On vérifie que la classe Bille est bien construite sinon si le thread est appelée avant on a une erreur
            if (myBilleThread != null) {
                // Récupération des valeurs en X et Y de l'accéléromètre
                float aX = Accelerometre.get_X();
                float aY = Accelerometre.get_Y();

                // Mise a jour de la position de la bille suivant les valeurs de l'accélérometre
                myBilleThread.updateBillePosition(aX, aY);

                // mise à jour de la vue
                try {
                    // Pas qui va déterminer la fréquence d’actualisation de l’affichage
                    Thread.sleep(rate);
                }
                catch (InterruptedException e) {
                    Log.d("Catch except Thread", "Error from Thread");
                    isRunning = false;
                }
            }
        }
    }
}
