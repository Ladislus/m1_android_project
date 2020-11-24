package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Profil extends AppCompatActivity {

    private TextView username;
    private EditText edit_username;
    private Button modifier;
    private Button valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        username = findViewById(R.id.username);
        edit_username= findViewById(R.id.inputNewUsername);
        modifier = findViewById(R.id.btn_modifier);
        valider = findViewById(R.id.btn_valider);
        username.setText(username.getText()+" Tom99");
    }

    public void modifier_username(View view) {
        username.setVisibility(View.GONE);
        modifier.setVisibility(View.GONE);

        edit_username.setVisibility(View.VISIBLE);
        valider.setVisibility(View.VISIBLE);
    }

    public void valider_username(View view) {
        username.setText(edit_username.getText());

        username.setVisibility(View.VISIBLE);
        modifier.setVisibility(View.VISIBLE);

        edit_username.setVisibility(View.GONE);
        valider.setVisibility(View.GONE);
    }
}