package univ.orleans.ttl.isokachallenge;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;

import univ.orleans.ttl.isokachallenge.orm.*;
import univ.orleans.ttl.isokachallenge.orm.entity.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB db = new DB(this);
        
        User u = new User("Ladislus", LocalDateTime.now());
        User u2 = new User("Max", LocalDateTime.now());

        Drawing d = new Drawing(0, "link", LocalDateTime.now());
        Challenge c = new Challenge(0, "Test", true, "link", LocalDateTime.now(), 10);
        Participation p = new Participation(u, d, c, true);

        db.save(u);
        db.save(u2);

        db.save(d);
        db.save(c);

        db.save(p);
        db.save(new Participation(u2, new Drawing(10, "link", LocalDateTime.now()), c, false));

        User u1 = new User("Max", LocalDateTime.now());
        Drawing d1 = new Drawing(0, "link1", LocalDateTime.now());
        Challenge c1 = new Challenge(0, "Test1", true, "link", LocalDateTime.now(), 10);
        Participation p1 = new Participation(u, d, c, true, 10);

        db.update(u1);
        db.update(d1);
        db.update(c1);
        db.update(p1);

        Log.d(Tables.DB_LOG, "GET 'Ladislus' : " + db.getUser("Ladislus"));
        Log.d(Tables.DB_LOG, "GET 'Boubil' : " + db.getUser("Boubil"));

        db.delete(u);
        db.delete(u1);
        db.delete(u2);
        db.delete(d);
        db.delete(d1);
        db.delete(c);
        db.delete(d1);
        db.delete(p1);

        String hashed = User.hash("Test");
        BCrypt.checkpw("Test", hashed);
    }
}