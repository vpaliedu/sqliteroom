package com.example.sqliteroom.ui;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.sqliteroom.database.AppDatabase;
import com.example.sqliteroom.database.UserDao;
import com.example.sqliteroom.databinding.ActivityListBinding;
import com.example.sqliteroom.model.User;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private ActivityListBinding binding;
    private List<User> users;
    AppDatabase db;
    UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recyclerView = binding.recyclerView;
        binding.recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
        userDao = db.userDao();
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //get users
                users = userDao.getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ListAdapter(users, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
        binding.button2.setOnClickListener(v -> {
            finish();
            startActivity(getIntent());
        });
    }
}