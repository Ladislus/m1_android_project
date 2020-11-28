package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import univ.orleans.ttl.isokachallenge.orm.Callback;
import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.Tables;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ParcoursParticipation extends AppCompatActivity {

    private int id_chall;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours_participation);

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

        this.id_chall = getIntent().getIntExtra("id_chall", 0);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewParticipation);
        TextView pasParticipation = (TextView) findViewById(R.id.participation0);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        final ParticipationAdapteur[] monAdapteur = {null};
        AppCompatActivity context = this;
        final String[] txt = {""};

        final ArrayList<Participation>[] participations = new ArrayList[]{null};
        new RequestWrapper().get(new Callback() {
            @Override
            public void onResponse() {
                HashMap<String, Pair<String, String>> map = new HashMap<>();
                map.put(Tables.PARTICIPATION_CHALLENGE_ID, new Pair(Tables.OPERATOR_EQ, String.valueOf(id_chall)));
                participations[0] = new ArrayList<>(DB.getInstance().getParticipations(map));

                participations[0].sort((o1, o2) -> {
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

                if (!Objects.isNull(participations[0])) {
                    if (participations[0].size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        pasParticipation.setVisibility(View.GONE);
                        monAdapteur[0] = new ParticipationAdapteur(context, participations[0]);

                        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                        recyclerView.setAdapter(monAdapteur[0]);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        pasParticipation.setVisibility(View.VISIBLE);

                        txt[0] = getResources().getString(R.string.no_participation_challenge);
                        pasParticipation.setText(txt[0]);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.VISIBLE);


        if (!Objects.isNull(participations[0])) {
            if (participations[0].size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                pasParticipation.setVisibility(View.GONE);
                monAdapteur[0] = new ParticipationAdapteur(this, participations[0]);

                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.setAdapter(monAdapteur[0]);
            } else {
                recyclerView.setVisibility(View.GONE);
                pasParticipation.setVisibility(View.VISIBLE);

                txt[0] = getResources().getString(R.string.no_participation_challenge);
                pasParticipation.setText(txt[0]);
            }
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
}