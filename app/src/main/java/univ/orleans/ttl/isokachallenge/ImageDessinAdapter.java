package univ.orleans.ttl.isokachallenge;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageDessinAdapter extends  RecyclerView.Adapter<ImageDessinAdapter.ImageDessinViewHolder>{

    private final List<ImageDessin> imageDessin;
    private OnItemClickListener mListenerDessin;

    public  interface  OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(ImageDessinAdapter.OnItemClickListener listener){
        mListenerDessin = listener;
    }

    public ImageDessinAdapter(List<ImageDessin> imageDessin){
        this.imageDessin = imageDessin;
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
        holder.setLocationData(imageDessin.get(position));
    }

    @Override
    public int getItemCount() {
        return this.imageDessin.size();
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

        void setLocationData(ImageDessin imageDessin){
            Log.d("Load","Image");
            Picasso.get().load(imageDessin.imageUrl).into(imageView);
            textTitle.setText(imageDessin.auteur);
            textLocation.setText(imageDessin.dateSoumission);
            textStarRating.setText(String.valueOf(imageDessin.startRating));

        }


    }

}
