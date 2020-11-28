package univ.orleans.ttl.isokachallenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;

public class ParticipationAdapteur extends RecyclerView.Adapter<ParticipationAdapteur.MyViewHolder> {

    private final Context mContext;
    private final List<Participation> mParticipation;

    /**
     * Constructeur des l'adapteur
     * @param mContext
     * @param mParticipation, liste de participation
     */
    public ParticipationAdapteur(AppCompatActivity mContext, List<Participation> mParticipation) {
        this.mContext = mContext;
        this.mParticipation = mParticipation;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_participation,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipationAdapteur.MyViewHolder holder, int position) {
        // affichage participation
        holder.pseudo.setText(mParticipation.get(position).getUser().getUsername());
        Picasso.get().load(mParticipation.get(position).getDrawing().getLink()).into(holder.dessin);
        holder.votes.setText(mParticipation.get(position).getVotes().toString());

        // recupération shared pref
        SharedPreferences sharedPref = mContext.getSharedPreferences("session", Context.MODE_PRIVATE);
        // Si connecté ...
        if( !(sharedPref.getString("username","").equals(""))) {
            // ... mettre un listener sur la card qui represent la participation à la position "position"
            holder.participationItem.setOnClickListener(v -> {

                try {
                    // Lancement requête de vote pour la particition mParticition[position]
                    new RequestWrapper().vote(mParticipation.get(position).toJson(), sharedPref.getString("username", ""), new JSONObjectRequestListener() {
                        // Si la requête réussie ...
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // ... mise à jour de la participation dans la base de données locale
                                DB.getInstance().update(Participation.fromJson(response));
                                // ... remplacement de paraticipation avec l'ancien vote avec le nouveau
                                // (Participation est immuable)
                                mParticipation.remove(position);
                                mParticipation.add(position,Participation.fromJson(response));
                                //  ... mise à jour de l'affichage vote
                                holder.votes.setText(mParticipation.get(position).getVotes().toString());
                            } catch (JSONException e) {
                                // Si le parse de la participation actuel en JSON échoue,
                                // on annule la requête
                                e.printStackTrace();
                            }
                        }

                        // Si la requête échoue
                        @Override
                        public void onError(ANError anError) {
                            // Si l'utilisateur a déjà voté
                            if (anError.getErrorCode() == 409) {
                                Toast.makeText(mContext, R.string.toastVoteDeja, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, R.string.communication_error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    // Si le parse de la participation échoue,
                    // on annule la requête
                    e.printStackTrace();
                }
            });
        } else {
            Toast.makeText(mContext, R.string.toatConnexionVote, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @return le nombre de participation contenu dans la liste des paricipations
     */
    @Override
    public int getItemCount() { return this.mParticipation.size(); }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // composants d'une participations
        TextView pseudo, votes;
        ImageView dessin;
        CardView participationItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Récupération elemens xml
            pseudo = (TextView) itemView.findViewById(R.id.txtPseudo);
            dessin = (ImageView) itemView.findViewById(R.id.imgParticipation);
            votes = (TextView) itemView.findViewById(R.id.textVotesParticipation);
            participationItem = (CardView) itemView.findViewById(R.id.participationLayout);

        }
    }
}
