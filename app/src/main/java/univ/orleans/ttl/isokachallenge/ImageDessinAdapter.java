package univ.orleans.ttl.isokachallenge;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import univ.orleans.ttl.isokachallenge.orm.entity.Drawing;
import univ.orleans.ttl.isokachallenge.orm.entity.Participation;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

public class ImageDessinAdapter extends  RecyclerView.Adapter<ImageDessinAdapter.ImageDessinViewHolder>{

    private final List<Drawing> dessins; // liste contenant tous les dessins d'un challenge
    private OnItemClickListener mListenerDessin;
    private Pair<String, LocalDateTime> theme; // paire représentant le thème d'un challenge

    public  interface  OnItemClickListener{
        void OnItemClick(int position);
    }

    /**
     * Permet d'ajouter un listener à un adapteur de dessin
     * @param listener
     */
    public void setOnItemClickListener(ImageDessinAdapter.OnItemClickListener listener){
        mListenerDessin = listener;
    }

    /**
     * Constructeur de l'adapteur des dessins
     * @param dessins, une liste de drawings (dessin)
     * @param theme, une paire contenant le string (url) de l'image et la date du challenge
     */
    public ImageDessinAdapter(List<Drawing> dessins, Pair<String, LocalDateTime> theme){
        this.dessins = dessins;
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

    /**
     * Lancement de l'affichage des dessins
     * @param holder
     * @param position, int qui represente l'index du dessin traité
     */
    @Override
    public void onBindViewHolder(@NonNull ImageDessinViewHolder holder, int position) {
        holder.setLocationData(dessins.get(position));
    }

    /**
     * @return le nombre de challenge contenu dans la liste des challenges
     */
    @Override
    public int getItemCount() {
        return this.dessins.size();
    }

    /**
     * Classe qui permet de spécifier les caractéristiques d'affichage d'un dessin
     */
    public static class ImageDessinViewHolder extends RecyclerView.ViewHolder    {

        // composants de l'affichage d'un dessin
        private final ImageView imageView;
        private final TextView textTitle;
        private final TextView textLocation;
        private final TextView textStarRating;
        private Pair<String, LocalDateTime> theme;

        public ImageDessinViewHolder(@NonNull View itemView, OnItemClickListener listener, Pair<String, LocalDateTime> theme) {
            super(itemView);

            // Récupération des élements graphiques
            imageView = itemView.findViewById(R.id.imageView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textLocation= itemView.findViewById(R.id.textLocation);
            textStarRating= itemView.findViewById(R.id.textStarRating);
            this.theme=theme;
            // Mise en place d'un listener sur les dessins
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
         * Affichage spécifié pour chaque dessin
         * @param dessin, un dessin de la liste dessins
         */
        void setLocationData(Drawing dessin) {
            // si les élements de la paire thème ne sont pas null alors
            // nous n'avons pas de dessin, et nous devons affiché le thème
            if (!(Objects.isNull(theme.first) && Objects.isNull(theme.second))) {
                Picasso.get().load(this.theme.first).into(imageView); // chargement de l'image avec le premier composant du thème
                textTitle.setText(R.string.theme); // affichage du Titre qui est Thème
                // Récupération et formatage de la date du thème
                LocalDateTime themeTime = this.theme.second;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy', 'HH'h'mm");
                String dateTheme = themeTime.format(formatter);
                textLocation.setText(dateTheme);// Affichage de date
                itemView.findViewById(R.id.votes).setVisibility(View.GONE); // Disparition du vote, car l'on ne peut pas voter sur un 1 thème
            } else {
                // si nous avons des dessins
                // Recherche dans la BD de la  participation du dessin au challenge
                HashMap<String, Pair<String, String>> mapParticipation = new HashMap<>();
                mapParticipation.put(Tables.PARTICIPATION_DRAWING_ID, new Pair<>(Tables.OPERATOR_EQ, dessin.getId().toString()));
                ArrayList<Participation> participations = new ArrayList<>(DB.getInstance().getParticipations(mapParticipation));

                // Recherche dans la BD de l'user qui a fait le dessin
                User user = DB.getInstance().getUserFromDrawing(dessin.getId());
                // affichage du dessin
                Picasso.get().load(dessin.getLink()).into(imageView);
                textTitle.setText(user.getUsername());
                textLocation.setText(dessin.getFormattedDate());
                // Mise en place de l'affichage des votes avec les infos de la participation
                textStarRating.setText(String.valueOf(participations.get(0).getVotes()));
            }
        }
    }
}
