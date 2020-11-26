package univ.orleans.ttl.isokachallenge.orm;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import java.io.ByteArrayOutputStream;

public class RequestWrapper {

    private static final String imgurAPI = "https://api.imgur.com/3/image";
    private static final String clientId = "e70fe5ed4d03b66";
    private static final String serverAPI = "https://thlato.pythonanywhere.com/api/";

    private final SQLiteDatabase _db;

    public RequestWrapper(SQLiteDatabase db) { this._db = db; }

    public void imgurUpload(Bitmap image, JSONObjectRequestListener callback) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String b64Image = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        Log.d(Tables.DB_LOG + "1", b64Image);
        AndroidNetworking.post(imgurAPI)
                .addHeaders("Authorization", "Client-ID " + clientId)
                .addBodyParameter("image", b64Image)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(callback);
    }
}

