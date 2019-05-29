package com.example.myprojectandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class Bille2 extends AppCompatActivity {

    // Pas utilisé
    int sizeX; // taille de la bille (px)
    int sizeY; // taille de la bille (px)

    // Les valeurs d'accélérations seront intégrées pour récupérer ces valeurs
    // Position de la bille sur l'écran
    float posX; // position de la bille en X
    float posY; // // position de la bille en Y

    // Variables pour replacer la bille au début du labyrinthe en cas d'échec
    float posXInitial; // position de la bille
    float posYInitial; // position de la bille

    // Les valeurs d'accélérations seront intégrées pour récupérer ces valeurs
    // Variable pour gérer l'acceleration et les calculs de frottement et de rebond
    float vitX;
    float vitY;
    // Maximum que la bille ne peut dépasser en X et en Y
    int maxX; // coordonnée max en X
    int maxY; // coordonnée max en Y

    // Image View et tableau d'Image View qui composent l'activite pour gérer les collisions et l'affichage
    ImageView myBilleImageView;
    ImageView myEndImageView;
    ArrayList<ImageView> listWallBille;
    ArrayList<ImageView> listHoleBille;
    
    JeuActivity2 mActivity; // l’activité Jeu 
    private static boolean flagArrivee = false; // flag qui permet de dire que le labyrinthe est terminé

    // Constructeur de la classe Bille qui appelle les objets du jeu pour gérer le mouvement de la bille
    public Bille2 (JeuActivity2 activity, ImageView myBille, int maxDisplayX, int maxDisplayY, ArrayList<ImageView> myWallBille, ArrayList<ImageView> myHoleBille, ImageView myEndImageView) {
        // Récupération des objets qui composent le jeu
        listWallBille = new ArrayList<ImageView>();
        listHoleBille = new ArrayList<ImageView>();
        this.myBilleImageView = myBille;
        this.listWallBille = myWallBille;
        this.listHoleBille = myHoleBille;
        this.myEndImageView = myEndImageView;

        // La position dépend des valeurs accélérométriques
        posX = myBilleImageView.getX();
        posY = myBilleImageView.getY();

        // Ces variables valent la valeur de départ de la bille dans le labyrinthe
        posXInitial = myBilleImageView.getX();
        posYInitial = myBilleImageView.getY();

        // Récupération de la taille de l'ecran à ne pas dépasser pour ne pas perdre la bille en dehors de l'écran
        maxX = maxDisplayX;
        maxY = maxDisplayY;

        mActivity = new JeuActivity2(); // Variable permettant d'étendre la classe JeuActivity à la classe de l'accelerometre

    }

    // Getter pour envoyer la valeur du flag d'arrivée à l'activité jeu
    public static boolean getFlag(){
        return flagArrivee;
    }

    // Fonction appelée par le thread du jeu pour actualiser la position de la bille et gérer sa dynamique
    public void updateBillePosition(float aX, float aY) {

        // Réduction de l'acceleration pour simuler les frottements, coefficient compris entre 0 et 1
        aX = (float) (aX * 0.10);
        aY = (float) (aY * 0.10);

        /* Acceleration en X */
        // La derivee est une soustraction
        // L'integrale est une addition
        // Deriver la position nous donnera la vitesse et ainsi de suite
        // Integrer l'acceleration nous donnera la vitesse et ainsi de suite

        // Calcul du rebond par rapport à l'axe X
        vitX = vitX - aX;

        // Si la bille tombe vers la droite de l'ecran
        if (vitX + posX > 0) {
            // et que la bille ne dépasse pas de l'ecran, sachant que la bille continue d'accélérer
            if (vitX + posX <= (maxX - myBilleImageView.getWidth())) {
                // on calcule la vitesse de la bille en prenant en compte les frottements
                vitX = (float) (vitX + (-0.05 * vitX));
                // la position évolue en fonction de la valeur de la vitesse
                posX = posX + vitX;
            }
            // ou la bille sort ici de l'ecran par le côté droit
            else {
                posX = maxX - myBilleImageView.getWidth();
                // on calcule le rebond en diminuant la vitesse petit à petit
                vitX = (float) (vitX * (-0.8));
            }
        }
        // Ici la bille sort de l'ecran cote gauche
        else {
            // La position est fixé au minimum de l'écran
            posX = 0;
            // on calcule le rebond en diminuant la vitesse petit à petit
            vitX = (float) (vitX *(-0.8));
        }

        /* Acceleration en Y */
        // La derivee est une soustraction
        // L'integrale est une addition
        // Deriver la position nous donnera la vitesse et ainsi de suite
        // Integrer l'acceleration nous donnera la vitesse et ainsi de suite

        // Calcul du rebond par rapport à l'axe Y
        vitY = vitY + aY;

        // Si la bille tombe vers la haut de l'ecran
        if (vitY + posY > 0) {
            // et que la bille ne dépasse pas de l'ecran - 50 pour prendre en compte la barre d'etat en bas de la tablette
            if (vitY + posY < (maxY - myBilleImageView.getHeight() - 50)) {
                // on calcule la vitesse de la bille en prenant en compte les frottements
                vitY = (float) (vitY + (-0.05 * vitY));
                // la position évolue en fonction de la valeur de la vitesse
                posY = vitY + posY;
            }
            // ou la bille sort ici de l'ecran par le haut
            else {
                posY = maxY - myBilleImageView.getHeight() - 50;
                // on calcule le rebond en diminuant la vitesse petit à petit
                vitY = (float) (vitY * (-0.8));
            }
        }
        // Ici la bille sort de l'ecran cote bas
        else {
            // La position est fixé au minimum de l'écran
            posY = 0;
            // on calcule le rebond en diminuant la vitesse petit à petit
            vitY = (float) (vitY * (-0.8));
        }

        /* Gestion des collisions */
        // Création de rectangle en fonction des image view pour détecter une collision
        // Ou en fonction d'une liste pour compter tous les murs

        // Création des rectangles pour symboliser les murs
        Rect rc5  = new Rect();
        Rect rc6  = new Rect();
        Rect rc7  = new Rect();
        Rect rc8  = new Rect();
        Rect rc9  = new Rect();
        Rect rc10  = new Rect();
        Rect rc11  = new Rect();
        Rect rc12  = new Rect();
        Rect rc13  = new Rect();
        Rect rc14  = new Rect();
        Rect rc15  = new Rect();
        Rect rc16  = new Rect();
        Rect rc17  = new Rect();
        Rect rc18  = new Rect();
        Rect rc19  = new Rect();
        Rect rc20  = new Rect();
        Rect rc21  = new Rect();
        Rect rc22  = new Rect();
        Rect rc23  = new Rect();
        Rect rc24  = new Rect();
        Rect rc25  = new Rect();
        Rect rc26  = new Rect();
        Rect rc27  = new Rect();
        Rect rc28  = new Rect();
        Rect rc29  = new Rect();
        Rect rc30  = new Rect();
        Rect rc31  = new Rect();
        Rect rc32  = new Rect();
        Rect rc33  = new Rect();

		// Association des rectangles à tous les murs du design
        listWallBille.get(0).getHitRect(rc5);
        listWallBille.get(1).getHitRect(rc6);
        listWallBille.get(2).getHitRect(rc7);
        listWallBille.get(3).getHitRect(rc8);
        listWallBille.get(4).getHitRect(rc9);
        listWallBille.get(5).getHitRect(rc10);
        listWallBille.get(6).getHitRect(rc11);
        listWallBille.get(7).getHitRect(rc12);
        listWallBille.get(8).getHitRect(rc13);
        listWallBille.get(9).getHitRect(rc14);
        listWallBille.get(10).getHitRect(rc15);
        listWallBille.get(11).getHitRect(rc16);
        listWallBille.get(12).getHitRect(rc17);
        listWallBille.get(13).getHitRect(rc18);
        listWallBille.get(14).getHitRect(rc19);
        listWallBille.get(15).getHitRect(rc20);
        listWallBille.get(16).getHitRect(rc21);
        listWallBille.get(17).getHitRect(rc22);
        listWallBille.get(18).getHitRect(rc23);
        listWallBille.get(19).getHitRect(rc24);
        listWallBille.get(20).getHitRect(rc25);
        listWallBille.get(21).getHitRect(rc26);
        listWallBille.get(22).getHitRect(rc27);
        listWallBille.get(23).getHitRect(rc28);
        listWallBille.get(24).getHitRect(rc29);
        listWallBille.get(25).getHitRect(rc30);
        listWallBille.get(26).getHitRect(rc31);
        listWallBille.get(27).getHitRect(rc32);
        listWallBille.get(28).getHitRect(rc33);

		// Création des rectangles pour symboliser les trous
        Rect rc40  = new Rect();
        Rect rc41  = new Rect();
        Rect rc42  = new Rect();
        Rect rc43  = new Rect();
        Rect rc44  = new Rect();
        Rect rc45  = new Rect();
		
		// Association des rectangles à tous les trous du design
        listHoleBille.get(0).getHitRect(rc40);
        listHoleBille.get(1).getHitRect(rc41);
        listHoleBille.get(2).getHitRect(rc42);
        listHoleBille.get(3).getHitRect(rc43);
        listHoleBille.get(4).getHitRect(rc44);
        listHoleBille.get(5).getHitRect(rc45);

        // Création du rectangle pour symboliser la bille
        Rect rc1 = new Rect();
        // Association du rectangle à la bille
        myBilleImageView.getHitRect(rc1);

        // Création du rectangle pour symboliser l'arrivee
        Rect rc4 = new Rect();
        // Association du rectangle à l'arrivee
        myEndImageView.getHitRect(rc4);

        // Détection d'une collision avec un des blocs mur du jeu
        if (Rect.intersects(rc1, rc5) ||
                Rect.intersects(rc1, rc6) ||
                Rect.intersects(rc1, rc7) ||
                Rect.intersects(rc1, rc8) ||
                Rect.intersects(rc1, rc9) ||
                Rect.intersects(rc1, rc10) ||
                Rect.intersects(rc1, rc11) ||
                Rect.intersects(rc1, rc12) ||
                Rect.intersects(rc1, rc13) ||
                Rect.intersects(rc1, rc14) ||
                Rect.intersects(rc1, rc15) ||
                Rect.intersects(rc1, rc16) ||
                Rect.intersects(rc1, rc17) ||
                Rect.intersects(rc1, rc18) ||
                Rect.intersects(rc1, rc19) ||
                Rect.intersects(rc1, rc20) ||
                Rect.intersects(rc1, rc21) ||
                Rect.intersects(rc1, rc22) ||
                Rect.intersects(rc1, rc23) ||
                Rect.intersects(rc1, rc24) ||
                Rect.intersects(rc1, rc25) ||
                Rect.intersects(rc1, rc26) ||
                Rect.intersects(rc1, rc27) ||
                Rect.intersects(rc1, rc28) ||
                Rect.intersects(rc1, rc29) ||
                Rect.intersects(rc1, rc30) ||
                Rect.intersects(rc1, rc31) ||
                Rect.intersects(rc1, rc32) ||
                Rect.intersects(rc1, rc33)) {

            // En fonction de ce jeu de conditions, nous détectons une collision sur la droite de la bille
            // Toutes les conditions permettent de ne pas déclarer une mauvaise collision si nous entrons dans le mur
            if ((((rc1.right > rc5.left) && (Rect.intersects(rc1, rc5)) && (rc1.left < rc5.right) && (rc1.right < rc5.right) && (rc1.top > rc5.top) && (rc1.bottom < rc5.bottom)) ||
                    ((rc1.right > rc6.left) && (Rect.intersects(rc1, rc6)) && (rc1.left < rc6.right) && (rc1.right < rc6.right) && (rc1.top > rc6.top) && (rc1.bottom < rc6.bottom)) ||
                    ((rc1.right > rc7.left) && (Rect.intersects(rc1, rc7)) && (rc1.left < rc7.right) && (rc1.right < rc7.right) && (rc1.top > rc7.top) && (rc1.bottom < rc7.bottom)) ||
                    ((rc1.right > rc8.left) && (Rect.intersects(rc1, rc8)) && (rc1.left < rc8.right) && (rc1.right < rc8.right) && (rc1.top > rc8.top) && (rc1.bottom < rc8.bottom)) ||
                    ((rc1.right > rc9.left) && (Rect.intersects(rc1, rc9)) && (rc1.left < rc9.right) && (rc1.right < rc9.right) && (rc1.top > rc9.top) && (rc1.bottom < rc8.bottom)) ||
                    ((rc1.right > rc10.left) && (Rect.intersects(rc1, rc10)) && (rc1.left < rc10.right) && (rc1.right < rc10.right) && (rc1.top > rc10.top) && (rc1.bottom < rc10.bottom)) ||
                    ((rc1.right > rc11.left) && (Rect.intersects(rc1, rc11)) && (rc1.left < rc11.right) && (rc1.right < rc11.right) && (rc1.top > rc11.top) && (rc1.bottom < rc11.bottom)) ||
                    ((rc1.right > rc12.left) && (Rect.intersects(rc1, rc12)) && (rc1.left < rc12.right) && (rc1.right < rc12.right) && (rc1.top > rc12.top) && (rc1.bottom < rc12.bottom)) ||
                    ((rc1.right > rc13.left) && (Rect.intersects(rc1, rc13)) && (rc1.left < rc13.right) && (rc1.right < rc13.right) && (rc1.top > rc13.top) && (rc1.bottom < rc13.bottom)) ||
                    ((rc1.right > rc14.left) && (Rect.intersects(rc1, rc14)) && (rc1.left < rc14.right) && (rc1.right < rc14.right) && (rc1.top > rc14.top) && (rc1.bottom < rc14.bottom)) ||
                    ((rc1.right > rc15.left) && (Rect.intersects(rc1, rc15)) && (rc1.left < rc15.right) && (rc1.right < rc15.right) && (rc1.top > rc15.top) && (rc1.bottom < rc15.bottom)) ||
                    ((rc1.right > rc16.left) && (Rect.intersects(rc1, rc16)) && (rc1.left < rc16.right) && (rc1.right < rc16.right) && (rc1.top > rc16.top) && (rc1.bottom < rc16.bottom)) ||
                    ((rc1.right > rc17.left) && (Rect.intersects(rc1, rc17)) && (rc1.left < rc17.right) && (rc1.right < rc17.right) && (rc1.top > rc17.top) && (rc1.bottom < rc17.bottom)) ||
                    ((rc1.right > rc18.left) && (Rect.intersects(rc1, rc18)) && (rc1.left < rc18.right) && (rc1.right < rc18.right) && (rc1.top > rc18.top) && (rc1.bottom < rc18.bottom)) ||
                    ((rc1.right > rc19.left) && (Rect.intersects(rc1, rc19)) && (rc1.left < rc19.right) && (rc1.right < rc19.right) && (rc1.top > rc19.top) && (rc1.bottom < rc19.bottom)) ||
                    ((rc1.right > rc20.left) && (Rect.intersects(rc1, rc20)) && (rc1.left < rc20.right) && (rc1.right < rc20.right) && (rc1.top > rc20.top) && (rc1.bottom < rc20.bottom)) ||
                    ((rc1.right > rc21.left) && (Rect.intersects(rc1, rc21)) && (rc1.left < rc21.right) && (rc1.right < rc21.right) && (rc1.top > rc21.top) && (rc1.bottom < rc21.bottom)) ||
                    ((rc1.right > rc22.left) && (Rect.intersects(rc1, rc22)) && (rc1.left < rc22.right) && (rc1.right < rc22.right) && (rc1.top > rc22.top) && (rc1.bottom < rc22.bottom)) ||
                    ((rc1.right > rc23.left) && (Rect.intersects(rc1, rc23)) && (rc1.left < rc23.right) && (rc1.right < rc23.right) && (rc1.top > rc23.top) && (rc1.bottom < rc23.bottom)) ||
                    ((rc1.right > rc24.left) && (Rect.intersects(rc1, rc24)) && (rc1.left < rc24.right) && (rc1.right < rc24.right) && (rc1.top > rc24.top) && (rc1.bottom < rc24.bottom)) ||
                    ((rc1.right > rc25.left) && (Rect.intersects(rc1, rc25)) && (rc1.left < rc25.right) && (rc1.right < rc25.right) && (rc1.top > rc25.top) && (rc1.bottom < rc25.bottom)) ||
                    ((rc1.right > rc26.left) && (Rect.intersects(rc1, rc26)) && (rc1.left < rc26.right) && (rc1.right < rc26.right) && (rc1.top > rc26.top) && (rc1.bottom < rc26.bottom)) ||
                    ((rc1.right > rc27.left) && (Rect.intersects(rc1, rc27)) && (rc1.left < rc27.right) && (rc1.right < rc27.right) && (rc1.top > rc27.top) && (rc1.bottom < rc27.bottom)) ||
                    ((rc1.right > rc28.left) && (Rect.intersects(rc1, rc28)) && (rc1.left < rc28.right) && (rc1.right < rc28.right) && (rc1.top > rc28.top) && (rc1.bottom < rc28.bottom)) ||
                    ((rc1.right > rc29.left) && (Rect.intersects(rc1, rc29)) && (rc1.left < rc29.right) && (rc1.right < rc29.right) && (rc1.top > rc29.top) && (rc1.bottom < rc29.bottom)) ||
                    ((rc1.right > rc30.left) && (Rect.intersects(rc1, rc30)) && (rc1.left < rc30.right) && (rc1.right < rc30.right) && (rc1.top > rc30.top) && (rc1.bottom < rc30.bottom)) ||
                    ((rc1.right > rc31.left) && (Rect.intersects(rc1, rc31)) && (rc1.left < rc31.right) && (rc1.right < rc31.right) && (rc1.top > rc31.top) && (rc1.bottom < rc31.bottom)) ||
                    ((rc1.right > rc32.left) && (Rect.intersects(rc1, rc32)) && (rc1.left < rc32.right) && (rc1.right < rc32.right) && (rc1.top > rc32.top) && (rc1.bottom < rc32.bottom)) ||
                    ((rc1.right > rc33.left) && (Rect.intersects(rc1, rc33)) && (rc1.left < rc33.right) && (rc1.right < rc33.right) && (rc1.top > rc33.top) && (rc1.bottom < rc33.bottom)))){
                Log.d("Catch collision", "Collision sur la droite de la bille");

                // Calcul du frottement
                vitX = (float) (vitX + (-0.02 * vitX));
                // Calcul de la future position
                posX = posX + vitX;

                // On recule la bille pour simuler un rebond simple et éviter les erreurs de collision
                posX = posX - 5;
                // Calcul du frottement
                vitX = (float) (vitX * (-0.8));
            }

            // En fonction de ce jeu de conditions, nous détectons une collision sur la gauche de la bille
            // Toutes les conditions permettent de ne pas déclarer une mauvaise collision si nous entrons dans le mur
            if ((((rc1.left < rc5.right) && (Rect.intersects(rc1, rc5)) && (rc1.right > rc5.left) && (rc1.left > rc5.left) && (rc1.top > rc5.top) && (rc1.bottom < rc5.bottom)) ||
                    ((rc1.left < rc6.right) && (Rect.intersects(rc1, rc6)) && (rc1.right > rc6.left) && (rc1.left > rc6.left) && (rc1.top > rc6.top) && (rc1.bottom < rc6.bottom)) ||
                    ((rc1.left < rc7.right) && (Rect.intersects(rc1, rc7)) && (rc1.right > rc7.left) && (rc1.left > rc7.left) && (rc1.top > rc7.top) && (rc1.bottom < rc7.bottom)) ||
                    ((rc1.left < rc8.right) && (Rect.intersects(rc1, rc8)) && (rc1.right > rc8.left) && (rc1.left > rc8.left) && (rc1.top > rc8.top) && (rc1.bottom < rc8.bottom)) ||
                    ((rc1.left < rc9.right) && (Rect.intersects(rc1, rc9)) && (rc1.right > rc9.left) && (rc1.left > rc9.left) && (rc1.top > rc9.top) && (rc1.bottom < rc8.bottom)) ||
                    ((rc1.left < rc10.right) && (Rect.intersects(rc1, rc10)) && (rc1.right > rc10.left) && (rc1.left > rc10.left) && (rc1.top > rc10.top) && (rc1.bottom < rc10.bottom)) ||
                    ((rc1.left < rc11.right) && (Rect.intersects(rc1, rc11)) && (rc1.right > rc11.left) && (rc1.left > rc11.left) && (rc1.top > rc11.top) && (rc1.bottom < rc11.bottom)) ||
                    ((rc1.left < rc12.right) && (Rect.intersects(rc1, rc12)) && (rc1.right > rc12.left) && (rc1.left > rc12.left) && (rc1.top > rc12.top) && (rc1.bottom < rc12.bottom)) ||
                    ((rc1.left < rc13.right) && (Rect.intersects(rc1, rc13)) && (rc1.right > rc13.left) && (rc1.left > rc13.left) && (rc1.top > rc13.top) && (rc1.bottom < rc13.bottom)) ||
                    ((rc1.left < rc14.right) && (Rect.intersects(rc1, rc14)) && (rc1.right > rc14.left) && (rc1.left > rc14.left) && (rc1.top > rc14.top) && (rc1.bottom < rc14.bottom)) ||
                    ((rc1.left < rc15.right) && (Rect.intersects(rc1, rc15)) && (rc1.right > rc15.left) && (rc1.left > rc15.left) && (rc1.top > rc15.top) && (rc1.bottom < rc15.bottom)) ||
                    ((rc1.left < rc16.right) && (Rect.intersects(rc1, rc16)) && (rc1.right > rc16.left) && (rc1.left > rc16.left) && (rc1.top > rc16.top) && (rc1.bottom < rc16.bottom)) ||
                    ((rc1.left < rc17.right) && (Rect.intersects(rc1, rc17)) && (rc1.right > rc17.left) && (rc1.left > rc17.left) && (rc1.top > rc17.top) && (rc1.bottom < rc17.bottom)) ||
                    ((rc1.left < rc18.right) && (Rect.intersects(rc1, rc18)) && (rc1.right > rc18.left) && (rc1.left > rc18.left) && (rc1.top > rc18.top) && (rc1.bottom < rc18.bottom)) ||
                    ((rc1.left < rc19.right) && (Rect.intersects(rc1, rc19)) && (rc1.right > rc19.left) && (rc1.left > rc19.left) && (rc1.top > rc19.top) && (rc1.bottom < rc19.bottom)) ||
                    ((rc1.left < rc20.right) && (Rect.intersects(rc1, rc20)) && (rc1.right > rc20.left) && (rc1.left > rc20.left) && (rc1.top > rc20.top) && (rc1.bottom < rc20.bottom)) ||
                    ((rc1.left < rc21.right) && (Rect.intersects(rc1, rc21)) && (rc1.right > rc21.left) && (rc1.left > rc21.left) && (rc1.top > rc21.top) && (rc1.bottom < rc21.bottom)) ||
                    ((rc1.left < rc22.right) && (Rect.intersects(rc1, rc22)) && (rc1.right > rc22.left) && (rc1.left > rc22.left) && (rc1.top > rc22.top) && (rc1.bottom < rc22.bottom)) ||
                    ((rc1.left < rc23.right) && (Rect.intersects(rc1, rc23)) && (rc1.right > rc23.left) && (rc1.left > rc23.left) && (rc1.top > rc23.top) && (rc1.bottom < rc23.bottom)) ||
                    ((rc1.left < rc24.right) && (Rect.intersects(rc1, rc24)) && (rc1.right > rc24.left) && (rc1.left > rc24.left) && (rc1.top > rc24.top) && (rc1.bottom < rc24.bottom)) ||
                    ((rc1.left < rc25.right) && (Rect.intersects(rc1, rc25)) && (rc1.right > rc25.left) && (rc1.left > rc25.left) && (rc1.top > rc25.top) && (rc1.bottom < rc25.bottom)) ||
                    ((rc1.left < rc26.right) && (Rect.intersects(rc1, rc26)) && (rc1.right > rc26.left) && (rc1.left > rc26.left) && (rc1.top > rc26.top) && (rc1.bottom < rc26.bottom)) ||
                    ((rc1.left < rc27.right) && (Rect.intersects(rc1, rc27)) && (rc1.right > rc27.left) && (rc1.left > rc27.left) && (rc1.top > rc27.top) && (rc1.bottom < rc27.bottom)) ||
                    ((rc1.left < rc28.right) && (Rect.intersects(rc1, rc28)) && (rc1.right > rc28.left) && (rc1.left > rc28.left) && (rc1.top > rc28.top) && (rc1.bottom < rc28.bottom)) ||
                    ((rc1.left < rc29.right) && (Rect.intersects(rc1, rc29)) && (rc1.right > rc29.left) && (rc1.left > rc29.left) && (rc1.top > rc29.top) && (rc1.bottom < rc29.bottom)) ||
                    ((rc1.left < rc30.right) && (Rect.intersects(rc1, rc30)) && (rc1.right > rc30.left) && (rc1.left > rc30.left) && (rc1.top > rc30.top) && (rc1.bottom < rc30.bottom)) ||
                    ((rc1.left < rc31.right) && (Rect.intersects(rc1, rc31)) && (rc1.right > rc31.left) && (rc1.left > rc31.left) && (rc1.top > rc31.top) && (rc1.bottom < rc31.bottom)) ||
                    ((rc1.left < rc32.right) && (Rect.intersects(rc1, rc32)) && (rc1.right > rc32.left) && (rc1.left > rc32.left) && (rc1.top > rc32.top) && (rc1.bottom < rc32.bottom)) ||
                    ((rc1.left < rc33.right) && (Rect.intersects(rc1, rc33)) && (rc1.right > rc33.left) && (rc1.left > rc33.left) && (rc1.top > rc33.top) && (rc1.bottom < rc33.bottom)))){
                Log.d("Catch collision", "Collision sur la gauche de la bille");

                // Calcul du frottement
                vitX = (float) (vitX + (-0.02 * vitX));
                // Calcul de la future position
                posX = posX + vitX;

                // On recule la bille pour simuler un rebond simple et éviter les erreurs de collision
                posX = posX + 5;
                // Calcul du frottement
                vitX = (float) (vitX * (-0.8));
            }

            // En fonction de ce jeu de conditions, nous détectons une collision sur le haut de la bille
            // Toutes les conditions permettent de ne pas déclarer une mauvaise collision si nous entrons dans le mur
            if ((((rc1.top < rc5.bottom) && (Rect.intersects(rc1, rc5)) && (rc1.bottom > rc5.top) && (rc1.top > rc5.top) && (rc1.left > rc5.left) && (rc1.right < rc5.right)) ||
                    ((rc1.top < rc6.bottom) && (Rect.intersects(rc1, rc6)) && (rc1.bottom > rc6.top) && (rc1.top > rc6.top) && (rc1.left > rc6.left) && (rc1.right < rc6.right)) ||
                    ((rc1.top < rc7.bottom) && (Rect.intersects(rc1, rc7)) && (rc1.bottom > rc7.top) && (rc1.top > rc7.top) && (rc1.left > rc7.left) && (rc1.right < rc7.right)) ||
                    ((rc1.top < rc8.bottom) && (Rect.intersects(rc1, rc8)) && (rc1.bottom > rc8.top) && (rc1.top > rc8.top) && (rc1.left > rc8.left) && (rc1.right < rc8.right)) ||
                    ((rc1.top < rc9.bottom) && (Rect.intersects(rc1, rc9)) && (rc1.bottom > rc9.top) && (rc1.top > rc9.top) && (rc1.left > rc9.left) && (rc1.right < rc9.right)) ||
                    ((rc1.top < rc10.bottom) && (Rect.intersects(rc1, rc10)) && (rc1.bottom > rc10.top) && (rc1.top > rc10.top) && (rc1.left > rc10.left) && (rc1.right < rc10.right)) ||
                    ((rc1.top < rc11.bottom) && (Rect.intersects(rc1, rc11)) && (rc1.bottom > rc11.top) && (rc1.top > rc11.top) && (rc1.left > rc11.left) && (rc1.right < rc11.right)) ||
                    ((rc1.top < rc12.bottom) && (Rect.intersects(rc1, rc12)) && (rc1.bottom > rc12.top) && (rc1.top > rc12.top) && (rc1.left > rc12.left) && (rc1.right < rc12.right)) ||
                    ((rc1.top < rc13.bottom) && (Rect.intersects(rc1, rc13)) && (rc1.bottom > rc13.top) && (rc1.top > rc13.top) && (rc1.left > rc13.left) && (rc1.right < rc13.right)) ||
                    ((rc1.top < rc14.bottom) && (Rect.intersects(rc1, rc14)) && (rc1.bottom > rc14.top) && (rc1.top > rc14.top) && (rc1.left > rc14.left) && (rc1.right < rc14.right)) ||
                    ((rc1.top < rc15.bottom) && (Rect.intersects(rc1, rc15)) && (rc1.bottom > rc15.top) && (rc1.top > rc15.top) && (rc1.left > rc15.left) && (rc1.right < rc15.right)) ||
                    ((rc1.top < rc16.bottom) && (Rect.intersects(rc1, rc16)) && (rc1.bottom > rc16.top) && (rc1.top > rc16.top) && (rc1.left > rc16.left) && (rc1.right < rc16.right)) ||
                    ((rc1.top < rc17.bottom) && (Rect.intersects(rc1, rc17)) && (rc1.bottom > rc17.top) && (rc1.top > rc17.top) && (rc1.left > rc17.left) && (rc1.right < rc17.right)) ||
                    ((rc1.top < rc18.bottom) && (Rect.intersects(rc1, rc18)) && (rc1.bottom > rc18.top) && (rc1.top > rc18.top) && (rc1.left > rc18.left) && (rc1.right < rc18.right)) ||
                    ((rc1.top < rc19.bottom) && (Rect.intersects(rc1, rc19)) && (rc1.bottom > rc19.top) && (rc1.top > rc19.top) && (rc1.left > rc19.left) && (rc1.right < rc19.right)) ||
                    ((rc1.top < rc20.bottom) && (Rect.intersects(rc1, rc20)) && (rc1.bottom > rc20.top) && (rc1.top > rc20.top) && (rc1.left > rc20.left) && (rc1.right < rc20.right)) ||
                    ((rc1.top < rc21.bottom) && (Rect.intersects(rc1, rc21)) && (rc1.bottom > rc21.top) && (rc1.top > rc21.top) && (rc1.left > rc21.left) && (rc1.right < rc21.right)) ||
                    ((rc1.top < rc22.bottom) && (Rect.intersects(rc1, rc22)) && (rc1.bottom > rc22.top) && (rc1.top > rc22.top) && (rc1.left > rc22.left) && (rc1.right < rc22.right)) ||
                    ((rc1.top < rc23.bottom) && (Rect.intersects(rc1, rc23)) && (rc1.bottom > rc23.top) && (rc1.top > rc23.top) && (rc1.left > rc23.left) && (rc1.right < rc23.right)) ||
                    ((rc1.top < rc24.bottom) && (Rect.intersects(rc1, rc24)) && (rc1.bottom > rc24.top) && (rc1.top > rc24.top) && (rc1.left > rc24.left) && (rc1.right < rc24.right)) ||
                    ((rc1.top < rc25.bottom) && (Rect.intersects(rc1, rc25)) && (rc1.bottom > rc25.top) && (rc1.top > rc25.top) && (rc1.left > rc25.left) && (rc1.right < rc25.right)) ||
                    ((rc1.top < rc26.bottom) && (Rect.intersects(rc1, rc26)) && (rc1.bottom > rc26.top) && (rc1.top > rc26.top) && (rc1.left > rc26.left) && (rc1.right < rc26.right)) ||
                    ((rc1.top < rc27.bottom) && (Rect.intersects(rc1, rc27)) && (rc1.bottom > rc27.top) && (rc1.top > rc27.top) && (rc1.left > rc27.left) && (rc1.right < rc27.right)) ||
                    ((rc1.top < rc28.bottom) && (Rect.intersects(rc1, rc28)) && (rc1.bottom > rc28.top) && (rc1.top > rc28.top) && (rc1.left > rc28.left) && (rc1.right < rc28.right)) ||
                    ((rc1.top < rc29.bottom) && (Rect.intersects(rc1, rc29)) && (rc1.bottom > rc29.top) && (rc1.top > rc29.top) && (rc1.left > rc29.left) && (rc1.right < rc29.right)) ||
                    ((rc1.top < rc30.bottom) && (Rect.intersects(rc1, rc30)) && (rc1.bottom > rc30.top) && (rc1.top > rc30.top) && (rc1.left > rc30.left) && (rc1.right < rc30.right)) ||
                    ((rc1.top < rc31.bottom) && (Rect.intersects(rc1, rc31)) && (rc1.bottom > rc31.top) && (rc1.top > rc31.top) && (rc1.left > rc31.left) && (rc1.right < rc31.right)) ||
                    ((rc1.top < rc32.bottom) && (Rect.intersects(rc1, rc32)) && (rc1.bottom > rc32.top) && (rc1.top > rc32.top) && (rc1.left > rc32.left) && (rc1.right < rc32.right)) ||
                    ((rc1.top < rc33.bottom) && (Rect.intersects(rc1, rc33)) && (rc1.bottom > rc33.top) && (rc1.top > rc33.top) && (rc1.left > rc33.left) && (rc1.right < rc33.right)))){
                Log.d("Catch collision", "Collision sur le haut de la bille");

                // Calcul du frottement
                vitY = (float) (vitY + (-0.02 * vitY));
                // Calcul de la future position
                posY = posY + vitY;

                // On recule la bille pour simuler un rebond simple et éviter les erreurs de collision
                posY = posY + 5;
                // Calcul du frottement
                vitY = (float) (vitY * (-0.8));
            }

            // En fonction de ce jeu de conditions, nous détectons une collision sur le bas de la bille
            // Toutes les conditions permettent de ne pas déclarer une mauvaise collision si nous entrons dans le mur
            if ((((rc1.bottom > rc5.top) && (Rect.intersects(rc1, rc5)) && (rc1.top < rc5.bottom) && (rc1.bottom < rc5.bottom) && (rc1.left > rc5.left) && (rc1.right < rc5.right)) ||
                    ((rc1.bottom > rc6.top) && (Rect.intersects(rc1, rc6)) && (rc1.top < rc6.bottom) && (rc1.bottom < rc6.bottom)&& (rc1.left > rc6.left) && (rc1.right < rc6.right)) ||
                    ((rc1.bottom > rc7.top) && (Rect.intersects(rc1, rc7)) && (rc1.top < rc7.bottom) && (rc1.bottom < rc7.bottom)&& (rc1.left > rc7.left) && (rc1.right < rc7.right)) ||
                    ((rc1.bottom > rc8.top) && (Rect.intersects(rc1, rc8)) && (rc1.top < rc8.bottom) && (rc1.bottom < rc8.bottom)&& (rc1.left > rc8.left) && (rc1.right < rc8.right)) ||
                    ((rc1.bottom > rc9.top) && (Rect.intersects(rc1, rc9)) && (rc1.top < rc9.bottom) && (rc1.bottom < rc9.bottom)&& (rc1.left > rc9.left) && (rc1.right < rc9.right)) ||
                    ((rc1.bottom > rc10.top) && (Rect.intersects(rc1, rc10)) && (rc1.top < rc10.bottom) && (rc1.bottom < rc10.bottom) && (rc1.left > rc10.left) && (rc1.right < rc10.right)) ||
                    ((rc1.bottom > rc11.top) && (Rect.intersects(rc1, rc11)) && (rc1.top < rc11.bottom) && (rc1.bottom < rc11.bottom) && (rc1.left > rc11.left) && (rc1.right < rc11.right)) ||
                    ((rc1.bottom > rc12.top) && (Rect.intersects(rc1, rc12)) && (rc1.top < rc12.bottom) && (rc1.bottom < rc12.bottom) && (rc1.left > rc12.left) && (rc1.right < rc12.right)) ||
                    ((rc1.bottom > rc13.top) && (Rect.intersects(rc1, rc13)) && (rc1.top < rc13.bottom) && (rc1.bottom < rc13.bottom) && (rc1.left > rc13.left) && (rc1.right < rc13.right)) ||
                    ((rc1.bottom > rc14.top) && (Rect.intersects(rc1, rc14)) && (rc1.top < rc14.bottom) && (rc1.bottom < rc14.bottom) && (rc1.left > rc14.left) && (rc1.right < rc14.right)) ||
                    ((rc1.bottom > rc15.top) && (Rect.intersects(rc1, rc15)) && (rc1.top < rc15.bottom) && (rc1.bottom < rc15.bottom) && (rc1.left > rc15.left) && (rc1.right < rc15.right)) ||
                    ((rc1.bottom > rc16.top) && (Rect.intersects(rc1, rc16)) && (rc1.top < rc16.bottom) && (rc1.bottom < rc16.bottom) && (rc1.left > rc16.left) && (rc1.right < rc16.right)) ||
                    ((rc1.bottom > rc17.top) && (Rect.intersects(rc1, rc17)) && (rc1.top < rc17.bottom) && (rc1.bottom < rc17.bottom) && (rc1.left > rc17.left) && (rc1.right < rc17.right)) ||
                    ((rc1.bottom > rc18.top) && (Rect.intersects(rc1, rc18)) && (rc1.top < rc18.bottom) && (rc1.bottom < rc18.bottom) && (rc1.left > rc18.left) && (rc1.right < rc18.right)) ||
                    ((rc1.bottom > rc19.top) && (Rect.intersects(rc1, rc19)) && (rc1.top < rc19.bottom) && (rc1.bottom < rc19.bottom) && (rc1.left > rc19.left) && (rc1.right < rc19.right)) ||
                    ((rc1.bottom > rc20.top) && (Rect.intersects(rc1, rc20)) && (rc1.top < rc20.bottom) && (rc1.bottom < rc20.bottom) && (rc1.left > rc20.left) && (rc1.right < rc20.right)) ||
                    ((rc1.bottom > rc21.top) && (Rect.intersects(rc1, rc21)) && (rc1.top < rc21.bottom) && (rc1.bottom < rc21.bottom) && (rc1.left > rc21.left) && (rc1.right < rc21.right)) ||
                    ((rc1.bottom > rc22.top) && (Rect.intersects(rc1, rc22)) && (rc1.top < rc22.bottom) && (rc1.bottom < rc22.bottom) && (rc1.left > rc22.left) && (rc1.right < rc22.right)) ||
                    ((rc1.bottom > rc23.top) && (Rect.intersects(rc1, rc23)) && (rc1.top < rc23.bottom) && (rc1.bottom < rc23.bottom) && (rc1.left > rc23.left) && (rc1.right < rc23.right)) ||
                    ((rc1.bottom > rc24.top) && (Rect.intersects(rc1, rc24)) && (rc1.top < rc24.bottom) && (rc1.bottom < rc24.bottom) && (rc1.left > rc24.left) && (rc1.right < rc24.right)) ||
                    ((rc1.bottom > rc25.top) && (Rect.intersects(rc1, rc25)) && (rc1.top < rc25.bottom) && (rc1.bottom < rc25.bottom) && (rc1.left > rc25.left) && (rc1.right < rc25.right)) ||
                    ((rc1.bottom > rc26.top) && (Rect.intersects(rc1, rc26)) && (rc1.top < rc26.bottom) && (rc1.bottom < rc26.bottom) && (rc1.left > rc26.left) && (rc1.right < rc26.right)) ||
                    ((rc1.bottom > rc27.top) && (Rect.intersects(rc1, rc27)) && (rc1.top < rc27.bottom) && (rc1.bottom < rc27.bottom) && (rc1.left > rc27.left) && (rc1.right < rc27.right)) ||
                    ((rc1.bottom > rc28.top) && (Rect.intersects(rc1, rc28)) && (rc1.top < rc28.bottom) && (rc1.bottom < rc28.bottom) && (rc1.left > rc28.left) && (rc1.right < rc28.right)) ||
                    ((rc1.bottom > rc29.top) && (Rect.intersects(rc1, rc29)) && (rc1.top < rc29.bottom) && (rc1.bottom < rc29.bottom) && (rc1.left > rc29.left) && (rc1.right < rc29.right)) ||
                    ((rc1.bottom > rc30.top) && (Rect.intersects(rc1, rc30)) && (rc1.top < rc30.bottom) && (rc1.bottom < rc30.bottom) && (rc1.left > rc30.left) && (rc1.right < rc30.right)) ||
                    ((rc1.bottom > rc31.top) && (Rect.intersects(rc1, rc31)) && (rc1.top < rc31.bottom) && (rc1.bottom < rc31.bottom) && (rc1.left > rc31.left) && (rc1.right < rc31.right)) ||
                    ((rc1.bottom > rc32.top) && (Rect.intersects(rc1, rc32)) && (rc1.top < rc32.bottom) && (rc1.bottom < rc32.bottom) && (rc1.left > rc32.left) && (rc1.right < rc32.right)) ||
                    ((rc1.bottom > rc33.top) && (Rect.intersects(rc1, rc33)) && (rc1.top < rc33.bottom) && (rc1.bottom < rc33.bottom) && (rc1.left > rc33.left) && (rc1.right < rc33.right)))){
                Log.d("Catch collision", "Collision sur le bas de la bille");

                // Calcul du frottement
                vitY = (float) (vitY + (-0.02 * vitY));
                // Calcul de la future position
                posY = posY + vitY;

                // On recule la bille pour simuler un rebond simple et éviter les erreurs de collision
                posY = posY - 5;
                // Calcul du frottement
                vitY = (float) (vitY * (-0.8));
            }
        }

		// Détection d'une collision avec un des blocs trou du jeu
        if (Rect.intersects(rc1, rc40) ||
                Rect.intersects(rc1, rc41) ||
                Rect.intersects(rc1, rc42) ||
                Rect.intersects(rc1, rc43) ||
                Rect.intersects(rc1, rc44) ||
                Rect.intersects(rc1, rc45)) {
			// la bille doit tomber complètement dans le trou
            if(rc1.top > rc20.top){
                Log.d("Catch collision", "Tombé dans le trou ! Dommage");
                // On dispose la bille au départ du labyrinthe
                posX = posXInitial;
                posY = posYInitial;
            }
        }

        // Détection d'une collision avec l'arrivée
        if (Rect.intersects(rc1, rc4)) {
            // La bille doit être complètement dans le bloc de l'arrivée
            if(rc1.top > rc4.top){
                Log.d("Catch collision", "Bravo vous etes arrivé ! Ouaaaaaais");
                // On retourne un flag pour afficher un toast dans l'activité jeu et avertir le joueur qu'il a gagné
                flagArrivee = true;
                getFlag();
            }
        }

        // Comme nous utilisons une autre classe pour gérer les threads, nous utilisons la méthode post
        myBilleImageView.post(new Runnable() {
            @Override
            // on paramètre la position de la bille par les setters et envoyons ces valeurs dans le thread
            public void run() {
                myBilleImageView.setX(posX);
                myBilleImageView.setY(posY);
            }
        });
    }
}
