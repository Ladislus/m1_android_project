package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import univ.orleans.ttl.isokachallenge.orm.Callback;
import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.Tables;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class ParcoursParticipation extends AppCompatActivity {

    // Composants de ParcoursParticipation
    private int id_chall;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours_participation);
        // recupération element xml
        navigationView = findViewById(R.id.navigation_menu);
        // recupération des sharedPref
        SharedPreferences sharedPref = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        if( !(sharedPref.getString("username","").equals(""))){
            //If user connecté
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, true);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, false);
        }else{
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, false);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, true);
        }

        // gestion de la Toolbar
        setUpToolbar();
        //gestion de la navigationView
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case  R.id.nav_challenge:
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                case  R.id.nav_inscription:
                    Intent inscription = new Intent(this, InscriptionActivity.class);
                    startActivity(inscription);
                    break;
                case R.id.nav_deconnexion:
                    Intent deco = new Intent(this, DeconnexionView.class);
                    startActivity(deco);
                    break;
                case R.id.nav_createChall:
                    Intent create = new Intent(this, CreationChallActivity.class);
                    startActivity(create);
                    break;
                case R.id.nav_profil:
                    Intent profil = new Intent(this, Profil.class);
                    startActivity(profil);
                    break;
                case  R.id.nav_connexion:
                    Intent connexion = new Intent(this, ConnexionView.class);
                    startActivity(connexion);
                    break;
            }
            return false;
        });
        // Récupération de l'id du challenge par l'intent
        this.id_chall = getIntent().getIntExtra("id_chall", 0);

        // Récupération de la progress bar
        ProgressBar progressBar = findViewById(R.id.progressBar);

        // Affichage de la progress bar
        progressBar.setVisibility(View.VISIBLE); //progress bar visible
        // Affichage des participations de la base de données locale
        // (En attendant la fin de la requête de mise à jour de la base de données)
        loadParticipations();

        AppCompatActivity context = this;
        // synchro des BD local et distante
        new RequestWrapper().get(new Callback() {
            // Si la requête réussie ...
            @Override
            public void onResponse() {
                // ... Mise à jour de l'affichage
                loadParticipations();
                // ... Suppression de la progress bar
                progressBar.setVisibility(View.GONE); //progress bar visible
            }

            // Si la requête échoue
            @Override
            public void onError(ANError error) {
                // ... Suppression de la progress bar
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, R.string.connexionImpossibleServ, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Gestion de l'affichage des challenge
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
     * Gestion du click sur le symbol du tiroir de navigation
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadParticipations() {
        // Récupération des elements du xml
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewParticipation);
        TextView pasParticipation = (TextView) findViewById(R.id.participation0);

        HashMap<String, Pair<String, String>> map = new HashMap<>();
        map.put(Tables.PARTICIPATION_CHALLENGE_ID, new Pair<>(Tables.OPERATOR_EQ, String.valueOf(id_chall)));
        List<Participation> participations = DB.getInstance().getParticipations(map);

        // tri des participation par date
        participations.sort((o1, o2) -> {
            String dateString1 = o1.getDrawing().getDate();
            String dateString2 = o2.getDrawing().getDate();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime dateTime1 = LocalDateTime.parse(dateString1, formatter);
            LocalDateTime dateTime2 = LocalDateTime.parse(dateString2, formatter);
            if(dateTime1.isAfter(dateTime2)) {
                Log.d("Sort","1");
                return -1;
            } else if(dateTime1.isBefore(dateTime2)) {
                Log.d("Sort","-1");
                return 1;
            } else {
                Log.d("Sort","0");
                return 0;
            }

        });

        // si des participation
        if (!participations.isEmpty()) {
            // gestion de l'affichage des participation
            recyclerView.setVisibility(View.VISIBLE);
            pasParticipation.setVisibility(View.GONE);

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(new ParticipationAdapteur(this, participations));
        } else {// si pas de participation
            recyclerView.setVisibility(View.GONE);
            pasParticipation.setVisibility(View.VISIBLE);

            pasParticipation.setText(getResources().getString(R.string.no_participation_challenge));
        }
    }
}