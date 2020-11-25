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
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        HashMap<String, Pair<String, String>> map = new HashMap<>();
        map.put(Tables.PARTICIPATION_CHALLENGE_ID, new Pair(Tables.OPERATOR_EQ, String.valueOf(this.id_chall)));
        ArrayList<Participation> participations = new ArrayList<>(MainActivity.db.getParticipations(map));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewParticipation);
        TextView pasParticipation = findViewById(R.id.participation0);
        if (participations.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            pasParticipation.setVisibility(View.GONE);
            ParticipationAdapteur monAdapteur = new ParticipationAdapteur(this, participations);

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(monAdapteur);
        }else{
            recyclerView.setVisibility(View.GONE);
            pasParticipation.setVisibility(View.VISIBLE);

            String txt = getResources().getString(R.string.no_participation_challenge);
            pasParticipation.setText(txt);
        }
    }
}