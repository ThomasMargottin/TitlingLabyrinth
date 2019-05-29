package com.example.myprojectandroid;

/* Importation des bibliothèques à utiliser */
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;
import android.view.Display;
import android.graphics.Point;
import android.widget.ImageView;
import java.util.ArrayList;

public class JeuActivity2 extends AppCompatActivity {

    // Variables qui composeront l'activite de jeu
    public String nomJoueur = "";
    public Accelerometre2 callAccelerometre; // Variable permettant d'utiliser la classe Accelerometre comprise dans l'activité jeu
    public Bille2 callmyBille;  // Variable permettant d'utiliser la classe Bille comprise dans l'activité jeu
    public myThread2 callMyThread; // Variable permettant d'utiliser la classe du thread comprise dans l'activité jeu

    // Gestion des alertes pour dire a l'utilisateur quand il a gagné ou perdu
    AlertDialog.Builder builder;
    // Liste des murs qui composent le jeu
    ArrayList<ImageView> listWall;
    // Liste des trous qui composent le jeu
    ArrayList<ImageView> listHole;

    // Flag d'arrivée pour afficher un message de victoire à l'utilisateur
    public boolean flagGetArrivee = false;

    @Override
    // Le bundle mémorise l'état de l'UI de l'activité lorsqu'elle passe en arrière plan
    // Si l'activité est tuée, on peut revenir à cette activité
    protected void onCreate(Bundle savedInstanceState) {

        // Recuperation des murs par son ID
        ImageView myWallImageView0 = findViewById(R.id.drawableWall0);
        ImageView myWallImageView1 = findViewById(R.id.drawableWall1);
        ImageView myWallImageView2 = findViewById(R.id.drawableWall2);
        ImageView myWallImageView3 = findViewById(R.id.drawableWall3);
        ImageView myWallImageView4 = findViewById(R.id.drawableWall4);
        ImageView myWallImageView5 = findViewById(R.id.drawableWall5);
        ImageView myWallImageView6 = findViewById(R.id.drawableWall6);
        ImageView myWallImageView7 = findViewById(R.id.drawableWall7);
        ImageView myWallImageView8 = findViewById(R.id.drawableWall8);
        ImageView myWallImageView9 = findViewById(R.id.drawableWall9);
        ImageView myWallImageView10 = findViewById(R.id.drawableWall10);
        ImageView myWallImageView11 = findViewById(R.id.drawableWall11);
        ImageView myWallImageView12 = findViewById(R.id.drawableWall12);
        ImageView myWallImageView13 = findViewById(R.id.drawableWall13);
        ImageView myWallImageView14 = findViewById(R.id.drawableWall14);
        ImageView myWallImageView15 = findViewById(R.id.drawableWall15);
        ImageView myWallImageView16 = findViewById(R.id.drawableWall16);
        ImageView myWallImageView17 = findViewById(R.id.drawableWall17);
        ImageView myWallImageView18 = findViewById(R.id.drawableWall18);
        ImageView myWallImageView19 = findViewById(R.id.drawableWall19);
        ImageView myWallImageView20 = findViewById(R.id.drawableWall20);
        ImageView myWallImageView21 = findViewById(R.id.drawableWall21);
        ImageView myWallImageView22 = findViewById(R.id.drawableWall22);
        ImageView myWallImageView23 = findViewById(R.id.drawableWall23);
        ImageView myWallImageView24 = findViewById(R.id.drawableWall24);
        ImageView myWallImageView25 = findViewById(R.id.drawableWall25);
        ImageView myWallImageView26 = findViewById(R.id.drawableWall26);
        ImageView myWallImageView27 = findViewById(R.id.drawableWall27);
        ImageView myWallImageView28 = findViewById(R.id.drawableWall28);

        // On remplit le tableau qui référencie les murs en incrémentant l'index
        listWall = new ArrayList<ImageView>();
        listWall.add(0, myWallImageView0);
        listWall.add(1, myWallImageView1);
        listWall.add(2, myWallImageView2);
        listWall.add(3, myWallImageView3);
        listWall.add(4, myWallImageView4);
        listWall.add(5, myWallImageView5);
        listWall.add(6, myWallImageView6);
        listWall.add(7, myWallImageView7);
        listWall.add(8, myWallImageView8);
        listWall.add(9, myWallImageView9);
        listWall.add(10, myWallImageView10);
        listWall.add(11, myWallImageView11);
        listWall.add(12, myWallImageView12);
        listWall.add(13, myWallImageView13);
        listWall.add(14, myWallImageView14);
        listWall.add(15, myWallImageView15);
        listWall.add(16, myWallImageView16);
        listWall.add(17, myWallImageView17);
        listWall.add(18, myWallImageView18);
        listWall.add(19, myWallImageView19);
        listWall.add(20, myWallImageView20);
        listWall.add(21, myWallImageView21);
        listWall.add(22, myWallImageView22);
        listWall.add(23, myWallImageView23);
        listWall.add(24, myWallImageView24);
        listWall.add(25, myWallImageView25);
        listWall.add(26, myWallImageView26);
        listWall.add(27, myWallImageView27);
        listWall.add(28, myWallImageView28);


        // Recuperation des trous par son ID
        ImageView myHoleImageView0 = findViewById(R.id.hole0);
        ImageView myHoleImageView1 = findViewById(R.id.hole1);
        ImageView myHoleImageView2 = findViewById(R.id.hole2);
        ImageView myHoleImageView3 = findViewById(R.id.hole3);
        ImageView myHoleImageView4 = findViewById(R.id.hole4);
        ImageView myHoleImageView5 = findViewById(R.id.hole5);

        // On remplit le tableau qui référencie les trous en incrémentant l'index
        listHole = new ArrayList<ImageView>();
        listHole.add(0, myHoleImageView0);
        listHole.add(1, myHoleImageView1);
        listHole.add(2, myHoleImageView2);
        listHole.add(3, myHoleImageView3);
        listHole.add(4, myHoleImageView4);
        listHole.add(5, myHoleImageView5);

        // Création de la boite de dialogue d'alertes
        builder = new AlertDialog.Builder(this);
        // On précise ici la vue à afficher dans ce callback
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu2);

        // Recuperation de l'intent qui provient de la Main Activity
        Intent intent = getIntent();
        // Affichage du nom du joueur dans le log apres l'avoir récupéré de la main activity
        nomJoueur = intent.getStringExtra(MainActivity.NOM);
        Log.d("JeuActivity2", "Player name = " + nomJoueur);

        // Bandeau qui affiche le nom de joueur au passage dans cette activité
        Toast.makeText(this, "Player name = " + nomJoueur, Toast.LENGTH_LONG).show();

        // Instanciation de l'accelerometre pour récupérer ses valeurs
        callAccelerometre = new Accelerometre2(this);
        // Instanciation du Thread pour gérer le temps pour le mouvement de la bille
        callMyThread = new myThread2();
    }

    // call-back utilisé si l'activité est active, dans son état stable "running"
    @Override
    protected void onResume() {
        super.onResume();
        // Démarrage du thread pour faire bouger la bille
        callMyThread.start();
        Log.d("JeuActivity", "onResume()");
        // Abonnement au capteur accelerometrique pour lire ses valeurs
        callAccelerometre.abonnementCapteur();
    }

    // call-back utilisé si l'activité est inactive, l'application aura sauvegardée son état précédent et est capable de le restituer
    @Override
    protected void onPause() {
        super.onPause();
        // Interruption du thread pour arreter la bille
        // Utilisable quand le joueur gagne ou perd la partie
        callMyThread.interrupt();
        Log.d("JeuActivity", "onPause()");
        // Désabonnement au capteur accelerometrique
        callAccelerometre.desabonnementCapteur();
    }

    // Affichage du message de victoire une fois arrivée
    public void testfonc(boolean flagArrivee){
        if(flagArrivee == true){
            Toast.makeText(this, "Bravo vous avez gagné !", Toast.LENGTH_LONG).show();
        }
    }

    // call-back appelé a chaque evenement car impossible de récupérer les attributs de ImageView dans le onCreate()
    @Override
    public void onWindowFocusChanged(boolean hasFocus){

        // Recuperation des murs par son ID
        ImageView myWallImageView0 = findViewById(R.id.drawableWall0);
        ImageView myWallImageView1 = findViewById(R.id.drawableWall1);
        ImageView myWallImageView2 = findViewById(R.id.drawableWall2);
        ImageView myWallImageView3 = findViewById(R.id.drawableWall3);
        ImageView myWallImageView4 = findViewById(R.id.drawableWall4);
        ImageView myWallImageView5 = findViewById(R.id.drawableWall5);
        ImageView myWallImageView6 = findViewById(R.id.drawableWall6);
        ImageView myWallImageView7 = findViewById(R.id.drawableWall7);
        ImageView myWallImageView8 = findViewById(R.id.drawableWall8);
        ImageView myWallImageView9 = findViewById(R.id.drawableWall9);
        ImageView myWallImageView10 = findViewById(R.id.drawableWall10);
        ImageView myWallImageView11 = findViewById(R.id.drawableWall11);
        ImageView myWallImageView12 = findViewById(R.id.drawableWall12);
        ImageView myWallImageView13 = findViewById(R.id.drawableWall13);
        ImageView myWallImageView14 = findViewById(R.id.drawableWall14);
        ImageView myWallImageView15 = findViewById(R.id.drawableWall15);
        ImageView myWallImageView16 = findViewById(R.id.drawableWall16);
        ImageView myWallImageView17 = findViewById(R.id.drawableWall17);
        ImageView myWallImageView18 = findViewById(R.id.drawableWall18);
        ImageView myWallImageView19 = findViewById(R.id.drawableWall19);
        ImageView myWallImageView20 = findViewById(R.id.drawableWall20);
        ImageView myWallImageView21 = findViewById(R.id.drawableWall21);
        ImageView myWallImageView22 = findViewById(R.id.drawableWall22);
        ImageView myWallImageView23 = findViewById(R.id.drawableWall23);
        ImageView myWallImageView24 = findViewById(R.id.drawableWall24);
        ImageView myWallImageView25 = findViewById(R.id.drawableWall25);
        ImageView myWallImageView26 = findViewById(R.id.drawableWall26);
        ImageView myWallImageView27 = findViewById(R.id.drawableWall27);
        ImageView myWallImageView28 = findViewById(R.id.drawableWall28);

        // On remplit le tableau qui référencie les murs en incrémentant l'index
        listWall = new ArrayList<ImageView>();
        listWall.add(0, myWallImageView0);
        listWall.add(1, myWallImageView1);
        listWall.add(2, myWallImageView2);
        listWall.add(3, myWallImageView3);
        listWall.add(4, myWallImageView4);
        listWall.add(5, myWallImageView5);
        listWall.add(6, myWallImageView6);
        listWall.add(7, myWallImageView7);
        listWall.add(8, myWallImageView8);
        listWall.add(9, myWallImageView9);
        listWall.add(10, myWallImageView10);
        listWall.add(11, myWallImageView11);
        listWall.add(12, myWallImageView12);
        listWall.add(13, myWallImageView13);
        listWall.add(14, myWallImageView14);
        listWall.add(15, myWallImageView15);
        listWall.add(16, myWallImageView16);
        listWall.add(17, myWallImageView17);
        listWall.add(18, myWallImageView18);
        listWall.add(19, myWallImageView19);
        listWall.add(20, myWallImageView20);
        listWall.add(21, myWallImageView21);
        listWall.add(22, myWallImageView22);
        listWall.add(23, myWallImageView23);
        listWall.add(24, myWallImageView24);
        listWall.add(25, myWallImageView25);
        listWall.add(26, myWallImageView26);
        listWall.add(27, myWallImageView27);
        listWall.add(28, myWallImageView28);


        // Recuperation des trous par son ID
        ImageView myHoleImageView0 = findViewById(R.id.hole0);
        ImageView myHoleImageView1 = findViewById(R.id.hole1);
        ImageView myHoleImageView2 = findViewById(R.id.hole2);
        ImageView myHoleImageView3 = findViewById(R.id.hole3);
        ImageView myHoleImageView4 = findViewById(R.id.hole4);
        ImageView myHoleImageView5 = findViewById(R.id.hole5);

        // On remplit le tableau qui référencie les trous en incrémentant l'index
        listHole = new ArrayList<ImageView>();
        listHole.add(0, myHoleImageView0);
        listHole.add(1, myHoleImageView1);
        listHole.add(2, myHoleImageView2);
        listHole.add(3, myHoleImageView3);
        listHole.add(4, myHoleImageView4);
        listHole.add(5, myHoleImageView5);

        // Permet de récupérer la taille de l'écran et avoir une application responsive
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d("JeuActivity2", "dim = " + width + " * " + height);

        // Recuperation de la bille par son ID
        ImageView myBilleImageView = findViewById(R.id.imageView8);
        // Recuperation de l'arrivee par son ID
        ImageView myEndImageView = findViewById(R.id.end0);

        /*if(flagGetArrivee == true){
           Toast.makeText(this, "Bravo vous avez gagné !", Toast.LENGTH_LONG).show();
       }*/

        // Appel de la methode de la bille de sa propre classe
        callmyBille = new Bille2(this, myBilleImageView, width, height, listWall, listHole, myEndImageView);
        // Appel de la methode du thread de sa propre classe
        callMyThread.recup(this.callmyBille);
    }
}
