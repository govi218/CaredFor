package com.example.admin.caredfor.MainActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.caredfor.AddSenior.AddSenior;
import com.example.admin.caredfor.CaretaskHandle.CaretaskRouter;
import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;
import com.example.admin.caredfor.SwipeListener.OnSwipeTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import Senior.Senior;
import user.Caretaker;
import user.Relative;
import user.User;

/**
 * Created by Gov on 6/6/2017.
 *
 */

public class MainActivity extends RootActivity{

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //get caretaker
        final User user;
        final String role = (String) getIntent().getSerializableExtra("role");

        if (role.equals("c")){
            user = (Caretaker) getIntent().getSerializableExtra("User");
        } else {
            user = (Relative) getIntent().getSerializableExtra("User");
        }

        final TextView seniorBox = (TextView) findViewById(R.id.senior_box);
        final TextView dateView = (TextView) findViewById(R.id.date_view);

        final RelativeLayout mainScroll = (RelativeLayout) findViewById(R.id.main_scroll);

        //if caretaker doesn't have a dependent, prompt to add
        if (user.getSeniorID() == null || user.getSeniorID().equals("")){

            seniorBox.setText(R.string.no_senior_main_activity);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This is a message!")
                    .setCancelable(false)
                    .setTitle("No Dependents")
                    .setPositiveButton("Add A Dependent", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(MainActivity.this, AddSenior.class);
                            intent.putExtra("User", user);
                            intent.putExtra("role", role);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            //query database and find dependent
            final DatabaseReference seniorRef = FirebaseDatabase.getInstance().getReference("senior");
            seniorRef.orderByChild("seniorID").equalTo(user.getSeniorID())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data : dataSnapshot.getChildren()) {

                                Log.d("senior:", data.toString());

                                final Senior senior = data.getValue(Senior.class);
                                String dispName = senior.getFirstName() + " " + senior.getLastName();
                                seniorBox.setText(dispName);
                                dateView.setText(Calendar.getInstance().getTime().toString());

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

                                dateView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new DatePickerDialog(MainActivity.this, date, myCalendar
                                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                    }
                                });

                                mainScroll.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
                                    public void onSwipeTop() {
                                        Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                                    }
                                    public void onSwipeRight() {
                                        Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                                    }
                                    public void onSwipeLeft() {
                                        Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                                    }
                                    public void onSwipeBottom() {
                                        Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                caretasks(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(MainActivity.this, CaretaskRouter.class);
                                        switch (v.getId()){
                                            case R.id.food_intake:
                                            case R.id.food_intake_image:
                                                intent.putExtra("caretask", "Food Intake");
                                                break;
                                            case R.id.hygiene:
                                            case R.id.hygiene_image:
                                                intent.putExtra("caretask", "Hygiene");
                                                break;
                                            case R.id.schedule:
                                            case R.id.schedule_image:
                                                intent.putExtra("caretask", "Care Schedule");
                                                break;
                                            case R.id.mood:
                                            case R.id.mood_image:
                                                intent.putExtra("caretask", "Mood");
                                                break;
                                            case R.id.sleep:
                                            case R.id.sleep_image:
                                                intent.putExtra("caretask", "Sleep");
                                                break;
                                            case R.id.activities:
                                            case R.id.activities_image:
                                                intent.putExtra("caretask", "Activities");
                                                break;
                                            case R.id.medication:
                                            default:
                                                //medication_image as well
                                                intent.putExtra("caretask", "Medication");
                                                break;
                                        }
                                        intent.putExtra("senior", senior);
                                        intent.putExtra("role", role);
                                        startActivity(intent);
                                    }
                                });
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
        }
    }

    private void caretasks(final View.OnClickListener listener) {

        final ImageButton hygieneImage = (ImageButton) findViewById(R.id.hygiene_image);
        hygieneImage.setOnClickListener(listener);
        final ImageButton foodImage = (ImageButton) findViewById(R.id.food_intake_image);
        foodImage.setOnClickListener(listener);
        final ImageButton medicationImage = (ImageButton) findViewById(R.id.medication_image);
        medicationImage.setOnClickListener(listener);
        final ImageButton scheduleImage = (ImageButton) findViewById(R.id.schedule_image);
        scheduleImage.setOnClickListener(listener);
        final ImageButton moodImage = (ImageButton) findViewById(R.id.mood_image);
        moodImage.setOnClickListener(listener);
        final ImageButton activitiesImage = (ImageButton) findViewById(R.id.activities_image);
        activitiesImage.setOnClickListener(listener);
        final ImageButton sleepImage = (ImageButton) findViewById(R.id.sleep_image);
        sleepImage.setOnClickListener(listener);

        final TextView hygiene = (TextView) findViewById(R.id.hygiene);
        hygiene.setOnClickListener(listener);
        final TextView food = (TextView) findViewById(R.id.food_intake);
        food.setOnClickListener(listener);
        final TextView medication = (TextView) findViewById(R.id.medication);
        medication.setOnClickListener(listener);
        final TextView schedule = (TextView) findViewById(R.id.schedule);
        schedule.setOnClickListener(listener);
        final TextView mood = (TextView) findViewById(R.id.mood);
        mood.setOnClickListener(listener);
        final TextView activities = (TextView) findViewById(R.id.activities);
        activities.setOnClickListener(listener);
        final TextView sleep = (TextView) findViewById(R.id.sleep);
        sleep.setOnClickListener(listener);
    }


    private void updateLabel(Calendar myCalendar) {

        final TextView dateView = (TextView) findViewById(R.id.date_view);

        //String myFormat = "MM/dd/yy"; //In which you need put here
        //SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateView.setText(myCalendar.getTime().toString());
    }
}
