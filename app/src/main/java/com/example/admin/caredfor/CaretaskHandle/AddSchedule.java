package com.example.admin.caredfor.CaretaskHandle;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.admin.caredfor.MainActivity.MainActivity;
import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Caretask.Careschedule;
import Senior.Senior;

/**
 * Created by Admin on 8/14/2017.
 *
 */

public class AddSchedule extends RootActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        //receive senior
        final Senior senior = (Senior) getIntent().getSerializableExtra("senior");
        final String date = (String) getIntent().getSerializableExtra("date");

        //initialize
        final Button submit = (Button) findViewById(R.id.submit_schedule);

        final EditText arrival = (EditText) findViewById(R.id.arrival);
        final EditText departure = (EditText) findViewById(R.id.departure);

        arrival.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (arrival != null) {
                            arrival.setText(String.format(Locale.CANADA, "%d:%d", selectedHour, selectedMinute));
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        departure.setText(String.format(Locale.CANADA, "%d:%d", selectedHour, selectedMinute));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        //on hitting submit
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //create a new caretask object
                Careschedule careschedule = new Careschedule();


                if (!arrival.getText().toString().isEmpty()){
                    careschedule.setArrival(arrival.getText().toString());
                }
                if (!departure.getText().toString().isEmpty()){
                    careschedule.setDeparture(departure.getText().toString());
                }

                //update the senior's caretask with the new information
                Map<String, Map<String, String>> seniorSchedule;

                if (senior.getCareSchedule() != null){
                    seniorSchedule = senior.getCareSchedule();
                } else {
                    seniorSchedule = new HashMap<>();
                }

                seniorSchedule.put(date, careschedule.toMap());
                senior.setCareSchedule(seniorSchedule);

                //delete the from db and re-add with new information
                Map<String, Object> newSenior = new HashMap<>();
                newSenior.put(senior.getSeniorID(), senior.toMap());

                final DatabaseReference seniorRef = FirebaseDatabase.getInstance().getReference("senior");

                seniorRef.child(senior.getSeniorID()).removeValue();
                seniorRef.updateChildren(newSenior);

                //on complete, show the user and get back
                Toast toast = Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT);
                toast.show();

                finish();
            }
        });

    }
}
