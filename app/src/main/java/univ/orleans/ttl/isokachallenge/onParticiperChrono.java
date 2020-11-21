package univ.orleans.ttl.isokachallenge;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class onParticiperChrono extends AppCompatActivity {

    private int id_chall;
    static final String API_KEY = "1321321321321";

    static final String BASE_URL = "https://thlato.pythonanywhere.com/api/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_participer_chrono);
        this.id_chall = getIntent().getIntExtra("id_chall", 0);
        callApi();
//        onParticiperChrono.FetchChallengeInfo fm = new onParticiperChrono.FetchChallengeInfo();
//        fm.execute(String.valueOf(this.id_chall));
    }

    private void callApi() {
        AndroidNetworking.get(BASE_URL+"challenge/get?id={id}")
                .addPathParameter("id", String.valueOf(this.id_chall))
                .addHeaders("apiKey", API_KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("prout", response.toString());
                        try {
                            ImageView iv = findViewById(R.id.imageViewChrono);
                Picasso.get().load(response.getString("theme")).into(iv);
                TextView labelChrono = findViewById(R.id.chronoLabel);

                long tempsTotal = response.getInt("timer")*60000;
                new CountDownTimer(tempsTotal, 1) {
                    long tempsEcoule = 0;
                    String labelSecondes = "00";
                    String labelMinutes = "00";
                    String labelHeures = "00";
                    long nbSecondes = 0;
                    long nbMinutes = 0;
                    long nbHeures = 0;
                    public void onTick(long millisUntilFinished) {

                        tempsEcoule = tempsTotal - millisUntilFinished;
                        nbSecondes = millisUntilFinished / 1000;
                        nbMinutes = nbSecondes / 60;
                        nbHeures = nbMinutes / 60;
                        if(nbHeures > 0){
                            nbMinutes = nbMinutes - (nbHeures * 60);
                        }
                        if(nbMinutes > 0){
                            nbSecondes = nbSecondes - (nbMinutes * 60);
                        }else{
                            if(nbHeures > 0){
                                nbSecondes = nbSecondes - (nbHeures * 3600);
                            }
                        }
                        if(nbSecondes < 10){
                            labelSecondes = "0"+String.valueOf(nbSecondes);
                        }else{
                            labelSecondes = String.valueOf(nbSecondes);
                        }
                        if(nbMinutes < 10){
                            labelMinutes = "0"+String.valueOf(nbMinutes);
                        }else{
                            labelMinutes = String.valueOf(nbMinutes);
                        }
                        if(nbHeures < 10){
                            labelHeures = "0"+String.valueOf(nbHeures);
                        }else{
                            labelHeures = String.valueOf(nbHeures);
                        }
                        labelChrono.setText(labelHeures+":"+labelMinutes+":"+labelSecondes);
                    }

                    public void onFinish() {
                        labelChrono.setText("Fini ! Prennez le en photo !");
                    }
                }.start();
                        }catch (JSONException e){
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("prout", error.getMessage());
                    }
                });
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void onPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Intent intent = new Intent(this, onConfirmationParticipation.class);
            intent.putExtra("bitmap", imageBitmap);
            startActivity(intent);
            finish();
        }
    }
//    private class FetchChallengeInfo extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//        @Override
//        protected String doInBackground(String... strings) {
//            Log.d("prout","je rentre dans doinbg avec strings : "+strings[0]+"");
//            try {
//                URL url = new URL(BASE_URL+"GetChallenge/"+strings[0]+"/123456");
//                Log.d("prout",url.toString());
//                URLConnection cnx = url.openConnection();
//                InputStream retour = cnx.getInputStream();
//                String text = null;
//                try (Scanner scanner = new Scanner(retour, StandardCharsets.UTF_8.name())) {
//                    text = scanner.useDelimiter("\\A").next();
//                }
//                Log.d("prout", "text : " +text);
//                return text;
//            } catch (MalformedURLException e) {
//                Log.d("prout", e.getMessage());
//            } catch (IOException e) {
//                Log.d("prout", e.getMessage());
//            }
//            return "";
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//                JSONObject obj = new JSONObject(s);
//                ImageView iv = findViewById(R.id.imageViewChrono);
//                Picasso.get().load(obj.getString("theme")).into(iv);
//                TextView labelChrono = findViewById(R.id.chronoLabel);
//
//                onParticiperChrono.tempsTotal = obj.getInt("timer")*60000;
//                new CountDownTimer(tempsTotal, 1) {
//                    long tempsEcoule = 0;
//                    String labelSecondes = "00";
//                    String labelMinutes = "00";
//                    String labelHeures = "00";
//                    long nbSecondes = 0;
//                    long nbMinutes = 0;
//                    long nbHeures = 0;
//                    public void onTick(long millisUntilFinished) {
//
//                        tempsEcoule = tempsTotal - millisUntilFinished;
//                        nbSecondes = millisUntilFinished / 1000;
//                        nbMinutes = nbSecondes / 60;
//                        nbHeures = nbMinutes / 60;
//                        if(nbHeures > 0){
//                            nbMinutes = nbMinutes - (nbHeures * 60);
//                        }
//                        if(nbMinutes > 0){
//                            nbSecondes = nbSecondes - (nbMinutes * 60);
//                        }else{
//                            if(nbHeures > 0){
//                                nbSecondes = nbSecondes - (nbHeures * 3600);
//                            }
//                        }
//                        if(nbSecondes < 10){
//                            labelSecondes = "0"+String.valueOf(nbSecondes);
//                        }else{
//                            labelSecondes = String.valueOf(nbSecondes);
//                        }
//                        if(nbMinutes < 10){
//                            labelMinutes = "0"+String.valueOf(nbMinutes);
//                        }else{
//                            labelMinutes = String.valueOf(nbMinutes);
//                        }
//                        if(nbHeures < 10){
//                            labelHeures = "0"+String.valueOf(nbHeures);
//                        }else{
//                            labelHeures = String.valueOf(nbHeures);
//                        }
//                        labelChrono.setText(labelHeures+":"+labelMinutes+":"+labelSecondes);
//                    }
//
//                    public void onFinish() {
//                        labelChrono.setText("Fini ! Prennez le en photo !");
//                    }
//                }.start();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

}