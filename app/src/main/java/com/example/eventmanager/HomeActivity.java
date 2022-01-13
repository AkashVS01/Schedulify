package com.example.eventmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView text1;
    TextView text2;

    MyDatabaseHelper myDB;
    ArrayList<String> event_id, event_name, event_desc,event_date,event_month,event_time,event_repeat;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home");

        initialize();
    }

    void initialize()
    {
        recyclerView = findViewById(R.id.recyclerView);
        text1 = findViewById(R.id.txt1);
        text2 = findViewById(R.id.txt2);
        empty_imageview = findViewById(R.id.imageView);
        add_button = findViewById(R.id.addButton);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddEvent.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(HomeActivity.this);
        event_id = new ArrayList<>();
        event_name = new ArrayList<>();
        event_desc = new ArrayList<>();
        event_month = new ArrayList<>();
        event_date = new ArrayList<>();
        event_time = new ArrayList<>();
        event_repeat = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(HomeActivity.this,this,event_id, event_name,
                event_desc,event_date,event_month,event_time,event_repeat);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays()
    {
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            text1.setVisibility(View.VISIBLE);
            text2.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                event_id.add(cursor.getString(0));
                event_name.add(cursor.getString(1));
                event_desc.add(cursor.getString(2));
                event_date.add(cursor.getString(3));
                event_month.add(cursor.getString(4));
                event_time.add(cursor.getString(5));
                event_repeat.add(cursor.getString(6));
            }
            empty_imageview.setVisibility(View.GONE);
            text1.setVisibility(View.GONE);
            text2.setVisibility(View.GONE);
        }
    }
}