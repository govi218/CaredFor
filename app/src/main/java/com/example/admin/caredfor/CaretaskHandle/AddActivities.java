package com.example.admin.caredfor.CaretaskHandle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Caretask.Activities;
import Senior.Senior;

/**
 * Created by Admin on 7/27/2017.
 *
 */

public class AddActivities extends RootActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activities);

        //receive senior
        final Senior senior = (Senior) getIntent().getSerializableExtra("senior");
        final Date date = (Date) getIntent().getSerializableExtra("date");
        final String role = (String) getIntent().getSerializableExtra("role");

        final List<CheckBox> checkBoxList = new ArrayList<>();
        final CheckBox shopping = (CheckBox) findViewById(R.id.shopping);
        final CheckBox walking = (CheckBox) findViewById(R.id.walking);
        final CheckBox physio = (CheckBox) findViewById(R.id.physio);
        final CheckBox games = (CheckBox) findViewById(R.id.games);
        final CheckBox socializing = (CheckBox) findViewById(R.id.socializing);
        final CheckBox medical_trips = (CheckBox) findViewById(R.id.medical);
        final CheckBox exercise = (CheckBox) findViewById(R.id.exercise);
        final CheckBox sports = (CheckBox) findViewById(R.id.sports);
        final CheckBox other = (CheckBox) findViewById(R.id.other_activities);

        checkBoxList.add(shopping);
        checkBoxList.add(walking);
        checkBoxList.add(physio);
        checkBoxList.add(games);
        checkBoxList.add(socializing);
        checkBoxList.add(medical_trips);
        checkBoxList.add(exercise);
        checkBoxList.add(sports);
        checkBoxList.add(other);

        final EditText comments = (EditText) findViewById(R.id.activities_comments);

        final RelativeLayout activities_layout
                = (RelativeLayout) findViewById(R.id.activities_relative_layout);

        final Button submit_activities = (Button) findViewById(R.id.submit_activities);

        submit_activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activities activities = new Activities();
                int i = 0;

                for (CheckBox checkBox : checkBoxList){
                    if (checkBox.isChecked()&& activities.getActivity() == null){
                        activities.setActivity(checkBox.getText().toString());
                        i++;
                    } else if (checkBox.isChecked()){
                        activities.setActivity(activities.getActivity() + ", "
                                + checkBox.getText().toString());
                        i++;
                    }
                }

                if (i == 0){
                    showError(activities_layout, "Please select at least one option");
                    return;
                }

                if (comments.getText().toString().isEmpty()){
                    activities.setCaretakerComments("");
                } else {
                    activities.setCaretakerComments(comments.getText().toString());
                }

                //update the senior's foodIntake with the new information
                Map<String, Map<String, String>> seniorActivities;

                if (senior.getActivities() != null){
                    seniorActivities = senior.getActivities();
                } else {
                    seniorActivities = new HashMap<>();
                }

                seniorActivities.put(date.toString(), activities.toMap());
                senior.setActivities(seniorActivities);

                //delete the from db and re-add with new information
                Map<String, Object> newSenior = new HashMap<>();
                newSenior.put(senior.getSeniorID(), senior.toMap());

                final DatabaseReference seniorRef = FirebaseDatabase.getInstance().getReference("senior");

                seniorRef.child(senior.getSeniorID()).removeValue();
                seniorRef.updateChildren(newSenior);

                //on complete, show the user and get back
                Toast toast = Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT);
                toast.show();

                Intent intent = new Intent(AddActivities.this, CaretaskRouter.class);
                intent.putExtra("senior", senior);
                intent.putExtra("date", date);
                intent.putExtra("caretask", "Activities");
                intent.putExtra("role", role);
                startActivity(intent);
                finish();
            }
        });
    }
}
