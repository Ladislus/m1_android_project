package univ.orleans.ttl.isokachallenge;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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

public class onChallenge extends AppCompatActivity {
    static final int ID_CHALL = 1; //Test avec l'id 1
    static final String BASE_URL = "https://thlato.pythonanywhere.com/api/";
    static final String API_KEY = "1321321321321";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_challenge);
        ImageView iv = findViewById(R.id.imageView);
        AndroidNetworking.initialize(getApplicationContext());

        callApi();



//        Picasso.get().load("https://histoire-image.org/sites/default/dor7_delacroix_001f.jpg").into(iv);
    }

    private void callApi() {
        AndroidNetworking.get(BASE_URL+"challenge/get?id={id}")
                .addPathParameter("id", String.valueOf(ID_CHALL))
                .addHeaders("apiKey", API_KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("prout", response.toString());
                        try {
                            TextView title = findViewById(R.id.nomChall);
                            title.setText(response.getString("name"));
                            ImageView iv = findViewById(R.id.imageView);
                            Picasso.get().load(response.getString("theme")).into(iv);

                            TextView timer = findViewById(R.id.timer);
                            timer.setText(response.getString("timer")+" minutes");

                            TextView dateFin = findViewById(R.id.dateFin);
                            dateFin.setText("Termine le : "+response.getString("date"));

                            TextView desc = findViewById(R.id.textView2);
                            desc.setText(response.getString("desc"));
                        }catch (JSONException e){
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("prout", error.getMessage());
                    }
                });
    }

    public void onParticiper(View view) {
        Intent intent = new Intent(this, onParticiperChrono.class);
        intent.putExtra("id_chall", ID_CHALL);
        startActivity(intent);
    }

}
