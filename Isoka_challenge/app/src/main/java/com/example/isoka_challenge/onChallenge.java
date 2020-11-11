package com.example.isoka_challenge;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
}