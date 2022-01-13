package com.example.eventmanager;

/*import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddEvent extends AppCompatActivity {

    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        add_button = (Button)findViewById(R.id.add_event_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddEvent.this);
                myDB.addBook("Semester","Need to study","24","NOV","7.00 AM","Weekly");
                setReminder("Semester 2","Once");
                Intent i = new Intent(AddEvent.this,HomeActivity.class);
                startActivity(i);
            }
        });
    }

    public void setReminder(String title,String repeat)
    {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("event", title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        cal.set(Calendar.MONTH, 9);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.YEAR, 2021);

        cal.set(Calendar.HOUR_OF_DAY,18);
        cal.set(Calendar.MINUTE,16);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.AM_PM,1);

        if(repeat.equals("Once"))
            am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
        else if(repeat.equals("Daily"))
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        else if(repeat.equals("Weekly"))
            am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 7*1440*60000 , pendingIntent);

    }
}*/

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


public class AddEvent extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    Button add_button;
    String event_name;
    String event_des;
    String event_date;
    String month;
    String time;
    String repeat;
    String event_t;
    EditText e1,e2,e3,e4;
    int alarm_date;
    int alarm_month;
    int alarm_hour;
    int alarm_min;
    String alarm_timeSet;
    Calendar myCalendar;
    String[] values = { "Once", "Daily", "Weekly"};
    String[] months = {"JAN", "FEB", "MARCH", "APRIL", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        add_button = (Button)findViewById(R.id.add_event_button);
        e1 = (EditText)findViewById(R.id.event_name);
        e2 = (EditText)findViewById(R.id.event_desc);

        myCalendar = Calendar.getInstance();

        EditText e3= (EditText) findViewById(R.id.event_day);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                event_date = String.valueOf(dayOfMonth);
                alarm_date = dayOfMonth;
                alarm_month = monthOfYear;
                month = months[monthOfYear];
                event_t = new StringBuilder().append(dayOfMonth).append("-").append(monthOfYear+1).append("-").append(year).toString();
                e3.setText(event_t);
            }

        };

        e3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddEvent.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        e4 = (EditText) findViewById(R.id.event_clock);

        e4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        int hour = selectedHour;
                        alarm_hour = selectedHour;
                        int minutes = selectedMinute;
                        alarm_min  = selectedMinute;
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
                        alarm_timeSet = timeSet;
                        time = new StringBuilder().append(hour).append(':').append(min).append(" ").append(timeSet).toString();
                        e4.setText(time);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        Spinner spin = (Spinner) findViewById(R.id.repeat);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,values);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_name = e1.getText().toString();
                event_des = e2.getText().toString();
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddEvent.this);
                myDB.addBook(event_name, event_des, event_date, month, time, repeat);
                setReminder(event_name, alarm_date, alarm_month, alarm_hour, alarm_min, alarm_timeSet, repeat);
                Intent i = new Intent(AddEvent.this,HomeActivity.class);
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

    public void setReminder(String title, int alarm_date, int alarm_month, int alarm_hour, int alarm_min, String alarm_timeSet,String repeat)
    {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("event", title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);


        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        cal.set(Calendar.MONTH, alarm_month);
        cal.set(Calendar.DAY_OF_MONTH, alarm_date);
        cal.set(Calendar.YEAR, 2021);

        cal.set(Calendar.HOUR_OF_DAY,alarm_hour);
        cal.set(Calendar.MINUTE,alarm_min);
        cal.set(Calendar.SECOND,0);
        if (alarm_timeSet.equals("AM"))
            cal.set(Calendar.AM_PM,0);
        else
            cal.set(Calendar.AM_PM,1);

        if(repeat.equals("Once"))
            am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
        else if(repeat.equals("Daily"))
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        else if(repeat.equals("Weekly"))
            am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 7*1440*60000 , pendingIntent);

    }
}