package com.example.admin.caredfor.CaretaskHandle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.admin.caredfor.R;
import com.example.admin.caredfor.RootActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Senior.Senior;

/**
 * Created by Admin on 7/27/2017.
 *
 */

public class CaretaskRouter extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretask);

        final Senior senior = (Senior) getIntent().getSerializableExtra("senior");
        final String caretask = (String) getIntent().getSerializableExtra("caretask");
        final String role = (String) getIntent().getSerializableExtra("role");

        final TextView title = (TextView) findViewById(R.id.caretask_view);
        final CalendarView calendar = (CalendarView) findViewById(R.id.calendar);

        title.setText(caretask);
        final Date date = new Date();

        try {
            calendar.setDate(new SimpleDateFormat("EEE, dd MMM yyyy", Locale.CANADA)
                    .parse(date.toString()).getTime(), true, true);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Calendar Parse:", e.toString());
        }

        if (!caretask.equals("Care Schedule")) {
            calendar.setMaxDate(date.getTime());
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date selected = calendar.getTime();

                Log.d("caretask", caretask);

                if (!date.equals(selected)){

                    DateFormat formatter = new SimpleDateFormat("EEE, dd MM yyyy", Locale.CANADA);
                    String date = formatter.format(selected);
                    Log.d("format:", selected.toString());

                    Intent intent = new Intent(CaretaskRouter.this, CaretaskHandler.class);
                    intent.putExtra("role", role);
                    intent.putExtra("senior", senior);
                    intent.putExtra("date", selected);
                    intent.putExtra("caretask", caretask);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
