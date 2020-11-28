package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

public class DeconnexionView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deconnexion_view);

        // Lors de la déconnexion ...
        SharedPreferences.Editor editor = this.getSharedPreferences("session", Context.MODE_PRIVATE).edit();
        // ... Clear l'utilisateur actuellement connecté
        editor.putString("username", "");
        editor.apply();

        // Puis retour à la MainActivity
        finish();
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }
}