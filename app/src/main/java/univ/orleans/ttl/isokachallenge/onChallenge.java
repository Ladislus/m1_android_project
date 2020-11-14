package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class onChallenge extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_challenge);
        ImageView iv = findViewById(R.id.imageView);
        Picasso.get().load("https://histoire-image.org/sites/default/dor7_delacroix_001f.jpg").into(iv);
    }

    public void onParticiper(View view) {
        Intent intent = new Intent(this, onParticiperChrono.class);
        startActivity(intent);
    }
}