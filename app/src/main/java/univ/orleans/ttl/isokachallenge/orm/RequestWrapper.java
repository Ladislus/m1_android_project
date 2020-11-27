package univ.orleans.ttl.isokachallenge.orm;

import android.Manifest;
import android.graphics.Bitmap;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

public class RequestWrapper {

    private static final String _imgurAPI = "https://api.imgur.com/3/image";
    private static final String _clientId = "e70fe5ed4d03b66";

    private static final String _serverAPI = "https://thlato.pythonanywhere.com/api/";
    private static final String _apiKey = "h1ZTSY38h4hAjWn5yFeBJ1MVw4VXjienv6ksBXV0Ek7hh3qo2A";

    public static final String REQUEST_LOG = "isoka_request_log";

    public static enum ROUTES {
        USER("user/"),
        DRAWING("drawing/"),
        CHALLENGE("challenge/"),
        PARTICIPATIONN("participation/");

        private final String _path;

        ROUTES(String path) { this._path = path; }
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public void imgurUpload(@NonNull Bitmap image, @Nullable JSONObjectRequestListener callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST IMGUR");

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

    @RequiresPermission(Manifest.permission.INTERNET)
    public void login(@NonNull String username, @NonNull String password, @Nullable JSONObjectRequestListener callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST LOGIN");

        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", User.hash(password, DB.getInstance().getUser(username).getSalt()));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        AndroidNetworking.post(_serverAPI + "login")
                .addHeaders("apiKey", _apiKey)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(callback);
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public void updatePassword(@NonNull String username, @NonNull String oldPassword, @NonNull String newPassword, @Nullable JSONObjectRequestListener callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST UPDATE PASSWORD");

        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("old", User.hash(oldPassword, DB.getInstance().getUser(username).getSalt()));
            json.put("new", User.hash(newPassword, DB.getInstance().getUser(username).getSalt()));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        AndroidNetworking.post(_serverAPI + "password")
                .addHeaders("apiKey", _apiKey)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(callback);
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public void get(@Nullable Callback callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST GET");

        AndroidNetworking.get(_serverAPI + "update")
                .addHeaders("apiKey", _apiKey)
                .addHeaders("Content-Type", "application/json")
                .addQueryParameter("max_drawing", DB.getInstance().maxDrawing())
                .addQueryParameter("max_challenge", DB.getInstance().maxChallenge())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray users = response.getJSONArray("users");
                            for (int i = 0; i < users.length(); i++) {
                                User u = User.fromJson(users.getJSONObject(i));
                                DB.getInstance().save(u);
                            }
                            JSONArray drawings = response.getJSONArray("drawings");
                            for (int i = 0; i < drawings.length(); i++) {
                                Drawing d = Drawing.fromJson(drawings.getJSONObject(i));
                                DB.getInstance().save(d);
                            }
                            JSONArray challenges = response.getJSONArray("challenges");
                            for (int i = 0; i < challenges.length(); i++) {
                                Challenge c = Challenge.fromJson(challenges.getJSONObject(i));
                                DB.getInstance().save(c);
                            }
                            JSONArray participations = response.getJSONArray("participations");
                            for (int i = 0; i < participations.length(); i++) {
                                Participation p = Participation.fromJson(participations.getJSONObject(i));
                                DB.getInstance().save(p);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                        if (!Objects.isNull(callback)) callback.onResponse();
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (!Objects.isNull(callback)) callback.onError(anError);
                    }
                });
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public void save(@NonNull ROUTES route, @NonNull JSONObject object, @Nullable JSONObjectRequestListener callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST SAVE WITH ROUTE: " + route._path + "save");

        AndroidNetworking.post(_serverAPI + route._path + "save")
                .addHeaders("apiKey", _apiKey)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(object)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(callback);
    }
}

