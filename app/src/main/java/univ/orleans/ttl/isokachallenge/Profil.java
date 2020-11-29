package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class Profil extends AppCompatActivity {

    private TextView username, msgRetourValidation;
    private EditText edit_password;
    private Button modifier, valider, validerMDP;
    private SharedPreferences pref;
    private String old_pass;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        navigationView = findViewById(R.id.navigation_menu);

        // Récupération des SharedPref
        this.pref = this.getSharedPreferences("session", Context.MODE_PRIVATE);

        // Si l'utilisateur est connecté
        if (!(this.pref.getString("username", "").equals(""))) {
            // Changement des options du tirroir de navigation
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, true);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, false);
        } else {
            // Changement des options du tirroir de navigation
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
                case R.id.nav_deconnexion:
                    Intent deco = new Intent(this, DeconnexionView.class);
                    startActivity(deco);
                    finish();
                    break;
                case R.id.nav_createChall:
                    Intent create = new Intent(this, CreationChallActivity.class);
                    startActivity(create);
                    finish();
                    break;
            }
            return false;
        });

        // Récupération des champs
        username = findViewById(R.id.username);
        edit_password = findViewById(R.id.inputNewPassword);
        modifier = findViewById(R.id.btn_modifier);
        valider = findViewById(R.id.btn_valider);
        validerMDP = findViewById(R.id.btn_mdp);
        msgRetourValidation = findViewById(R.id.msgRetourValidation);
        
        // Affichage du nom d'utilisateur connecté
        username.setText(username.getText() + " " + this.pref.getString("username",null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Change le textView, recupère le nouveau mdp et fait la requête pour le changer
     * @param view La view sur laquelle se trouve le bouton
     */
    public void valider_new_mdp(View view) {
        // Lancement de la requête de vérification de l'existance de l'utilisateur
        new RequestWrapper().getUser(pref.getString("username", null), new JSONObjectRequestListener() {
            // Si l'utilisateur existe ...
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // ... Tentative d'insertion dans la base de données locale ...
                    DB.getInstance().save(User.fromJson(response));
                    // ... Lancement de la requête de mise à jour du mot de passe
                    new RequestWrapper().updatePassword(pref.getString("username",null),old_pass,edit_password.getText().toString(),new JSONObjectRequestListener() {
                        // Si la requête réussie ...
                        @Override
                        public void onResponse(JSONObject response) {
                            // Message de confirmation
                            msgRetourValidation.setText(R.string.text_validation_password_updated);
                            msgRetourValidation.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));

                            // ... Affichage de la page profil de base
                            username.setText(getResources().getString(R.string.pseudoTitre) + pref.getString("username",null));
                            modifier.setVisibility(View.VISIBLE);
                            msgRetourValidation.setVisibility(View.VISIBLE);
                            edit_password.setVisibility(View.GONE);
                            valider.setVisibility(View.GONE);

                            // Clear de old_pass
                            old_pass = "";
                        }

                        // Si la requête echoue ...
                        @Override
                        public void onError(ANError anError) {
                            // ... Affichage d'un message d'erreur
                            msgRetourValidation.setText(R.string.txt_validate_password_not_updated);
                            msgRetourValidation.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));

                            // ... Affichage de la page de profil de base
                            username.setText(getResources().getString(R.string.pseudoTitre) + pref.getString("username",null));
                            modifier.setVisibility(View.VISIBLE);
                            msgRetourValidation.setVisibility(View.VISIBLE);
                            edit_password.setVisibility(View.GONE);
                            valider.setVisibility(View.GONE);

                            // ... Clear de old_pass
                            old_pass = "";
                        }
                    });
                } catch (JSONException e) {
                    // Erreur lors de la reception des données
                    e.printStackTrace();

                    // ... Affichage d'un message d'erreur
                    msgRetourValidation.setText(R.string.corrupted_response_data);
                    msgRetourValidation.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));

                    // ... Affichage de la page de profil de base
                    username.setText(getResources().getString(R.string.pseudoTitre) + pref.getString("username",null));
                    modifier.setVisibility(View.VISIBLE);
                    msgRetourValidation.setVisibility(View.VISIBLE);
                    edit_password.setVisibility(View.GONE);
                    valider.setVisibility(View.GONE);

                    // ... Clear de old_pass
                    old_pass = "";
                }
            }

            // Si l'utilisateur n'existe pas
            @Override
            public void onError(ANError anError) {
                msgRetourValidation.setText(R.string.server_desync);
                msgRetourValidation.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));

                // ... Affichage de la page de profil de base
                username.setText(getResources().getString(R.string.pseudoTitre) + pref.getString("username",null));
                modifier.setVisibility(View.VISIBLE);
                msgRetourValidation.setVisibility(View.VISIBLE);
                edit_password.setVisibility(View.GONE);
                valider.setVisibility(View.GONE);

                // ... Clear de old_pass
                old_pass = "";
            }
        });
    }

    /**
     * configuration de ma Toolbar et de tiroir de navigation
     */
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

    /**
     * Affiche le bon textView et fait disparaitre les bouton
     */
    public void verif_mdp(View view) {
        username.setText(R.string.ActualPasswordDisplay);

        edit_password.setVisibility(View.VISIBLE);
        modifier.setVisibility(View.GONE);
        validerMDP.setVisibility(View.VISIBLE);
    }

    /**
     * recupère  l'ancien mot de passe, change le text et change le bouton
     */
    public void new_mpd(View view) {
        old_pass = edit_password.getText().toString();
        edit_password.setText("");
        username.setText(R.string.NewPasswordDisplay);
        validerMDP.setVisibility(View.GONE);
        valider.setVisibility(View.VISIBLE);
    }
}