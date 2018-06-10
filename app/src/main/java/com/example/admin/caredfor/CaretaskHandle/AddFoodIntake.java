package com.example.admin.caredfor.CaretaskHandle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.caredfor.MainActivity.MainActivity;
import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Caretask.FoodIntake;
import Senior.Senior;

/**
 * Created by Gov on 6/19/2017.
 *
 */

public class AddFoodIntake extends RootActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        //receive senior
        final Senior senior = (Senior) getIntent().getSerializableExtra("senior");
        final String date = (String) getIntent().getSerializableExtra("date");

        //initialize
        final Button submit = (Button) findViewById(R.id.submit_food);

        final EditText breakfast = (EditText) findViewById(R.id.breakfast);
        final EditText lunch = (EditText) findViewById(R.id.lunch);
        final EditText dinner = (EditText) findViewById(R.id.dinner);

        //on hitting submit
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //create a new FoodIntake object
                FoodIntake foodIntake = new FoodIntake();

                //add meal input if not empty
                //try to find a more efficient way of doing this
                if (!breakfast.getText().toString().isEmpty()){
                    foodIntake.setBreakfast(breakfast.getText().toString());
                }
                if (!lunch.getText().toString().isEmpty()){
                    foodIntake.setLunch(lunch.getText().toString());
                }
                if (!dinner.getText().toString().isEmpty()){
                    foodIntake.setDinner(dinner.getText().toString());
                }

                //update the senior's foodIntake with the new information
                Map<String, Map<String, String>> seniorFoodIntake;

                if (senior.getFoodIntake() != null){
                    seniorFoodIntake = senior.getFoodIntake();
                } else {
                    seniorFoodIntake = new HashMap<>();
                }

                seniorFoodIntake.put(date, foodIntake.toMap());
                senior.setFoodIntake(seniorFoodIntake);

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
