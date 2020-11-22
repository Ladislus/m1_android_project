package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnexionView extends AppCompatActivity {

    private EditText mdp, login;
    private CheckBox checkLogin;
    private boolean remember;

    static final String BASE_URL = "https://thlato.pythonanywhere.com/api/";
    static final String API_KEY = "1321321321321";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion_view);
        this.login = findViewById(R.id.inputLogin);
        this.mdp = findViewById(R.id.inputMDP);
        this.checkLogin = findViewById(R.id.checkLogin);
        this.remember = false;
    }

    public void onConnexion(View view) {
        AndroidNetworking.get(BASE_URL+"user/get?username={pseudo}")
                .addPathParameter("pseudo", String.valueOf(this.login.getText()))
                .addHeaders("apiKey", API_KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("login", "Reponse : "+response.toString()+"\n");

                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("login", error.toString());
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(this.checkLogin.isChecked()){
            editor.putString("login", String.valueOf(this.login.getText()));
            editor.putString("mdp", String.valueOf(this.mdp.getText()));
        }else{
            editor.putString("login", "");
            editor.putString("mdp", "");
        }
        editor.putBoolean("isChecked", this.checkLogin.isChecked());
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        this.login.setText(sharedPref.getString("login",""));
        this.mdp.setText(sharedPref.getString("mdp",""));
        this.remember = sharedPref.getBoolean("isChecked",false);
        this.checkLogin.setChecked(this.remember);

    }

    public void onChecked(View view) {
        this.remember = this.checkLogin.isChecked();
    }
}