package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import univ.orleans.ttl.isokachallenge.orm.Callback;
import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.Tables;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

//TODO Comments
public class onChallenge extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private int idchall;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_challenge);
        ImageView iv = findViewById(R.id.imageView);
        AndroidNetworking.initialize(getApplicationContext());

        navigationView = findViewById(R.id.navigation_menu);
        this.idchall = getIntent().getIntExtra("idchall", 1);

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
        DB db = DB.getInstance();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
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
                    Intent challenge = new Intent(this, MainActivity.class);
                    startActivity(challenge);
                    finish();
                    break;
                case R.id.nav_profil:
                    Intent profil = new Intent(this, Profil.class);
                    startActivity(profil);
                    finish();
                    break;
                case R.id.nav_deconnexion:
                    Intent deco = new Intent(this, DeconnexionView.class);
                    startActivity(deco);
                    finish();
                    break;
                case R.id.nav_createChall:
                    Intent create = new Intent(this, CreationChallActivity.class);
                    startActivity(create);
                    finish();
                    break;
            }
            return false;
        });
        Challenge chall = db.getChallenge(this.idchall);
        if(chall != null){
            //Remplissage des champs avec les infos du challenge
            TextView title = findViewById(R.id.nomChall);
            title.setText(chall.getName());
            Picasso.get().load(chall.getTheme()).into(iv);

            TextView timer = findViewById(R.id.timer);
            timer.setText(chall.getTimer()+getString(R.string.minutes));

            TextView dateFin = findViewById(R.id.dateFin);
            dateFin.setText(getString(R.string.dateFin)+chall.getFormattedDate());

            TextView desc = findViewById(R.id.textView2);
            desc.setText(chall.getDesc());
        }else{
            TextView title = findViewById(R.id.nomChall);
            title.setText(R.string.error);
        }



        new RequestWrapper().get(new Callback() {
            @Override
            public void onResponse() {
                //Check si la date de fin du challenge est dépassée
                Challenge challenge = DB.getInstance().getChallenge(chall.getId());
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime dateTime = LocalDateTime.parse(challenge.getDate(), formatter);

                if( !(sharedPref.getString("username","").equals(""))) {
                    if (LocalDateTime.now().isBefore(dateTime)){
                        User user = DB.getInstance().getUser(sharedPref.getString("username", ""));

                        HashMap<String, Pair<String, String>> map = new HashMap<>();
                        map.put(Tables.PARTICIPATION_CHALLENGE_ID, new Pair(Tables.OPERATOR_EQ, String.valueOf(idchall)));
                        map.put(Tables.PARTICIPATION_USER_ID, new Pair(Tables.OPERATOR_EQ, user.getUsername()));

                        List<Participation> participations = DB.getInstance().getParticipations(map);
                        if (participations.isEmpty()) {
                            //Si l'utilisateur n'a pas encore participer, alors il peut
                            findViewById(R.id.btnParticipe).setEnabled(true);
                        }
                    }
                }else{
                    //Si l'utilisateur est déconnecté, alors il peut cliquer sur participer mais il sera redirigé
                    //Vers la connexion
                    findViewById(R.id.btnParticipe).setEnabled(true);
                }
            }

            @Override
            public void onError(ANError error) {
                //TODO
            }
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

    /**
     * Fonction du clique sur le bouton "Participer" d'un challenge
     * Lance l'activity onParticiperChrono
     * Nécessite d'être connecté
     */
    public void onParticiper(View view) {
        SharedPreferences sharedPref = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        if( !(sharedPref.getString("username","").equals(""))) {
            Intent intent = new Intent(this, onParticiperChrono.class);
            intent.putExtra("id_chall", this.idchall);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this, ConnexionView.class);
            startActivity(intent);
         }
    }

    /**
     * Fonction du clique sur le bouton "Parcourir" d'un challenge
     * Lance l'activity ParcoursParticipation
     */
    public void parcoursParticipation(View view) {
        Intent intent = new Intent(this, ParcoursParticipation.class);
        intent.putExtra("id_chall", this.idchall);
        startActivity(intent);
    }
}
