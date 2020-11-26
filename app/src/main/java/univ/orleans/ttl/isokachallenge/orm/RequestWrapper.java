package univ.orleans.ttl.isokachallenge.orm;

import android.graphics.Bitmap;

import android.util.Base64;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import java.io.ByteArrayOutputStream;

public class RequestWrapper {

    private static final String _imgurAPI = "https://api.imgur.com/3/image";
    private static final String _clientId = "e70fe5ed4d03b66";

    private static final String _serverAPI = "https://thlato.pythonanywhere.com/api/";
    private static final String _apiKey = "h1ZTSY38h4hAjWn5yFeBJ1MVw4VXjienv6ksBXV0Ek7hh3qo2A";

    public static final String REQUEST_LOG = "isoka_request_log";

    public void imgurUpload(Bitmap image, JSONObjectRequestListener callback) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String b64Image = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        AndroidNetworking.post(_imgurAPI)
                .addHeaders("Authorization", "Client-ID " + _clientId)
                .addBodyParameter("image", b64Image)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(callback);
    }

    public void login(String username, String password, JSONObjectRequestListener callback) {
        AndroidNetworking.post(_serverAPI + "login")
                .addHeaders("apiKey", _apiKey)
                .addBodyParameter("username", username)
                .addBodyParameter("password", password)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(callback);
    }
}

