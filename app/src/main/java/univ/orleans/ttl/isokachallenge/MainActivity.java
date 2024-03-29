package univ.orleans.ttl.isokachallenge;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.content.Context;

import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;

import com.androidnetworking.error.ANError;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import univ.orleans.ttl.isokachallenge.orm.Callback;
import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;

public class MainActivity extends AppCompatActivity {

    // Composants de l'accueil
    private RecyclerView recyclerView;
    private List<Challenge> challenges;
    private ChallengeAdapter monAdapteur;
    private ProgressBar pg;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Récuperation d'un element du xml
        navigationView = findViewById(R.id.navigation_menu);
        // Initialisation de la base de données
        AndroidNetworking.initialize(getApplicationContext());
        if(!DB.isInitialized()){
            DB.init(this);
        }

        // Récupération des ShardPref
        this.sharedPref = getSharedPreferences("session", Context.MODE_PRIVATE);

        // test pour savoir si l'user est connecté
        if( !(this.sharedPref.getString("username","").equals(""))){
            //If user connecté
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, true);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, false);
        }else{
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, false);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, true);
        }

        setUpToolbar(); // gestion de la bar de navigation

        // Récupération d'élements du xml
        navigationView = findViewById(R.id.navigation_menu);
        pg = findViewById(R.id.progressBar);
        // mise en place d'un listener sur le tiroir de navigation
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {   // si case vrai alors lancé une autre activity
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
                    this.onResume();
                    break;
                case R.id.nav_profil:
                    Intent profil = new Intent(this, Profil.class);
                    startActivity(profil);
                    break;
                case R.id.nav_deconnexion:
                    Intent deco = new Intent(this, DeconnexionView.class);
                    startActivity(deco);
                    finish();
                    break;
                case R.id.nav_createChall:
                    Intent create = new Intent(this, CreationChallActivity.class);
                    startActivity(create);
                    break;
            }
            // fermeture du tiroir de navigation
            drawerLayout.closeDrawers();
            return false;
        });
        // Récupération d'élements du xml
        recyclerView = findViewById(R.id.myRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Test si user connecté
        if( !(this.sharedPref.getString("username","").equals(""))){
            //If user connecté
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, true);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, false);
        }else{
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, false);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, true);
        }

        // Mise à jour base de données locale
        loadChallenge();
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
     * Gestion du click sur le symbol du tiroir de navigation
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Gestion de l'affichage des challenge
     */
    public void loadChallenge() {
        // Synchronisation BD local et distante
        new RequestWrapper().get(new Callback() {
            @Override
            public void onResponse() { // Si les 2 BD arrivent à être synchro alors

                // Mise à jour de l'affichage des challenges après mise à jour de la base de données locale
                displayChallenges();

                pg.setVisibility(View.GONE); // disparition de la progress bar = fin synchro BD
            }

            @Override
            public void onError(ANError error) {
                Toast.makeText(getApplicationContext(), R.string.errorLoadChall, Toast.LENGTH_LONG).show();
                pg.setVisibility(View.GONE); // disparition de la progress bar = fin synchro BD
            }
        });
        pg.setVisibility(View.VISIBLE); // tant que synchro pas fini progress bar visible

        // Mise à jour de l'affichage des challenges après mise à jour de la base de données locale
        displayChallenges();
    }

    public void displayChallenges() {
        challenges = DB.getInstance().getAllChallenges(); // récupération de tous les challenges

        sortList(challenges); // trie challenge par date

        // affectation challenge adapteur
        monAdapteur = new ChallengeAdapter(challenges);
        // gestion recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(monAdapteur);
        // mise en place du listener sur l'adapteur
        monAdapteur.setOnItemClickListener(position -> {
            Challenge chall = challenges.get(position);
            Intent gotoChall = new Intent(this, onChallenge.class);
            gotoChall.putExtra("idchall", chall.getId());
            startActivity(gotoChall);
        });
    }

    /**
     * Trier la liste de challenge en fonction des date
     * @param list, liste de challenge
     */
    public void sortList(List<Challenge> list){
        list.sort((o1, o2) -> {
            String dateString1 = o1.getDate();
            String dateString2 = o2.getDate();
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
    }

}
