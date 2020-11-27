package univ.orleans.ttl.isokachallenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;

public class ParticipationAdapteur extends RecyclerView.Adapter<ParticipationAdapteur.MyViewHolder> {

    private Context mContext;
    private List<Participation> mParticipation;

    public ParticipationAdapteur(Context mContext, List<Participation> mParticipation) {
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
        holder.pseudo.setText(mParticipation.get(position).getUser().getUsername());
        Picasso.get().load(mParticipation.get(position).getDrawing().getLink()).into(holder.dessin);
        holder.votes.setText(mParticipation.get(position).getVotes().toString());

        SharedPreferences sharedPref = mContext.getSharedPreferences("session", Context.MODE_PRIVATE);
        if( !(sharedPref.getString("username","").equals(""))) {
            holder.participationItem.setOnClickListener(v -> {
                MainActivity.db.incrementParticipation(mParticipation.get(position));
                holder.votes.setText(mParticipation.get(position).getVotes().toString());
            });
        }
    }

    @Override
    public int getItemCount() { return this.mParticipation.size(); }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pseudo, votes;
        ImageView dessin;
        CardView participationItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pseudo = (TextView) itemView.findViewById(R.id.txtPseudo);
            dessin = (ImageView) itemView.findViewById(R.id.imgParticipation);
            votes = (TextView) itemView.findViewById(R.id.textVotesParticipation);
            participationItem = (CardView) itemView.findViewById(R.id.participationLayout);

        }
    }
}