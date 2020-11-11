package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoOnChallenge(View view) {
        Intent act = new Intent(this, onChallenge.class);
        startActivity(act);
    }
}
