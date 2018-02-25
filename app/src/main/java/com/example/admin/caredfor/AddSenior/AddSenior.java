package com.example.admin.caredfor.AddSenior;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.admin.caredfor.Database.CallbackNewSenior;
import com.example.admin.caredfor.Database.Database;
import com.example.admin.caredfor.MainActivity.MainActivity;
import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Senior.Senior;
import user.Caretaker;
import user.Relative;
import user.User;

/**
 * Created by Gov on 7/21/2017.
 *
 */

public class AddSenior extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_senior);

        final User user;
        final String role = (String) getIntent().getSerializableExtra("role");

        if (role.equals("c")){
            user = (Caretaker) getIntent().getSerializableExtra("User");
        } else {
            user = (Relative) getIntent().getSerializableExtra("User");
        }

        //initialize
        final Button submit = (Button) findViewById(R.id.add_senior_button);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.add_senior_relative);

        final EditText firstName = (EditText) findViewById(R.id.first_name_senior);
        final EditText lastName = (EditText) findViewById(R.id.last_name_senior);
        final EditText address = (EditText) findViewById(R.id.address_senior);
        final EditText dateOfBirth = (EditText) findViewById(R.id.date_of_birth);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddSenior.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //on submit
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get selected sex
                final RadioGroup radioGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
                final int selectedID = radioGroupSex.getCheckedRadioButtonId();
                final RadioButton sex = (RadioButton) findViewById(selectedID);

                //add all edit texts to a list to check for good input
                final ArrayList<EditText> editTextList = new ArrayList<>();
                editTextList.add(firstName);
                editTextList.add(lastName);
                editTextList.add(address);
                editTextList.add(dateOfBirth);

                Log.wtf("Debug:", "wtf man");

                //check for bad input
                for(EditText edit : editTextList){

                    Log.d("empty edit", edit.getText().toString());
                    if(TextUtils.isEmpty(edit.getText())|| sex == null){
                        showError(layout, "Please fill in all fields");
                        return;
                    }
                }

                //get main database reference and relative reference
                final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference ref = mDatabase.getReference();
                final DatabaseReference userRef;

                if (role.equals("c")){
                   userRef = mDatabase.getReference("caretaker")
                           .child(user.getUserID()).child("seniorID");
                } else {

                    userRef = mDatabase.getReference("relative")
                            .child(user.getUserID()).child("seniorID");
                }

                //check if senior already exists
                final DatabaseReference seniorRef = mDatabase.getReference("senior");

                Log.d("Debug", "seniorRef - " + seniorRef.toString());

                seniorRef.orderByChild("firstName").equalTo(firstName.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d("debug", dataSnapshot.toString());
                                //Create a relative
                                for(DataSnapshot data : dataSnapshot.getChildren()) {

                                    Senior senior = data.getValue(Senior.class);
                                    if (senior.getLastName().equals(lastName.getText().toString())
                                            && senior.getAddress().equals(address.getText().toString())
                                            && senior.getDateOfBirth().equals(dateOfBirth.getText().toString())
                                            && senior.getSex().equals(sex.getText().toString())) {

                                        Log.d("Firebase data", data.toString());

                                        //already exists, add caretaker
                                        senior.addListener(user.getUserID());

                                        //update db
                                        seniorRef.child(senior.getSeniorID()).child("listeners").removeValue();
                                        seniorRef.child(senior.getSeniorID()).child("listeners").setValue(senior.getListeners());

                                        user.setSeniorID(senior.getSeniorID());
                                        userRef.setValue(senior.getSeniorID());

                                        //Ding!
                                        Toast toast = Toast.makeText(getApplicationContext(), "Dependent added!", Toast.LENGTH_SHORT);
                                        toast.show();

                                        //get back
                                        Intent intent = new Intent(AddSenior.this, MainActivity.class);
                                        intent.putExtra("User", user);
                                        intent.putExtra("role", role);
                                        startActivity(intent);
                                        finish();

                                    }
                                }

                            //    if (dataSnapshot.toString().equals("DataSnapshot { key = senior, value = null })")) {

                                    Log.d("debug:", "senior does not exist");
                                    Database.addNewSenior(new CallbackNewSenior() {
                                        @Override
                                        public void addNewSenior() {

                                            //set Senior key
                                            String key = ref.child("senior").push().getKey();

                                            //create new Senior
                                            Senior senior = new Senior(firstName.getText().toString(), lastName.getText().toString()
                                                    , address.getText().toString(),dateOfBirth.getText().toString(), sex.getText().toString());

                                            ArrayList<String> listeners = senior.getListeners();
                                            listeners.add(user.getUserID());

                                            senior.setSeniorID(key);

                                            //create a map to push Senior to db
                                            Map<String, Object> childUpdates = new HashMap<>();
                                            childUpdates.put("/senior/" + key, senior.toMap());

                                            //update seniorID for this Caretaker
                                            user.setSeniorID(key);
                                            userRef.setValue(key);

                                            //push Senior
                                            ref.updateChildren(childUpdates);

                                            //Ding!
                                            Toast toast = Toast.makeText(getApplicationContext(), "Dependent added!", Toast.LENGTH_SHORT);
                                            toast.show();

                                            //get back
                                            Intent intent = new Intent(AddSenior.this, MainActivity.class);
                                            intent.putExtra("User", user);
                                            intent.putExtra("role", role);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                             //   }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("database error", databaseError.toString());
                            }
                        });

                Log.wtf("Debug:", "wtf man");
            }
        });
    }

    private void updateLabel(Calendar myCalendar) {

        final EditText dateOfBirth = (EditText)findViewById(R.id.date_of_birth);

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateOfBirth.setText(sdf.format(myCalendar.getTime()));
    }

}
