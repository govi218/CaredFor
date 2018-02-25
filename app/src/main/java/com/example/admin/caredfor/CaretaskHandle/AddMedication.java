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

import Caretask.Medication;
import Senior.Senior;

public class AddMedication extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        //receive senior
        final Senior senior = (Senior) getIntent().getSerializableExtra("senior");
        final Date date = (Date) getIntent().getSerializableExtra("date");
        final String role = (String) getIntent().getSerializableExtra("role");

        final List<CheckBox> checkBoxList = new ArrayList<>();
        final CheckBox medTaken = (CheckBox) findViewById(R.id.med_taken);
        final CheckBox medLow = (CheckBox) findViewById(R.id.med_low);
        final CheckBox medRefilled = (CheckBox) findViewById(R.id.med_refilled);

        checkBoxList.add(medTaken);
        checkBoxList.add(medLow);
        checkBoxList.add(medRefilled);

        final EditText comments = (EditText) findViewById(R.id.medication_comments);

        final RelativeLayout medication_layout
                = (RelativeLayout) findViewById(R.id.add_medication_layout);

        final Button submit_medication = (Button) findViewById(R.id.submit_medication);

        submit_medication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medication medication = new Medication();
                int i = 0;

                for (CheckBox checkBox : checkBoxList){
                    if (checkBox.isChecked()&& medication.getMedication() == null){
                        medication.setMedication(checkBox.getText().toString());
                        i++;
                    } else if (checkBox.isChecked()){
                        medication.setMedication(medication.getMedication() + ", "
                                + checkBox.getText().toString());
                        i++;
                    }
                }

                if (i == 0){
                    showError(medication_layout, "Please select at least one option");
                    return;
                }

                if (comments.getText().toString().isEmpty()){
                    medication.setCaretakerComments("");
                } else {
                    medication.setCaretakerComments(comments.getText().toString());
                }

                //update the senior's foodIntake with the new information
                Map<String, Map<String, String>> seniorMedication;

                if (senior.getMedication() != null){
                    seniorMedication = senior.getMedication();
                } else {
                    seniorMedication = new HashMap<>();
                }

                seniorMedication.put(date.toString(), medication.toMap());
                senior.setMedication(seniorMedication);

                //delete the from db and re-add with new information
                Map<String, Object> newSenior = new HashMap<>();
                newSenior.put(senior.getSeniorID(), senior.toMap());

                final DatabaseReference seniorRef = FirebaseDatabase.getInstance().getReference("senior");

                seniorRef.child(senior.getSeniorID()).removeValue();
                seniorRef.updateChildren(newSenior);

                //on complete, show the user and get back
                Toast toast = Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT);
                toast.show();

                Intent intent = new Intent(AddMedication.this, CaretaskRouter.class);
                intent.putExtra("senior", senior);
                intent.putExtra("date", date);
                intent.putExtra("caretask", "Medication");
                intent.putExtra("role", role);
                startActivity(intent);
                finish();
            }
        });
    }
}
