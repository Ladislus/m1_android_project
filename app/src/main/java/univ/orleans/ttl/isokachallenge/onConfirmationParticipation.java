package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

public class onConfirmationParticipation extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private ImageView imageViewConfirmation;
    private DB db;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_confirmation_participation);
        navigationView = findViewById(R.id.navigation_menu);
        SharedPreferences sharedPref = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        if( !(sharedPref.getString("username","").equals(""))){
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, true);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, false);
        }else{
            navigationView.getMenu().setGroupVisible(R.id.groupeConnecter, false);
            navigationView.getMenu().setGroupVisible(R.id.groupeDeco, true);
        }
        setUpToolbar();
        this.db = DB.getInstance();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case  R.id.nav_connexion:
                    Intent intent = new Intent(this, ConnexionView.class);
                    startActivity(intent);
                    finish();
                    break;
                case  R.id.nav_inscription:
                    Intent inscription = new Intent(this, InscriptionActivity.class);
                    startActivity(inscription);
                    finish();
                    break;
                case R.id.nav_challenge:
                    Intent challenge = new Intent(this, MainActivity.class);
                    startActivity(challenge);
                    finish();
                    break;
                case R.id.nav_profil:
                    Intent profil = new Intent(this, Profil.class);
                    startActivity(profil);
                    finish();
                    break;
                case R.id.nav_deconnexion:
                    Intent deco = new Intent(this, DeconnexionView.class);
                    startActivity(deco);
                    finish();
                    break;
                case R.id.nav_createChall:
                    Intent create = new Intent(this, CreationChallActivity.class);
                    startActivity(create);
                    finish();
                    break;
            }
            return false;
        });

        Intent intent = getIntent();
        this.image = intent.getParcelableExtra("bitmap");
        imageViewConfirmation = findViewById(R.id.imageViewConfirmation);
        imageViewConfirmation.setImageBitmap(image);

   }
    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void onReprendrePhoto(View view) {
        /**
         * Permet a l'utilisateur de changer la photo qu'il va envoyer.
         */
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * Récupère la nouvelle photo à envoyé et affiche l'aperçut dans l'imageView
         */
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            getIntent().putExtra("bitmap",(Bitmap) extras.get("data"));
            this.image = (Bitmap) extras.get("data");
            imageViewConfirmation.setImageBitmap(this.image);
        }
    }

    public void onConfirmer(View view) {
        /**
         * Permet d'uploader dans la BD distante et local la participation de l'utilisateur courant.
         * Nécessite d'être connecté.
         * La fonction publie d'abord l'image sur IMGUR, pour stocker le lien dans les BD (moins lourd que les images).
         * Ensuite, on publie sur les BD l'objet Drawing (créer avec le lien de l'image, renvoyé par l'API IMGUR).
         * Enfin, on publie sur les BD l'objet Participation (créer avec Drawing, Challenge et User).
         * Une fois ces trois requêtes terminées, l'activity se finish() et on revient logiquement la a
         * description du challenge (activity onChallenge).
         */
        SharedPreferences sharedPref = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        if( !(sharedPref.getString("username","").equals(""))){
            sharedPref = this.getSharedPreferences("session", Context.MODE_PRIVATE);
            User userCourant = this.db.getUser(sharedPref.getString("username",""));
            ProgressBar pg = findViewById(R.id.progressBar);
            JSONObjectRequestListener callback = new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Drawing dessin = null;
                    try {
                        dessin = new Drawing(response.getJSONObject("data").getString("link"), LocalDateTime.now());
                        Challenge chall = db.getChallenge(getIntent().getIntExtra("idchall",0));
                        new RequestWrapper().save(RequestWrapper.ROUTES.DRAWING, dessin.toJson(), new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Drawing dessin2 = Drawing.fromJson(response);
                                    db.save(dessin2);
                                    Participation participation = new Participation(userCourant, dessin2, chall, false);
                                    new RequestWrapper().save(RequestWrapper.ROUTES.PARTICIPATION, participation.toJson(), new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                db.save(Participation.fromJson(response));
                                                finish();
                                                pg.setVisibility(View.INVISIBLE);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onError(ANError anError) {

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(ANError anError) {
                    pg.setVisibility(View.INVISIBLE);
                    Log.d("onUploadIMG", String.valueOf(anError.getErrorCode()));
                    Log.d("onUploadIMG", anError.getErrorDetail());
                }
            };
            pg.setVisibility(View.VISIBLE);
            new RequestWrapper().imgurUpload(this.image, callback);
        }else{
            Intent intent = new Intent(this, ConnexionView.class);
            finish();
            startActivity(intent);
        }
    }
}