package univ.orleans.ttl.isokachallenge;

import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;

public class Challenge {

    private String titre;
    private List<ImageDessin> imageDessinList;
    private int cpt;

    public Challenge(String titre, List<ImageDessin> imageDessinList) {
        this.titre = titre;
        this.imageDessinList = imageDessinList;
        this.cpt= 0;
    }

    public Challenge(String titre) {
        this.titre = titre;
        this.imageDessinList = new ArrayList<>();
    }

    public String getTitre() {
        return titre;
    }

    public List<ImageDessin> getImageDessinList() {
        return imageDessinList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ajout(ImageDessin imageDessin){
        if (this.cpt<5){
            this.imageDessinList.add(imageDessin);
            this.cpt++;
        }

        this.imageDessinList.sort((o1, o2) -> {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'à' hh'h'mm");
                Date d1 = format.parse(o1.dateSoumission);
                Date d2 = format.parse(o2.dateSoumission);
                if(d1.after(d2) ) {
                    Log.d("Sort","1");
                    return -1;
                } else if(d1.before(d2)) {
                    Log.d("Sort","-1");
                    return 1;
                } else {
                    Log.d("Sort","0");
                    return 0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("Sort","10");
            return 10;
        });


    }



}
