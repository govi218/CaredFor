package com.example.admin.caredfor.CaretaskHandle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.admin.caredfor.MainActivity.MainActivity;
import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Caretask.Sleep;
import Senior.Senior;

/**
 * Created by Gov on 8/10/2017.
 *
 */

public class AddSleep extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep);

        //receive senior
        final Senior senior = (Senior) getIntent().getSerializableExtra("senior");
        final String date = (String) getIntent().getSerializableExtra("date");
        final String role = (String) getIntent().getSerializableExtra("role");

        final List<CheckBox> checkBoxList = new ArrayList<>();
        final CheckBox morning = (CheckBox) findViewById(R.id.morning);
        final CheckBox afternoon = (CheckBox) findViewById(R.id.afternoon);
        final CheckBox night = (CheckBox) findViewById(R.id.night);

        checkBoxList.add(morning);
        checkBoxList.add(afternoon);
        checkBoxList.add(night);

        final EditText comments = (EditText) findViewById(R.id.sleep_comments);

        final RelativeLayout sleep_layout
                = (RelativeLayout) findViewById(R.id.add_sleep_layout);

        final Button submit_sleep = (Button) findViewById(R.id.submit_sleep);

        submit_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sleep sleep = new Sleep();
                int i = 0;

                for (CheckBox checkBox : checkBoxList){
                    if (checkBox.isChecked()&& sleep.getSleep() == null){
                        sleep.setSleep(checkBox.getText().toString());
                        i++;
                    } else if (checkBox.isChecked()){
                        sleep.setSleep(sleep.getSleep() + ", "
                                + checkBox.getText().toString());
                        i++;
                    }
                }

                if (i == 0){
                    showError(sleep_layout, "Please select at least one option");
                    return;
                }

                if (comments.getText().toString().isEmpty()){
                    sleep.setCaretakerComments("");
                } else {
                    sleep.setCaretakerComments(comments.getText().toString());
                }

                //update the senior's foodIntake with the new information
                Map<String, Map<String, String>> seniorSleep;

                if (senior.getSleep() != null){
                    seniorSleep = senior.getSleep();
                } else {
                    seniorSleep = new HashMap<>();
                }

                seniorSleep.put(date, sleep.toMap());
                senior.setSleep(seniorSleep);

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
