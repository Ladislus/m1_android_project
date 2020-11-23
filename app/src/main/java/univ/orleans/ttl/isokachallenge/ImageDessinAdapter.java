package univ.orleans.ttl.isokachallenge;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;
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
            Log.d("Load","Image");
            User user = MainActivity.db.getUserFromDrawing(dessin.getId());
            Picasso.get().load(dessin.getLink()).into(imageView);
            textTitle.setText(user.getUsername());

            String dateString = dessin.getDate();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH'h'mm");
            String dessinDate = dateTime.format(formatter2);
            textLocation.setText(dessinDate);
            //textStarRating.setText(String.valueOf(dessin.startRating));

        }

    }

}
