package com.example.myprojectandroid;

/* Importation des bibliothèques à utiliser */
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;

public class Accelerometre2 {

    private static float X; // accélération sur l’axe X
    private static float Y; // accélération sur l’axe Y

    private SensorManager mManager; // qui va gérer l’accéléromètre
    private Sensor mAccelerometre; // l’accéléromètre
    private SensorEventListener mSensorEventListener; // Listener sur le changement de valeurs du capteur

    // Constructeur de la classe de l'accelerometre
    public Accelerometre2 (JeuActivity2 activity) {

        JeuActivity2 mActivity; // l’activité Jeu
        mActivity = activity; // Variable permettant d'étendre la classe JeuActivity à la classe de l'accelerometre

        // Initialisation des attributs précédents
        // On récupère le contexte de l'activité jeu puis on recherche le capteur de la tablette de type accéléromètre
        mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        mAccelerometre = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // listener sur les evenements du capteur accelerometrique
        mSensorEventListener = new SensorEventListener() {
            // pour récupérer les valeurs de l'accéléromètre
            @Override
            // A chaque changement de valeur de l'accelerometre
            // Lecture de la valeur de X et de Y du capteur
            public void onSensorChanged(SensorEvent pEvent) {
                X = pEvent.values[0];
                Y = pEvent.values[1];
                //Log.d("Acceler - sensorchanged", "new sensor values: X=" + Accelerometre.get_X() + " Y=" + Accelerometre.get_Y());
            }
            @Override
            public void onAccuracyChanged(Sensor pSensor, int pAccuracy) {
                //Log.d("Acceler - Accuracy", "idle");
            }
        };
    }

    // Méthodes pour s’abonner et se désabonner au capteur
    // Sera appelée dans le onResume de l'actitvité jeu car cela signifie que l'activité est dans son état stable "running"
    public void abonnementCapteur() {
        mManager.registerListener(mSensorEventListener, mAccelerometre, SensorManager.SENSOR_DELAY_GAME);
    }
    // Sera appelée dans le onPause de l'actitvité jeu car cela signifie que l'activité est arretée
    // si ce callback est appelé, l'application aura sauvegardée son état précédent et est capable de le restituer à l'appel de onResume
    public void desabonnementCapteur() {
        mManager.unregisterListener(mSensorEventListener, mAccelerometre);
    }

    // Instanciation des Getters pour récupérer l'axe X et l'axe Y dans l'activite
    public static float get_X(){
        return X;
    }

    public static float get_Y(){
        return Y;
    }
}
