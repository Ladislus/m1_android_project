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
    private int id_chall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours_participation);

        this.id_chall = getIntent().getIntExtra("id_chall", 0);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewParticipation);
        ParticipationAdapteur monAdapteur = new ParticipationAdapteur(this, participations);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(monAdapteur);
    }
}