package univ.orleans.ttl.isokachallenge;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import univ.orleans.ttl.isokachallenge.orm.Tables;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

public class ImageDessinAdapter extends  RecyclerView.Adapter<ImageDessinAdapter.ImageDessinViewHolder>{

    private final List<Drawing> dessin;
    private OnItemClickListener mListenerDessin;

    public  interface  OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(ImageDessinAdapter.OnItemClickListener listener){
        mListenerDessin = listener;
    }

    public ImageDessinAdapter(List<Drawing> dessin){
        this.dessin = dessin;
    }

    @NonNull
    @Override
    public ImageDessinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageDessinViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_location,
                        parent,
                        false
                ),mListenerDessin
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ImageDessinViewHolder holder, int position) {
        holder.setLocationData(dessin.get(position));
    }

    @Override
    public int getItemCount() {
        return this.dessin.size();
    }

    static class ImageDessinViewHolder extends RecyclerView.ViewHolder    {

        private final ImageView imageView;
        private final TextView textTitle;
        private final TextView textLocation;
        private final TextView textStarRating;

        public ImageDessinViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textLocation= itemView.findViewById(R.id.textLocation);
            textStarRating= itemView.findViewById(R.id.textStarRating);

            itemView.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.OnItemClick(position);
                    }
                }
            });

        }

        void setLocationData(Drawing dessin){
            User user = MainActivity.db.getUserFromDrawing(dessin.getId());

            HashMap<String, Pair<String, String>> mapParticipation = new HashMap<>();
            mapParticipation.put(Tables.PARTICIPATION_DRAWING_ID, new Pair(Tables.OPERATOR_EQ, dessin.getId().toString()));
            ArrayList<Participation> participations = new ArrayList<Participation>(MainActivity.db.getParticipations(mapParticipation));

            if (user!= null) {
                Picasso.get().load(dessin.getLink()).into(imageView);
                textTitle.setText(user.getUsername());

                textLocation.setText(dessin.getFormattedDate());
                textStarRating.setText(String.valueOf(participations.get(0).getVotes()));
            }else{
                HashMap<String, Pair<String, String>> map = new HashMap<>();
                map.put(Tables.CHALLENGE_THEME, new Pair(Tables.OPERATOR_EQ, dessin.getLink()));
                ArrayList<Challenge> challenges = new ArrayList<>(MainActivity.db.getChallenges(map));

                Picasso.get().load(dessin.getLink()).into(imageView);
                textTitle.setText(R.string.theme);
                textLocation.setText(challenges.get(0).getFormattedDate());
                //textStarRating.setVisibility(View.GONE);
                itemView.findViewById(R.id.votes).setVisibility(View.GONE);

            }

        }

    }

}
