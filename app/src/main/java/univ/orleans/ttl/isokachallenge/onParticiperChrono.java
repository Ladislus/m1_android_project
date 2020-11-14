package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

public class onParticiperChrono extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_participer_chrono);
        TextView labelChrono = findViewById(R.id.chronoLabel);
        labelChrono.setText("Temps total : 00:01:30");

        ProgressBar pg = findViewById(R.id.progressChrono);
        int tempsTotal = 10000; //Temps total de dessin : 1m30 secondes
        pg.setProgress(0);
        pg.setMax(tempsTotal);
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


                pg.setProgress((int) ((( tempsEcoule * tempsTotal) / tempsTotal) % tempsTotal));


            }

            public void onFinish() {
                labelChrono.setText("Fini ! Prennez le en photo !");
                pg.setProgress(0);
            }
        }.start();

        pg.setProgress(0);
    }
}