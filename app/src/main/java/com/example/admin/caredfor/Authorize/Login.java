package com.example.admin.caredfor.Authorize;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.caredfor.Database.CallbackAuthorize;
import com.example.admin.caredfor.Database.Database;
import com.example.admin.caredfor.MainActivity.MainActivity;
import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;
import com.google.firebase.database.DataSnapshot;

import user.Caretaker;
import user.Relative;

/**
 * Created by Gov on 5/12/2017.
 * Login page for CaredFor
 */

public class Login extends RootActivity {

    volatile int a = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize Elements
        final RelativeLayout loginLayout = (RelativeLayout) findViewById(R.id.login_relative);

        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);

        final TextView emailText = (TextView) findViewById(R.id.email_view);
        final TextView passText = (TextView) findViewById(R.id.password_view);

        final Button login = (Button) findViewById(R.id.submit_login);
        final Button register = (Button) findViewById(R.id.register_account_login);

        final ImageView loading = (ImageView) findViewById(R.id.loading_image);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.login_load_bar);

        //UI stuff
        progressBar.setVisibility(View.INVISIBLE);
        loading.setImageResource(R.color.transparent);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText.setText(R.string.email);
                email.setHint("");
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passText.setText(R.string.password);
                password.setHint("");
            }
        });

        //On submission
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Catch empty fields
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    showError(loginLayout, "Please enter your details");
                    return;
                }

                //More UI stuff, this brings out the progress circle while authorizing
                Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                Animation fromBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_bottom);
                loading.setImageResource(R.color.gray);
                loading.startAnimation(fadein);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.startAnimation(fromBottom);

                //Async callback
                new Database();
                Database.mReadDataCaretaker(email.getText().toString(), new CallbackAuthorize(){

                    @Override
                    public int authorizeCaretaker(DataSnapshot dataSnapshot) {

                        //success status code
                        int success = 1;

                        //UI: Loading done at this point
                        loading.setImageResource(R.color.transparent);
                        progressBar.setVisibility(View.INVISIBLE);

                        //Go through dataSnapshot from Database.authorizeCaretaker
                        //and check if there is a match for this email
                        for(DataSnapshot data : dataSnapshot.getChildren()){

                            Caretaker caretaker = data.getValue(Caretaker.class);
                            caretaker.setUserID(data.getKey());

                            if (password.getText().toString().equals(caretaker.getPassword())) {
                                //if authorize succeeds2s
                                success++;
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("User", caretaker);
                                intent.putExtra("role", "c");
                                startActivity(intent);

                            } else if (!(password.getText().toString().equals(caretaker.getPassword()))){
                                //if authorize fails
                                showError(loginLayout, "Incorrect email/password combination");
                                return 0;
                            } else {
                                //callback to relative
                                return 2;
                            }
                        }
                        return success;
                    }

                    @Override
                    public int authorizeRelative(DataSnapshot dataSnapshot) {

                        int success = 1;

                        //Create a relative
                        for(DataSnapshot data : dataSnapshot.getChildren()){

                            Relative relative = data.getValue(Relative.class);
                            relative.setUserID(data.getKey());

                            //Authorize
                            if (password.getText().toString().equals(relative.getPassword())) {
                                success++;
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("User", relative);
                                intent.putExtra("role","r");
                                a++;
                                startActivity(intent);
                            } else {
                                showError(loginLayout, "Wrong password");
                                return 0;
                            }
                        }
                        return success;
                    }

                    @Override
                    public void authorizeFail() {
                        //If db error
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "An error occurred, please try again", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void userNotFound() {
                        showError(loginLayout, "User not found, please try again!");
                    }
                });
            }

            /*public void mReadDataOnce(EditText email, Callback callback){

                //If fields are filled, summon the almighty database
                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                //Get child databases of caretakers and relatives
                final DatabaseReference caretakerRef = database.getReference("caretaker");
                final DatabaseReference relativeRef = database.getReference("relative");

                //Try finding a caretaker with this email
                caretakerRef.orderByChild("email").equalTo(email.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //create caretaker
                                for(DataSnapshot data : dataSnapshot.getChildren()){

                                    Log.d("Snapshot toString", dataSnapshot.toString());
                                    Log.d("data toString", data.toString());
                                    Caretaker caretaker = data.getValue(Caretaker.class);
                                    caretaker.setUserID(data.getKey());
                                    loading.setImageResource(R.color.transparent);
                                    progressBar.setVisibility(View.INVISIBLE);

                                    //Authorize
                                    if (password.getText().toString().equals(caretaker.getPassword())) {
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.putExtra("User", caretaker);
                                        intent.putExtra("role", "c");
                                        startActivity(intent);

                                        //callback.authorize();
                                    } else {
                                        showError(loginLayout, "Incorrect email/password combination");
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                //If db error
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "An error occurred, please try again", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                //Create a relative
                relativeRef.orderByChild("email").equalTo(email.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //Create a relative
                                for(DataSnapshot data : dataSnapshot.getChildren()){

                                    Relative relative = data.getValue(Relative.class);
                                    relative.setUserID(data.getKey());
                                    loading.setImageResource(R.color.transparent);
                                    progressBar.setVisibility(View.INVISIBLE);

                                    //Authorize
                                    if (password.getText().toString().equals(relative.getPassword())) {
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.putExtra("User", relative);
                                        intent.putExtra("role","r");
                                        a++;
                                        startActivity(intent);
                                    } else {
                                        showError(loginLayout, "Wrong password");
                                        return;
                                    }
                                }

                                if (dataSnapshot.toString().equals("DataSnapshot { key = relative, value = null }") && a ==0) {
                                    Log.d("Not found", "User");
                                    showError(loginLayout, "User not found");
                                    progressBar.setVisibility(View.INVISIBLE);
                                    loading.setImageResource(R.color.transparent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                //If db error
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "An error occurred, please try again", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
            }*/
        });

        //Create a new account
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(R.anim.nothing, R.anim.to_bottom);
        System.exit(0);
    }
}