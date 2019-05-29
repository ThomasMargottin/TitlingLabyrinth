package com.example.myprojectandroid;

/* Importation des bibliothèques à utiliser */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class MainActivity extends AppCompatActivity {

    //Déclaration d'un attribut constant qui servira de clé pour les filtres d’intention
    public final static String NOM = "fr.ensea.margottinjanishon.myprojectandroid.intent.NOM";
    // Définition des niveaux appelés dans le Spinner
    Spinner spin;
    String[] choiceNiveau = {"Niveau 1", "Niveau 2"};

    /* Dynamique de la page d'accueil */
    @Override
    // Le bundle mémorise l'état de l'UI de l'activité lorsqu'elle passe en arrière plan
    // Si la future activité JeuActivite est tuée, on peut revenir à cette activité
    protected void onCreate(Bundle savedInstanceState) {
        // On précise ici la vue à afficher dans ce callback
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instanciation d'un Spinner pour le choix du niveau
        spin = (Spinner) findViewById(R.id.spinner);

        // Quand on clique sur le bouton Jouer, on passe dans l'activite suivante grace a l'objet Intent
        // Par la suite, nous verrons que le choix du niveau sera conditionné par le choix dans le spinner
        Button bJouer = (Button) findViewById(R.id.buttonJouer);

        // Création d'un ArrayAdapter qui contient la liste des niveaux
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, choiceNiveau);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Afficher les données de ArrayAdapter dans le Spinner
        spin.setAdapter(aa);

        // Listener sur le bouton qui prends en argument un View.OnClickListener pour capturer l'appui sur le bouton
        bJouer.setOnClickListener(new View.OnClickListener() {
            @Override
            // Méthode OnClick appelée à chaque appui sur le bouton
            public void onClick(View v) {

                // l'utilisateur décide de jouer le niveau 1
                if(spin.getSelectedItem() == choiceNiveau[0]){
                    // Utilisation d'un Intent explicite car l’activité cible est toujours la même
                    Intent intent = new Intent(MainActivity.this, JeuActivity.class);

                    // Récupération du nom du joueur à partir du Widget EditText
                    EditText playerName = (EditText) findViewById(R.id.editTextPlayerName);

                    // Affichage du nom du joueur dans le LogCat
                    Log.d("MainActivity", "Player name = " + playerName.getText());
                    // On associe a notre clé le nom renseigné par l'utilisateur
                    // On ajoute toString() pour convertir le android.text.SpannableString en string
                    intent.putExtra(NOM, "" + playerName.getText().toString());
                    // Démarre la nouvelle activité
                    startActivity(intent);
                }

                // l'utilisateur décide de jouer le niveau 2
                else if(spin.getSelectedItem() == choiceNiveau[1]){
                    // Utilisation d'un Intent explicite car l’activité cible est toujours la même
                    Intent intent2 = new Intent(MainActivity.this, JeuActivity2.class);

                    // Récupération du nom du joueur à partir du Widget EditText
                    EditText playerName = (EditText) findViewById(R.id.editTextPlayerName);

                    // Affichage du nom du joueur dans le LogCat
                    Log.d("MainActivity", "Player name = " + playerName.getText());
                    // On associe a notre clé le nom renseigné par l'utilisateur
                    // On ajoute toString() pour convertir le android.text.SpannableString en string
                    intent2.putExtra(NOM, "" + playerName.getText().toString());
                    // Démarre la nouvelle activité
                    startActivity(intent2);
                }
            }
        });
    }
}
