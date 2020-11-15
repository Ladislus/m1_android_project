package univ.orleans.ttl.isokachallenge;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Challenge> challenges;
    private ChallengeAdapter monAdapteur;
    private static Context context;

    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.myRecyclerView);

        challenges = new ArrayList<>();

        List<ImageDessin> imageDessins = new ArrayList<>();

        ImageDessin imageDessinTourEiffel = new ImageDessin();
        imageDessinTourEiffel.imageUrl = "https://www.infinityandroid.com/images/france_eiffel_tower.jpg";
        imageDessinTourEiffel.auteur = "France";
        imageDessinTourEiffel.dateSoumission = "Eiffel Tower";
        imageDessinTourEiffel.startRating = 48;
        imageDessins.add(imageDessinTourEiffel);

        ImageDessin imageDessinMoutainView = new ImageDessin();
        imageDessinMoutainView.imageUrl = "https://www.infinityandroid.com/images/indonesia_mountain_view.jpg";
        imageDessinMoutainView.auteur = "Indonesia";
        imageDessinMoutainView.dateSoumission = "Mountain View";
        imageDessinMoutainView.startRating = 45;
        imageDessins.add(imageDessinMoutainView);

        ImageDessin imageDessinTajMahal = new ImageDessin();
        imageDessinTajMahal.imageUrl = "https://www.infinityandroid.com/images/india_taj_mahal.jpg";
        imageDessinTajMahal.auteur = "India";
        imageDessinTajMahal.dateSoumission = "Taj Mahal";
        imageDessinTajMahal.startRating = 43;
        imageDessins.add(imageDessinTajMahal);

        ImageDessin imageDessinLakeView = new ImageDessin();
        imageDessinLakeView.imageUrl = "https://www.infinityandroid.com/images/canada_lake_view.jpg";
        imageDessinLakeView.auteur = "Canada";
        imageDessinLakeView.dateSoumission = "Lake View";
        imageDessinLakeView.startRating = 10 ;
        imageDessins.add(imageDessinLakeView);

        ImageDessin imageDessinTest = new ImageDessin();
        imageDessinTest.imageUrl = "https://images-na.ssl-images-amazon.com/images/I/71wvedvViFL._AC_SY679_.jpg";
        imageDessinTest.auteur = "Tom99";
        imageDessinTest.dateSoumission = "8/11/2020 à 18h12";
        imageDessinTest.startRating = 42;
        imageDessins.add(imageDessinTest);

        ImageDessin imageDessinTestAnime = new ImageDessin();
        imageDessinTestAnime.imageUrl = "https://wallpapercave.com/wp/wp4443741.jpg";
        imageDessinTestAnime.auteur = "Paysage";
        imageDessinTestAnime.dateSoumission = "Landscape";
        imageDessinTestAnime.startRating = 45;
        imageDessins.add(imageDessinTestAnime);

        List<ImageDessin> test = new ArrayList<>();

        ImageDessin imageDessinAnime = new ImageDessin();
        imageDessinAnime.imageUrl = "https://static.wikia.nocookie.net/dr-stone/images/3/34/Senku_Ishigami_Anime_Infobox.png/revision/latest?cb=20190710063915";
        imageDessinAnime.auteur = "Tom99";
        imageDessinAnime.dateSoumission = "8/11/2020 à 18h10";
        imageDessinAnime.startRating = 450;
        test.add(imageDessinAnime);
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
}