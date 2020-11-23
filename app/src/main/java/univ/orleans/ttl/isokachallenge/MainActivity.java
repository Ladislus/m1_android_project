package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Challenge> challenges;
    private ChallengeAdapter monAdapteur;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        setUpToolbar();
        DB db = new DB(this);
        navigationView = findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case  R.id.nav_connexion:
                    Intent intent = new Intent(this, ConnexionView.class);
                    startActivity(intent);
                    break;

                case R.id.nav_challengeTest:
                    Intent act = new Intent(this, onChallenge.class);
                    startActivity(act);
                    break;
                case R.id.nav_profil:
                    Intent profil = new Intent(this, Profil.class);
                    startActivity(profil);
                    break;
            }
            return false;
        });

        recyclerView = findViewById(R.id.myRecyclerView);

        challenges = new ArrayList<>();

        List<ImageDessin> imageDessins = new ArrayList<>();

        Challenge challenge1 = new Challenge("Test");

        ImageDessin imageDessinTourEiffel = new ImageDessin();
        imageDessinTourEiffel.imageUrl = "https://www.infinityandroid.com/images/france_eiffel_tower.jpg";
        imageDessinTourEiffel.auteur = "France";
        imageDessinTourEiffel.dateSoumission = "08/11/2020 à 18h12";
        imageDessinTourEiffel.startRating = 48;
        challenge1.ajout(imageDessinTourEiffel);

        ImageDessin imageDessinMoutainView = new ImageDessin();
        imageDessinMoutainView.imageUrl = "https://www.infinityandroid.com/images/indonesia_mountain_view.jpg";
        imageDessinMoutainView.auteur = "Indonesia";
        imageDessinMoutainView.dateSoumission = "08/11/2020 à 18h12";
        imageDessinMoutainView.startRating = 45;
        challenge1.ajout(imageDessinMoutainView);

        ImageDessin imageDessinTajMahal = new ImageDessin();
        imageDessinTajMahal.imageUrl = "https://www.infinityandroid.com/images/india_taj_mahal.jpg";
        imageDessinTajMahal.auteur = "India";
        imageDessinTajMahal.dateSoumission = "08/11/2020 à 18h12";
        imageDessinTajMahal.startRating = 43;
        challenge1.ajout(imageDessinTajMahal);

        ImageDessin imageDessinLakeView = new ImageDessin();
        imageDessinLakeView.imageUrl = "https://www.infinityandroid.com/images/canada_lake_view.jpg";
        imageDessinLakeView.auteur = "Canada";
        imageDessinLakeView.dateSoumission = "21/11/2020 à 1h12";
        imageDessinLakeView.startRating = 10 ;
        challenge1.ajout(imageDessinLakeView);

        ImageDessin imageDessinTest = new ImageDessin();
        imageDessinTest.imageUrl = "https://images-na.ssl-images-amazon.com/images/I/71wvedvViFL._AC_SY679_.jpg";
        imageDessinTest.auteur = "Tom99";
        imageDessinTest.dateSoumission = "21/11/2020 à 16h12";
        imageDessinTest.startRating = 42;
        challenge1.ajout(imageDessinTest);

        ImageDessin imageDessinTestAnime = new ImageDessin();
        imageDessinTestAnime.imageUrl = "https://wallpapercave.com/wp/wp4443741.jpg";
        imageDessinTestAnime.auteur = "Paysage";
        imageDessinTestAnime.dateSoumission = "08/11/2020 à 18h12";
        imageDessinTestAnime.startRating = 45;
        challenge1.ajout(imageDessinTestAnime);

        List<ImageDessin> test = new ArrayList<>();

        ImageDessin imageDessinAnime = new ImageDessin();
        imageDessinAnime.imageUrl = "https://static.wikia.nocookie.net/dr-stone/images/3/34/Senku_Ishigami_Anime_Infobox.png/revision/latest?cb=20190710063915";
        imageDessinAnime.auteur = "Tom99";
        imageDessinAnime.dateSoumission = "8/11/2020 à 18h10";
        imageDessinAnime.startRating = 450;
        test.add(imageDessinAnime);

        Challenge challenge2 = new Challenge("Ishigami Senku",test);
        challenges.add(challenge1);
        challenges.add(challenge2);

        monAdapteur = new ChallengeAdapter(challenges);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(monAdapteur);

        monAdapteur.setOnItemClickListener(position -> {
            Challenge user = challenges.get(position);
            Toast.makeText(getBaseContext(),
                    "RecyclerView : Challenge Title : "+user.getTitre(),
                    Toast.LENGTH_SHORT).show();
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

//    public void gotoOnChallenge(View view) {
//        Intent act = new Intent(this, onChallenge.class);
//        startActivity(act);
//    }

}
