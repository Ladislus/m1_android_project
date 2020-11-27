package univ.orleans.ttl.isokachallenge;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TintInfo;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import univ.orleans.ttl.isokachallenge.orm.Callback;
import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.RequestWrapper;
import univ.orleans.ttl.isokachallenge.orm.Tables;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.MyViewHolder> {

    private List<Challenge> challenges;
    private OnItemClickListener mListener;
    private ProgressBar progressBar;

    public  interface  OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void setListChallengeAdapter(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    public ChallengeAdapter(List<Challenge> challenges, ProgressBar progressBar) {
        this.challenges = challenges;
        this.progressBar = progressBar;
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
        holder.display(this.challenges.get(position), this.progressBar);
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
        void display (Challenge challenge, ProgressBar progressBar){
            this.titreChallenge.setText(challenge.getName());

            final ArrayList<Drawing>[] listDessinChallenge = new ArrayList[]{new ArrayList<>(DB.getInstance().getDrawingsFromChallenge(challenge.getId()))};
            if (listDessinChallenge[0].size()==0){
                String dateChallenge = challenge.getDate();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime dateTimeChallenge = LocalDateTime.parse(dateChallenge, formatter);
                listDessinChallenge[0].add(new Drawing(challenge.getTheme(),dateTimeChallenge));
            }

            sortList(listDessinChallenge[0]);

            ArrayList<Drawing> listDessinChallengeTrier = new ArrayList<>();
            if (listDessinChallenge[0].size()>=5) {
                for (int i = 0; i < 5; i++) {
                    listDessinChallengeTrier.add(listDessinChallenge[0].get(i));
                }
            }
            final ImageDessinAdapter[] dessinAdapter = {null};
            if (listDessinChallenge[0].size()>=5){
                dessinAdapter[0] = new ImageDessinAdapter(listDessinChallengeTrier);
            }else {
                dessinAdapter[0] = new ImageDessinAdapter(listDessinChallenge[0]);
            }

            Log.d("bonjour", "display: "+ listDessinChallenge[0]);

            dessinAdapter[0].setOnItemClickListener(
                    position -> {
                        Drawing dessin = listDessinChallenge[0].get(position);
                        Intent gotoChall = new Intent(this.context, onChallenge.class);
                        gotoChall.putExtra("idchall", challenge.getId());
                        context.startActivity(gotoChall);
                    }
            );
            this.imagesCaroussel.setAdapter(dessinAdapter[0]);

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

            new RequestWrapper().get(new Callback() {
                @Override
                public void onResponse() {
                    progressBar.setVisibility(View.GONE);

                    listDessinChallenge[0] = new ArrayList<>(DB.getInstance().getDrawingsFromChallenge(challenge.getId()));
                    if (listDessinChallenge[0].size()==0){
                        String dateChallenge = challenge.getDate();
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                        LocalDateTime dateTimeChallenge = LocalDateTime.parse(dateChallenge, formatter);
                        listDessinChallenge[0].add(new Drawing(challenge.getTheme(),dateTimeChallenge));
                    }
                    sortList(listDessinChallenge[0]);

                    if (listDessinChallenge[0].size()>=5) {
                        for (int i = 0; i < 5; i++) {
                            listDessinChallengeTrier.add(listDessinChallenge[0].get(i));
                        }
                    }

                    if (listDessinChallenge[0].size()>=5){
                        dessinAdapter[0] = new ImageDessinAdapter(listDessinChallengeTrier);
                    }else {
                        dessinAdapter[0] = new ImageDessinAdapter(listDessinChallenge[0]);
                    }

                    Log.d("bonjour", "onResponse: "+listDessinChallenge[0].toString());

                    imagesCaroussel.setAdapter(dessinAdapter[0]);
                    Log.d("bonjour", "onResponse: bonjour ");
                }

                @Override
                public void onError(ANError error) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        void sortList(ArrayList<Drawing> list){
            list.sort((o1, o2) -> {
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
        }
    }
}
