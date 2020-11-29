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

/**
 * Class permettant la réalisation des différentes requêtes vers l'API Imgur,
 * ainsi que la base de données distantes
 */
public class RequestWrapper {

    /**
     * Adressse et clef d'API d'Imgur
     */
    private static final String _imgurAPI = "https://api.imgur.com/3/image";
    private static final String _clientId = "e70fe5ed4d03b66";

    /**
     * Adresse et clef d'API de la base de donnée distante
     * (Serveur Flask hébergé sur PythonAnywhere.com)
     */
    private static final String _serverAPI = "https://thlato.pythonanywhere.com/api/";
    private static final String _apiKey = "h1ZTSY38h4hAjWn5yFeBJ1MVw4VXjienv6ksBXV0Ek7hh3qo2A";

    public static final String REQUEST_LOG = "isoka_request_log";

    /**
     * Enum des diffentes routes disponibles vers la base de données distante
     */
    public enum ROUTES {
        USER("user/"),
        DRAWING("drawing/"),
        CHALLENGE("challenge/"),
        PARTICIPATION("participation/");

        private final String _path;

        ROUTES(String path) { this._path = path; }
    }

    /**
     * Fonction permettant l'envoi d'une image à l'API Imgur,
     * afin de récupérer le lien vers cette image à stocker dans
     * la base de données
     * @param image L'image prise par l'appareil du téléphone à la fin du timer d'un challenge
     * @param callback Interface contenant les fonctions onResponse et onError à implémenter
     *                 qui seront éxecutées lors de résolution de la requête
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void imgurUpload(@NonNull Bitmap image, @Nullable JSONObjectRequestListener callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST IMGUR");

        // Transformation du bitmap de l'image en
        // String base64 pour transfert
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String b64Image = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        // Requêtes POST vers l'API Imgur
        AndroidNetworking.post(_imgurAPI)
                // Ajout de l'APIKEY
                .addHeaders("Authorization", "Client-ID " + _clientId)
                // Aout de l'image
                .addBodyParameter("image", b64Image)
                .setPriority(Priority.HIGH)
                .build()
                // Définition des fonctions à éxecuter lors de
                // la résolution de la requêtes
                .getAsJSONObject(callback);
    }

    /**
     * Fonction permettant l'envoi d'une requête d'obtention d'un utilisateur dans la base de données distante,
     * utilisé lors de la connexion afin de vérifier que l'utilisateur existe, et d'obtenir
     * le salt permettant de hasher son mot de passe dans la requête de login
     * @param username Le nom de l'utilisateur à récupérer
     * @param callback Interface contenant les fonctions onResponse et onError à implémenter
     *                 qui seront éxecutées lors de résolution de la requête
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void getUser(@NonNull String username, @Nullable JSONObjectRequestListener callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST GET USER " + username);

        // Requêtes GET vers la base de données distante (Route: api/user/get)
        AndroidNetworking.get(_serverAPI + ROUTES.USER._path + "get")
                // Ajout de l'APIKEY
                .addHeaders("apiKey", _apiKey)
                // Ajout du paramètre username
                .addQueryParameter("username", username)
                .setPriority(Priority.HIGH)
                .build()
                // Définition des fonctions à éxecuter lors de
                // la résolution de la requêtes
                .getAsJSONObject(callback);
    }

    /**
     * Fonction permettant l'envoi d'une requête de connexion d'un utilisateur dans la base de données distante
     * ATTENTION: Cette requête doit toujours être effectué après une requêtes getUser() (dans sa fonction onResponse)
     * Si l'utilisateur n'existe pas l'instruction "User.hash(password, DB.getInstance().getUser(username).getSalt())"
     * provoquera une NullPointerException, et plantera l'application
     * @param username Le nom de l'utilisateur qui tente de se connecter
     * @param password Le mot de passe de l'utilisateur qui tente de se connecter
     * @param callback Interface contenant les fonctions onResponse et onError à implémenter
     *                 qui seront éxecutées lors de résolution de la requête
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void login(@NonNull String username, @NonNull String password, @Nullable JSONObjectRequestListener callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST LOGIN");

        // Création du corps de la requête (JSON)
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            // Hashage du mot de passe de l'utilisateur grâce au hash reçu de la base de données
            json.put("password", User.hash(password, DB.getInstance().getUser(username).getSalt()));
        } catch (JSONException e) {
            // Si la création du body (parsing JSON) échoue, annule la requête
            e.printStackTrace();
            return;
        }

        // Requêtes POST vers la base de données distante (Route: api/login)
        AndroidNetworking.post(_serverAPI + "login")
                // Ajout de l'APIKEY
                .addHeaders("apiKey", _apiKey)
                // Définition du contenu comme étant au format JSON
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .build()
                // Définition des fonctions à éxecuter lors de
                // la résolution de la requêtes
                .getAsJSONObject(callback);
    }

    /**
     * Fonction permettant l'envoi d'une requête de mise à jour de mot de passe dans la base de données distante
     * ATTENTION: Cette requête doit toujours être effectué après une requêtes getUser() (dans sa fonction onResponse)
     * Si l'utilisateur n'existe pas l'instruction "User.hash(password, DB.getInstance().getUser(username).getSalt())"
     * provoquera une NullPointerException, et plantera l'application
     * @param username Le nom de l'utilisateur qui tente de se connecter
     * @param oldPassword L'ancien mot de passe de l'utilisateur
     * @param newPassword Le nouveau mot de passe de l'utilisateur
     * @param callback Interface contenant les fonctions onResponse et onError à implémenter
     *                 qui seront éxecutées lors de résolution de la requête
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void updatePassword(@NonNull String username, @NonNull String oldPassword, @NonNull String newPassword, @Nullable JSONObjectRequestListener callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST UPDATE PASSWORD");

        // Création du corps de la requête (JSON)
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            // Hashage de l'ancien mot de passe de l'utilisateur grâce au hash reçu de la base de données
            // afin de vérifier son identitié
            json.put("old", User.hash(oldPassword, DB.getInstance().getUser(username).getSalt()));
            // Hashage du nouveau mot de passe de l'utilisateur grâce au hash reçu de la base de données
            // afin de ne pas transmettre en clair les informations de l'utilisateur
            json.put("new", User.hash(newPassword, DB.getInstance().getUser(username).getSalt()));
        } catch (JSONException e) {
            // Si la création du body (parsing JSON) échoue, annule la requête
            e.printStackTrace();
            return;
        }

        // Requêtes POST vers la base de données distante (Route: api/password)
        AndroidNetworking.post(_serverAPI + "password")
                // Ajout de l'APIKEY
                .addHeaders("apiKey", _apiKey)
                // Définition du contenu comme étant au format JSON
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .build()
                // Définition des fonctions à éxecuter lors de
                // la résolution de la requêtes
                .getAsJSONObject(callback);
    }

    /**
     * Fonction permettant d'obtenir les informations de la base de données distant afin de mettre à jour
     * la base de données locale. Toutes les manipulations (ou presque) de la base de données locale devraient être contenu
     * dans le callback d'une requête get()
     * @param callback Interface contenant les fonctions onResponse et onError à implémenter
     *                  qui seront éxecutées lors de résolution de la requête
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void get(@Nullable Callback callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST GET");

        /*
         * Requêtes GET vers la base de données distante (Route: api/update)
         * FIXME:
         * lors de cette requête, tous les utilisateurs de la base de données distante sont renvoyés,
         * si beaucoup d'utilisateurs sont présent, la requêtes peut devenir très volumineuse, et donc très lente.
         * Afin de palier à cela, il faudrait également envoyer la date d'inscription du dernière utilisateur inscrit
         * en base de données locale, afin de ne renvoyer que les nouveaux utilisateurs.
         */
        AndroidNetworking.get(_serverAPI + "update")
                // Ajout de l'APIKEY
                .addHeaders("apiKey", _apiKey)
                // Envoi de l'ID max des dessins (pour n'obtenir que les dessins ayant un ID supérieur)
                .addQueryParameter("max_drawing", DB.getInstance().maxDrawing())
                // Envoi de l'ID max des challenges (pour n'obtenir que les dessins ayant un ID supérieur)
                .addQueryParameter("max_challenge", DB.getInstance().maxChallenge())
                .setPriority(Priority.HIGH)
                .build()
                // Lors de la résolution de la requêtes
                .getAsJSONObject(new JSONObjectRequestListener() {
                    // Si la requête réussie (Code == 2XX)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Récupération de la liste des utilisateurs de la requête
                            JSONArray users = response.getJSONArray("users");
                            // Pour chaque utilisateur ...
                            for (int i = 0; i < users.length(); i++) {
                                // ... Création d'un User depuis les informations JSON
                                User u = User.fromJson(users.getJSONObject(i));
                                // Tentative de sauvegarde de l'utilisateur dans la base de données locale
                                if (!DB.getInstance().save(u)) {
                                    // Si la sauvegarde échoue (l'utilisateur est déjà en base de données locale,
                                    // met à jour l'utilisateur en base de données locale
                                    DB.getInstance().update(u);
                                }
                            }
                            // Récupération de la liste des dessins de la requête
                            JSONArray drawings = response.getJSONArray("drawings");
                            // Pour chaque dessin ...
                            for (int i = 0; i < drawings.length(); i++) {
                                // ... Création d'un Drawing depuis les informations JSON
                                Drawing d = Drawing.fromJson(drawings.getJSONObject(i));
                                // Tentative de sauvegarde du dessin dans la base de données locale.
                                // Si la sauvegarde échoue, le dessin est déjà en base de données locale
                                // (Les dessins ne pouvant pas être modifiés en base de données distante,
                                // la mise à jour est inutile)
                                DB.getInstance().save(d);
                            }
                            // Récupération de la liste des dessins de la requête
                            JSONArray challenges = response.getJSONArray("challenges");
                            // Pour chaque challenge ...
                            for (int i = 0; i < challenges.length(); i++) {
                                // ... Création d'un Challenge depuis les informations JSON
                                Challenge c = Challenge.fromJson(challenges.getJSONObject(i));
                                // Tentative de sauvegarde du challenge dans la base de données locale.
                                // Si la sauvegarde échoue, le challenge est déjà en base de données locale
                                // (Les dessins ne pouvant pas être modifiés en base de données distante,
                                // la mise à jour est inutile)
                                DB.getInstance().save(c);
                            }
                            // Récupération de la liste des utilisateurs de la requête
                            JSONArray participations = response.getJSONArray("participations");
                            // Pour chaque participation ...
                            for (int i = 0; i < participations.length(); i++) {
                                // ... Création d'une Participation depuis les informations JSON
                                Participation p = Participation.fromJson(participations.getJSONObject(i));
                                // Tentative de sauvegarde de la participation dans la base de données locale
                                if (!DB.getInstance().save(p)) {
                                    // Si la sauvegarde échoue (la participation est déjà en base de données locale,
                                    // met à jour la participation en base de données locale
                                    DB.getInstance().update(p);
                                }
                            }
                        } catch (JSONException e) {
                            // Si le parse des informations de la réquêtes échoue, annule la requête
                            e.printStackTrace();
                            return;
                        }
                        // Si la requêtes et la mise à jour de la base de données locale
                        // ont réussi, lancement de callbacl.onResponse (si callback != null)
                        if (!Objects.isNull(callback)) callback.onResponse();
                    }

                    // Si la requête échoue (Code != 2XX)
                    @Override
                    public void onError(ANError anError) {
                        // Lancement de callback.onError() défini (si callback != null)
                        if (!Objects.isNull(callback)) callback.onError(anError);
                    }
                });
    }

    /**
     * Fonction permettant l'envoi d'un requêtes de sauvegarde d'une entité dans la base de données distante
     * @param route La route à utiliser ("api/user/save" pour les User, "api/drawing/save" pour les Drawing ...)
     * @param object L'entité à sauvegarder au format JSON (utiliser "toJson()" ou "toJson(String password)" pour les User)
     * @param callback Interface contenant les fonctions onResponse et onError à implémenter
     *                  qui seront éxecutées lors de résolution de la requête
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void save(@NonNull ROUTES route, @NonNull JSONObject object, @Nullable JSONObjectRequestListener callback) {
        Log.d(RequestWrapper.REQUEST_LOG, "REQUEST SAVE WITH ROUTE: " + route._path + "save");

        // Requêtes POST vers la base de données distante (Route: api/{user, drawing, participation, challenge}/save)
        AndroidNetworking.post(_serverAPI + route._path + "save")
                // Ajout de l'APIKEY
                .addHeaders("apiKey", _apiKey)
                // Définition du contenu comme étant au format JSON
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(object)
                .setPriority(Priority.HIGH)
                .build()
                // Définition des fonctions à éxecuter lors de
                // la résolution de la requêtes
                .getAsJSONObject(callback);
    }

    /**
     * Fonction permettant l'envoi d'une requête de vote en base de données distante
     * @param participation la participation au format JSON (utiliser Participation.toJson()
     * @param username Le nom de l'utilisateur votant (permet d'empêcher à un utilisateur de voter plusieurs fois)
     * @param callback Interface contenant les fonctions onResponse et onError à implémenter
     *                  qui seront éxecutées lors de résolution de la requête
     * @throws JSONException Si le JSONObject participation ne contient pas les champs requis
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void vote(@NonNull JSONObject participation, @NonNull String username, @Nullable JSONObjectRequestListener callback) throws JSONException {
        Log.d(RequestWrapper.REQUEST_LOG, "ADD VOTE (" + participation.getString("u_id") + ", " + participation.getString("d_id") + ", " + participation.getString("c_id") + ")");

        // Requêtes PUT vers la base de données distante (Route: api/participation/vote)
        AndroidNetworking.put(_serverAPI + ROUTES.PARTICIPATION._path + "vote")
                // Ajout de l'APIKEY
                .addHeaders("apiKey", _apiKey)
                // Ajout des ids de la participation
                .addQueryParameter("u_id", participation.getString("u_id"))
                .addQueryParameter("d_id", participation.getString("d_id"))
                .addQueryParameter("c_id", participation.getString("c_id"))
                .addQueryParameter("voter", username)
                .setPriority(Priority.HIGH)
                .build()
                // Définition des fonctions à éxecuter lors de
                // la résolution de la requêtes
                .getAsJSONObject(callback);
    }
}

