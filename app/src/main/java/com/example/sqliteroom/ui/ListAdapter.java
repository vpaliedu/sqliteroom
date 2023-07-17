package com.example.sqliteroom.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.sqliteroom.R;
import com.example.sqliteroom.database.AppDatabase;
import com.example.sqliteroom.database.UserDao;
import com.example.sqliteroom.model.User;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*
    A RecyclerView.Adapter-t és a RecyclerView.ViewHolder-t, amelyek felelősek az adatok megjelenítéséért
    és kezeléséért a RecyclerView-ben.
    A ListAdapter egy testreszabott RecyclerView.Adapter, amely felelős a RecyclerView adatokkal történő feltöltéséért.
    kiterjeszti a RecyclerView.Adapter-t. Ez az osztály kezeli az adatok listáját, amelyeket meg szeretnénk jeleníteni a RecyclerView-ban.
    A ListAdapter konstruktorában átadjuk az adatok listáját
*/

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<User> items;
    Context context;
    AppDatabase db;
    UserDao userDao;

    public ListAdapter(List<User> items, Context context) {
        this.items = items;
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, "database-name").build();
        this.userDao = db.userDao();
    }

    /*
        Az onCreateViewHolder metódus felelős a ViewHolder létrehozásáért, amely tartalmazza a nézetet (a lista egy elemét).
        Ez a metódus inflates a list_item.xml layout-ot és létrehoz belőle egy ViewHolder objektumot
    */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    /*
        Az onBindViewHolder metódus felelős az adatok beállításáért a ViewHolder-ben, azaz az adatok összekötéséért a nézettel.
        Ez a metódus meghívódik minden egyes lista elem esetében, és beállítja a megfelelő adatokat az adott elemre.
    */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User item = items.get(position);
        holder.textViewNumber.setText(item.id+"");
        holder.textViewName.setText(item.name);
        holder.textViewEmail.setText(item.email);
        holder.textViewNumber.setOnClickListener(v->{
            User user = items.get(holder.getAdapterPosition());
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    userDao.delete(user);
                    //frissítsük a lista elemeket az UI szálán
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //távolítsuk el a törölt elemet a listából és értesítsük az adaptert a változásról
                            items.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        });
    }

    /*
        Az getItemCount metódus visszaadja a lista elemeinek számát. Ez a metódus fontos a RecyclerView.Adapter számára, mert ezzel tudja meg, hány elemet kell megjeleníteni.
    */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /*
    Végül, a ViewHolder osztály egy belső osztály a ListAdapter-ben. Ez az osztály felelős az egyes lista elemek nézeteinek tárolásáért
    és kezeléséért. A ViewHolder általában tartalmazza a nézeteket, amelyek az adatokat tartalmazzák, ebben az esetben egy TextView-t.
    A ViewHolder konstruktorában található a nézetek inicializálása.
    */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber;
        TextView textViewName;
        TextView textViewEmail;

        ViewHolder(View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
        }
    }
}