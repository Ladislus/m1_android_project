package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;

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

        SharedPreferences sharedPref = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        if( !(sharedPref.getString("username","").equals(""))){
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
                case R.id.nav_challengeTest:
                    Intent act = new Intent(this, onChallenge.class);
                    startActivity(act);
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

        pref = getApplicationContext().getSharedPreferences("session", MODE_PRIVATE);

        username = findViewById(R.id.username);
        edit_password = findViewById(R.id.inputNewPassword);
        modifier = findViewById(R.id.btn_modifier);
        valider = findViewById(R.id.btn_valider);
        validerMDP = findViewById(R.id.btn_mdp);
        msgRetourValidation = findViewById(R.id.msgRetourValidation);
        username.setText(username.getText()+" "+pref.getString("username",null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void modifier_username(View view) {
        username.setText("Enter your actual password :");
        old_pass = edit_password.getText().toString();


    }

    public void valider_new_mdp(View view) {
        //TODO

        username.setText(getResources().getString(R.string.pseudoTitre)+pref.getString("username",null));
        modifier.setVisibility(View.VISIBLE);
        msgRetourValidation.setVisibility(View.VISIBLE);

        edit_password.setVisibility(View.GONE);
        valider.setVisibility(View.GONE);

        new RequestWrapper().updatePassword(pref.getString("username",null),old_pass,edit_password.getText().toString(),new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                msgRetourValidation.setText(R.string.text_validation_password_updated);
                msgRetourValidation.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
            }

            @Override
            public void onError(ANError anError) {
                msgRetourValidation.setText(R.string.txt_validate_password_not_updated);
                msgRetourValidation.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
                Log.d(RequestWrapper.REQUEST_LOG, anError.toString());
                Log.d(RequestWrapper.REQUEST_LOG, anError.getErrorDetail());
                Log.d(RequestWrapper.REQUEST_LOG, anError.getErrorBody());
                Log.d(RequestWrapper.REQUEST_LOG, String.valueOf(anError.getErrorCode()));
            }
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

    public void verif_mdp(View view) {
        username.setText("Enter your actual password :");

        edit_password.setVisibility(View.VISIBLE);
        modifier.setVisibility(View.GONE);
        validerMDP.setVisibility(View.VISIBLE);
    }

    public void new_mpd(View view) {
        old_pass = edit_password.getText().toString();
        edit_password.setText("");
        username.setText("Enter your new password :");
        validerMDP.setVisibility(View.GONE);
        valider.setVisibility(View.VISIBLE);
    }
}