package univ.orleans.ttl.isokachallenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import univ.orleans.ttl.isokachallenge.orm.Tables;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;

public class ParticipationAdapteur extends RecyclerView.Adapter<ParticipationAdapteur.MyViewHolder> {

    private Context mContext;
    private List<Participation> mParticipation;

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
        if( !(sharedPref.getString("username","").equals(""))) { // si connecté
            holder.participationItem.setOnClickListener(v -> { // mettre un listener

                //TODO Use request
                try { // synchro BD local et distante
                    new RequestWrapper().vote(mParticipation.get(position).toJson(), sharedPref.getString("username", ""), new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // gestion du vote
                                DB.getInstance().update(Participation.fromJson(response));
                                // remplacement de paraticipation avec l'ancien vote avec le nouveau
                                mParticipation.remove(position);
                                mParticipation.add(position,Participation.fromJson(response));
                                // maj affichage vote
                                holder.votes.setText(mParticipation.get(position).getVotes().toString());
                            } catch (JSONException e) {
                                Log.d("bonjour", "onResponse: salut");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d(RequestWrapper.REQUEST_LOG, "onError: "+anError.toString());
                            Log.d(RequestWrapper.REQUEST_LOG, "onError: "+anError.getErrorDetail());
                            Log.d(RequestWrapper.REQUEST_LOG, "onError: "+anError.getErrorBody());
                            Log.d(RequestWrapper.REQUEST_LOG, "onError: "+anError.getErrorCode());
                            if (anError.getErrorCode() == 409) {
                                Log.d(RequestWrapper.REQUEST_LOG, "Déjà voté");
                                Toast.makeText(mContext,R.string.toastVoteDeja, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //holder.votes.setText(mParticipation.get(position).getVotes().toString());
            });
        }else{
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
