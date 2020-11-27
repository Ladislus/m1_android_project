package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

public class InscriptionActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        navigationView = findViewById(R.id.navigation_menu);

        SharedPreferences sharedPref = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        if( !(sharedPref.getString("username","").equals(""))){
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, true);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, false);
        }else{
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, false);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, true);
        }

        setUpToolbar();
        this.db = DB.getInstance();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case  R.id.nav_connexion:
                    Intent intent = new Intent(this, ConnexionView.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.nav_challenge:
                    Intent challenge = new Intent(this, MainActivity.class);
                    startActivity(challenge);
                    finish();
                    break;
                case R.id.nav_challengeTest:
                    Intent act = new Intent(this, onChallenge.class);
                    startActivity(act);
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

    public void onInscrire(View view) {
        /**
         * Fonction qui valide l'inscription et ajoute l'utilisateur à la fois dans la
         * BD locale et Distante.
         * Nécessite d'avoir remplis tout les champs
         * Que les deux mots de passe correspondent et que le username soit inutilisé.
         */
        EditText username = findViewById(R.id.inputLoginInscr);
        EditText mdp1 = findViewById(R.id.inputmdp1Inscription);
        EditText mdp2 = findViewById(R.id.inputmdp2Inscription);
        TextView errorMsg = findViewById(R.id.errorLabelInscr);
        ProgressBar pg = findViewById(R.id.progressBar);


        if(username.getText().toString().equals("") || mdp1.getText().toString().equals("") || mdp2.getText().toString().equals("")){
            errorMsg.setText(R.string.ErrorInscriptionInput);
        }else{
            if(mdp1.getText().toString().equals(mdp2.getText().toString())){
                if(this.db.getUser(username.getText().toString()) != null ){
                    errorMsg.setText(R.string.ErrorInscriptionUsername);
                }else{
                    errorMsg.setText("");
                    User user = new User(username.getText().toString(), LocalDateTime.now());
                    RequestWrapper rq = new RequestWrapper();
                    Intent connect = new Intent(this , ConnexionView.class);
                    JSONObjectRequestListener callback = new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            db.save(user);
                            finish();
                            startActivity(connect);
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d("inscrUser",anError.getErrorBody());
                            Log.d("inscrUser", user.getDate());
                            Log.d("inscrUser", user.getFormattedDate());
                            Log.d("inscrUser",anError.getErrorDetail());
                            Log.d("inscrUser", String.valueOf(anError.getErrorCode()));
                            pg.setVisibility(View.INVISIBLE);
                            errorMsg.setText(R.string.connexionImpossibleServ);
                            if (anError.getErrorCode() == 409){
                                errorMsg.setText(R.string.ErrorInscriptionUsername);
                            }
                        }
                    };
                    pg.setVisibility(View.VISIBLE);
                    rq.save(RequestWrapper.ROUTES.USER, user.toJson(mdp1.getText().toString()), callback );
                }
            }else{
                errorMsg.setText(R.string.ErrorInscriptionMDP);
            }
        }



    }
}