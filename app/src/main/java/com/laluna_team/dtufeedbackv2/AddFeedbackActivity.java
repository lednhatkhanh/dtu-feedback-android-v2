package com.laluna_team.dtufeedbackv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.laluna_team.dtufeedbackv2.utils.NetworkUtils;

public class AddFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);

        NetworkUtils.isOnline(this);
    }
}
