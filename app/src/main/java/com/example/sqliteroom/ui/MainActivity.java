package com.example.sqliteroom.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.sqliteroom.database.AppDatabase;
import com.example.sqliteroom.database.UserDao;
import com.example.sqliteroom.databinding.ActivityMainBinding;
import com.example.sqliteroom.model.User;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    AppDatabase db;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.button.setOnClickListener(v->{
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name").build();
            userDao = db.userDao();
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    // insert user
                    User user = new User();
                    user.name = binding.editTextText.getText().toString();
                    user.email = binding.editTextTextEmailAddress.getText().toString();
                    userDao.insert(user);
                    // update user
                    //    user.name = "Updated User";
                    //    userDao.update(user);
                }
            });

            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        });
    }
}