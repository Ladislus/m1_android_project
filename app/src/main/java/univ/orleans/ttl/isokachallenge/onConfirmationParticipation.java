package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class onConfirmationParticipation extends AppCompatActivity {

    private ImageView imageViewConfirmation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_confirmation_participation);
        Intent intent = getIntent();
        Bitmap image = (Bitmap) intent.getParcelableExtra("bitmap");
        imageViewConfirmation = findViewById(R.id.imageViewConfirmation);
//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        String imageS = sharedPref.getString("bitmap","");
//        if(!(imageS.equals(""))){
//            image = StringToBitMap(imageS);
//            Log.d("imageConf","il y a une image dans le sharedpref");
//        }
        imageViewConfirmation.setImageBitmap(image);
   }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void onReprendrePhoto(View view) {
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
            imageViewConfirmation.setImageBitmap(imageBitmap);
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void onConfirmer(View view) {
        //Upload dans imgur
        finish();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        BitmapDrawable bmD = (BitmapDrawable) imageViewConfirmation.getDrawable();
//        editor.putString("bitmap",BitMapToString(bmD.getBitmap()));
//        editor.apply();
//    }


/*    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String imageS = sharedPref.getString("bitmap","");
        if(!(imageS.equals(""))){
            Bitmap image = StringToBitMap(imageS);
            imageViewConfirmation.setImageBitmap(image);
        }

    }*/
}