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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

//TODO comments
public class ConnexionView extends AppCompatActivity {

    private EditText mdpField, loginField;
    private CheckBox checkLogin;
    private boolean remember;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion_view);
        //Récupération des inputs de l'utilisateur
        this.loginField = findViewById(R.id.inputLogin);
        this.mdpField = findViewById(R.id.inputMDP);
        this.checkLogin = findViewById(R.id.checkLogin);
        this.remember = false;

        navigationView = findViewById(R.id.navigation_menu);

        this.sharedPref = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        if(!(this.sharedPref.getString("username","").equals(""))){
            //If user connecté
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
                case R.id.nav_createChall:
                    Intent create = new Intent(this, CreationChallActivity.class);
                    startActivity(create);
                    finish();
                    break;
            }
            return false;
        });
    }
    /**
     * Fonction qui connecte un utilisateur à l'application en stockant dans une
     * sharepref global (nommée 'session') sont id (username).
     * L'utilisateur est ensuite redirigé vers l'activité MainActivity (challenge en cours a.k.a l'acceuil).
     * Fonction appelée lors du clique sur le bouton 'connexion' de l'activité
     * ConnexionView
     */
    public void onConnexion(View view) {
        RequestWrapper rq = new RequestWrapper();
        ProgressBar pg = findViewById(R.id.progressBar);
        Intent home = new Intent(this, MainActivity.class);

        JSONObjectRequestListener loginCallback = new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Une fois le mot de passe validé, on ajoute l'utilisateur
                    //dans le shared pref session sous la clé "username"
                    pg.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", response.getString("username"));
                    editor.apply();
                    finish();
                    //Redirection vers l'accueil
                    startActivity(home);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                pg.setVisibility(View.INVISIBLE);
                TextView labelError = findViewById(R.id.errorConnexion);
                labelError.setText(R.string.connexionImpossibleServ);
                if(anError.getErrorCode() == 400){
                    labelError.setText(R.string.invalidInfo);
                }
            }
        };

        JSONObjectRequestListener getUserCallback = new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Après avoir récupéré l'utilisateur, on l'ajoute en BD local puis on lance la
                    //Requete login pour vérifier son mot de passe
                    DB.getInstance().save(User.fromJson(response));
                    new RequestWrapper().login(loginField.getText().toString(), mdpField.getText().toString(), loginCallback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                pg.setVisibility(View.INVISIBLE);
                TextView labelError = findViewById(R.id.errorConnexion);
                labelError.setText(R.string.connexionImpossibleServ);
                if (anError.getErrorCode() == 400) {
                    labelError.setText(R.string.invalidInfo);
                }
            }
        };

        pg.setVisibility(View.VISIBLE);
        rq.getUser(this.loginField.getText().toString(), getUserCallback);
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
     * Save les inputs si la case "Se souvenir de moi" et cochée
     */
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = this.sharedPref.edit();

        if(this.checkLogin.isChecked()){
            editor.putString("login", String.valueOf(this.loginField.getText()));
            editor.putString("mdp", String.valueOf(this.mdpField.getText()));
        }else{
            editor.putString("login", "");
            editor.putString("mdp", "");
        }
        editor.putBoolean("isChecked", this.checkLogin.isChecked());
        editor.apply();
    }
    /**
     * Restore les inputs s'ils étaient save
     */
    @Override
    protected void onStart() {
        super.onStart();
        this.loginField.setText(this.sharedPref.getString("login",""));
        this.mdpField.setText(this.sharedPref.getString("mdp",""));
        this.remember = this.sharedPref.getBoolean("isChecked",false);
        this.checkLogin.setChecked(this.remember);
    }

    /**
     * Setteur de l'attribut remember lors du clique sur la checkbox 'Se souvenir de moi'
     */
    public void onChecked(View view) {
        this.remember = this.checkLogin.isChecked();
    }
}