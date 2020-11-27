package univ.orleans.ttl.isokachallenge;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;

public class onParticiperChrono extends AppCompatActivity {

    private int id_chall;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_participer_chrono);
        this.id_chall = getIntent().getIntExtra("id_chall", 0);
        DB db = DB.getInstance();
        Challenge chall = db.getChallenge(id_chall);
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
                case R.id.nav_profil:
                    Intent profil = new Intent(this, Profil.class);
                    startActivity(profil);
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

        if(chall != null){
            ImageView iv = findViewById(R.id.imageViewChrono);
            Picasso.get().load(chall.getTheme()).into(iv);
            TextView labelChrono = findViewById(R.id.chronoLabel);

            long tempsTotal = chall.getTimer()*60000;
            new CountDownTimer(tempsTotal, 1) {
                long tempsEcoule = 0;
                String labelSecondes = "00";
                String labelMinutes = "00";
                String labelHeures = "00";
                long nbSecondes = 0;
                long nbMinutes = 0;
                long nbHeures = 0;
                public void onTick(long millisUntilFinished) {

                    tempsEcoule = tempsTotal - millisUntilFinished;
                    nbSecondes = millisUntilFinished / 1000;
                    nbMinutes = nbSecondes / 60;
                    nbHeures = nbMinutes / 60;
                    if(nbHeures > 0){
                        nbMinutes = nbMinutes - (nbHeures * 60);
                    }
                    if(nbMinutes > 0){
                        nbSecondes = nbSecondes - (nbMinutes * 60);
                    }else{
                        if(nbHeures > 0){
                            nbSecondes = nbSecondes - (nbHeures * 3600);
                        }
                    }
                    if(nbSecondes < 10){
                        labelSecondes = "0"+String.valueOf(nbSecondes);
                    }else{
                        labelSecondes = String.valueOf(nbSecondes);
                    }
                    if(nbMinutes < 10){
                        labelMinutes = "0"+String.valueOf(nbMinutes);
                    }else{
                        labelMinutes = String.valueOf(nbMinutes);
                    }
                    if(nbHeures < 10){
                        labelHeures = "0"+String.valueOf(nbHeures);
                    }else{
                        labelHeures = String.valueOf(nbHeures);
                    }
                    labelChrono.setText(labelHeures+":"+labelMinutes+":"+labelSecondes);
                }

                public void onFinish() {
                    labelChrono.setText("Fini ! Prennez le en photo !");
                }
            }.start();
        }
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void onPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Intent intent = new Intent(this, onConfirmationParticipation.class);
            intent.putExtra("bitmap", imageBitmap);
            intent.putExtra("idchall",this.id_chall);
            startActivity(intent);
            finish();
        }
    }
}