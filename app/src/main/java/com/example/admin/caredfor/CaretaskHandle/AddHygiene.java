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

import Caretask.Hygiene;
import Senior.Senior;

/**
 * Created by Admin on 8/10/2017.
 *
 */

public class AddHygiene extends RootActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hygiene);

        //receive senior
        final Senior senior = (Senior) getIntent().getSerializableExtra("senior");
        final String date = (String) getIntent().getSerializableExtra("date");
        final String role = (String) getIntent().getSerializableExtra("role");

        final List<CheckBox> checkBoxList = new ArrayList<>();
        final CheckBox grooming = (CheckBox) findViewById(R.id.grooming);
        final CheckBox bathing = (CheckBox) findViewById(R.id.bathing);
        final CheckBox oral = (CheckBox) findViewById(R.id.oral_hygiene);
        final CheckBox skin = (CheckBox) findViewById(R.id.skin_care);
        final CheckBox pedicure = (CheckBox) findViewById(R.id.pedicure);
        final CheckBox dressing = (CheckBox) findViewById(R.id.dressing);
        final CheckBox toilet = (CheckBox) findViewById(R.id.toilet);
        final CheckBox shaving = (CheckBox) findViewById(R.id.shaving);
        final CheckBox other = (CheckBox) findViewById(R.id.other_add_hygiene);

        checkBoxList.add(grooming);
        checkBoxList.add(bathing);
        checkBoxList.add(oral);
        checkBoxList.add(skin);
        checkBoxList.add(pedicure);
        checkBoxList.add(dressing);
        checkBoxList.add(toilet);
        checkBoxList.add(shaving);
        checkBoxList.add(other);

        final EditText comments = (EditText) findViewById(R.id.hygiene_comments);

        final RelativeLayout hygiene_layout
                = (RelativeLayout) findViewById(R.id.hygiene_relative_layout);

        final Button submit_hygiene = (Button) findViewById(R.id.submit_hygiene);

        submit_hygiene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hygiene hygiene = new Hygiene();
                int i = 0;

                for (CheckBox checkBox : checkBoxList){
                    if (checkBox.isChecked()&& hygiene.getHygiene() == null){
                        hygiene.setHygiene(checkBox.getText().toString());
                        i++;
                    } else if (checkBox.isChecked()){
                        hygiene.setHygiene(hygiene.getHygiene() + ", "
                                + checkBox.getText().toString());
                        i++;
                    }
                }

                if (i == 0){
                    showError(hygiene_layout, "Please select at least one option");
                    return;
                }

                if (comments.getText().toString().isEmpty()){
                    hygiene.setCaretakerComments("");
                } else {
                    hygiene.setCaretakerComments(comments.getText().toString());
                }

                //update the senior's foodIntake with the new information
                Map<String, Map<String, String>> seniorHygiene;

                if (senior.getHygiene() != null){
                    seniorHygiene = senior.getHygiene();
                } else {
                    seniorHygiene = new HashMap<>();
                }

                seniorHygiene.put(date, hygiene.toMap());
                senior.setHygiene(seniorHygiene);

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
