package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Challenge> challenges;
    private ChallengeAdapter monAdapteur;
    public static DB db;

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

        User user1 = new User(
                "WeeboMaster",
                LocalDateTime.now()
        );

        Challenge challenge1 = new Challenge(
                "Test",
                true,
                "test",
                LocalDateTime.now().minusDays(10),
                30,
                "test de challenge"
        );

        Challenge challenge2 = new Challenge(
                "Ishigami Senku",
                true,
                "https://static.wikia.nocookie.net/dr-stone/images/3/34/Senku_Ishigami_Anime_Infobox.png/revision/latest?cb=20190710063915",
                LocalDateTime.now().minusDays(15),
                30,
                "Dessiner Ishigamis Senku en 30 min"
        );

        Challenge challenge3 = new Challenge(
                "Ishigami Senku 2",
                true,
                "https://static.wikia.nocookie.net/dr-stone/images/3/34/Senku_Ishigami_Anime_Infobox.png/revision/latest?cb=20190710063915",
                LocalDateTime.now(),
                30,
                "Dessiner Ishigamis Senku en 30 min"
        );

        Drawing dessin1 = new Drawing(
                "https://upload.wikimedia.org/wikipedia/en/thumb/9/9e/Haikyu_season_1_DVD_cover.jpg/220px-Haikyu_season_1_DVD_cover.jpg",
                LocalDateTime.now().minusDays(100)
        );
        Drawing dessin2 = new Drawing(
                "https://recenthighlights.com/wp-content/uploads/2020/09/Solo-Leveling-Chapter-120.png",
                LocalDateTime.now().minusDays(10)
        );
        Drawing dessin3 = new Drawing(
                "https://pm1.narvii.com/6763/0529da37b02fd921fde5e90c01b5508dce487591v2_hq.jpg",
                LocalDateTime.now().minusDays(5)
        );
        Drawing dessin4 = new Drawing(
                "https://www.manga-news.com/public/images/series/The-Beginning-After-The-End-webtoon-visual.jpg",
                LocalDateTime.now().minusDays(2)
        );
        Drawing dessin5 = new Drawing(
                "https://images-na.ssl-images-amazon.com/images/I/71wvedvViFL._AC_SY679_.jpg",
                LocalDateTime.now()
        );
        Drawing dessin6 = new Drawing(
                "https://wallpapercave.com/wp/wp4443741.jpg",
                LocalDateTime.now().minusDays(1)
        );


        Participation p1 = new Participation(
                user1,
                dessin1,
                challenge1,
                true
        );
        Participation p2 = new Participation(
                user1,
                dessin2,
                challenge1,
                true
        );
        Participation p3 = new Participation(
                user1,
                dessin3,
                challenge1,
                true
        );
        Participation p4 = new Participation(
                user1,
                dessin4,
                challenge1,
                true
        );
        Participation p5 = new Participation(
                user1,
                dessin5,
                challenge1,
                true
        );
        Participation p6 = new Participation(
                user1,
                dessin6,
                challenge1,
                true
        );

        db.save(user1, "tom");
        db.save(challenge1);
        db.save(challenge2);
        db.save(challenge3);
        db.save(dessin1);
        db.save(dessin2);
        db.save(dessin3);
        db.save(dessin4);
        db.save(dessin5);
        db.save(dessin6);
        db.save(p1);
        db.save(p2);
        db.save(p3);
        db.save(p4);
        db.save(p5);
        db.save(p6);

        navigationView = findViewById(R.id.navigation_menu);
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



        challenges = db.getAllChallenges();

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


        monAdapteur = new ChallengeAdapter(challenges);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(monAdapteur);

        monAdapteur.setOnItemClickListener(position -> {
            Challenge chall = challenges.get(position);
            Intent gotoChall = new Intent(this, onChallenge.class);
            gotoChall.putExtra("idchall", chall.getId());
            startActivity(gotoChall);
        });
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
        challenges =MainActivity.db.getAllChallenges();

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
        challenges =MainActivity.db.getAllChallenges();

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

//    public void gotoOnChallenge(View view) {
//        Intent act = new Intent(this, onChallenge.class);
//        startActivity(act);
//    }

}
