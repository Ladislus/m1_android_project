package univ.orleans.ttl.isokachallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.MyViewHolder> {

    private final List<Challenge> challenges;
    private OnItemClickListener mListener;

    public  interface  OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ChallengeAdapter(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    @NonNull
    @Override
    public ChallengeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.challenge_item,parent,false);
        return new MyViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeAdapter.MyViewHolder holder, int position) {
        holder.display(this.challenges.get(position));
    }

    @Override
    public int getItemCount() {
        return this.challenges.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView titreChallenge;
        private final ViewPager2 imagesCaroussel;
        private Context context;

        public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            context = itemView.getContext();
            this.titreChallenge = itemView.findViewById(R.id.textTitreChallenge);
            this.imagesCaroussel = itemView.findViewById(R.id.locationsViewPager);
            itemView.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.OnItemClick(position);
                    }
                }
            });
        }

        void display (Challenge challenge){
            this.titreChallenge.setText(challenge.getTitre());
            ImageDessinAdapter dessinAdapter = new ImageDessinAdapter(challenge.getImageDessinList());
            dessinAdapter.setOnItemClickListener(
                    position -> {
                        ImageDessin user = challenge.getImageDessinList().get(position);
                        Toast.makeText(context,
                                "Image : Auteur = "+user.getAuteur()+" avec "+user.getStartRating()+" votes.",
                                Toast.LENGTH_SHORT).show();
                    }
            );
            this.imagesCaroussel.setAdapter(dessinAdapter);

            this.imagesCaroussel.setClipToPadding(false);
            this.imagesCaroussel.setClipChildren(false);
            this.imagesCaroussel.setOffscreenPageLimit(3);
            this.imagesCaroussel.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer((page, position) -> {
                float r= 1 - Math.abs(position);
                page.setScaleY(0.95f+r*0.05f);
            });

            this.imagesCaroussel.setPageTransformer(compositePageTransformer);
        }
    }
}
