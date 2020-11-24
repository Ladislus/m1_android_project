package univ.orleans.ttl.isokachallenge;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;

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
            this.titreChallenge.setText(challenge.getName());
            ArrayList<Drawing> listDessinChallenge = new ArrayList<>(MainActivity.db.getDrawingsFromChallenge(challenge.getId()));

            listDessinChallenge.sort((o1, o2) -> {
                String dateString1 = o1.getDate();
                String dateString2 = o2.getDate();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime dateTime1 = LocalDateTime.parse(dateString1, formatter);
                LocalDateTime dateTime2 = LocalDateTime.parse(dateString2, formatter);
                if(dateTime1.isAfter(dateTime2)) {
                    Log.d("Sort","1");
                    return -1;
                } else if(dateTime1.isBefore(dateTime2)) {
                    Log.d("Sort","-1");
                    return 1;
                } else {
                    Log.d("Sort","0");
                    return 0;
                }
            });

            ImageDessinAdapter dessinAdapter = new ImageDessinAdapter(listDessinChallenge);
            Log.d("bonjour", "display: "+listDessinChallenge);
            //ImageDessinAdapter dessinAdapter = new ImageDessinAdapter(challenge.getImageDessinList());
            dessinAdapter.setOnItemClickListener(
                    position -> {
                        Drawing dessin = listDessinChallenge.get(position);
//                        Toast.makeText(context,
//                                "Image : Auteur = "+user.getAuteur()+" avec "+user.getStartRating()+" votes.",
//                                Toast.LENGTH_SHORT).show();
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
