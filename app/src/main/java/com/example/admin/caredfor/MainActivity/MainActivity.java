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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.caredfor.AddSenior.AddSenior;
import com.example.admin.caredfor.CaretaskHandle.CaretaskHandler;
import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;
import com.example.admin.caredfor.SwipeListener.OnSwipeTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    private static final int BARCODE_READER_REQUEST_CODE = 1;

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
        final Button scanner = (Button) findViewById(R.id.scanner);
        final RelativeLayout mainScroll = (RelativeLayout) findViewById(R.id.main_scroll);

        //if caretaker doesn't have a dependent, prompt to add
        if (user.getSeniorID() == null || user.getSeniorID().equals("")){

            seniorBox.setText(R.string.no_senior_main_activity);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This is a message!")
                    .setCancelable(false)
                    .setTitle("No Family Members")
                    .setPositiveButton("Add A Family Member", new DialogInterface.OnClickListener() {
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
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data : dataSnapshot.getChildren()) {

                                Log.d("senior:", data.toString());

                                final Senior senior = data.getValue(Senior.class);
                                final TextView dateView = (TextView) findViewById(R.id.date_view);
                                final Calendar myCalendar = Calendar.getInstance();


                                final SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.CANADA);
                                final String formatted = format.format(myCalendar.getTime());
                                dateView.setText(formatted);
                                seniorBox.setText(String.format("%s %s", senior.getFirstName(), senior.getLastName()));

                                mainScroll.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){

                                    public void onSwipeRight() {
                                        Calendar cal = Calendar.getInstance();
                                        try {
                                            cal.setTime(format.parse(dateView.getText().toString()));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        cal.add(Calendar.DATE, -1);
                                        dateView.setText(format.format(cal.getTime()));
                                    }
                                    public void onSwipeLeft() {
                                        if (dateView.getText().toString().equals(formatted)) {
                                            Toast.makeText(MainActivity.this, "Already at current date!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Calendar cal = Calendar.getInstance();
                                            try {
                                                cal.setTime(format.parse(dateView.getText().toString()));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            cal.add(Calendar.DATE, 1);
                                            dateView.setText(format.format(cal.getTime()));
                                        }

                                    }
                                });


                                final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                          int dayOfMonth) {

                                        myCalendar.set(Calendar.YEAR, year);
                                        myCalendar.set(Calendar.MONTH, monthOfYear);
                                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        myCalendar.add(Calendar.DATE, 0);

                                        String newFormatted = format.format(myCalendar.getTime());
                                        dateView.setText(newFormatted);
                                    }
                                };

                                dateView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, datePicker, myCalendar
                                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                                myCalendar.get(Calendar.DAY_OF_MONTH));
                                        dpd.getDatePicker().setMaxDate(new Date().getTime());
                                        dpd.show();
                                    }
                                });

                                /*scanner.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                                        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
                                    }
                                });*/
                                caretasks(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(MainActivity.this, CaretaskHandler.class);
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
                                        intent.putExtra("date", dateView.getText().toString());
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

}
