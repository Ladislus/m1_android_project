package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageCarousel carousel;

    private  List<CarouselItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.carousel = findViewById(R.id.carousel);

        this.list= new ArrayList<>();

        list.add(
                new CarouselItem(
                         "https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080",
                         "Photo by Aaron Wu on Unsplash"
                )
        );
        list.add(
                new CarouselItem(
                        "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080",
                        "Photo by Johannes Plenio on Unsplash"
                )
        );
        list.add(new CarouselItem("https://images-na.ssl-images-amazon.com/images/I/71wvedvViFL._AC_SY679_.jpg","Zero Two"));

        carousel.addData(list);



    }
}