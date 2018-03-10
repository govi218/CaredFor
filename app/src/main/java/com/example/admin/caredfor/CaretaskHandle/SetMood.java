package com.example.admin.caredfor.CaretaskHandle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.caredfor.MainActivity.MainActivity;
import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Caretask.Mood;
import Senior.Senior;

/**
 * Created by Admin on 7/31/2017.
 *
 */

public class SetMood extends RootActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mood);

        final Senior senior = (Senior) getIntent().getSerializableExtra("senior");
        final String date = (String) getIntent().getSerializableExtra("date");
        final String role = (String) getIntent().getSerializableExtra("role");

        final ImageView sad = (ImageView) findViewById(R.id.sad);
        final ImageView neutral = (ImageView) findViewById(R.id.neutral);
        final ImageView happy = (ImageView) findViewById(R.id.happy);

        sad.setImageResource(R.drawable.sad_face);
        happy.setImageResource(R.drawable.smiley_face);
        neutral.setImageResource(R.drawable.neutral_face);

        final SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        final TextView progress_view = (TextView) findViewById(R.id.progress_seek_bar);
        final Button submit = (Button) findViewById(R.id.submit_mood);
        final EditText comments = (EditText) findViewById(R.id.mood_comments);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_view.setText("" + progress + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Mood mood = new Mood(progress_view.getText().toString());
                if (comments.getText().toString().isEmpty()) {
                    mood.setCaretakerComments("");
                } else {
                    mood.setCaretakerComments(comments.getText().toString());
                }

                //update the senior's foodIntake with the new information
                Map<String, Map<String, String>> seniorMood;

                if (senior.getMood() != null){
                    seniorMood = senior.getMood();
                } else {
                    seniorMood = new HashMap<>();
                }

                seniorMood.put(date, mood.toMap());
                senior.setMood(seniorMood);

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
