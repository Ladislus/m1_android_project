package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class onParticiperChrono extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_participer_chrono);
        TextView labelChrono = findViewById(R.id.chronoLabel);
        labelChrono.setText("Temps total : 00:01:30");
        this.iv = findViewById(R.id.imageViewChrono);
        Picasso.get().load("https://histoire-image.org/sites/default/dor7_delacroix_001f.jpg").into(iv);
//        ProgressBar pg = findViewById(R.id.progressChrono);
        int tempsTotal = 10000; //Temps total de dessin : 1m30 secondes
//        pg.setProgress(0);
//        pg.setMax(tempsTotal);
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


//                pg.setProgress((int) ((( tempsEcoule * tempsTotal) / tempsTotal) % tempsTotal));


            }

            public void onFinish() {
                labelChrono.setText("Fini ! Prennez le en photo !");
//                pg.setProgress(0);
            }
        }.start();

//        pg.setProgress(0);
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
        }
    }

}