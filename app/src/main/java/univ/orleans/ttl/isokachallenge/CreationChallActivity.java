package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;

// TODO Comments
public class CreationChallActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_chall);
        navigationView = findViewById(R.id.navigation_menu);
        this.sharedPref = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        if( !(sharedPref.getString("username","").equals(""))){
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, true);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, false);
        }else{
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, false);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, true);

        }

        setUpToolbar();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case  R.id.nav_connexion:
                    Intent intent = new Intent(this, ConnexionView.class);
                    startActivity(intent);
                    finish();
                    break;
                case  R.id.nav_inscription:
                    Intent inscription = new Intent(this, InscriptionActivity.class);
                    startActivity(inscription);
                    finish();
                    break;
                case R.id.nav_challenge:
                    Intent challenge = new Intent(this, MainActivity.class);
                    startActivity(challenge);
                    finish();
                    break;
                case R.id.nav_profil:
                    Intent profil = new Intent(this, Profil.class);
                    startActivity(profil);
                    finish();
                    break;
                case R.id.nav_deconnexion:
                    Intent deco = new Intent(this, DeconnexionView.class);
                    startActivity(deco);
                    finish();
                    break;
            }
            return false;
        });
    }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Confirmation de la création du challenge
     * Nécéssite d'être connecté et d'avoir completer tous les champs correctement
     * Appel l'api afin de sauvegarder le challenge sur la BD distante
     * Le challange est aussi sauvegardé sur la BD local.
     */
    public void onCreateChall(View view) {
        // Si l'utilisateur est connecté
        if (!(this.sharedPref.getString("username","").equals(""))) {
            EditText name = findViewById(R.id.inputNameChall);
            EditText desc = findViewById(R.id.inputDesc);
            EditText theme = findViewById(R.id.inputTheme);
            DatePicker dateFin = findViewById(R.id.inputFinDate);
            EditText timer = findViewById(R.id.inputTimer);
            TextView errorLabel = findViewById(R.id.errorCreateChall);

            int month = dateFin.getMonth() + 1;

            if (name.getText().toString().equals("") || desc.getText().toString().equals("") || theme.getText().toString().equals("") || timer.getText().toString().equals("")) {
                // Si un champ est vide, afficher un message
                errorLabel.setText(R.string.errorCreateChallEmpty);
            } else {
                // Clear du potentiel message d'erreur
                errorLabel.setText("");

                // Récupération de la date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(dateFin.getDayOfMonth() + "/" + month + "/" + dateFin.getYear() + " 00:00", formatter);

                // Création du challenge
                Challenge chall = new Challenge(name.getText().toString(), true, theme.getText().toString(), dateTime, Integer.valueOf(timer.getText().toString()) ,desc.getText().toString());
                // Création de l'intent pour la participation
                Intent gotoChall = new Intent(this, onChallenge.class);

                ProgressBar pg = findViewById(R.id.progressBar);
                pg.setVisibility(View.VISIBLE);

                // Sauvegarde du context pour l'affichage des Toasts
                AppCompatActivity context = this;

                // Lancement de la requête pour sauvegarder le challenge
                new RequestWrapper().save(RequestWrapper.ROUTES.CHALLENGE, chall.toJson(), new JSONObjectRequestListener() {
                    // Si la sauvegarde réussie ...
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Sauvegarde du challenge dans la base de données locale
                            Challenge challenge = Challenge.fromJson(response);
                            DB.getInstance().save(challenge);

                            // Fin de l'activity, et redirection vers le challenge créer
                            gotoChall.putExtra("idchall", challenge.getId());
                            finish();
                            startActivity(gotoChall);
                        } catch (JSONException e) {
                            // Erreur de parse des données reçues dans response (Save Challenge)
                            e.printStackTrace();
                            Toast.makeText(context, R.string.corrupted_response_data, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                    // Si la sauvegarde échoue ...
                    @Override
                    public void onError(ANError anError) {
                        // Affichage d'un Toast
                        Toast.makeText(context, R.string.unableToSaveChallenge, Toast.LENGTH_LONG).show();
                        pg.setVisibility(View.INVISIBLE);
                    }
                });
            }
        } else {
            // Si l'utilisateur n'est pas connecté, redirection vers la connexion
            Intent intent = new Intent(this, ConnexionView.class);
            startActivity(intent);
        }
    }
}