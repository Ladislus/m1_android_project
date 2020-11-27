package univ.orleans.ttl.isokachallenge;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.Tables;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

public class ImageDessinAdapter extends  RecyclerView.Adapter<ImageDessinAdapter.ImageDessinViewHolder>{

    private final List<Drawing> dessin;
    private OnItemClickListener mListenerDessin;
    private Pair<String, LocalDateTime> theme;

    public  interface  OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(ImageDessinAdapter.OnItemClickListener listener){
        mListenerDessin = listener;
    }

    public ImageDessinAdapter(List<Drawing> dessin, Pair<String, LocalDateTime> theme){
        this.dessin = dessin;
        this.theme = theme;
    }

    @NonNull
    @Override
    public ImageDessinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageDessinViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_location,
                        parent,
                        false
                ),mListenerDessin, theme
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
        private Pair<String, LocalDateTime> theme;

        public ImageDessinViewHolder(@NonNull View itemView, OnItemClickListener listener, Pair<String, LocalDateTime> theme) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textLocation= itemView.findViewById(R.id.textLocation);
            textStarRating= itemView.findViewById(R.id.textStarRating);
            this.theme=theme;

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
            if ( !(Objects.isNull(theme.first) && Objects.isNull(theme.second))){
                Picasso.get().load(this.theme.first).into(imageView);
                textTitle.setText(R.string.theme);
                LocalDateTime themeTime = this.theme.second;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy', 'HH'h'mm");
                String dateTheme = themeTime.format(formatter);
                textLocation.setText(dateTheme);
                itemView.findViewById(R.id.votes).setVisibility(View.GONE);
            }else{
                HashMap<String, Pair<String, String>> mapParticipation = new HashMap<>();
                mapParticipation.put(Tables.PARTICIPATION_DRAWING_ID, new Pair(Tables.OPERATOR_EQ, dessin.getId().toString()));
                ArrayList<Participation> participations = new ArrayList<>(DB.getInstance().getParticipations(mapParticipation));

                User user = DB.getInstance().getUserFromDrawing(dessin.getId());
                Picasso.get().load(dessin.getLink()).into(imageView);
                textTitle.setText(user.getUsername());
                textLocation.setText(dessin.getFormattedDate());
                textStarRating.setText(String.valueOf(participations.get(0).getVotes()));
            }

        }
    }
}
