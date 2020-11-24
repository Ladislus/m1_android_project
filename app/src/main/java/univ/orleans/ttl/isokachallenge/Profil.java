package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Profil extends AppCompatActivity {

    private TextView username, password;
    private EditText edit_password;
    private Button modifier, valider;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        pref = getApplicationContext().getSharedPreferences("session", MODE_PRIVATE);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        edit_password = findViewById(R.id.inputNewPassword);
        modifier = findViewById(R.id.btn_modifier);
        valider = findViewById(R.id.btn_valider);
        username.setText(username.getText()+" "+pref.getString("username",null));
    }

    public void modifier_username(View view) {
        modifier.setVisibility(View.GONE);
        username.setVisibility(View.GONE);

        password.setVisibility(View.VISIBLE);
        edit_password.setVisibility(View.VISIBLE);
        valider.setVisibility(View.VISIBLE);
    }

    public void valider_username(View view) {
        User user = MainActivity.db.getUser(pref.getString("username",null));
        //user.
        //MainActivity.db.update();

        username.setVisibility(View.VISIBLE);
        modifier.setVisibility(View.VISIBLE);

        edit_password.setVisibility(View.GONE);
        valider.setVisibility(View.GONE);
    }
}