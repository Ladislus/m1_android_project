package univ.orleans.ttl.isokachallenge;

import java.util.List;

public class Challenge {

    private String titre;
    private List<ImageDessin> imageDessinList;

    public Challenge(String titre, List<ImageDessin> imageDessinList) {
        this.titre = titre;
        this.imageDessinList = imageDessinList;
    }

    public String getTitre() {
        return titre;
    }

    public List<ImageDessin> getImageDessinList() {
        return imageDessinList;
    }


}
