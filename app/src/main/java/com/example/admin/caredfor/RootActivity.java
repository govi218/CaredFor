package com.example.admin.caredfor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class RootActivity extends AppCompatActivity {
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_right);

        } else if (onStartCount == 1) {
            onStartCount++;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

    protected void showError(ViewGroup layout, String message) {
        Context context = getApplicationContext();
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        layout.startAnimation(shake);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
