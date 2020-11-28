package univ.orleans.ttl.isokachallenge.orm;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import java.time.LocalDateTime;

import univ.orleans.ttl.isokachallenge.orm.entity.*;

/**
 * [ SINGLETON ]
 * Class permettant la manipulation de la base de données à l'aide
 * de méthodes prédéfinies, pour ne pas à avoir à écrire des requêtes
 * SQL brut dans le reste du code, permettant d'éviter la duplication de
 * code lourd
 */
public class DB extends SQLiteOpenHelper {

    private static DB _instance;

    /**
     * Constructeur par défaut
     * @param context Le context de l'application
     */
    private DB(@NonNull Context context) {
        super(context, Tables.DB_NAME, null, Tables.DB_VERSION);
    }

    /**
     * Fonction permetttant l'instanciation du singleton de la base de données
     * Si la base de données a déjà été instanciée, AssertionError
     * @param context Le context de l'application à passer au constructeur
     */
    public static void init(Context context) {
        if (!Objects.isNull(_instance)) throw new AssertionError("Database is already initialized !");
        _instance = new DB(context);
    }

    /**
     * Fonction permetttant l'obtention de l'instanciation singleton de la base de données
     * Si la base de données n'a pas encore été instanciée, AssertionError
     * @return L'instance DB
     */
    @NonNull
    public static DB getInstance() {
        if (Objects.isNull(_instance)) throw new AssertionError("Database is not initialized !");
        return _instance;
    }

    /**
     * Fonction permettant de vérifier si la base de données a été instanciée
     * @return True si la base de données est instanciée, false sinon
     */
    @NonNull
    public static Boolean isInitialized() { return !Objects.isNull(_instance); }

    /**
     * Fonction exécutée avant toutes manipulations de la base de données
     * Permet de forcer la gestion des contraintes ForeignKey (pas activées par défauts)
     * @param db La base de données concernées
     */
    @Override
    public void onConfigure(@NonNull SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        //Création des tables grâce aux requêtes SQL contenues dans Tables
        db.execSQL(Tables.USER_CREATE);
        db.execSQL(Tables.DRAWING_CREATE);
        db.execSQL(Tables.CHALLENGE_CREATE);
        db.execSQL(Tables.PARTICIPATION_CREATE);

        Log.d(Tables.DB_LOG, "Database created !");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Tables.DROP);
        this.onCreate(db);
    }

    //////////////////////////////
    //          SAVE            //
    //////////////////////////////

    /**
     * Fonction permettant de sauvegarder un utilisateur en base de données locale
     * @param user L'utilisateur à sauvegarder
     * @return True si l'insertion à été complétée, false si elle a échouée
     */
    public boolean save(@NonNull User user) {
        try {
            // Création d'un ContentValue avec les informations de l'utilisateur
            ContentValues values = new ContentValues();
            values.put(Tables.USER_NAME, user.getUsername());
            values.put(Tables.USER_DATE, user.getDate());
            values.put(Tables.USER_SALT, user.getSalt());

            // Insertion dans la base de données (throw SQLException)
            this.getWritableDatabase().insertOrThrow(Tables.USER_TABLE, null, values);
            Log.d(Tables.DB_LOG, "SAVED " + user);
            // L'opération s'est bien déroulée
            return true;
        } catch (SQLException ex) {
            // Si l'insertion a échouée
            Log.e(Tables.DB_LOG, "COULDN'T SAVE " + user + "\n\tDETAILS :\n\t\t" + ex.getMessage());
            return false;
        }
    }

    /**
     * Fonction permettant de sauvegarder un dessin en base de données locale
     * @param drawing Le dessin à sauvegarder
     * @return True si l'insertion à été complétée, false si elle a échouée
     */
    public boolean save(@NonNull Drawing drawing) {
        try {
            // Création d'un ContentValue avec les informations du dessin
            ContentValues values = new ContentValues();
            values.put(Tables.DRAWING_ID, drawing.getId());
            values.put(Tables.DRAWING_LINK, drawing.getLink());
            values.put(Tables.DRAWING_DATE, drawing.getDate());

            // Insertion dans la base de données (throw SQLException)
            this.getWritableDatabase().insertOrThrow(Tables.DRAWING_TABLE, null, values);
            Log.d(Tables.DB_LOG, "SAVED " + drawing);
            // L'opération s'est bien déroulée
            return true;
        } catch (SQLException ex) {
            // Si l'insertion a échouée
            Log.e(Tables.DB_LOG, "COULDN'T SAVE " + drawing + "\n\tDETAILS :\n\t\t" + ex.getMessage());
            return false;
        }
    }

    /**
     * Fonction permettant de sauvegarder un challenge en base de données locale
     * @param challenge Le challenge à sauvegarder
     * @return True si l'insertion à été complétée, false si elle a échouée
     */
    public boolean save(@NonNull Challenge challenge) {
        try {
            // Création d'un ContentValue avec les informations du challenge
            ContentValues values = new ContentValues();
            values.put(Tables.CHALLENGE_ID, challenge.getId());
            values.put(Tables.CHALLENGE_NAME, challenge.getName());
            values.put(Tables.CHALLENGE_TYPE, challenge.getType());
            values.put(Tables.CHALLENGE_THEME, challenge.getTheme());
            values.put(Tables.CHALLENGE_DURATION, challenge.getDate());
            values.put(Tables.CHALLENGE_TIMER, challenge.getTimer());
            values.put(Tables.CHALLENGE_DESCRIPTION, challenge.getDesc());

            // Insertion dans la base de données (throw SQLException)
            this.getWritableDatabase().insertOrThrow(Tables.CHALLENGE_TABLE, null, values);
            Log.d(Tables.DB_LOG, "SAVED " + challenge);
            // L'opération s'est bien déroulée
            return true;
        } catch (SQLException ex) {
            // Si l'insertion a échouée
            Log.e(Tables.DB_LOG, "COULDN'T SAVE " + challenge + "\n\tDETAILS :\n\t\t" + ex.getMessage());
            return false;
        }
    }

    /**
     * Fonction permettant de sauvegarder une participation en base de données locale
     * @param participation La participation à sauvegarder
     * @return True si l'insertion à été complétée, false si elle a échouée
     */
    public boolean save(@NonNull Participation participation) {
        try {
            // Création d'un ContentValue avec les informations de la participation
            ContentValues values = new ContentValues();
            values.put(Tables.PARTICIPATION_USER_ID, participation.getUser().getUsername());
            values.put(Tables.PARTICIPATION_DRAWING_ID, participation.getDrawing().getId());
            values.put(Tables.PARTICIPATION_CHALLENGE_ID, participation.getChallenge().getId());
            values.put(Tables.PARTICIPATION_IS_CREATOR, participation.isCreator());
            values.put(Tables.PARTICIPATION_VOTES, participation.getVotes());

            // Insertion dans la base de données (throw SQLException)
            this.getWritableDatabase().insertOrThrow(Tables.PARTICIPATION_TABLE, null, values);
            Log.d(Tables.DB_LOG, "SAVED " + participation);
            // L'opération s'est bien déroulée
            return true;
        } catch (SQLException ex) {
            // Si l'insertion a échouée
            Log.e(Tables.DB_LOG, "COULDN'T SAVE " + participation + "\n\tDETAILS :\n\t\t" + ex.getMessage());
            return false;
        }
    }

    //////////////////////////////
    //          UPDATE          //
    //////////////////////////////

    /**
     * Fonction permettant la mise à jour d'un utilisateur en base de données locale
     * @param user Les nouvelles information de l'utilisateur
     * @return Le nombre de lignes affectées
     */
    public int update(@NonNull User user) {
        // Création d'un ContentValue avec les informations de l'utilisateur
        ContentValues values = new ContentValues();
        values.put(Tables.USER_NAME, user.getUsername());
        values.put(Tables.USER_DATE, user.getDate());
        values.put(Tables.USER_SALT, user.getSalt());

        // Mise à jour
        int nbAffected = this.getWritableDatabase().update(Tables.USER_TABLE, values, Tables.USER_NAME + " = ?", new String[] { user.getUsername() });
        Log.d(Tables.DB_LOG, "(USER) NUMBER OF ROWS AFFECTED: " + nbAffected);
        return nbAffected;
    }

    /**
     * Fonction permettant la mise à jour d'un dessin en base de données locale
     * @param drawing Les nouvelles information du dessin
     * @return Le nombre de lignes affectées
     */
    public int update(@NonNull Drawing drawing) {
        // Création d'un ContentValue avec les informations du dessin
        ContentValues values = new ContentValues();
        values.put(Tables.DRAWING_ID, drawing.getId());
        values.put(Tables.DRAWING_LINK, drawing.getLink());
        values.put(Tables.DRAWING_DATE, drawing.getDate());

        // Mise à jour
        int nbAffected = this.getWritableDatabase().update(Tables.DRAWING_TABLE, values, Tables.DRAWING_ID + " = ?", new String[] { String.valueOf(drawing.getId()) });
        Log.d(Tables.DB_LOG, "(DRAWING) NUMBER OF ROWS AFFECTED: " + nbAffected);
        return nbAffected;
    }

    /**
     * Fonction permettant la mise à jour d'un challenge en base de données locale
     * @param challenge Les nouvelles information du challenge
     * @return Le nombre de lignes affectées
     */
    public int update(@NonNull Challenge challenge) {
        // Création d'un ContentValue avec les informations du challenge
        ContentValues values = new ContentValues();
        values.put(Tables.CHALLENGE_ID, challenge.getId());
        values.put(Tables.CHALLENGE_NAME, challenge.getName());
        values.put(Tables.CHALLENGE_TYPE, challenge.getType());
        values.put(Tables.CHALLENGE_THEME, challenge.getTheme());
        values.put(Tables.CHALLENGE_DURATION, challenge.getDate());
        values.put(Tables.CHALLENGE_TIMER, challenge.getTimer());
        values.put(Tables.CHALLENGE_DESCRIPTION, challenge.getDesc());

        // Mise à jour
        int nbAffected = this.getWritableDatabase().update(Tables.CHALLENGE_TABLE, values, Tables.CHALLENGE_ID + " = ?", new String[] { String.valueOf(challenge.getId()) });
        Log.d(Tables.DB_LOG, "(CHALLENGE) NUMBER OF ROWS AFFECTED: " + nbAffected);
        return nbAffected;
    }

    /**
     * Fonction permettant la mise à jour d'une participation en base de données locale
     * @param participation Les nouvelles information de la participation
     * @return Le nombre de lignes affectées
     */
    public int update(@NonNull Participation participation) {
        // Création d'un ContentValue avec les informations de la participation
        ContentValues values = new ContentValues();
        values.put(Tables.PARTICIPATION_USER_ID, participation.getUser().getUsername());
        values.put(Tables.PARTICIPATION_DRAWING_ID, participation.getDrawing().getId());
        values.put(Tables.PARTICIPATION_CHALLENGE_ID, participation.getChallenge().getId());
        values.put(Tables.PARTICIPATION_IS_CREATOR, participation.isCreator());
        values.put(Tables.PARTICIPATION_VOTES, participation.getVotes());

        // Mise à jour
        int nbAffected = this.getWritableDatabase().update(Tables.PARTICIPATION_TABLE, values, Tables.PARTICIPATION_USER_ID + " = ? AND " + Tables.PARTICIPATION_DRAWING_ID + " = ? AND " + Tables.PARTICIPATION_CHALLENGE_ID + " = ?", new String[] { participation.getUser().getUsername(), String.valueOf(participation.getDrawing().getId()), String.valueOf(participation.getChallenge().getId()) });
        Log.d(Tables.DB_LOG, "(PARTICIPATION) NUMBER OF ROWS AFFECTED: " + nbAffected);
        return nbAffected;
    }

    //////////////////////////////
    //          DELETE          //
    //////////////////////////////

    /**
     * Fonction permettant la suppression d'un utilisateur en base de données locale
     * @param user L'utilisateur à supprimer
     * @return Le nombre de lignes affectées
     */
    public int delete(@NonNull User user) {
        int nbAffected = this.getWritableDatabase().delete(Tables.USER_TABLE, Tables.USER_NAME + " = ?", new String[] { user.getUsername() });
        Log.d(Tables.DB_LOG, "(USER) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    /**
     * Fonction permettant la suppression d'un dessin en base de données locale
     * @param drawing Le dessin à supprimer
     * @return Le nombre de lignes affectées
     */
    public int delete(@NonNull Drawing drawing) {
        int nbAffected = this.getWritableDatabase().delete(Tables.DRAWING_TABLE, Tables.DRAWING_ID + " = ?", new String[] { String.valueOf(drawing.getId()) });
        Log.d(Tables.DB_LOG, "(DRAWING) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    /**
     * Fonction permettant la suppression d'un challenge en base de données locale
     * @param challenge Le challenge à supprimer
     * @return Le nombre de lignes affectées
     */
    public int delete(@NonNull Challenge challenge) {
        int nbAffected = this.getWritableDatabase().delete(Tables.CHALLENGE_TABLE, Tables.CHALLENGE_ID + " = ?", new String[] { String.valueOf(challenge.getId() )});
        Log.d(Tables.DB_LOG, "(CHALLENGE) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    /**
     * Fonction permettant la suppression d'une participation en base de données locale
     * @param participation La participation à supprimer
     * @return Le nombre de lignes affectées
     */
    public int delete(@NonNull Participation participation) {
        int nbAffected =  this.getWritableDatabase().delete(Tables.PARTICIPATION_TABLE, Tables.PARTICIPATION_USER_ID + " = ? AND " + Tables.PARTICIPATION_DRAWING_ID + " = ? AND " + Tables.PARTICIPATION_CHALLENGE_ID + " = ?", new String[] { participation.getUser().getUsername(), String.valueOf(participation.getDrawing().getId()), String.valueOf(participation.getChallenge().getId()) });
        Log.d(Tables.DB_LOG, "(PARTICIPATION) NUMBER OF ROWS DELETED: " + nbAffected);
        return nbAffected;
    }

    //////////////////////////////
    //            GET           //
    //////////////////////////////

    /**
     * Fonction permettant d'obtenir un unique utilisateur en fonction de sa clef primaire
     * @param username Le nom d'utilisateur (unique) de l'utilisateur
     * @return l'utilisateur correspondant ayant le nom d'utilisateur correspondant, null si rien n'a té trouvé
     */
    @Nullable
    public User getUser(String username) {
        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c =getReadableDatabase().query(
                Tables.USER_TABLE,
                new String[] { Tables.USER_NAME, Tables.USER_DATE, Tables.USER_SALT },
                Tables.USER_NAME + " = ?",
                new String[] { username },
                null,
                null,
                null
            )
        ) {
            // Si la query a retournée une ligne ...
           if (c.moveToFirst()) {
               // ... retourne la ligne transformée en User
               User returned = new User(c.getString(Tables.USER_NAME_INDEX), LocalDateTime.parse(c.getString(Tables.USER_DATE_INDEX)), c.getString(Tables.USER_SALT_INDEX));
               Log.d(Tables.DB_LOG, "(USER) FOUND " + returned);
               return returned;
           } else {
               // ... Sinon retourne null
               Log.d(Tables.DB_LOG, "(USER) NOTHING FOUND");
               return null;
           }
        }
    }

    /**
     * Fonction permettant d'obtenir un unique dessin en fonction de sa clef primaire
     * @param id L'id (unique) du dessin
     * @return le dessin correspondant ayant l'id correspondant, null si rien n'a té trouvé
     */
    @Nullable
    public Drawing getDrawing(int id) {
        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.DRAWING_TABLE,
                    new String[] { Tables.DRAWING_ID, Tables.DRAWING_LINK, Tables.DRAWING_DATE },
                    Tables.DRAWING_ID + " = ?",
                    new String[] { String.valueOf(id) },
                    null,
                    null,
                    null
            )
        ) {
            // Si la query a retournée une ligne ...
            if (c.moveToFirst()) {
                // ... retourne la ligne transformée en Drawing
                Drawing returned = new Drawing(c.getInt(Tables.DRAWING_ID_INDEX), c.getString(Tables.DRAWING_LINK_INDEX), LocalDateTime.parse(c.getString(Tables.DRAWING_DATE_INDEX)));
                Log.d(Tables.DB_LOG, "(DRAWING) FOUND " + returned);
                return returned;
            } else {
                // ... Sinon retourne null
                Log.d(Tables.DB_LOG, "(DRAWING) NOTHING FOUND");
                return null;
            }
        }
    }

    /**
     * Fonction permettant d'obtenir un unique challenge en fonction de sa clef primaire
     * @param id L'id (unique) du challenge
     * @return le challenge correspondant ayant l'id correspondant, null si rien n'a té trouvé
     */
    @Nullable
    public Challenge getChallenge(int id) {
        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.CHALLENGE_TABLE,
                    new String[] { Tables.CHALLENGE_ID, Tables.CHALLENGE_NAME, Tables.CHALLENGE_TYPE, Tables.CHALLENGE_THEME, Tables.CHALLENGE_DURATION, Tables.CHALLENGE_TIMER, Tables.CHALLENGE_DESCRIPTION },
                    Tables.CHALLENGE_ID + " = ?",
                    new String[] { String.valueOf(id) },
                    null,
                    null,
                    null
            )
        ) {
            // Si la query a retournée une ligne ...
            if (c.moveToFirst()) {
                // ... retourne la ligne transformée en Challenge
                Challenge returned = new Challenge(c.getInt(Tables.CHALLENGE_ID_INDEX), c.getString(Tables.CHALLENGE_NAME_INDEX), c.getInt(Tables.CHALLENGE_TYPE_INDEX) > 0, c.getString(Tables.CHALLENGE_THEME_INDEX), LocalDateTime.parse(c.getString(Tables.CHALLENGE_DURATION_INDEX)), c.getInt(Tables.CHALLENGE_TIMER_INDEX), c.getString(Tables.CHALLENGE_DESCRIPTION_INDEX));
                Log.d(Tables.DB_LOG, "(CHALLENGE) FOUND " + returned);
                return returned;
            } else {
                // ... Sinon retourne null
                Log.d(Tables.DB_LOG, "(USER) NOTHING FOUND");
                return null;
            }
        }
    }

    /**
     * Fonction permettant d'obtenir un unique challenge en fonction de ses clefs primaires
     * @param id_user le nom d'utilisateur (unique) qui a réalisé la participation
     * @param id_drawing l'ID (unique) du dessin réalisé
     * @param id_challenge l'ID (unique) du challenge
     * @return la participation correspondante, null si rien n'a té trouvé
     */
    @Nullable
    public Participation getParticipation(String id_user, int id_drawing, int id_challenge) {
        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.PARTICIPATION_TABLE,
                    new String[] { Tables.PARTICIPATION_USER_ID, Tables.PARTICIPATION_DRAWING_ID, Tables.PARTICIPATION_CHALLENGE_ID, Tables.PARTICIPATION_IS_CREATOR, Tables.PARTICIPATION_VOTES },
                    Tables.PARTICIPATION_USER_ID + " = ? AND " + Tables.PARTICIPATION_DRAWING_ID + " = ? AND " + Tables.PARTICIPATION_CHALLENGE_ID + " = ?",
                    new String[] { id_user, String.valueOf(id_drawing), String.valueOf(id_challenge) },
                    null,
                    null,
                    null
            )
        ) {
            // Si la query a retournée une ligne ...
            if (c.moveToFirst()) {
                // ... retourne la ligne transformée en Participation
                Participation returned = new Participation(getUser(c.getString(Tables.PARTICIPATION_USER_ID_INDEX)), getDrawing(c.getInt(Tables.PARTICIPATION_DRAWING_ID_INDEX)), getChallenge(c.getInt(Tables.PARTICIPATION_CHALLENGE_ID_INDEX)), c.getInt(Tables.PARTICIPATION_IS_CREATOR_INDEX) > 0, c.getInt(Tables.PARTICIPATION_VOTES_INDEX));
                Log.d(Tables.DB_LOG, "(PARTICIPATION) FOUND " + returned);
                return returned;
            } else {
                // ... Sinon retourne null
                Log.d(Tables.DB_LOG, "(USER) NOTHING FOUND");
                return null;
            }
        }
    }

    //////////////////////////////
    //          GETALL          //
    //////////////////////////////

    /**
     * Fonction permettant d'obtenir la liste de tous les utilisateurs en base de données
     * @return Une liste (potentiellement vide) contenant tous les utilisateurs
     */
    @NonNull
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.USER_TABLE,
                    new String[] { Tables.USER_NAME, Tables.USER_DATE, Tables.USER_SALT },
                    null,
                    null,
                    null,
                    null,
                    null
            )
        ) {
            // Pour toutes les lignes reçues ...
            while (c.moveToNext()) {
                // ... Ajout de la ligne transformée en User dans la liste des utilisateurs
                users.add(new User(c.getString(Tables.USER_NAME_INDEX), LocalDateTime.parse(c.getString(Tables.USER_DATE_INDEX)), c.getString(Tables.USER_SALT_INDEX)));
            }
        }
        //Debug Log
        StringBuilder sb = new StringBuilder("ALL USERS :\n\t[\n");
        for (User u : users) sb.append("\t\t").append(u).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return users;
    }

    /**
     * Fonction permettant d'obtenir la liste de tous les dessins en base de données
     * @return Une liste (potentiellement vide) contenant tous les dessins
     */
    @NonNull
    public List<Drawing> getAllDrawings() {
        List<Drawing> drawings = new ArrayList<>();

        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.DRAWING_TABLE,
                    new String[] { Tables.DRAWING_ID, Tables.DRAWING_LINK, Tables.DRAWING_DATE },
                    null,
                    null,
                    null,
                    null,
                    null
            )
        ) {
            // Pour toutes les lignes reçues ...
            while (c.moveToNext()) {
                // ... Ajout de la ligne transformée en Drawing dans la liste des dessins
                drawings.add(new Drawing(c.getInt(Tables.DRAWING_ID_INDEX), c.getString(Tables.DRAWING_LINK_INDEX), LocalDateTime.parse(c.getString(Tables.DRAWING_DATE_INDEX))));
            }
        }
        //Debug Log
        StringBuilder sb = new StringBuilder("ALL DRAWINGS :\n\t[\n");
        for (Drawing d : drawings) sb.append("\t\t").append(d).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return drawings;
    }

    /**
     * Fonction permettant d'obtenir la liste de tous les challenges en base de données
     * @return Une liste (potentiellement vide) contenant tous les challenges
     */
    @NonNull
    public List<Challenge> getAllChallenges() {
        List<Challenge> challenges = new ArrayList<>();

        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.CHALLENGE_TABLE,
                    new String[] { Tables.CHALLENGE_ID, Tables.CHALLENGE_NAME, Tables.CHALLENGE_TYPE, Tables.CHALLENGE_THEME, Tables.CHALLENGE_DURATION, Tables.CHALLENGE_TIMER, Tables.CHALLENGE_DESCRIPTION },
                    null,
                    null,
                    null,
                    null,
                    null
            )
        ) {
            // Pour toutes les lignes reçues ...
            while (c.moveToNext()) {
                // ... Ajout de la ligne transformée en Challenge dans la liste des challenges
                challenges.add(new Challenge(c.getInt(Tables.CHALLENGE_ID_INDEX), c.getString(Tables.CHALLENGE_NAME_INDEX), c.getInt(Tables.CHALLENGE_TYPE_INDEX) > 0, c.getString(Tables.CHALLENGE_THEME_INDEX), LocalDateTime.parse(c.getString(Tables.CHALLENGE_DURATION_INDEX)), c.getInt(Tables.CHALLENGE_TIMER_INDEX), c.getString(Tables.CHALLENGE_DESCRIPTION_INDEX)));
            }
        }
        //Debug Log
        StringBuilder sb = new StringBuilder("ALL CHALLENGES :\n\t[\n");
        for (Challenge c : challenges) sb.append("\t\t").append(c).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return challenges;
    }

    /**
     * Fonction permettant d'obtenir la liste de toutes les participations en base de données
     * @return Une liste (potentiellement vide) contenant toutes les participations
     */
    @NonNull
    public List<Participation> getAllParticipations() {
        List<Participation> participations = new ArrayList<>();

        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.PARTICIPATION_TABLE,
                    new String[] { Tables.PARTICIPATION_USER_ID, Tables.PARTICIPATION_DRAWING_ID, Tables.PARTICIPATION_CHALLENGE_ID, Tables.PARTICIPATION_IS_CREATOR, Tables.PARTICIPATION_VOTES },
                    null,
                    null,
                    null,
                    null,
                    null
            )
        ) {
            // Pour toutes les lignes reçues ...
            while (c.moveToNext()) {
                // ... Ajout de la ligne transformée en Participation dans la liste des participations
                participations.add(new Participation(getUser(c.getString(Tables.PARTICIPATION_USER_ID_INDEX)), getDrawing(c.getInt(Tables.PARTICIPATION_DRAWING_ID_INDEX)), getChallenge(c.getInt(Tables.PARTICIPATION_CHALLENGE_ID_INDEX)), c.getInt(Tables.PARTICIPATION_IS_CREATOR_INDEX) > 0, c.getInt(Tables.PARTICIPATION_VOTES_INDEX)));
            }
        }
        //Debug Log
        StringBuilder sb = new StringBuilder("ALL PARTICIPATIONS :\n\t[\n");
        for (Participation p : participations) sb.append("\t\t").append(p).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return participations;
    }

    //////////////////////////////
    //         GET WHERE        //
    //////////////////////////////

    /**
     * Fonction permettant d'obtenir une liste d'utilisateurs correspondant
     * aux critères données. Passer une Map nulle revient à faire un GetAll
     * @param wheres Une Map sous la forme champ : Pair(opérateur, valeur)
     *               champ : Le champ sur lequel appliqué l'opération (Ex Tables.USER_NAME pour tester le nom d'utilisateur)
     *               opérateur : L'opérateur à utiliser (Ex Tables.OPERATOR_EQ pour l'égalité)
     *               valeur : La valeur avec laquelle tester le champs (Ex: "MamanGvomi")
     * @return Une liste (potentiellement vide) de tous les utilisateurs correspondant aux critères
     */
    @NonNull
    public List<User> getUsers(@Nullable Map<String, Pair<String, String>> wheres) {

        // Si la map est nulle ou vide, retourner GetAll
        if (Objects.isNull(wheres) || wheres.isEmpty()) return getAllUsers();

        List<User> users = new ArrayList<>();

        // Transformation des operations dans map en String SQL
        StringBuilder query = new StringBuilder("1");
        String[] args = new String[wheres.size()];
        int index = 0;
        for (String key : wheres.keySet()) {
            query.append(" AND ").append(key).append(wheres.get(key).first).append("?");
            args[index++] = wheres.get(key).second;
        }

        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.USER_TABLE,
                    new String[] { Tables.USER_NAME, Tables.USER_DATE, Tables.USER_SALT },
                    query.toString(),
                    args,
                    null,
                    null,
                    null
            )
        ) {
            // Pour toutes les lignes reçues ...
            while (c.moveToNext()) {
                // ... Ajout de la ligne transformée en User dans la liste des utilisateurs
                users.add(new User(c.getString(Tables.USER_NAME_INDEX), LocalDateTime.parse(c.getString(Tables.USER_DATE_INDEX)), c.getString(Tables.USER_SALT_INDEX)));
            }
        }
        //Debug Log
        StringBuilder sb = new StringBuilder("USERS FOUND :\n\t[\n");
        for (User u : users) sb.append("\t\t").append(u).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return users;
    }

    /**
     * Fonction permettant d'obtenir une liste de dessins correspondant
     * aux critères données. Passer une Map nulle revient à faire un GetAll
     * @param wheres Une Map sous la forme champ : Pair(opérateur, valeur)
     *               champ : Le champ sur lequel appliqué l'opération (Ex Tables.DRAWING_ID pour tester l'id)
     *               opérateur : L'opérateur à utiliser (Ex Tables.OPERATOR_EQ pour l'égalité)
     *               valeur : La valeur avec laquelle tester le champs (Ex: "1")
     * @return Une liste (potentiellement vide) de tous les dessins correspondant aux critères
     */
    @NonNull
    public List<Drawing> getDrawings(@Nullable  Map<String, Pair<String, String>> wheres) {

        // Si la map est nulle ou vide, retourner GetAll
        if (Objects.isNull(wheres) || wheres.isEmpty()) return getAllDrawings();

        List<Drawing> drawings = new ArrayList<>();

        // Transformation des operations dans map en String SQL
        StringBuilder query = new StringBuilder("1");
        String[] args = new String[wheres.size()];
        int index = 0;
        for (String key : wheres.keySet()) {
            query.append(" AND ").append(key).append(wheres.get(key).first).append("?");
            args[index++] = wheres.get(key).second;
        }

        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.DRAWING_TABLE,
                    new String[] { Tables.DRAWING_ID, Tables.DRAWING_LINK, Tables.DRAWING_DATE },
                    query.toString(),
                    args,
                    null,
                    null,
                    null
            )
        ) {
            // Pour toutes les lignes reçues ...
            while (c.moveToNext()) {
                // ... Ajout de la ligne transformée en Drawing dans la liste des dessins
                drawings.add(new Drawing(c.getInt(Tables.DRAWING_ID_INDEX), c.getString(Tables.DRAWING_LINK_INDEX), LocalDateTime.parse(c.getString(Tables.DRAWING_DATE_INDEX))));
            }
        }
        //Debug Log
        StringBuilder sb = new StringBuilder("DRAWINGS FOUND :\n\t[\n");
        for (Drawing d : drawings) sb.append("\t\t").append(d).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return drawings;
    }

    /**
     * Fonction permettant d'obtenir une liste de challenges correspondant
     * aux critères données. Passer une Map nulle revient à faire un GetAll
     * @param wheres Une Map sous la forme champ : Pair(opérateur, valeur)
     *               champ : Le champ sur lequel appliqué l'opération (Ex Tables.CHALLENGE_ID pour tester l'id)
     *               opérateur : L'opérateur à utiliser (Ex Tables.OPERATOR_EQ pour l'égalité)
     *               valeur : La valeur avec laquelle tester le champs (Ex: "1")
     * @return Une liste (potentiellement vide) de tous les challenges correspondant aux critères
     */
    @NonNull
    public List<Challenge> getChallenges(@Nullable Map<String, Pair<String, String>> wheres) {

        // Si la map est nulle ou vide, retourner GetAll
        if (Objects.isNull(wheres) || wheres.isEmpty()) return getAllChallenges();

        List<Challenge> challenges = new ArrayList<>();

        // Transformation des operations dans map en String SQL
        StringBuilder query = new StringBuilder("1");
        String[] args = new String[wheres.size()];
        int index = 0;
        for (String key : wheres.keySet()) {
            query.append(" AND ").append(key).append(wheres.get(key).first).append("?");
            args[index++] = wheres.get(key).second;
        }

        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.CHALLENGE_TABLE,
                    new String[] { Tables.CHALLENGE_ID, Tables.CHALLENGE_NAME, Tables.CHALLENGE_TYPE, Tables.CHALLENGE_THEME, Tables.CHALLENGE_DURATION, Tables.CHALLENGE_TIMER, Tables.CHALLENGE_DESCRIPTION },
                    query.toString(),
                    args,
                    null,
                    null,
                    null
            )
        ) {
            // Pour toutes les lignes reçues ...
            while (c.moveToNext()) {
                // ... Ajout de la ligne transformée en Challenge dans la liste des challenges
                challenges.add(new Challenge(c.getInt(Tables.DRAWING_ID_INDEX), c.getString(Tables.CHALLENGE_NAME_INDEX), c.getInt(Tables.CHALLENGE_TYPE_INDEX) > 0, c.getString(Tables.CHALLENGE_THEME_INDEX), LocalDateTime.parse(c.getString(Tables.CHALLENGE_DURATION_INDEX)), c.getInt(Tables.CHALLENGE_TIMER_INDEX), c.getString(Tables.CHALLENGE_DESCRIPTION_INDEX)));
            }
        }
        //Debug Log
        StringBuilder sb = new StringBuilder("CHALLENGES FOUND :\n\t[\n");
        for (Challenge c : challenges) sb.append("\t\t").append(c).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return challenges;
    }

    /**
     * Fonction permettant d'obtenir une liste de participations correspondant
     * aux critères données. Passer une Map nulle revient à faire un GetAll
     * @param wheres Une Map sous la forme champ : Pair(opérateur, valeur)
     *               champ : Le champ sur lequel appliqué l'opération (Ex Tables.PARTICIPATION_USER_ID pour tester l'id de l'utilisateur)
     *               opérateur : L'opérateur à utiliser (Ex Tables.OPERATOR_EQ pour l'égalité)
     *               valeur : La valeur avec laquelle tester le champs (Ex: "MamanGvomi")
     * @return Une liste (potentiellement vide) de toutes les participations correspondant aux critères
     */
    @NonNull
    public List<Participation> getParticipations(@Nullable Map<String, Pair<String, String>> wheres) {

        // Si la map est nulle ou vide, retourner GetAll
        if (Objects.isNull(wheres) || wheres.isEmpty()) return getAllParticipations();

        List<Participation> participations = new ArrayList<>();

        // Transformation des operations dans map en String SQL
        StringBuilder query = new StringBuilder("1");
        String[] args = new String[wheres.size()];
        int index = 0;
        for (String key : wheres.keySet()) {
            query.append(" AND ").append(key).append(wheres.get(key).first).append("?");
            args[index++] = wheres.get(key).second;
        }

        try (
            // Exucution de la query dans un Try-With-Ressource (auto-close du Cursor)
            Cursor c = getReadableDatabase().query(
                    Tables.PARTICIPATION_TABLE,
                    new String[] { Tables.PARTICIPATION_USER_ID, Tables.PARTICIPATION_DRAWING_ID, Tables.PARTICIPATION_CHALLENGE_ID, Tables.PARTICIPATION_IS_CREATOR, Tables.PARTICIPATION_VOTES },
                    query.toString(),
                    args,
                    null,
                    null,
                    null
            )
        ) {
            // Pour toutes les lignes reçues ...
            while (c.moveToNext()) {
                // ... Ajout de la ligne transformée en Participation dans la liste des participations
                participations.add(new Participation(getUser(c.getString(Tables.PARTICIPATION_USER_ID_INDEX)), getDrawing(c.getInt(Tables.PARTICIPATION_DRAWING_ID_INDEX)), getChallenge(c.getInt(Tables.PARTICIPATION_CHALLENGE_ID_INDEX)), c.getInt(Tables.PARTICIPATION_IS_CREATOR_INDEX) > 0, c.getInt(Tables.PARTICIPATION_VOTES_INDEX)));
            }
        }
        StringBuilder sb = new StringBuilder("PARTICIPATIONS FOUND :\n\t[\n");
        for (Participation p : participations) sb.append("\t\t").append(p).append("\n");
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());
        Log.d(Tables.DB_LOG, sb.append("\t]").toString());

        return participations;
    }

    //////////////////////////////
    //         SPECIALS         //
    //////////////////////////////

    /**
     * Fonction permettant d'obtenir l'utilisateur ayant effectué ce dessin
     * @param id L'id du dessin
     * @return Retourne l'utilisateurs ayant effectué le dessin, ou null si aucun dessin n'a l'ID correspondant,
     *          ou bien que le dessin n'est utilisé dans aucune participation
     */
    @Nullable
    public User getUserFromDrawing(int id) {
        // Récupération du dessin
        Drawing d = getDrawing(id);
        // Si le dessin n'existe pas, retourne null
        if (Objects.isNull(d)) return null;

        // Obtention des participations utilisant ce dessin (normalement unique)
        HashMap<String, Pair<String, String>> wheres = new HashMap<>();
        wheres.put(Tables.PARTICIPATION_DRAWING_ID, new Pair<>(Tables.OPERATOR_EQ, String.valueOf(d.getId())));
        List<Participation> participations = getParticipations(wheres);

        // Si aucune participations n'utilise ce dessin, return null
        if (participations.isEmpty()) return null;
        // Retourne l'utilisateur ayant réalisé la participation
        return participations.get(0).getUser();
    }

    /**
     * Fonction permettant d'obtenir la liste de tous les dessins réalisés par un utilisateur
     * @param username Le nom d'utilisateur (unique) de l'utilisateur recherché
     * @return Une liste (potentiellement vide) de tous les dessins réalisé par l'utilisateur
     */
    @NonNull
    public List<Drawing> getDrawingsFromUser(@NonNull String username) {
        // Récupération des toutes les participations de l'utilisateur (si le nom d'utilisateur
        // ne correspond à aucun utilisateur en base de donnée locale, la liste est vide)
        Map<String, Pair<String, String>> wheres = new HashMap<>();
        wheres.put(Tables.PARTICIPATION_USER_ID, new Pair<>(Tables.OPERATOR_EQ, username));
        List<Participation> participations = getParticipations(wheres);

        List<Drawing> drawings = new ArrayList<>();
        // Pour toutes les participations, stocke le dessin de la participation dans la liste à retourner
        for (Participation p : participations) { drawings.add(p.getDrawing()); }
        return drawings;
    }

    /**
     * Fonction permettant d'obtenir la liste de tous les utilisateurs ayant participé à un challenge
     * @param id L'id du challenge
     * @return Une liste (potentiellement vide) de tous les utilisateurs
     */
    @NonNull
    public List<User> getUsersFromChallenge(int id) {
        // Récupération des toutes les participations au challenge ayant l'id (si l'id
        // ne correspond à aucun challenge en base de donnée locale, la liste est vide)
        Map<String, Pair<String, String>> wheres = new HashMap<>();
        wheres.put(Tables.PARTICIPATION_CHALLENGE_ID, new Pair<>(Tables.OPERATOR_EQ, String.valueOf(id)));
        List<Participation> participations = getParticipations(wheres);

        List<User> users = new ArrayList<>();
        // Pour toutes les participations, stocke l'utilisateur ayant réalisé la participation dans la liste à retourner
        for (Participation p : participations) { users.add(p.getUser()); }
        return users;
    }

    /**
     * Fonction permettant d'obtenir tous les dessins d'un challenge
     * @param id L'id du challenge
     * @return Une liste (potentiellement vide) de tous les dessins liés au challenge
     */
    @NonNull
    public List<Drawing> getDrawingsFromChallenge(int id) {
        // Récupération des toutes les participations au challenge ayant l'id (si l'id
        // ne correspond à aucun challenge en base de donnée locale, la liste est vide)
        Map<String, Pair<String, String>> wheres = new HashMap<>();
        wheres.put(Tables.PARTICIPATION_CHALLENGE_ID, new Pair<>(Tables.OPERATOR_EQ, String.valueOf(id)));
        List<Participation> participations = getParticipations(wheres);

        List<Drawing> drawings = new ArrayList<>();
        // Pour toutes les participations, stocke l'utilisateur ayant réalisé la participation dans la liste à retourner
        for (Participation p : participations) { drawings.add(p.getDrawing()); }
        return drawings;
    }

    /**
     * Fonction permettant d'obtenir l'id maximum des dessins
     * Permet de ne demdander que les dessins ayant un id supérieur à celui retourné lors
     * de la requête de mise à jour de la base de données locale
     * @return l'id maximum en String
     */
    @NonNull
    public String maxDrawing() {
        try (Cursor c = DB.getInstance().getWritableDatabase().rawQuery("SELECT MAX(" + Tables.DRAWING_ID + ") FROM " + Tables.DRAWING_TABLE + ";", null)) {
            if (c.moveToFirst()) return String.valueOf(c.getInt(0));
            return "-1";
        }
    }

    /**
     * Fonction permettant d'obtenir l'id maximum des challenges
     * Permet de ne demdander que les challenges ayant un id supérieur à celui retourné lors
     * de la requête de mise à jour de la base de données locale
     * @return l'id maximum en String
     */
    @NonNull
    public String maxChallenge() {
        try (Cursor c = DB.getInstance().getWritableDatabase().rawQuery("SELECT MAX(" + Tables.CHALLENGE_ID + ") FROM " + Tables.CHALLENGE_TABLE + ";", null)) {
            if (c.moveToFirst()) return String.valueOf(c.getInt(0));
            return "-1";
        }
    }

    //////////////////////////////
    //           DEBUG          //
    //////////////////////////////

    /**
     * Fonction permettant déstruction et la recréation de toutes les tables de la base de données locale
     */
    public void drop() {
        this.getWritableDatabase().execSQL(Tables.DROP);
        this.onCreate(this.getWritableDatabase());
    }
}
