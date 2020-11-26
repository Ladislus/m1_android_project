package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class Profil extends AppCompatActivity {

    private TextView username, password, msgRetourValidation;
    private EditText edit_password;
    private Button modifier, valider;
    private SharedPreferences pref;
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
                    break;
                case  R.id.nav_inscription:
                    Intent inscription = new Intent(this, InscriptionActivity.class);
                    startActivity(inscription);
                    break;
                case R.id.nav_challenge:
                    Intent challenge = new Intent(this, MainActivity.class);
                    startActivity(challenge);
                    break;
                case R.id.nav_challengeTest:
                    Intent act = new Intent(this, onChallenge.class);
                    startActivity(act);
                    break;
                case R.id.nav_deconnexion:
                    Intent deco = new Intent(this, DeconnexionView.class);
                    startActivity(deco);
                    break;
                case R.id.nav_createChall:
                    Intent create = new Intent(this, CreationChallActivity.class);
                    startActivity(create);
                    break;
            }
            return false;
        });

        pref = getApplicationContext().getSharedPreferences("session", MODE_PRIVATE);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        edit_password = findViewById(R.id.inputNewPassword);
        modifier = findViewById(R.id.btn_modifier);
        valider = findViewById(R.id.btn_valider);
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
        modifier.setVisibility(View.GONE);
        username.setVisibility(View.GONE);

        password.setVisibility(View.VISIBLE);
        edit_password.setVisibility(View.VISIBLE);
        valider.setVisibility(View.VISIBLE);
    }

    public void valider_username(View view) {
        Boolean bool =  MainActivity.db.updatePassword(pref.getString("username",null),edit_password.getText().toString());

        username.setVisibility(View.VISIBLE);
        modifier.setVisibility(View.VISIBLE);
        msgRetourValidation.setVisibility(view.VISIBLE);

        edit_password.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        valider.setVisibility(View.GONE);

        if (bool){
            msgRetourValidation.setText(R.string.text_validation_password_updated);
            msgRetourValidation.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        }else{
            msgRetourValidation.setText(R.string.txt_validate_password_not_updated);
            msgRetourValidation.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
        }


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
}