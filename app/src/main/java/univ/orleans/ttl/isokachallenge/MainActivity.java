package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.time.LocalDateTime;

import univ.orleans.ttl.isokachallenge.orm.*;
import univ.orleans.ttl.isokachallenge.orm.entity.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB db = new DB(this);
        db.save(new User("Ladislus", "Admin", LocalDateTime.now()));
    }
}