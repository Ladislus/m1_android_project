package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import univ.orleans.ttl.isokachallenge.orm.Tables;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;

import android.os.Bundle;
import android.util.Pair;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParcoursParticipation extends AppCompatActivity {

    private List<Participation> participations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours_participation);

        int test = 0;

        HashMap<String, Pair<String, String>> map = new HashMap<>();
        map.put(Tables.PARTICIPATION_CHALLENGE_ID, new Pair(Tables.OPERATOR_EQ, String.valueOf(test)));
        participations = new ArrayList<>(MainActivity.db.getParticipations(map));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewParticipation);
        ParticipationAdapteur monAdapteur = new ParticipationAdapteur(this, participations);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(monAdapteur);
    }
}