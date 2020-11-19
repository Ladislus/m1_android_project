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

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class onChallenge extends AppCompatActivity {
    static final int ID_CHALL = 1; //Test avec l'id 1
    static final String BASE_URL = "https://thlato.pythonanywhere.com/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_challenge);
        ImageView iv = findViewById(R.id.imageView);
        FetchChallengeInfo fm = new FetchChallengeInfo();
        fm.execute(String.valueOf(ID_CHALL));
//        Picasso.get().load("https://histoire-image.org/sites/default/dor7_delacroix_001f.jpg").into(iv);
    }

    public void onParticiper(View view) {
        Intent intent = new Intent(this, onParticiperChrono.class);
        intent.putExtra("id_chall", ID_CHALL);
        startActivity(intent);
    }
    private class FetchChallengeInfo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            Log.d("prout","je rentre dans doinbg avec strings : "+strings[0]+"");
            try {
                URL url = new URL(BASE_URL+"GetChallenge/"+strings[0]+"/123456");
                Log.d("prout",url.toString());
                URLConnection cnx = url.openConnection();
                InputStream retour = cnx.getInputStream();
                String text = null;
                try (Scanner scanner = new Scanner(retour, StandardCharsets.UTF_8.name())) {
                    text = scanner.useDelimiter("\\A").next();
                }
                Log.d("prout", "text : " +text);
                return text;
            } catch (MalformedURLException e) {
                Log.d("prout", e.getMessage());
            } catch (IOException e) {
                Log.d("prout", e.getMessage());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject obj = new JSONObject(s);
                TextView title = findViewById(R.id.nomChall);
                title.setText(obj.getString("nomImgTheme"));
                ImageView iv = findViewById(R.id.imageView);
                Picasso.get().load(obj.getString("theme")).into(iv);

                TextView timer = findViewById(R.id.timer);
                timer.setText(obj.getString("timer")+" minutes");

                TextView dateFin = findViewById(R.id.dateFin);
                dateFin.setText("Termine le : "+obj.getString("duree"));

                TextView desc = findViewById(R.id.textView2);
                desc.setText(obj.getString("description"));
//                TextView tx = findViewById(R.id.textView);
//                tx.setText(obj.getString("name")+" : "+obj.getJSONObject("main").getString("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
