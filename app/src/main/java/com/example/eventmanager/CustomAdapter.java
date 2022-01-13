package com.example.eventmanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>
{
    private Context context;
    private Activity activity;
    private ArrayList event_id , event_name, event_desc, event_date,event_month,event_time,event_repeat;

    CustomAdapter(Activity activity, Context context, ArrayList event_id, ArrayList event_name,
                  ArrayList event_desc,ArrayList event_date,ArrayList event_month,ArrayList event_time,ArrayList event_repeat)
    {
        this.activity = activity;
        this.context = context;
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_desc = event_desc;
        this.event_date = event_date;
        this.event_month = event_month;
        this.event_time = event_time;
        this.event_repeat = event_repeat;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {

        holder.event_name_txt.setText(String.valueOf(event_name.get(position)));
        holder.event_desc_txt.setText(String.valueOf(event_desc.get(position)));
        holder.event_date_txt.setText(String.valueOf(event_date.get(position)));
        holder.event_month_txt.setText(String.valueOf(event_month.get(position)));
        holder.event_time_txt.setText(String.valueOf(event_time.get(position)));
        holder.event_repeat_txt.setText(String.valueOf(event_repeat.get(position)));

        holder.options_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, holder.options_image);
                popup.inflate(R.menu.popup_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update_item:
                                Intent i = new Intent(context, EditEvent.class);

                                i.putExtra("Event_Id", String.valueOf(event_id.get(position)));
                                i.putExtra("Event_Name", String.valueOf(event_name.get(position)));
                                i.putExtra("Event_Desc", String.valueOf(event_desc.get(position)));
                                i.putExtra("Event_Date", String.valueOf(event_date.get(position)));
                                i.putExtra("Event_Month", String.valueOf(event_month.get(position)));
                                i.putExtra("Event_Time", String.valueOf(event_time.get(position)));
                                i.putExtra("Event_Repeat", String.valueOf(event_repeat.get(position)));

                                activity.startActivityForResult(i,1 );
                                return true;

                            case R.id.delete_item:
                                confirmDelete(String.valueOf(event_id.get(position)),String.valueOf(event_name.get(position)),position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();

            }
        });
    }

    void confirmDelete(String id,String title,int pos)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete " + title + " ?");
        builder.setMessage("Are you sure you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                myDB.deleteOneRow(id);
                ((HomeActivity)activity).initialize();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }



    @Override
    public int getItemCount() {
        return event_id.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView event_name_txt, event_desc_txt,event_date_txt,event_month_txt,event_time_txt,event_repeat_txt;
        ImageButton options_image;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            event_name_txt = itemView.findViewById(R.id.event_name);
            event_desc_txt = itemView.findViewById(R.id.event_desc);
            event_date_txt = itemView.findViewById(R.id.event_date);
            event_month_txt = itemView.findViewById(R.id.event_month);
            event_time_txt = itemView.findViewById(R.id.event_time);
            event_repeat_txt = itemView.findViewById(R.id.event_repeat);
            options_image = itemView.findViewById(R.id.options_select);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
