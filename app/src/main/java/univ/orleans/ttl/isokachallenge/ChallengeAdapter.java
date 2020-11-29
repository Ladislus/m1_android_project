package univ.orleans.ttl.isokachallenge;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import univ.orleans.ttl.isokachallenge.orm.DB;
import univ.orleans.ttl.isokachallenge.orm.entity.Challenge;
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.MyViewHolder> {

    private final List<Challenge> challenges; // contient tous les challenges de la BD
    private OnItemClickListener mListener; // Listener du click sur les challenge

    public  interface  OnItemClickListener{
        void OnItemClick(int position);
    }

    /**
     * Permet d'ajouter un listener à un adapteur de challenge
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /**
     * Constructeur de l'adapteur des challenge
     * @param challenges, liste de tous les challenges de la BD
     */
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

    /**
     * Lancement de l'affichage des challenges
     * @param holder
     * @param position, int qui represente l'index du challenge traité
     */
    @Override
    public void onBindViewHolder(@NonNull ChallengeAdapter.MyViewHolder holder, int position) {
        holder.display(this.challenges.get(position));
        Log.d("bonjour", "onBindViewHolder: display");
    }

    /**
     * @return le nombre de challenge contenu dans la liste des challenges
     */
    @Override
    public int getItemCount() {
        return this.challenges.size();
    }

    /**
     * Classe qui permet de spécifier les caractéristiques d'affichage d'un challenge
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // composants de l'affichage d'un challenge
        private final TextView titreChallenge;
        private final ViewPager2 imagesCaroussel;
        private final Context context;

        public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            context = itemView.getContext(); // Récupération du context
            // Récupération des élements graphiques
            this.titreChallenge = itemView.findViewById(R.id.textTitreChallenge);
            this.imagesCaroussel = itemView.findViewById(R.id.locationsViewPager);
            // Mise en place d'un listener sur les challenges
            itemView.setOnClickListener(v -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.OnItemClick(position);
                    }
                }
            });
        }

        /**
         * Affichage spécifié pour chaque challenge
         * @param challenge, un challenge de la liste challenges
         */
        void display (Challenge challenge) {
            this.titreChallenge.setText(challenge.getName()); // affichage du Titre

            // Récupération de la liste des dessins pour un challenge en fonction de l'id du challenge via la
            // BD local
            List<Drawing> listDessinChallenge = DB.getInstance().getDrawingsFromChallenge(challenge.getId());
            Pair<String, LocalDateTime> theme = new Pair<>(null, null); // Paire pour récupérer le thème d'un challenge
            Log.d("bonjour", "display: listDessinChallenge");
            if (listDessinChallenge.isEmpty()) { // si nous n'avons pas de participation = pas de dessin pour un challenge
                // convertion de la date du challenge en LocalDatetTime
                String dateChallenge = challenge.getDate();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime dateTimeChallenge = LocalDateTime.parse(dateChallenge, formatter);

                // création de la paire avec les infos du challenge
                theme = new Pair<>(challenge.getTheme(),dateTimeChallenge);
                Log.d("bonjour", "display: après pair "+theme.first);

                // ajout d'un drawing fictif pour que l'on puisse afficher le thème
                listDessinChallenge.add(new Drawing(challenge.getTheme(),dateTimeChallenge));
            }

            // trie de la liste en focntion de leur date (+ recent au - recent)
            sortList(listDessinChallenge);

            // n'afficher que 5 dessins pour un challenge, sur la page d'accueil
            ArrayList<Drawing> listDessinChallengeTrier = new ArrayList<>();
            if (listDessinChallenge.size()>=5) {
                for (int i = 0; i < 5; i++) {
                    listDessinChallengeTrier.add(listDessinChallenge.get(i));
                }
            }

            ImageDessinAdapter dessinAdapter;
            if (listDessinChallenge.size() >= 5){ // si plus de 5 dessins
                // afficher que 5 dessins avec un thème null
                dessinAdapter = new ImageDessinAdapter(listDessinChallengeTrier, theme);
            } else {
                // afficher le thème car pas de dessin, juste un dessin fictif dans la listDessinChallenge
                dessinAdapter = new ImageDessinAdapter(listDessinChallenge, theme);
            }

            // un listener sur le click des dessins, peut importe si dessin ou thème de challenge
            dessinAdapter.setOnItemClickListener(
                    position -> {
                        // Lancer l'activité de la descritpion d'un challenge si click sur un dessin du
                        // dit challenge
                        Intent gotoChall = new Intent(this.context, onChallenge.class);
                        gotoChall.putExtra("idchall", challenge.getId()); // passage de l'id du challenge
                        context.startActivity(gotoChall);
                    }
            );
            // Initialisation du caroussel des dessins d'un challenge
            this.imagesCaroussel.setAdapter(dessinAdapter);
            this.imagesCaroussel.setClipToPadding(false);
            this.imagesCaroussel.setClipChildren(false);
            this.imagesCaroussel.setOffscreenPageLimit(3);
            this.imagesCaroussel.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer((page, position) -> {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.95f + r * 0.05f);
            });

            this.imagesCaroussel.setPageTransformer(compositePageTransformer);
        }

        /**
         * Permet de trier une liste de dessin en fonction de leur date de soumission
         * les plus recent avant le plus ancien
         * @param list, liste de drawings (dessin)
         */
        void sortList(List<Drawing> list){
            list.sort((o1, o2) -> {
                // Récupération de la date en string des 2 dessins
                String dateString1 = o1.getDate();
                String dateString2 = o2.getDate();
                // passage des string en LocalDateTime
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime dateTime1 = LocalDateTime.parse(dateString1, formatter);
                LocalDateTime dateTime2 = LocalDateTime.parse(dateString2, formatter);
                // Comparaison des 2 dates en LocalDateTime
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
