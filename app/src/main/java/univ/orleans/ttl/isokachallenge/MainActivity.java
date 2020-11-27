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

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

    private RecyclerView recyclerView;
    private List<Challenge> challenges;
    private ChallengeAdapter monAdapteur;
    public static DB db;
    private ProgressBar pg;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return actionBarDrawerToggle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.navigation_menu);

        AndroidNetworking.initialize(getApplicationContext());
        if(!DB.isInitialized()){
            DB.init(this);
        }

        SharedPreferences sharedPref = this.getSharedPreferences("session",Context.MODE_PRIVATE);
        if( !(sharedPref.getString("username","").equals(""))){
            //If user connecté
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, true);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, false);
        }else{
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, false);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, true);
        }

        setUpToolbar();

        db = DB.getInstance();

        new RequestWrapper().get(null);

        navigationView = findViewById(R.id.navigation_menu);
        pg = findViewById(R.id.progressBar);
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
                    this.onResume();
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
            drawerLayout.closeDrawers();
            return false;
        });

        recyclerView = findViewById(R.id.myRecyclerView);

        loadChallenge();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Main", "onStart: ");
        SharedPreferences sharedPref = this.getSharedPreferences("session",Context.MODE_PRIVATE);
        if( !(sharedPref.getString("username","").equals(""))){
            //If user connecté
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, true);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, false);
        }else{
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, false);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, true);
        }
        challenges =DB.getInstance().getAllChallenges();

        challenges.sort((o1, o2) -> {
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

        monAdapteur.setListChallengeAdapter(challenges);
        recyclerView.setAdapter(monAdapteur);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Main", "onResume: ");
        challenges =DB.getInstance().getAllChallenges();

        challenges.sort((o1, o2) -> {
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

        monAdapteur.setListChallengeAdapter(challenges);
        recyclerView.setAdapter(monAdapteur);
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

    public void loadChallenge(){
        challenges = db.getAllChallenges();
        new RequestWrapper().get(new Callback() {
            @Override
            public void onResponse() {
                challenges = db.getAllChallenges();

                sortList(challenges);

                pg = findViewById(R.id.progressBar);
                monAdapteur = new ChallengeAdapter(challenges,pg);

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(monAdapteur);

                monAdapteur.setOnItemClickListener(position -> {
                    Challenge chall = challenges.get(position);
                    Intent gotoChall = new Intent(getApplicationContext(), onChallenge.class);
                    gotoChall.putExtra("idchall", chall.getId());
                    startActivity(gotoChall);
                });
                pg.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError error) {
                pg.setVisibility(View.GONE);
            }
        });
        pg.setVisibility(View.VISIBLE);

        sortList(challenges);

        monAdapteur = new ChallengeAdapter(challenges,pg);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(monAdapteur);

        monAdapteur.setOnItemClickListener(position -> {
            Challenge chall = challenges.get(position);
            Intent gotoChall = new Intent(this, onChallenge.class);
            gotoChall.putExtra("idchall", chall.getId());
            startActivity(gotoChall);
        });
    }

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
