package univ.orleans.ttl.isokachallenge;

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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Challenge> challenges;
    private ChallengeAdapter monAdapteur;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        navigationView = findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case  R.id.nav_connexion:
                    Log.d("Bonjour","CAVA");
                    break;
            }
            return false;
        });

        recyclerView = findViewById(R.id.myRecyclerView);

        challenges = new ArrayList<>();

        List<ImageDessin> imageDessins = new ArrayList<>();

        ImageDessin imageDessinTourEiffel = new ImageDessin();
        imageDessinTourEiffel.imageUrl = "https://www.infinityandroid.com/images/france_eiffel_tower.jpg";
        imageDessinTourEiffel.auteur = "France";
        imageDessinTourEiffel.dateSoumission = "08/11/2020 à 18h12";
        imageDessinTourEiffel.startRating = 48;
        imageDessins.add(imageDessinTourEiffel);

        ImageDessin imageDessinMoutainView = new ImageDessin();
        imageDessinMoutainView.imageUrl = "https://www.infinityandroid.com/images/indonesia_mountain_view.jpg";
        imageDessinMoutainView.auteur = "Indonesia";
        imageDessinMoutainView.dateSoumission = "08/11/2020 à 18h12";
        imageDessinMoutainView.startRating = 45;
        imageDessins.add(imageDessinMoutainView);

        ImageDessin imageDessinTajMahal = new ImageDessin();
        imageDessinTajMahal.imageUrl = "https://www.infinityandroid.com/images/india_taj_mahal.jpg";
        imageDessinTajMahal.auteur = "India";
        imageDessinTajMahal.dateSoumission = "08/11/2020 à 18h12";
        imageDessinTajMahal.startRating = 43;
        imageDessins.add(imageDessinTajMahal);

        ImageDessin imageDessinLakeView = new ImageDessin();
        imageDessinLakeView.imageUrl = "https://www.infinityandroid.com/images/canada_lake_view.jpg";
        imageDessinLakeView.auteur = "Canada";
        imageDessinLakeView.dateSoumission = "21/11/2020 à 1h12";
        imageDessinLakeView.startRating = 10 ;
        imageDessins.add(imageDessinLakeView);

        ImageDessin imageDessinTest = new ImageDessin();
        imageDessinTest.imageUrl = "https://images-na.ssl-images-amazon.com/images/I/71wvedvViFL._AC_SY679_.jpg";
        imageDessinTest.auteur = "Tom99";
        imageDessinTest.dateSoumission = "21/11/2020 à 16h12";
        imageDessinTest.startRating = 42;
        imageDessins.add(imageDessinTest);

        ImageDessin imageDessinTestAnime = new ImageDessin();
        imageDessinTestAnime.imageUrl = "https://wallpapercave.com/wp/wp4443741.jpg";
        imageDessinTestAnime.auteur = "Paysage";
        imageDessinTestAnime.dateSoumission = "08/11/2020 à 18h12";
        imageDessinTestAnime.startRating = 45;
        imageDessins.add(imageDessinTestAnime);

        List<ImageDessin> test = new ArrayList<>();

        ImageDessin imageDessinAnime = new ImageDessin();
        imageDessinAnime.imageUrl = "https://static.wikia.nocookie.net/dr-stone/images/3/34/Senku_Ishigami_Anime_Infobox.png/revision/latest?cb=20190710063915";
        imageDessinAnime.auteur = "Tom99";
        imageDessinAnime.dateSoumission = "8/11/2020 à 18h10";
        imageDessinAnime.startRating = 450;
        test.add(imageDessinAnime);

        imageDessins.sort((o1, o2) -> {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'à' hh'h'mm");
                Date d1 = format.parse(o1.dateSoumission);
                Date d2 = format.parse(o2.dateSoumission);
                if(d1.after(d2) ) {
                    Log.d("Sort","1");
                    return -1;
                } else if(d1.before(d2)) {
                    Log.d("Sort","-1");
                    return 1;
                } else {
                    Log.d("Sort","0");
                    return 0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("Sort","10");
            return 10;
        });


        Challenge challenge1 = new Challenge("Test",imageDessins);
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


}