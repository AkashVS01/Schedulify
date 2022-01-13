package com.example.eventmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import java.util.Calendar;
import java.util.Arrays;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class EditEvent extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    Button edit_button;
    String event_id ;
    String event_name ;
    String event_des ;
    String event_date ;
    String month ;
    String time ;
    String repeat ;
    EditText e1,e2,e3,e4;
    Spinner spin;
    Calendar myCalendar;
    String[] values = { "Once", "Daily", "Weekly"};
    String[] months = {"JAN", "FEB", "MARCH", "APRIL", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    TimePickerDialog mTimePicker;
    int hr = 0, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);


        edit_button = (Button)findViewById(R.id.edit_event_button);
        e1 = (EditText)findViewById(R.id.event_name);
        e2 = (EditText)findViewById(R.id.event_desc);
        e3= (EditText) findViewById(R.id.event_day);
        e4 = (EditText) findViewById(R.id.event_clock);


        //getdata
        Intent intent = getIntent();
        event_id = intent.getStringExtra("Event_Id");
        event_name = intent.getStringExtra("Event_Name");
        event_des = intent.getStringExtra("Event_Desc");
        event_date = intent.getStringExtra("Event_Date");
        month = intent.getStringExtra("Event_Month");
        time = intent.getStringExtra("Event_Time");
        repeat = intent.getStringExtra("Event_Repeat");

        //setdata
        e1.setText(event_name);
        e2.setText(event_des);
        e3.setText(new StringBuilder().append(event_date).append("/").append(Arrays.asList(months).indexOf(month)+1).append("/").append("2021").append(" "));
        e4.setText(time);


        String[] arrOfStr = time.split(":", 2);
        String[] arr2 = arrOfStr[1].split(" ",2);
        min = Integer.parseInt(arr2[0]);
        if(arr2[1].equals("AM") && !arrOfStr[0].equals("12"))
            hr = Integer.parseInt(arrOfStr[0]);
        else if(arr2[1].equals("PM"))
        {
            if(!arrOfStr[0].equals("12"))
            {
                int t = Integer.parseInt(arrOfStr[0]);
                hr = t + 12;
            }
            else
                hr = Integer.parseInt(arrOfStr[0]);
        }

        myCalendar = Calendar.getInstance();
        myCalendar.set(2021,Arrays.asList(months).indexOf(month),Integer.parseInt(event_date));


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                event_date = String.valueOf(dayOfMonth);
                month = months[monthOfYear];
                e3.setText(new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear+1).append("/").append(year).append(" "));
            }

        };

        e3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditEvent.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        e4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(EditEvent.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        int hour = selectedHour;
                        int minutes = selectedMinute;
                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes ;
                        else
                            min = String.valueOf(minutes);
                        time = new StringBuilder().append(hour).append(':').append(min).append(" ").append(timeSet).toString();
                        e4.setText(time);
                    }
                }, hr, min, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        spin = (Spinner) findViewById(R.id.repeat);
        spin.setOnItemSelectedListener(this);



        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,values);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        int spinnerPosition = aa.getPosition(repeat);
        spin.setSelection(spinnerPosition);



        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_name = e1.getText().toString();
                event_des = e2.getText().toString();
                MyDatabaseHelper myDB = new MyDatabaseHelper(EditEvent.this);
                myDB.updateBook(event_id, event_name, event_des, event_date, month, time, repeat);
                Intent i = new Intent(EditEvent.this,HomeActivity.class);
                startActivity(i);
            }
        });


    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        repeat = values[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}