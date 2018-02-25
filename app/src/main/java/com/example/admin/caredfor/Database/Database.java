package com.example.admin.caredfor.Database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Senior.Senior;
import user.Caretaker;
import user.Relative;
import user.User;

/**
 * Created by Admin on 7/25/2017.
 *
 */

public class Database {

    public static void mReadDataCaretaker(final String email, final CallbackAuthorize callbackAuthorize){

        //If fields are filled, summon the almighty database
        DatabaseReference caretakerRef = FirebaseDatabase.getInstance().getReference("caretaker");

        Log.d("Debug:", "In readData");

        //Try finding a caretaker with this email
        caretakerRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("debug:", "before callbackAuthorize");
                        int success = callbackAuthorize.authorizeCaretaker(dataSnapshot);
                        Log.d("debug:", "after callbackAuthorize, success =" + success);
                        if (success == 1) {
                            mAuthorizeCaretaker(email, callbackAuthorize);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callbackAuthorize.authorizeFail();
                    }
                });
    }

    private static void mAuthorizeCaretaker(String email, final CallbackAuthorize callbackAuthorize) {

        DatabaseReference relativeRef = FirebaseDatabase.getInstance().getReference("relative");

        //Create a relative
        relativeRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int success = callbackAuthorize.authorizeRelative(dataSnapshot);
                        if (success == 1) {
                            callbackAuthorize.userNotFound();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        callbackAuthorize.authorizeFail();

                    }
                });
    }

    //writing user to db
    public static void RegisterUser(final User user, final char role, Context context) {
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
            Toast toast = Toast.makeText(context, "Account successfully created", Toast.LENGTH_SHORT);
            toast.show();

        } catch (Exception e){
            //failsafe, Firebase should handle offline updates
            Toast toast = Toast.makeText(context, "An error occurred, please try again", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void addNewSenior(final CallbackNewSenior callbackNewSenior) {
        callbackNewSenior.addNewSenior();
    }

    public static void isNewUser(final User user, final char role, final CallbackNewUser callbackNewUser) {
        DatabaseReference userRef;

        if (role == 'c'){
            userRef = FirebaseDatabase.getInstance().getReference("caretaker");
        } else {
            userRef = FirebaseDatabase.getInstance().getReference("relative");
        }

        userRef.orderByChild("email").equalTo(user.getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        callbackNewUser.isNewUser(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
