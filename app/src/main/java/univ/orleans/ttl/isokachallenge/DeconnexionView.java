package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class DeconnexionView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deconnexion_view);
        SharedPreferences sharedPref = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", "");
        editor.apply();
        finish();
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }

//    public void onGoBack(View view) {
//        /**
//         * Fonction appelé lors du clique sur le bouton "Retour à l'acceuil" dans
//         * l'activity DeconnexionView
//         */
//        finish();
//        Intent home = new Intent(this, MainActivity.class);
//        startActivity(home);
//    }
}