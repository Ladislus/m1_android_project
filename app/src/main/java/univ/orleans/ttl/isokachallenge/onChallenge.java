package univ.orleans.ttl.isokachallenge;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;

public class onChallenge extends AppCompatActivity {
    static final int ID_CHALL = 1; //Test avec l'id 1
    static final String BASE_URL = "https://thlato.pythonanywhere.com/api/";
    static final String API_KEY = "1321321321321";
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_challenge);
        ImageView iv = findViewById(R.id.imageView);
        AndroidNetworking.initialize(getApplicationContext());
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
            }
            return false;
        });
//        Picasso.get().load("https://histoire-image.org/sites/default/dor7_delacroix_001f.jpg").into(iv);

        Challenge chall = db.getChallenge(ID_CHALL);
        if(chall != null){
            TextView title = findViewById(R.id.nomChall);
            title.setText(chall.getName());
            Picasso.get().load(chall.getTheme()).into(iv);

            TextView timer = findViewById(R.id.timer);
            timer.setText(chall.getTimer()+" minutes");

            TextView dateFin = findViewById(R.id.dateFin);
            dateFin.setText("Termine le : "+chall.getDate());

            TextView desc = findViewById(R.id.textView2);
            desc.setText(chall.getDesc());
        }else{
            TextView title = findViewById(R.id.nomChall);
            title.setText(R.string.error);
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

    public void onParticiper(View view) {
        Intent intent = new Intent(this, onParticiperChrono.class);
        intent.putExtra("id_chall", ID_CHALL);
        startActivity(intent);
    }

}
