package com.example.admin.caredfor.Authorize;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.admin.caredfor.Database.CallbackNewUser;
import com.example.admin.caredfor.Database.Database;
import com.example.admin.caredfor.MainActivity.MainActivity;
import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import user.Caretaker;
import user.Relative;
import user.User;

/**
 * Created by Gov on 5/16/2017.
 * Register a new CaredFor User
 */

public class Register extends RootActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Submit button
        final   Button submit = (Button) findViewById(R.id.submit_account);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Initialize Elements
                RadioGroup roleGroup = (RadioGroup) findViewById(R.id.radio_group);

                //find which radio button is selected and assign it to a radio button
                int SelectedID = roleGroup.getCheckedRadioButtonId();
                final RadioButton role = (RadioButton) findViewById(SelectedID);

                final EditText pass = (EditText) findViewById(R.id.password);
                final EditText confirmPass = (EditText) findViewById(R.id.confirm_password);

                ScrollView layout = (ScrollView) findViewById(R.id.scrollview);

                //list containing future user
                ArrayList<String> tempuser = new ArrayList<>();

                //list of EditTexts to check if they are empty
                ArrayList<EditText> editTextList = new ArrayList<>();
                editTextList.add((EditText) findViewById(R.id.first_name));
                editTextList.add((EditText) findViewById(R.id.last_name));
                editTextList.add((EditText) findViewById(R.id.email));
                editTextList.add((EditText) findViewById(R.id.phone_number));
                editTextList.add(pass);
                editTextList.add(confirmPass);


                //catch empty EditText or no picked role
                for(EditText edit : editTextList){
                    if(TextUtils.isEmpty(edit.getText()) || role == null){
                        showError(layout, "Please fill in all fields");
                        return;
                    }
                    tempuser.add(edit.getText().toString());
                }

                //mismatched passwords
                if (!pass.getText().toString().equals(confirmPass.getText().toString())){
                    showError(layout, "Passwords do not match");
                    return;
                }


                /*----NEW STUFF----*/


                //Assign role to user
                if (role.getText().toString().equals("Caregiver")){
                    final Caretaker caretaker = new Caretaker(tempuser.get(0), tempuser.get(1), tempuser.get(2), tempuser.get(3));
                    caretaker.setPassword(pass.getText().toString());
                    caretaker.setSeniorID("");
                    Log.i("Clicks", "New Caretaker");

                    Database.isNewUser(caretaker, 'c', new CallbackNewUser() {
                        @Override
                        public void isNewUser(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.toString().equals("DataSnapshot { key = caretaker, value = null }")){
                                Toast toast = Toast.makeText(getApplicationContext(), "An account with this email already exists"
                                        , Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                Database.RegisterUser(caretaker, 'c', getApplicationContext());
                            }
                        }
                    });

                /*-----------------*/


                } else if (role.getText().toString().equals("Relative")){
                    final Relative relative = new Relative(tempuser.get(0), tempuser.get(1), tempuser.get(2), tempuser.get(3));
                    relative.setPassword(pass.getText().toString());
                    relative.setSeniorID("");
                    Log.i("Clicks", "New Relative");
                    Database.isNewUser(relative, 'r', new CallbackNewUser() {
                        @Override
                        public void isNewUser(DataSnapshot dataSnapshot) {
                            Log.d("datasnap", dataSnapshot.toString());
                            if (!dataSnapshot.toString().equals("DataSnapshot { key = relative, value = null }")){
                                Toast toast = Toast.makeText(getApplicationContext(), "An account with this email already exists"
                                        , Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                Database.RegisterUser(relative, 'r', getApplicationContext());
                            }
                        }
                    });
                } else {
                    //failsafe, probably not going to be called
                    showError(layout, "Please fill all fields");
                    return;
                }

                //Go back to login
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }

            /*//writing user to db
            private void WriteToDB(User user, char role) {
                try {
                    //get database reference
                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    //generate a key and set it to be userID
                    String key = mDatabase.child("posts").push().getKey();
                    user.setUserID(key);

                    //create a map containing user info
                    Map<String, Object> userValues = user.toMap();

                    //format it with correct location in db
                    Map<String, Object> childUpdates = new HashMap<>();
                    if (role == 'r') {
                        childUpdates.put("/relative/" + key, userValues);
                    } else {
                        childUpdates.put("/caretaker/" + key, userValues);
                    }

                    //push to db
                    mDatabase.updateChildren(childUpdates);
                    Toast toast = Toast.makeText(getApplicationContext(), "Account successfully created", Toast.LENGTH_SHORT);
                    toast.show();

                } catch (Exception e){
                    //failsafe, Firebase should handle offline updates
                    Toast toast = Toast.makeText(getApplicationContext(), "An error occurred, please try again", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }*/
        });
    }
}
