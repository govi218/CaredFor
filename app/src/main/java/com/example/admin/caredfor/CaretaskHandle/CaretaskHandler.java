package com.example.admin.caredfor.CaretaskHandle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import Senior.Senior;

/**
 * Created by Admin on 7/31/2017.
 *
 */

public class CaretaskHandler extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretask_for_date_new);
        //get objects from the previous view
        final String date = (String) getIntent().getSerializableExtra("date");
        final Senior senior = (Senior) getIntent().getSerializableExtra("senior");

        final String role = (String) getIntent().getSerializableExtra("role");
        final String task = (String) getIntent().getSerializableExtra("caretask");

        //set the title and date
        final TextView title = (TextView) findViewById(R.id.relative_date_view);
        final TextView dateView = (TextView) findViewById(R.id.caretask_date);
        final TextView tags = (TextView) findViewById(R.id.tags);
        final TextView comments = (TextView) findViewById(R.id.comments);
        final TextView commentsView = (TextView) findViewById(R.id.comments_view);

        Log.d("date in handler", date);

        title.setText(String.format("%s %s", senior.getFirstName(), senior.getLastName()));
        dateView.setText(date);

        ArrayList<String> populateTaskList = new ArrayList<>();

        final Intent intent;

        //retrieve the caretask map and check for a value at this date
        Map<String, Map<String, String>> taskMap;

        switch (task){
            case "Activities":
                taskMap = senior.getActivities();
                intent = new Intent(CaretaskHandler.this, AddActivities.class);
                break;
            case "Mood":
                taskMap = senior.getMood();
                intent = new Intent(CaretaskHandler.this, SetMood.class);
                break;
            case "Food Intake":
                taskMap = senior.getFoodIntake();
                intent = new Intent(CaretaskHandler.this, AddFoodIntake.class);
                break;
            case "Medication":
                taskMap = senior.getMedication();
                intent = new Intent(CaretaskHandler.this, AddMedication.class);
                break;
            case "Hygiene":
                taskMap = senior.getHygiene();
                intent = new Intent(CaretaskHandler.this, AddHygiene.class);
                break;
            case "Sleep":
                taskMap = senior.getSleep();
                intent = new Intent(CaretaskHandler.this, AddSleep.class);
                break;
            default:
                taskMap = senior.getCareSchedule();
                intent = new Intent(CaretaskHandler.this, AddSchedule.class);
                break;
        }

        if (taskMap != null && taskMap.containsKey(date)) {

            //if contains, add it to list adapter to populate
            Map<String, String> taskForDate = taskMap.get(date);

            populateTaskList.addAll(taskForDate.values());
            if (task.equals("Care Schedule")) {

                commentsView.setText(R.string.schedulr_for_this_date);
                comments.setText(String.format("Arrival: %s\nDeparture: %s", populateTaskList.get(0), populateTaskList.get(1)));
            } else {

                String setTags = "Tags: " + populateTaskList.get(0);
                tags.setText(setTags);

                comments.setText(populateTaskList.get(1));
            }

        } else{
            commentsView.setText("");
            if (role.equals("c")) {
                comments.setText(String.format("Tap to set %s for this date", task.toLowerCase()));
                comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra("senior", senior);
                        intent.putExtra("date", date);
                        intent.putExtra("role", role);
                        startActivity(intent);
                        finish();
                    }
                });

            } else {
                comments.setText(R.string.no_comments_found);
            }
        }
    }
}
